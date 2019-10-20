package legend.util.logic;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static legend.util.FileUtil.dealFiles;
import static legend.util.FileUtil.readFormBinaryFile;
import static legend.util.FileUtil.writeToBinaryFile;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.StringUtil.gsph;

import java.nio.file.Path;

import legend.util.entity.FileSPK;
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
                byte[] spkCache = readFormBinaryFile(get(spkCode.getFilePath(),spkCode.getFileName() + EXT_SPK));
                byte[] stcCache = readFormBinaryFile(get(spkCode.getFilePath(),spkCode.getFileName() + EXT_STC));
                
                writeToBinaryFile(get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_SPK),spkCache);
                writeToBinaryFile(get(spkCode.getRepackPath(),spkCode.getFileName() + EXT_STC),stcCache);
            }catch(Exception e){
                CS.sl(gsph(ERR_INFO,e.toString()));
            }
        });
    }
}
