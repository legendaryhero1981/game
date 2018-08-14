package legend.game.poe2;

import static java.nio.file.Paths.get;
import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.CS;
import static legend.util.FileUtil.dealFiles;
import static legend.util.JsonUtil.formatJson;
import static legend.util.JsonUtil.trimJson;
import static legend.util.TimeUtil.runWithConsole;

import legend.game.poe2.intf.IMain;
import legend.util.MD5Util;
import legend.util.ProgressUtil;
import legend.util.intf.IFileUtil;
import legend.util.intf.IProgress;
import legend.util.param.FileParam;

public class Main implements IMain{
    private static final IProgress progress;
    private static final FileParam srcParam;
    static{
        progress = ProgressUtil.ConsoleProgress();
        srcParam = new FileParam();
        srcParam.setOpt(OPT_INSIDE);
    }

    public static void main(String[] args){
        runWithConsole(t->runCmd(args),args,HELP_POE);
    }

    public static void runCmd(String[] args){
        dealParam(args);
        switch(args[0]){
            case POE_DATA_ENCODE:
            progress.runUntillFinish(Main::encodeJson);
            break;
            case POE_DATA_DECODE:
            progress.runUntillFinish(Main::decodeJson);
            break;
            case POE_OBJ_GUID:
            showGuid();
        }
    }

    private static void showGuid(){
        CS.s(MD5Util.getGuidL32(srcParam.getReplacement())).l(2);
    }

    private static void encodeJson(IProgress progress){
        loadData(progress);
        srcParam.getPathMap().values().parallelStream().forEach(path->{
            trimJson(path);
            progress.update(1);
        });
    }

    private static void decodeJson(IProgress progress){
        loadData(progress);
        srcParam.getPathMap().values().parallelStream().forEach(path->{
            formatJson(path);
            progress.update(1);
        });
    }

    private static void loadData(IProgress progress){
        progress.reset(1,1,1);
        dealFiles(srcParam);
        progress.reset(srcParam.getPathMap().size());
    }

    private static void dealParam(String[] args){
        try{
            switch(args[0]){
                case POE_DATA_ENCODE:
                case POE_DATA_DECODE:
                srcParam.setCmd(IFileUtil.CMD_FIND);
                srcParam.setPattern(compile(args[1]));
                srcParam.setSrcPath(get(args[2]));
                break;
                case POE_OBJ_GUID:
                srcParam.setReplacement(args[1]);
                break;
                default:
                CS.showError(ERR_ARG_ANLS,new String[]{ST_ARG_ERR});
            }
        }catch(Exception e){
            CS.showError(ERR_CMD_EXEC,new String[]{e.toString()});
        }
    }
}
