package legend.util.logic;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static legend.util.FileUtil.dealFiles;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.ProcessUtil.handleProcess;
import static legend.util.StringUtil.collectionToArray;
import static legend.util.StringUtil.concat;
import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.glph;
import static legend.util.StringUtil.gsph;
import static legend.util.TimeUtil.getDurationString;
import static legend.util.ValueUtil.nonEmpty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;

import legend.util.FileUtil.PathComparator;
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
        final String dataExecPath = dsrp.getDataRepackerExecutablePath(), dcxExecPath = dsrp.getDcxRepackerExecutablePath();
        final float amount = dsrp.getTasks().size(), scale = 1 / param.getPathMap().size();
        dsrp.getTasks().parallelStream().forEach(task->{
            final String mode = task.getMode();
            final FileParam dataParam = task.getDataParam();
            final FileParam dcxParam = task.getDcxParam();
            final StringBuilder builder = new StringBuilder();
            if(MODE_ZIP.equals(mode)){
                exec(dataParam,dataExecPath,builder,param->{
                    Collection<Path> pathsCache = new ConcurrentLinkedQueue<>();
                    param.getPathsCache().parallelStream().forEach(p1->{
                        Path p2 = get(p1.toString().replaceAll(REG_DSRP_DOT,REP_DSRP_HYPHEN));
                        if(p2.toFile().isDirectory()) pathsCache.add(p2);
                        else pathsCache.add(p1);
                    });
                    param.getPathsCache().clear();
                    param.getPathsCache().addAll(pathsCache.parallelStream().sorted(new PathComparator(false)).collect(toList()));
                });
                exec(dcxParam,dcxExecPath,builder);
            }else{
                exec(dcxParam,dcxExecPath,builder);
                final Set<Path> pathSet = new ConcurrentSkipListSet<>();
                for(int size = -1;size != pathSet.size();){
                    size = pathSet.size();
                    exec(dataParam,dataExecPath,builder,param->{
                        if(!pathSet.isEmpty()){
                            Collection<Path> pathsCache = new ConcurrentLinkedQueue<>();
                            param.getPathsCache().parallelStream().filter(p->!pathSet.contains(p)).forEach(p->{
                                pathSet.add(p);
                                pathsCache.add(p);
                            });
                            param.getPathsCache().clear();
                            param.getPathsCache().addAll(pathsCache);
                        }else pathSet.addAll(param.getPathsCache());
                    });
                }
            }
            param.getDetailOptional().ifPresent(c->CS.s(builder.toString()));
            param.getProgressOptional().ifPresent(c->PG.update(PG.countUpdate(amount,1,scale),PROGRESS_SCALE));
        });
    }

    @SafeVarargs
    private void exec(FileParam param, String path, StringBuilder builder, Consumer<FileParam>... consumers){
        dealFiles(param);
        if(nonEmpty(consumers)) for(Consumer<FileParam> consumer : consumers) consumer.accept(param);
        String[] cmds = collectionToArray(param.getPathsCache(),path,null);
        if(2 > cmds.length) return;
        String cmd = concat(cmds,S_SPACE,true);
        builder.append(gl(1) + glph(ST_PRG_EXTN_START,cmd));
        String duration = getDurationString(t->handleProcess(process->{
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),CHARSET_GBK))){
                reader.lines().forEach(line->builder.append(line + SPRT_LINE));
            }catch(Exception e){
                builder.append(glph(ERR_EXEC_CMD_SPEC,cmd,e.toString()));
            }
        },cmds));
        builder.append(gl(1) + gsph(ST_PRG_EXTN_DONE,cmd) + N_TIME + S_COLON + duration + S_PERIOD + gl(1));
    }
}
