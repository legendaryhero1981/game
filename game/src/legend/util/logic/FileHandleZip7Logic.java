package legend.util.logic;

import static legend.util.JaxbUtil.convertToObject;

import java.nio.file.Path;

import legend.util.entity.Zip7;
import legend.util.entity.intf.IZip7;
import legend.util.param.FileParam;

public class FileHandleZip7Logic extends BaseFileLogic implements IZip7{
    public FileHandleZip7Logic(FileParam param){
        super(param);
        initialize(CONF_FILE_7ZIP,ST_FILE_SPK_CONF,Zip7.class);
    }

    @Override
    public void execute(Path path){
        Zip7 zip7 = convertToObject(path,Zip7.class);
        String exec = zip7.getZip7ExecutablePath();
        CS.sl(exec);
    }
}
