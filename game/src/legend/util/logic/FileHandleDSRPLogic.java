package legend.util.logic;

import static java.util.Arrays.asList;
import static legend.util.JaxbUtil.convertToObject;

import java.nio.file.Path;

import legend.util.entity.DSRP;
import legend.util.entity.intf.IDSRP;
import legend.util.param.FileParam;

public class FileHandleDSRPLogic extends BaseFileLogic implements IDSRP{
    public FileHandleDSRPLogic(FileParam param){
        super(param);
        initialize(CONF_FILE_DSRP,ST_FILE_DSRP_CONF,DSRP.class);
    }
    
    @Override
    public void execute(Path path){
        DSRP dsrp = convertToObject(path,DSRP.class);
        CS.checkError(ERR_FLE_ANLS,asList(()->path.toString(),()->dsrp.getErrorInfo()),()->!dsrp.trim().validate());
        final float amount = dsrp.getTasks().size();
        dsrp.getTasks().parallelStream().forEach(task->{
            
        });
    }
}
