package legend.util.logic;

import static java.nio.ByteBuffer.wrap;
import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static legend.util.FileUtil.dealFiles;
import static legend.util.FileUtil.readBinaryFormFile;
import static legend.util.FileUtil.writeBinaryToFile;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.StringUtil.bytesIndexOfBytes;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

import legend.util.entity.FileSPK;
import legend.util.entity.SPKFormat;
import legend.util.entity.SPKHeader;
import legend.util.entity.SPKHeader.MetaData;
import legend.util.entity.STCFormat;
import legend.util.entity.intf.IFileSPK;
import legend.util.param.FileParam;

public class FileReplaceSPKCodeLogic extends BaseFileLogic implements IFileSPK{
    public FileReplaceSPKCodeLogic(FileParam param){
        super(param);
        initialize(CONF_FILE_SPK,ST_FILE_SPK_CONF,FileSPK.class);
    }

    @Override
    public void execute(Path path){
        FileSPK fileSPK = convertToObject(path,FileSPK.class);
        CS.showError(ERR_FLE_ANLS,asList(()->path.toString(),()->fileSPK.getErrorInfo()),()->!fileSPK.trim().validate());
        fileSPK.getCodes().parallelStream().forEach(spkCode->{
            try(FileParam fp = new FileParam()){
                // 正则查询所有匹配的已修改文件
                fp.setCmd(CMD_FIND);
                fp.setPattern(compile(spkCode.getQueryRegex()));
                fp.setSrcPath(get(spkCode.getUnpackPath()));
                dealFiles(fp);
                // 处理.stc编码文件
                byte[] stcCache = readBinaryFormFile(get(spkCode.getFilePath(),spkCode.getFileName() + EXT_STC));
                ByteBuffer stcBuffer = wrap(stcCache).order(ByteOrder.LITTLE_ENDIAN);
                STCFormat stcFormat = spkCode.getStcFormat();
                SPKHeader stcHeader = stcFormat.getHeaderInfo();
                SPKHeader stcBody = stcFormat.getBodyInfo();
                SPKHeader stcList = stcFormat.getListInfo();
                int deviation = bytesIndexOfBytes(stcCache,stcList.getFlagData().getBytes(),true) + stcList.getSizeData().getSize();
                String[] filePaths = new String(stcCache,deviation,stcCache.length - deviation - 1,CHARSET_UTF8).split(SPC_NUL);
                MetaData[] metaDatas = new MetaData[filePaths.length];
                fp.getPathMap().entrySet().parallelStream().forEach(e->{
                    for(int i = 0,j = stcHeader.getSizeData().getSize(),k = stcBody.getSizeData().getSize();i < filePaths.length;i++)
                        if(e.getValue().endsWith(get(filePaths[i]))){
                            MetaData metaData = new SPKHeader.MetaData();
                            int offset = j + i * k;
                            int size = (int)e.getKey().size();
                            metaData.setOffset(offset);
                            metaData.setSize(size);
                            size -= stcBuffer.getInt(offset + stcBody.getFileSizeData().getOffset() - 1);
                            metaData.setPosition(stcBuffer.getInt(offset + stcBody.getFileStartPosData().getOffset() - 1));
                            metaData.setDeviation(size);
                            metaData.setBytes(readBinaryFormFile(e.getValue()));
                            metaDatas[i] = metaData;
                            return;
                        }
                });
                for(int i = (deviation = 0),j = stcHeader.getSizeData().getSize(),k = stcBody.getSizeData().getSize();i < metaDatas.length;i++){
                    if(0 == deviation && isEmpty(metaDatas[i])) continue;
                    int offset = j + i * k + stcBody.getFileSizeData().getOffset() - 1;
                    int position = j + i * k + stcBody.getFileStartPosData().getOffset() - 1;
                    if(0 != deviation) stcBuffer.putInt(position,stcBuffer.getInt(position) + deviation);
                    if(nonEmpty(metaDatas[i]) && 0 != metaDatas[i].getDeviation()){
                        stcBuffer.putInt(offset,metaDatas[i].getSize());
                        deviation += metaDatas[i].getDeviation();
                    }
                }
                writeBinaryToFile(get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_STC),stcCache);
                // 处理.spk编码文件
                byte[] spkOriginal = readBinaryFormFile(get(spkCode.getFilePath(),spkCode.getFileName() + EXT_SPK));
                byte[] spkCache = new byte[spkOriginal.length + deviation];
                ByteBuffer spkBuffer = wrap(spkCache).order(ByteOrder.LITTLE_ENDIAN);
                SPKFormat spkFormat = spkCode.getSpkFormat();
                SPKHeader spkBody = spkFormat.getBodyInfo();
                SPKHeader spkList = spkFormat.getListInfo();
                SPKHeader spkTail = spkFormat.getTailInfo();
                byte[] bodyFlags = spkBody.getFlagData().getBytes();
                byte[] listFlags = spkList.getFlagData().getBytes();
                byte[] tailFlags = spkTail.getFlagData().getBytes();
                for(int i = 0,j = (deviation = 0),k;i < metaDatas.length;i++,j = deviation){
                    deviation = bytesIndexOfBytes(spkOriginal,bodyFlags,deviation + spkBody.getSizeData().getSize(),false);
                    if(-1 == deviation) deviation = bytesIndexOfBytes(spkOriginal,listFlags,j + spkBody.getSizeData().getSize(),false);
                    if(nonEmpty(metaDatas[i])){
                        k = spkBody.getFileSizeData().getOffset() - 1;
                        spkBuffer.put(spkOriginal,j,k);
                        for(k += j,j = 0;j < spkBody.getFileSizeData().getSize();j++)
                            spkBuffer.putInt(metaDatas[i].getSize());
                        j = k + 4 * j;
                        spkBuffer.put(spkOriginal,j,metaDatas[i].getPosition() - j);
                        spkBuffer.put(metaDatas[i].getBytes());
                    }else spkBuffer.put(spkOriginal,j,deviation - j);
                }
                for(int i = 0,j = deviation,k;i < metaDatas.length;i++,j = deviation){
                    deviation = bytesIndexOfBytes(spkOriginal,listFlags,deviation + spkList.getSizeData().getSize(),false);
                    if(-1 == deviation) deviation = bytesIndexOfBytes(spkOriginal,tailFlags,j + spkList.getSizeData().getSize(),false);
                    if(nonEmpty(metaDatas[i])){
                        k = spkList.getFileSizeData().getOffset() - 1;
                        spkBuffer.put(spkOriginal,j,k);
                        for(k += j,j = 0;j < spkList.getFileSizeData().getSize();j++)
                            spkBuffer.putInt(metaDatas[i].getSize());
                        j = k + 4 * j;
                    }
                    spkBuffer.put(spkOriginal,j,deviation - j);
                }
                spkBuffer.put(spkOriginal,deviation,spkOriginal.length - deviation);
                writeBinaryToFile(get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_SPK),spkCache);
            }catch(Exception e){
                CS.sl(gsph(ERR_INFO,e.toString()));
            }
        });
    }
}
