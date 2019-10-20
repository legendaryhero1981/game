package legend.util.logic;

import static java.nio.file.Paths.get;
import static legend.intf.IValue.newInstance;
import static legend.util.FileUtil.existsPath;
import static legend.util.JaxbUtil.convertToXml;

import java.nio.file.Path;

import legend.util.intf.IFileUtil;
import legend.util.logic.intf.ILogic;
import legend.util.param.FileParam;

public abstract class BaseFileLogic implements ILogic<Path>,IFileUtil{
    protected FileParam param;

    protected BaseFileLogic(FileParam param){
        this.param = param;
    }

    protected Path initialize(String file, String info, Class<?> clazz){
        Path path = get(file);
        if(!existsPath(path)){
            param.getCmdOptional().ifPresent(c->convertToXml(path,newInstance(clazz)));
            param.getDetailOptional().ifPresent(c->CS.sl(info));
        }
        return path;
    }
}
