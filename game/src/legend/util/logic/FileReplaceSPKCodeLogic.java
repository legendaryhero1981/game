package legend.util.logic;

import static java.nio.ByteBuffer.wrap;
import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static legend.util.FileUtil.dealFiles;
import static legend.util.FileUtil.existsPath;
import static legend.util.FileUtil.readBinaryFormFile;
import static legend.util.FileUtil.writeBinaryToFile;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.StringUtil.bytesIndexOfBytes;
import static legend.util.StringUtil.fillBytes;
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
                Path stcPath = get(spkCode.getFilePath(),spkCode.getFileName() + EXT_STC);
                Path spkPath = get(spkCode.getFilePath(),spkCode.getFileName() + EXT_SPK);
                CS.showError(ERR_SPK_NON,new String[]{stcPath.toString(),spkPath.toString()},()->!existsPath(stcPath) || !existsPath(spkPath));
                // 正则查询所有匹配的已修改文件
                fp.setCmd(CMD_FIND);
                fp.setPattern(compile(spkCode.getQueryRegex()));
                fp.setSrcPath(get(spkCode.getUnpackPath()));
                dealFiles(fp);
                // 处理.stc编码文件
                byte[] stcCache = readBinaryFormFile(stcPath);
                ByteBuffer stcBuffer = wrap(stcCache).order(ByteOrder.LITTLE_ENDIAN);
                STCFormat stcFormat = spkCode.getStcFormat();
                SPKHeader stcHeader = stcFormat.getHeaderInfo();
                SPKHeader stcBody = stcFormat.getBodyInfo();
                SPKHeader stcList = stcFormat.getListInfo();
                int deviation = bytesIndexOfBytes(stcCache,stcList.getFlagData().getBytes(),true) + stcList.getSizeData().getSize();
                String[] filePaths = new String(stcCache,deviation,stcCache.length - deviation - 1,CHARSET_UTF8).split(SPC_NUL);
                MetaData[] metaDatas = new MetaData[filePaths.length];
                fp.getPathMap().entrySet().parallelStream().forEach(e->{
                    for(int i = 0,offset,size;i < filePaths.length;i++)
                        if(e.getValue().endsWith(get(filePaths[i]))){
                            MetaData metaData = new SPKHeader.MetaData();
                            offset = stcHeader.getSizeData().getSize() + i * stcBody.getSizeData().getSize();
                            size = (int)e.getKey().size();
                            metaData.setOffset(offset);
                            metaData.setSize(size);
                            size -= stcBuffer.getInt(offset + stcBody.getFileSizeData().getOffset() - 1);
                            if(0 != size){
                                stcBuffer.putInt(offset + stcBody.getFileSizeData().getOffset() - 1,metaData.getSize());
                                metaData.setDeviation(size);
                                if(0 > size) metaData.setNulbytes(fillBytes(0,-1 * size));
                            }
                            metaData.setPosition(stcBuffer.getInt(offset + stcBody.getFileStartPosData().getOffset() - 1));
                            if(filePaths.length > i + 1) metaData.setNextPosition(stcBuffer.getInt(offset + stcBody.getSizeData().getSize() + stcBody.getFileStartPosData().getOffset() - 1));
                            metaData.setBytes(readBinaryFormFile(e.getValue()));
                            metaDatas[i] = metaData;
                            return;
                        }
                });
                writeBinaryToFile(get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_STC),stcCache);
                // 处理.spk编码文件
                byte[] spkOriginal = readBinaryFormFile(spkPath);
                byte[] spkCache = new byte[spkOriginal.length];
                ByteBuffer spkBuffer = wrap(spkCache).order(ByteOrder.LITTLE_ENDIAN);
                SPKFormat spkFormat = spkCode.getSpkFormat();
                SPKHeader spkBody = spkFormat.getBodyInfo();
                SPKHeader spkList = spkFormat.getListInfo();
                SPKHeader spkTail = spkFormat.getTailInfo();
                byte[] bodyFlags = spkBody.getFlagData().getBytes();
                byte[] listFlags = spkList.getFlagData().getBytes();
                byte[] tailFlags = spkTail.getFlagData().getBytes();
                MetaData metaData = null;
                for(int i = 0,j = (deviation = 0),k;i < metaDatas.length;i++,j = deviation){
                    deviation = bytesIndexOfBytes(spkOriginal,bodyFlags,deviation + spkBody.getSizeData().getSize(),false);
                    if(-1 == deviation) deviation = bytesIndexOfBytes(spkOriginal,listFlags,j + spkBody.getSizeData().getSize(),false);
                    if(nonEmpty(metaDatas[i])){
                        k = spkBody.getFileSizeData().getOffset() - 1;
                        spkBuffer.put(spkOriginal,j,k);
                        for(k += j,j = 0;j < spkBody.getFileSizeData().getSize();j++)
                            spkBuffer.putInt(metaDatas[i].getSize());
                        j = k + 4 * j;
                        k = metaDatas[i].getPosition() - j;
                        if(isEmpty(metaData)) spkBuffer.put(spkOriginal,j,k);
                        else if(0 > metaData.getDeviation()){
                            spkBuffer.put(spkOriginal,j,k);
                            spkBuffer.put(metaData.getNulbytes());
                        }else spkBuffer.put(spkOriginal,j,k - metaData.getDeviation());
                        spkBuffer.put(metaDatas[i].getBytes());
                        metaData = metaDatas[i];
                    }else if(nonEmpty(metaData)){
                        k = metaData.getNextPosition();
                        if(0 > metaData.getDeviation()){
                            spkBuffer.put(spkOriginal,j,k - j);
                            spkBuffer.put(metaData.getNulbytes());
                        }else spkBuffer.put(spkOriginal,j,k - j - metaData.getDeviation());
                        spkBuffer.put(spkOriginal,k,deviation - k);
                        metaData = null;
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
