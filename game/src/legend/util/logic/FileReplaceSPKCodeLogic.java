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
import static legend.util.StringUtil.fillBytes;
import static legend.util.StringUtil.gsph;
import static legend.util.StringUtil.indexOfBytes;
import static legend.util.ValueUtil.nonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import legend.intf.IValue;
import legend.util.entity.FileSPK;
import legend.util.entity.SPKCode;
import legend.util.entity.SPKFormat;
import legend.util.entity.SPKHeader;
import legend.util.entity.SPKHeader.MetaData;
import legend.util.entity.STCFormat;
import legend.util.entity.intf.IFileSPK;
import legend.util.param.FileParam;
import legend.util.param.SingleValue;

public class FileReplaceSPKCodeLogic extends BaseFileLogic implements IFileSPK{
    public FileReplaceSPKCodeLogic(FileParam param){
        super(param);
        initialize(CONF_FILE_SPK,ST_FILE_SPK_CONF,FileSPK.class);
    }

    @Override
    public void execute(Path path){
        final FileSPK fileSPK = convertToObject(path,FileSPK.class);
        CS.checkError(ERR_FLE_ANLS,asList(()->path.toString(),()->fileSPK.getErrorInfo()),()->!fileSPK.trim().validate());
        final boolean isNormalMode = MODE_NORMAL.equals(fileSPK.getFileSizeMode());
        fileSPK.getCodes().parallelStream().forEach(spkCode->{
            try(FileParam fileParam = new FileParam()){
                Path stcPath = get(spkCode.getFilePath(),spkCode.getFileName() + EXT_STC);
                Path spkPath = get(spkCode.getFilePath(),spkCode.getFileName() + EXT_SPK);
                Path stcRepackPath = get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_STC);
                Path spkRepackPath = get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_SPK);
                CS.checkError(ERR_SPK_NON,new String[]{stcPath.toString(),spkPath.toString()},()->!existsPath(stcPath) || !existsPath(spkPath));
                // .stc和.spk编码文件大小检测
                CS.checkError(ERR_SPK_FILE_SIZE,new String[]{stcPath.toString(),SPK_MAX_SIZE_NORMAL + S_EMPTY},()->SPK_MAX_SIZE_NORMAL < stcPath.toFile().length());
                CS.checkError(ERR_SPK_FILE_SIZE,new String[]{spkPath.toString(),SPK_MAX_SIZE_NORMAL + S_EMPTY},()->isNormalMode && SPK_MAX_SIZE_NORMAL < spkPath.toFile().length());
                // 正则查询所有匹配的已修改文件
                fileParam.setCmd(CMD_FIND);
                fileParam.setPattern(compile(spkCode.getQueryRegex()));
                fileParam.setSrcPath(get(spkCode.getUnpackPath()));
                dealFiles(fileParam);
                if(isNormalMode) dealFileWithNormalSzie(fileParam,spkCode,stcPath,spkPath,stcRepackPath,spkRepackPath);
                else dealFileWithBiggerSzie(fileParam,spkCode,stcPath,spkPath,stcRepackPath,spkRepackPath);
            }catch(Exception e){
                CS.sl(gsph(ERR_INFO,e.toString()));
            }
        });
    }

    private void dealFileWithNormalSzie(FileParam fileParam, SPKCode spkCode, Path stcPath, Path spkPath, Path stcRepackPath, Path spkRepackPath) throws Exception{
        // .stc和.spk编码文件数据更新处理
        final STCFormat stcFormat = spkCode.getStcFormat();
        final SPKHeader stcHeader = stcFormat.getHeaderInfo();
        final SPKHeader stcBody = stcFormat.getBodyInfo();
        final SPKFormat spkFormat = spkCode.getSpkFormat();
        final SPKHeader spkBody = spkFormat.getBodyInfo();
        final SPKHeader spkList = spkFormat.getListInfo();
        final SPKHeader spkTail = spkFormat.getTailInfo();
        final byte[] stcCache = readBinaryFormFile(stcPath);
        final ByteBuffer stcBuffer = wrap(stcCache).order(ByteOrder.LITTLE_ENDIAN);
        final byte[] spkOriginal = readBinaryFormFile(spkPath);
        final ByteBuffer spkReader = wrap(spkOriginal).order(ByteOrder.LITTLE_ENDIAN);
        int deviation = indexOfBytes(stcCache,stcHeader.getHeaderFlagData().getBytes(),false) + stcHeader.getRecordSizeData().getOffset() - 1;
        final int dataSize = stcBuffer.getInt(deviation);
        CS.checkError(ERR_SPK_ANLS,new String[]{stcPath.toString()},()->1 > dataSize);
        final byte[] pathCache = new byte[MAX_SIZE_FILE_PATH];
        final MetaData[] stcDatas = new MetaData[dataSize], spkDatas = new MetaData[dataSize];
        int i, j, k;
        for(i = 0;i < dataSize;i++){
            stcDatas[i] = new SPKHeader.MetaData();
            deviation = stcHeader.getHeaderSizeData().getSize() + i * stcBody.getHeaderSizeData().getSize();
            stcDatas[i].setOffset(deviation);
            stcDatas[i].setPosition(stcBuffer.getInt(deviation + stcBody.getFileStartPosData().getOffset() - 1));
            stcDatas[i].setSize(stcBuffer.getInt(deviation + stcBody.getFileSizeData().getOffset() - 1));
            spkDatas[i] = new SPKHeader.MetaData();
            if(0 == i) spkDatas[i].setOffset(0);
            else{
                spkDatas[i].setOffset(stcDatas[i - 1].getPosition() + stcDatas[i - 1].getSize());
                spkDatas[i - 1].setDeviation(spkDatas[i].getOffset() - spkDatas[i - 1].getOffset());
            }
            spkDatas[i].setPosition(stcDatas[i].getPosition());
            spkDatas[i].setSize(stcDatas[i].getSize());
            spkDatas[i].setPathSize(spkReader.getShort(spkDatas[i].getOffset() + spkBody.getPathSizeData().getOffset() - 1));
            spkReader.position(spkDatas[i].getOffset() + spkBody.getHeaderSizeData().getSize()).get(pathCache,0,spkDatas[i].getPathSize());
            spkDatas[i].setFilePath(get(new String(pathCache,0,spkDatas[i].getPathSize())));
        }
        spkDatas[dataSize - 1].setDeviation(spkDatas[dataSize - 1].getPosition() + spkDatas[dataSize - 1].getSize() - spkDatas[dataSize - 1].getOffset());
        fileParam.getPathMap().entrySet().parallelStream().forEach(e->{
            for(int index = 0,size1,size2,mc;index < dataSize;index++){
                if(e.getValue().endsWith(spkDatas[index].getFilePath())){
                    CS.checkError(ERR_SPK_FILE_SIZE,new String[]{e.getValue().toString(),SPK_MAX_SIZE_NORMAL + S_EMPTY},()->SPK_MAX_SIZE_NORMAL < e.getKey().size());
                    spkDatas[index].setMod(true);
                    spkDatas[index].setBytes(readBinaryFormFile(e.getValue()));
                    size1 = (int)e.getKey().size();
                    size2 = spkDatas[index].getSize();
                    spkDatas[index].setSize(size1);
                    if(size1 != size2){
                        stcBuffer.putInt(stcDatas[index].getOffset() + stcBody.getFileSizeData().getOffset() - 1,size1);
                        if(dataSize > index + 1){
                            size2 = size1 + spkBody.getHeaderSizeData().getSize() + spkDatas[index + 1].getPathSize();
                            mc = (SPK_MODULUS - size2 % SPK_MODULUS) % SPK_MODULUS;
                            stcDatas[index].setDeviation(size2 + mc + stcDatas[index].getPosition() - stcDatas[index + 1].getPosition());
                            if(0 < mc) spkDatas[index + 1].setNulbytes(fillBytes(0,mc));
                        }
                    }
                }
            }
        });
        deviation = stcDatas[0].getDeviation();
        for(i = 1;i < stcDatas.length;i++){
            if(0 != deviation){
                stcDatas[i].setPosition(stcDatas[i].getPosition() + deviation);
                stcBuffer.putInt(stcDatas[i].getOffset() + stcBody.getFileStartPosData().getOffset() - 1,stcDatas[i].getPosition());
            }
            deviation += stcDatas[i].getDeviation();
        }
        final byte[] spkCache = new byte[spkOriginal.length + deviation];
        final ByteBuffer spkWriter = wrap(spkCache).order(ByteOrder.LITTLE_ENDIAN);
        for(i = 0;i < dataSize;i++){
            j = spkDatas[i].getOffset();
            if(spkDatas[i].isMod()){
                spkWriter.put(spkOriginal,j,deviation = spkBody.getFileSizeData().getOffset() - 1);
                for(deviation += j,j = 0;j < spkBody.getFileSizeData().getSize();j++) spkWriter.putInt(spkDatas[i].getSize());
                j = deviation + 4 * j;
                if(nonNull(spkDatas[i].getNulbytes())){
                    deviation = spkBody.getHeaderSizeData().getSize() + spkDatas[i].getPathSize() + spkDatas[i].getOffset() - j;
                    spkWriter.put(spkOriginal,j,deviation);
                    spkWriter.put(spkDatas[i].getNulbytes());
                }else spkWriter.put(spkOriginal,j,spkDatas[i].getPosition() - j);
                spkWriter.put(spkDatas[i].getBytes());
            }else spkWriter.put(spkOriginal,j,spkDatas[i].getDeviation());
        }
        deviation = spkDatas[dataSize - 1].getOffset() + spkDatas[dataSize - 1].getDeviation();
        for(i = 0,j = deviation;i < dataSize;i++,j = deviation){
            deviation = indexOfBytes(spkOriginal,spkList.getHeaderFlagData().getBytes(),deviation + spkList.getHeaderSizeData().getSize() + spkDatas[i].getPathSize(),false);
            if(0 > deviation) deviation = indexOfBytes(spkOriginal,spkTail.getHeaderFlagData().getBytes(),j + spkList.getHeaderSizeData().getSize() + spkDatas[i].getPathSize(),false);
            if(spkDatas[i].isMod()){
                spkWriter.put(spkOriginal,j,k = spkList.getFileSizeData().getOffset() - 1);
                for(k += j,j = 0;j < spkList.getFileSizeData().getSize();j++) spkWriter.putInt(spkDatas[i].getSize());
                j = k + 4 * j;
            }
            spkWriter.put(spkOriginal,j,deviation - j);
        }
        spkWriter.put(spkOriginal,deviation,spkOriginal.length - deviation);
        // 保存已更新数据的.stc和.spk编码文件
        writeBinaryToFile(stcRepackPath,stcCache);
        writeBinaryToFile(spkRepackPath,spkCache);
    }

    private void dealFileWithBiggerSzie(FileParam fileParam, SPKCode spkCode, Path stcPath, Path spkPath, Path stcRepackPath, Path spkRepackPath) throws Exception{
        final STCFormat stcFormat = spkCode.getStcFormat();
        final SPKHeader stcHeader = stcFormat.getHeaderInfo();
        final SPKHeader stcBody = stcFormat.getBodyInfo();
        final SPKFormat spkFormat = spkCode.getSpkFormat();
        final SPKHeader spkBody = spkFormat.getBodyInfo();
        final SPKHeader spkList = spkFormat.getListInfo();
        final byte[] stcCache = readBinaryFormFile(stcPath);
        final ByteBuffer stcBuffer = wrap(stcCache).order(ByteOrder.LITTLE_ENDIAN);
        final int dataSize = stcBuffer.getInt(indexOfBytes(stcCache,stcHeader.getHeaderFlagData().getBytes(),false) + stcHeader.getRecordSizeData().getOffset() - 1);
        CS.checkError(ERR_SPK_ANLS,new String[]{stcPath.toString()},()->1 > dataSize);
        try(final FileChannel readChannel = open(spkPath,StandardOpenOption.READ);
            final FileChannel writeChannel = open(spkRepackPath,StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING)){
            final int headerSize = spkBody.getHeaderSizeData().getSize() + MAX_SIZE_FILE_PATH;
            final byte[] pathCache = new byte[MAX_SIZE_FILE_PATH];
            final byte[] spkLastCache = new byte[SPK_MODULUS], spkNextCache = new byte[headerSize];
            ByteBuffer spkLastBuffer = wrap(spkLastCache).order(ByteOrder.LITTLE_ENDIAN), spkNextBuffer = wrap(spkNextCache).order(ByteOrder.LITTLE_ENDIAN);
            long offset = 0, deviation = 0;
            int i = 0, j = stcHeader.getHeaderSizeData().getSize(), k;
            readChannel.position(offset).read(spkLastBuffer.limit(headerSize).rewind());
            final MetaData[] metaDatas = new MetaData[dataSize];
            metaDatas[i] = new SPKHeader.MetaData();
            metaDatas[i].setOffset(j);
            metaDatas[i].setLongPosition(stcBuffer.getLong(j + stcBody.getFileStartPosData().getOffset() - 1));
            metaDatas[i].setLongSize(stcBuffer.getLong(j + stcBody.getFileSizeData().getOffset() - 1));
            metaDatas[i].setLongOffset(metaDatas[i].getLongPosition() + metaDatas[i].getLongSize());
            metaDatas[i].setPathSize(spkLastBuffer.getShort(spkBody.getPathSizeData().getOffset() - 1));
            spkLastBuffer.position(spkBody.getHeaderSizeData().getSize()).get(pathCache,0,metaDatas[i].getPathSize());
            metaDatas[i].setFilePath(get(new String(pathCache,0,metaDatas[0].getPathSize())));
            final IValue<Path> filePath = new SingleValue<>(null);
            for(;i < dataSize;i++){
                boolean hasNext = dataSize > i + 1;
                if(hasNext){
                    j += stcBody.getHeaderSizeData().getSize();
                    readChannel.position(metaDatas[i].getLongOffset()).read(spkNextBuffer.rewind());
                    metaDatas[i + 1] = new SPKHeader.MetaData();
                    metaDatas[i + 1].setOffset(j);
                    metaDatas[i + 1].setLongPosition(stcBuffer.getLong(j + stcBody.getFileStartPosData().getOffset() - 1));
                    metaDatas[i + 1].setLongSize(stcBuffer.getLong(j + stcBody.getFileSizeData().getOffset() - 1));
                    metaDatas[i + 1].setLongOffset(metaDatas[i + 1].getLongPosition() + metaDatas[i + 1].getLongSize());
                    metaDatas[i + 1].setPathSize(spkNextBuffer.getShort(spkBody.getPathSizeData().getOffset() - 1));
                    spkNextBuffer.position(spkBody.getHeaderSizeData().getSize()).get(pathCache,0,metaDatas[i + 1].getPathSize());
                    metaDatas[i + 1].setFilePath(get(new String(pathCache,0,metaDatas[i + 1].getPathSize())));
                }
                if(0 != deviation) stcBuffer.putLong(metaDatas[i].getOffset() + stcBody.getFileStartPosData().getOffset() - 1,metaDatas[i].getLongPosition());
                filePath.setValue(metaDatas[i].getFilePath());
                if(fileParam.getPathMap().entrySet().parallelStream().anyMatch(e->{
                    if(e.getValue().endsWith(filePath.getValue())){
                        filePath.setValue(e.getValue());
                        return true;
                    }
                    return false;
                })) try(FileChannel fileChannel = open(filePath.getValue(),StandardOpenOption.READ)){
                    long size1 = fileChannel.size(), size2 = metaDatas[i].getLongSize();
                    CS.checkError(ERR_SPK_FILE_SIZE,new String[]{filePath.getValue().toString(),SPK_MAX_SIZE_BIGGER + S_EMPTY},()->SPK_MAX_SIZE_BIGGER < size1);
                    metaDatas[i].setMod(true);
                    metaDatas[i].setSize((int)size1);
                    if(size1 != size2){
                        if(hasNext){
                            size2 = size1 + spkBody.getHeaderSizeData().getSize() + metaDatas[i + 1].getPathSize();
                            int mc = (int)(SPK_MODULUS - size2 % SPK_MODULUS) % SPK_MODULUS;
                            if(0 < mc) metaDatas[i + 1].setNulbytes(fillBytes(0,mc));
                            metaDatas[i].setLongDeviation(size2 + mc + metaDatas[i].getLongPosition() - metaDatas[i + 1].getLongPosition());
                            deviation += metaDatas[i].getLongDeviation();
                        }
                        stcBuffer.putLong(metaDatas[i].getOffset() + stcBody.getFileSizeData().getOffset() - 1,size1);
                        spkLastBuffer.position(spkBody.getFileSizeData().getOffset() - 1);
                        for(k = 0;k < spkBody.getFileSizeData().getSize();k++) spkLastBuffer.putInt(metaDatas[i].getSize());
                    }
                    if(nonNull(metaDatas[i].getNulbytes())) writeChannel.position(writeChannel.size()).write(spkLastBuffer.limit(fillBytes(spkLastCache,metaDatas[i].getNulbytes(),spkBody.getHeaderSizeData().getSize() + metaDatas[i].getPathSize())).rewind());
                    else{
                        writeChannel.position(writeChannel.size()).write(spkLastBuffer.limit(spkBody.getHeaderSizeData().getSize() + metaDatas[i].getPathSize()).rewind());
                        writeChannel.transferFrom(readChannel.position(offset + spkLastBuffer.limit()),writeChannel.position(writeChannel.size()).position(),metaDatas[i].getLongPosition() - offset - spkLastBuffer.limit());
                    }
                    writeChannel.transferFrom(fileChannel,writeChannel.position(writeChannel.size()).position(),size1);
                }
                else writeChannel.transferFrom(readChannel.position(offset),writeChannel.position(writeChannel.size()).position(),metaDatas[i].getLongOffset() - offset);
                offset = metaDatas[i].getLongOffset();
                spkLastBuffer.limit(fillBytes(spkLastCache,spkNextCache));
            }
            metaDatas[dataSize - 1].setLongOffset(offset);
            final byte[] spkCache = new byte[(int)(readChannel.size() - offset)];
            final ByteBuffer spkBuffer = wrap(spkCache).order(ByteOrder.LITTLE_ENDIAN);
            readChannel.position(offset).read(spkBuffer);
            for(i = j = 0;i < dataSize;i++){
                if(metaDatas[i].isMod()){
                    spkBuffer.position(j + spkList.getFileSizeData().getOffset() - 1);
                    for(k = 0;k < spkBody.getFileSizeData().getSize();k++) spkBuffer.putInt(metaDatas[i].getSize());
                }
                j = indexOfBytes(spkCache,spkList.getHeaderFlagData().getBytes(),j + spkList.getHeaderSizeData().getSize() + metaDatas[i].getPathSize(),false);
            }
            writeChannel.position(writeChannel.size()).write(spkBuffer.rewind());
            writeBinaryToFile(stcRepackPath,stcCache);
        }
    }
}
