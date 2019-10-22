package legend.util.logic;

import static java.nio.ByteBuffer.wrap;
import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static legend.util.FileUtil.dealFiles;
import static legend.util.FileUtil.readFormBinaryFile;
import static legend.util.FileUtil.writeToBinaryFile;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.StringUtil.bytesIndexOfBytes;
import static legend.util.StringUtil.gsph;

import java.nio.ByteBuffer;
import java.nio.file.Path;

import legend.util.entity.FileSPK;
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
                fp.setCmd(CMD_FIND);
                fp.setPattern(compile(spkCode.getQueryRegex()));
                fp.setSrcPath(get(spkCode.getUnpackPath()));
                dealFiles(fp);
                byte[] stcCache = readFormBinaryFile(get(spkCode.getFilePath(),spkCode.getFileName() + EXT_STC));
                byte[] spkCache = readFormBinaryFile(get(spkCode.getFilePath(),spkCode.getFileName() + EXT_SPK));
                ByteBuffer stcBuffer = wrap(stcCache);
                STCFormat stcFormat = spkCode.getStcFormat();
                SPKHeader headerInfo = stcFormat.getHeaderInfo();
                SPKHeader bodyInfo = stcFormat.getBodyInfo();
                SPKHeader listInfo = stcFormat.getListInfo();
                int index = bytesIndexOfBytes(stcCache,listInfo.getFlagData().getBytes(),true) + listInfo.getSizeData().getSize();
                String[] filePaths = new String(stcCache,index,stcCache.length - index - 1,CHARSET_UTF8).split(SPC_NUL);
                MetaData[] metaDatas = new MetaData[filePaths.length];
                fp.getPathMap().entrySet().parallelStream().forEach(e->{
                    for(int i = 0,j = headerInfo.getSizeData().getSize(),k = bodyInfo.getSizeData().getSize();i < filePaths.length;i++)
                        if(e.getValue().endsWith(get(filePaths[i]))){
                            MetaData metaData = new SPKHeader.MetaData();
                            int offset = j + i * k;
                            int size = (int)e.getKey().size();
                            metaData.setOffset(offset);
                            metaData.setSize(size);
                            size -= stcBuffer.getInt(offset + bodyInfo.getFileSizeData().getOffset() - 1);
                            metaData.setPosition(stcBuffer.getInt(offset + bodyInfo.getFileStartPosData().getOffset() - 1) + size);
                            metaData.setDeviation(size);
                            metaData.setMod(true);
                            metaDatas[i] = metaData;
                            return;
                        }
                });
                
                writeToBinaryFile(get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_STC),stcCache);
                writeToBinaryFile(get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_SPK),spkCache);
            }catch(Exception e){
                CS.sl(gsph(ERR_INFO,e.toString()));
            }
        });
    }
}
