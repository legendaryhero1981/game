package legend.util.logic;

import java.nio.file.Path;

import legend.util.logic.intf.IFileMergeLogic;
import legend.util.param.FileParam;

public class FileMergeLogic implements IFileMergeLogic{
    private FileParam param;

    public FileMergeLogic(FileParam param){
        this.param = param;
    }

    @Override
    public void execute(Path path){
        final int size = param.getPathMap().size();
    }
}
