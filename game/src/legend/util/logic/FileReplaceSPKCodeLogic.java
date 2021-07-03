package legend.util.logic;

import static java.nio.ByteBuffer.wrap;
import static java.nio.channels.FileChannel.open;
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
import static legend.util.ValueUtil.nonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import legend.util.entity.FileSPK;
import legend.util.entity.SPKCode;
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
        CS.checkError(ERR_FLE_ANLS,asList(()->path.toString(),()->fileSPK.getErrorInfo()),()->!fileSPK.trim().validate());
        fileSPK.getCodes().parallelStream().forEach(spkCode->{
            try(FileParam fileParam = new FileParam()){
                Path stcPath = get(spkCode.getFilePath(),spkCode.getFileName() + EXT_STC);
                Path spkPath = get(spkCode.getFilePath(),spkCode.getFileName() + EXT_SPK);
                CS.checkError(ERR_SPK_NON,new String[]{stcPath.toString(),spkPath.toString()},()->!existsPath(stcPath) || !existsPath(spkPath));
                // 正则查询所有匹配的已修改文件
                fileParam.setCmd(CMD_FIND);
                fileParam.setPattern(compile(spkCode.getQueryRegex()));
                fileParam.setSrcPath(get(spkCode.getUnpackPath()));
                dealFiles(fileParam);
                if(MODE_NORMAL.equals(fileSPK.getFileSizeMode())) dealFileWithNormalSzie(fileParam,spkCode,stcPath,spkPath);
                else dealFileWithBiggerSzie(fileParam,spkCode,stcPath,spkPath);
            }catch(Exception e){
                CS.sl(gsph(ERR_INFO,e.toString()));
            }
        });
    }

    private void dealFileWithNormalSzie(FileParam fileParam, SPKCode spkCode, Path stcPath, Path spkPath){
        // .stc和.spk编码文件数据更新处理
        STCFormat stcFormat = spkCode.getStcFormat();
        SPKHeader stcHeader = stcFormat.getHeaderInfo();
        SPKHeader stcBody = stcFormat.getBodyInfo();
        SPKFormat spkFormat = spkCode.getSpkFormat();
        SPKHeader spkBody = spkFormat.getBodyInfo();
        SPKHeader spkList = spkFormat.getListInfo();
        SPKHeader spkTail = spkFormat.getTailInfo();
        final byte[] stcCache = readBinaryFormFile(stcPath);
        ByteBuffer stcBuffer = wrap(stcCache).order(ByteOrder.LITTLE_ENDIAN);
        final byte[] spkOriginal = readBinaryFormFile(spkPath);
        ByteBuffer spkReader = wrap(spkOriginal).order(ByteOrder.LITTLE_ENDIAN);
        int deviation = bytesIndexOfBytes(stcCache,stcHeader.getHeaderFlagData().getBytes(),false) + stcHeader.getRecordSizeData().getOffset() - 1;
        final int dataSize = stcBuffer.getInt(deviation);
        CS.checkError(ERR_SPK_ANLS,new String[]{stcPath.toString()},()->1 > dataSize);
        final byte[] pathCache = new byte[255];
        MetaData[] stcDatas = new MetaData[dataSize], spkDatas = new MetaData[dataSize];
        for(int i = 0;i < dataSize;i++){
            stcDatas[i] = new SPKHeader.MetaData();
            deviation = stcHeader.getHeaderSizeData().getSize() + i * stcBody.getHeaderSizeData().getSize();
            stcDatas[i].setSize(stcBuffer.getInt(deviation + stcBody.getFileSizeData().getOffset() - 1));
            stcDatas[i].setOffset(deviation);
            stcDatas[i].setPosition(stcBuffer.getInt(deviation + stcBody.getFileStartPosData().getOffset() - 1));
            spkDatas[i] = new SPKHeader.MetaData();
            spkDatas[i].setSize(stcDatas[i].getSize());
            spkDatas[i].setPosition(stcDatas[i].getPosition());
            if(0 == i) spkDatas[i].setOffset(0);
            else{
                spkDatas[i].setOffset(stcDatas[i - 1].getPosition() + stcDatas[i - 1].getSize());
                spkDatas[i - 1].setDeviation(spkDatas[i].getOffset() - spkDatas[i - 1].getOffset());
            }
            spkDatas[i].setPathLength(spkReader.getShort(spkDatas[i].getOffset() + spkBody.getFilePathData().getOffset() - 1));
            spkReader.position(spkDatas[i].getOffset() + spkBody.getHeaderSizeData().getSize()).get(pathCache,0,spkDatas[i].getPathLength());
            spkDatas[i].setFilePath(get(new String(pathCache,0,spkDatas[i].getPathLength())));
        }
        spkDatas[dataSize - 1].setDeviation(spkDatas[dataSize - 1].getPosition() + spkDatas[dataSize - 1].getSize() - spkDatas[dataSize - 1].getOffset());
        fileParam.getPathMap().entrySet().parallelStream().forEach(e->{
            for(int i = 0,size1,size2,mc;i < dataSize;i++){
                if(e.getValue().endsWith(spkDatas[i].getFilePath())){
                    spkDatas[i].setBytes(readBinaryFormFile(e.getValue()));
                    size1 = (int)e.getKey().size();
                    size2 = spkDatas[i].getSize();
                    spkDatas[i].setSize(size1);
                    if(size1 != size2){
                        stcBuffer.putInt(stcDatas[i].getOffset() + stcBody.getFileSizeData().getOffset() - 1,size1);
                        if(dataSize > i + 1){
                            size2 = size1 + spkBody.getHeaderSizeData().getSize() + spkDatas[i + 1].getPathLength();
                            mc = (SPK_MODULUS - size2 % SPK_MODULUS) % SPK_MODULUS;
                            stcDatas[i].setDeviation(size2 + mc + stcDatas[i].getPosition() - stcDatas[i + 1].getPosition());
                            if(0 < mc) spkDatas[i + 1].setNulbytes(fillBytes(0,mc));
                        }
                    }
                }
            }
        });
        deviation = stcDatas[0].getDeviation();
        for(int i = 1;i < stcDatas.length;i++){
            if(0 != deviation){
                stcDatas[i].setPosition(stcDatas[i].getPosition() + deviation);
                stcBuffer.putInt(stcDatas[i].getOffset() + stcBody.getFileStartPosData().getOffset() - 1,stcDatas[i].getPosition());
            }
            deviation += stcDatas[i].getDeviation();
        }
        final byte[] spkCache = new byte[spkOriginal.length + deviation];
        ByteBuffer spkWriter = wrap(spkCache).order(ByteOrder.LITTLE_ENDIAN);
        for(int i = 0,j;i < dataSize;i++){
            j = spkDatas[i].getOffset();
            if(nonNull(spkDatas[i].getBytes())){
                deviation = spkBody.getFileSizeData().getOffset() - 1;
                spkWriter.put(spkOriginal,j,deviation);
                for(deviation += j,j = 0;j < spkBody.getFileSizeData().getSize();j++) spkWriter.putInt(spkDatas[i].getSize());
                j = deviation + 4 * j;
                if(nonNull(spkDatas[i].getNulbytes())){
                    deviation = spkBody.getHeaderSizeData().getSize() + spkDatas[i].getPathLength() + spkDatas[i].getOffset() - j;
                    spkWriter.put(spkOriginal,j,deviation);
                    spkWriter.put(spkDatas[i].getNulbytes());
                }else spkWriter.put(spkOriginal,j,spkDatas[i].getPosition() - j);
                spkWriter.put(spkDatas[i].getBytes());
            }else spkWriter.put(spkOriginal,j,spkDatas[i].getDeviation());
        }
        deviation = spkDatas[dataSize - 1].getOffset() + spkDatas[dataSize - 1].getDeviation();
        byte[] listFlags = spkList.getHeaderFlagData().getBytes();
        byte[] tailFlags = spkTail.getHeaderFlagData().getBytes();
        for(int i = 0,j = deviation;i < stcDatas.length;i++,j = deviation){
            deviation = bytesIndexOfBytes(spkOriginal,listFlags,deviation + spkList.getHeaderSizeData().getSize(),false);
            if(0 > deviation) deviation = bytesIndexOfBytes(spkOriginal,tailFlags,j + spkList.getHeaderSizeData().getSize(),false);
            if(nonNull(spkDatas[i].getBytes())){
                int k = spkList.getFileSizeData().getOffset() - 1;
                spkWriter.put(spkOriginal,j,k);
                for(k += j,j = 0;j < spkList.getFileSizeData().getSize();j++) spkWriter.putInt(spkDatas[i].getSize());
                j = k + 4 * j;
            }
            spkWriter.put(spkOriginal,j,deviation - j);
        }
        spkWriter.put(spkOriginal,deviation,spkOriginal.length - deviation);
        // 保存已更新数据的.stc和.spk编码文件
        writeBinaryToFile(get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_STC),stcCache);
        writeBinaryToFile(get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_SPK),spkCache);
    }

    private void dealFileWithBiggerSzie(FileParam fileParam, SPKCode spkCode, Path stcPath, Path spkPath) throws Exception{
        // .stc和.spk编码文件数据更新处理
        STCFormat stcFormat = spkCode.getStcFormat();
        SPKHeader stcHeader = stcFormat.getHeaderInfo();
        SPKHeader stcBody = stcFormat.getBodyInfo();
        SPKFormat spkFormat = spkCode.getSpkFormat();
        SPKHeader spkBody = spkFormat.getBodyInfo();
        SPKHeader spkList = spkFormat.getListInfo();
        SPKHeader spkTail = spkFormat.getTailInfo();
        final byte[] stcCache = readBinaryFormFile(stcPath);
        ByteBuffer stcBuffer = wrap(stcCache).order(ByteOrder.LITTLE_ENDIAN);
        int deviation = bytesIndexOfBytes(stcCache,stcHeader.getHeaderFlagData().getBytes(),false) + stcHeader.getRecordSizeData().getOffset() - 1;
        final int dataSize = stcBuffer.getInt(deviation);
        CS.checkError(ERR_SPK_ANLS,new String[]{stcPath.toString()},()->1 > dataSize);
        final byte[] pathCache = new byte[255];
        MetaData[] stcDatas = new MetaData[dataSize], spkDatas = new MetaData[dataSize];
        final Path spkRepackPath = get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_SPK);
        try(FileChannel readChannel = open(spkPath,StandardOpenOption.READ);
            FileChannel writeChannel = open(spkRepackPath,StandardOpenOption.CREATE)){
            int r = (int)(readChannel.size() % Integer.MAX_VALUE), p = (int)(readChannel.size() / Integer.MAX_VALUE);
        }
    }
}
