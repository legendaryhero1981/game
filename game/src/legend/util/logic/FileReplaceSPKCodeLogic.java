package legend.util.logic;

import static java.nio.file.Paths.get;
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
        if(existsPath(path)){
            param.getCmdOptional().ifPresent(c->convertToXml(path,new FileSPK()));
            param.getDetailOptional().ifPresent(c->CS.sl(ST_FILE_SPK_CONF));
        }
    }

    @Override
    public void execute(Path path){
        CS.showError(ERR_CONF_SPK_NON,new String[]{path.toString()},()->!path.toFile().isFile());
        FileSPK fileSPK = convertToObject(path,FileSPK.class);
        CS.showError(ERR_CONF_SPKC_NODE_NON,new String[]{path.toString()},()->!fileSPK.trim().validate());
        
    }
}
