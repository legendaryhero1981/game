package legend.util.logic;

import static java.nio.file.Paths.get;
import static legend.util.FileUtil.existsPath;
import static legend.util.JaxbUtil.convertToXml;

import java.nio.file.Path;

import legend.util.entity.FileMerge;
import legend.util.entity.intf.IFileSPK;
import legend.util.logic.intf.ILogic;
import legend.util.param.FileParam;

public class FileReplaceSPKCodeLogic implements IFileSPK,ILogic<Path>{
    private FileParam param;

    public FileReplaceSPKCodeLogic(FileParam param){
        this.param = param;
        Path path = get(CONF_FILE_SPK);
        if(!existsPath(path)){
            convertToXml(path,new FileMerge());
            param.getDetailOptional().ifPresent(c->CS.sl(ST_FILE_SPK_CONF));
        }
    }

    @Override
    public void execute(Path path){
        
    }
}
