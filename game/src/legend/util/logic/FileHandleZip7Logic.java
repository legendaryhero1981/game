package legend.util.logic;

import static java.util.Arrays.asList;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.ProcessUtil.handleProcess;
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
        CS.showError(ERR_FLE_ANLS,asList(()->path.toString(),()->zip7.getErrorInfo()),()->!zip7.trim().validate());
        final float amount = zip7.getCmds().size(), scale = 1 / param.getPathMap().size();
        zip7.getCmds().stream().forEach(cmd->{
            handleProcess(cmd.toArray(new String[0]));
            param.getProgressOptional().ifPresent(c->PG.update(PG.countUpdate(amount,1,scale),PROGRESS_SCALE));
        });
    }
}
