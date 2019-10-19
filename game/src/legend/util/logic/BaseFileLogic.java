package legend.util.logic;

import java.nio.file.Path;

import legend.util.logic.intf.ILogic;
import legend.util.param.FileParam;

public abstract class BaseFileLogic implements ILogic<Path>{
    protected FileParam param;
    
    protected BaseFileLogic(FileParam param){
        this.param = param;
    }
}
