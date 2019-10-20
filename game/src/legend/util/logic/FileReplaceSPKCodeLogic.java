package legend.util.logic;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static legend.util.FileUtil.existsPath;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.JaxbUtil.convertToXml;

import java.nio.file.Path;

import legend.util.entity.FileSPK;
import legend.util.entity.intf.IFileSPK;
import legend.util.param.FileParam;

public class FileReplaceSPKCodeLogic extends BaseFileLogic implements IFileSPK{
    public FileReplaceSPKCodeLogic(FileParam param){
        super(param);
        Path path = get(CONF_FILE_SPK);
        if(!existsPath(path)){
            param.getCmdOptional().ifPresent(c->convertToXml(path,new FileSPK()));
            param.getDetailOptional().ifPresent(c->CS.sl(ST_FILE_SPK_CONF));
        }
    }

    @Override
    public void execute(Path path){
        FileSPK fileSPK = convertToObject(path,FileSPK.class);
        CS.showError(ERR_FLE_ANLS,asList(()->path.toString(),()->fileSPK.getErrorInfo()),()->!fileSPK.trim().validate());
    }
}
