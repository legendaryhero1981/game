package legend.util.logic;

import static java.util.Arrays.asList;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.ProcessUtil.handleProcess;
import static legend.util.StringUtil.concat;
import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.glph;
import static legend.util.StringUtil.gsph;
import static legend.util.TimeUtil.getDurationString;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        CS.checkError(ERR_FLE_ANLS,asList(()->path.toString(),()->zip7.getErrorInfo()),()->!zip7.trim().validate());
        final float amount = zip7.getCmds().size(), scale = 1 / param.getPathMap().size();
        zip7.getCmds().parallelStream().forEach(cmd->{
            final String cmdString = concat(cmd,S_SPACE);
            final StringBuilder builder = new StringBuilder(gl(1) + glph(ST_PRG_EXTN_START,cmdString));
            final String duration = getDurationString(t->handleProcess(process->{
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),CHARSET_GBK))){
                    reader.lines().forEach(line->builder.append(line + SPRT_LINE));
                }catch(Exception e){
                    builder.append(glph(ERR_EXEC_CMD_SPEC,cmdString,e.toString()));
                }
            },cmd));
            builder.append(gl(1) + gsph(ST_PRG_EXTN_DONE,cmdString) + N_TIME + S_COLON + duration + S_PERIOD);
            param.getDetailOptional().ifPresent(c->CS.sl(builder.toString()));
            param.getProgressOptional().ifPresent(c->PG.update(PG.countUpdate(amount,1,scale),PROGRESS_SCALE));
        });
    }
}
