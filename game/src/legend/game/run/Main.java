package legend.game.run;

import static java.io.File.createTempFile;
import static java.nio.file.Paths.get;
import static legend.intf.ICommon.gl;
import static legend.intf.ICommon.glph;
import static legend.intf.ICommon.gs;
import static legend.intf.ICommon.gsph;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ConsoleUtil.IN;
import static legend.util.JaxbUtil.convertToJavaBean;
import static legend.util.JaxbUtil.convertToXml;
import static legend.util.TimeUtil.runWithConsole;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import legend.game.run.entity.Game;
import legend.game.run.entity.Games;
import legend.game.run.intf.IMain;

public final class Main implements IMain{
    private static final Path config;
    private static final StringBuilder script;
    private static final String[] names;
    private static PrintStream PS;
    private static Game game;
    static{
        config = get(RUN_FILE_CONFIG);
        script = new StringBuilder();
        names = new String[]{"","","","",""};
        try{
            PS = new PrintStream(new File(RUN_FILE_LOG));
            CS.cacheStream(PS);
        }catch(FileNotFoundException e){
            CS.sl(gsph(ERR_LOG_FLE_CRT,RUN_FILE_LOG,e.toString()));
        }
        game = new Game();
    }

    public static void main(String[] args){
        runWithConsole(t->runCmd(args),args,HELP_RUN);
    }

    public static void runCmd(String[] args){
        dealParam(args);
        switch(args[0]){
            case CMD_EXEC:
            exec();
            break;
            case CMD_LINK:
            link();
            break;
            case CMD_LINK_ALL:
            linkAll();
            break;
            case CMD_VIEW:
            view();
            break;
            case CMD_DEL:
            del();
            break;
            case CMD_ADD:
            add();
            break;
            case CMD_CREATE:
            create();
        }
    }

    private static void exec(){
        try{
            // 根据ID获得要执行的游戏模型
            loadData();
            // 生成游戏执行前、游戏执行后及游戏进程监控需要执行的Bat脚本文件
            writeOtherScript();
            // 生成VBS主脚本文件
            writeMainScript();
            // 执行VBS主脚本文件
            Runtime.getRuntime().exec(names[names.length - 1]);
        }catch(IOException e){
            CS.showError(ERR_CREATE_FAIL,new String[]{e.toString()});
        }catch(Exception e){
            CS.showError(ERR_RUN_FAIL,new String[]{e.toString()});
        }
    }

    private static void linkAll(){
        // 数据验证
        List<Game> games = loadModel().getGames();
        games.parallelStream().forEach(g->CS.showError(ERR_VALIDATE,new String[]{RUN_FILE_CONFIG},()->g.trim().validate()));
        // 执行脚本批量生成游戏快捷方式
        games.parallelStream().forEach(g->{
            try{
                runLinkScript(g);
            }catch(IOException e){
                CS.showError(ERR_CREATE_FAIL,new String[]{e.toString()});
            }catch(Exception e){
                CS.showError(ERR_RUN_FAIL,new String[]{e.toString()});
            }
        });
    }

    private static void link(){
        try{
            loadData();
            runLinkScript(game);
        }catch(IOException e){
            CS.showError(ERR_CREATE_FAIL,new String[]{e.toString()});
        }catch(Exception e){
            CS.showError(ERR_RUN_FAIL,new String[]{e.toString()});
        }
    }

    private static void view(){
        ConcurrentMap<String,Game> gameMap = loadModel().getGameMap();
        gameMap.values().parallelStream().forEach(game->CS.s(gl(game.toString(),2)));
    }

    private static void del(){
        Games games = loadModel();
        CS.showError(ERR_ID_NON,new String[]{RUN_FILE_CONFIG,game.getId()},()->!games.getGameMap().containsKey(game.getId()));
        games.getGames().remove(games.getGameMap().get(game.getId()));
        saveModel(games);
    }

    private static void add(){
        Games games = loadModel();
        CS.showError(ERR_ID_EXISTS,new String[]{RUN_FILE_CONFIG,game.getId()},()->games.getGameMap().containsKey(game.getId()));
        games.getGames().add(game);
        saveModel(games);
    }

    private static void create(){
        Games games = new Games();
        games.setComment(GAMES_COMMENT);
        games.getGames().add(game);
        saveModel(games);
    }

    private static void loadData(){
        ConcurrentMap<String,Game> gameMap = loadModel().getGameMap();
        String id = game.getId();
        if(isEmpty(id)){
            gameMap.values().parallelStream().forEach(g->CS.sl(g.toString()));
            CS.l(1);
            do{
                CS.s(ST_CHOICE_ID);
                id = IN.nextLine();
                CS.sl(false,id);
            }while(isEmpty(game = gameMap.get(id)));
            CS.l(1);
        }else game = gameMap.get(id);
        // 数据验证
        CS.showError(ERR_ID_NON,new String[]{RUN_FILE_CONFIG,id},()->isEmpty(game));
        CS.showError(ERR_EXE_NUL,new String[]{RUN_FILE_CONFIG,game.getId()},()->game.trim().validate());
        // 处理等待时间
        Pattern pattern = Pattern.compile(REG_TIME);
        Matcher matcher = pattern.matcher(game.getBeforeWait());
        if(!matcher.matches()) game.setBeforeWait(WAIT_TIME);
        if(!matcher.reset(game.getAfterWait()).matches()) game.setAfterWait(WAIT_TIME);
        if(!matcher.reset(game.getWatchWait()).matches()) game.setWatchWait(WAIT_TIME);
    }

    private static void dealParam(String[] args){
        try{
            switch(args[0]){
                case CMD_EXEC:
                case CMD_VIEW:
                case CMD_LINK:
                case CMD_LINK_ALL:
                if(args.length > 1) game.setId(args[1]);
                break;
                case CMD_DEL:
                game.setId(args[1]);
                break;
                case CMD_ADD:
                case CMD_CREATE:
                game.setId(args[1]);
                game.setPath(args[2]);
                game.setExe(args[3]);
                game.setName(args[4]);
                if(args.length > 5) game.setComment(args[5]);
                else game.setComment(game.getName());
                break;
                default:
                CS.showError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT});
            }
            game.trim();
        }catch(Exception e){
            CS.showError(ERR_CMD_EXEC,new String[]{e.toString()});
        }
    }

    private static void saveModel(Games games){
        convertToXml(config,games);
    }

    private static Games loadModel(){
        CS.showError(ERR_CONFIG_NON,null,()->!config.toFile().isFile());
        Games games = convertToJavaBean(config,Games.class);
        CS.showError(ERR_CONFIG_NUL,null,()->isEmpty(games.getGames()));
        return games;
    }

    private static void runLinkScript(Game game) throws Exception{
        String[] names = new String[2];
        File vbsFile = createTempFile(FILE_PREFIX,FILE_SUFFIX_VBS);
        StringBuilder script = new StringBuilder(gl(CMD_VBS_SH_INIT,1) + gl(CMD_VBS_WMI_INIT,1));
        names[0] = vbsFile.getCanonicalPath();
        script.append(glph(CMD_VBS_SC,1,game.getName()));
        script.append(glph(CMD_VBS_SC_ARG,1,game.getId()));
        script.append(glph(CMD_VBS_SC_IL,1,game.getPath(),nonEmpty(game.getIcon()) ? game.getIcon() : game.getExe() + FILE_SUFFIX_EXE));
        script.append(glph(CMD_VBS_SC_DESC,1,game.getComment()));
        script.append(glph(CMD_VBS_SC_WD,1,game.getPath()));
        script.append(glph(CMD_VBS_SC_TP,1));
        script.append(gl(CMD_VBS_SC_WS,1));
        script.append(gl(CMD_VBS_SC_SAVE,1));
        script.append(glph(CMD_VBS_RUN_DEL,1,names[0]));
        script.append(glph(CMD_VBS_SLEEP,1,SLEEP_TIME));
        names[1] = script.toString();
        CS.sl(names[0]).sl(names[1]);
        write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(vbsFile),ENCODING_GBK)),names[1]);
        names[1] = gsph(CMD_CS_RUN,names[0]);
        CS.s(gl(names[1],2));
        Runtime.getRuntime().exec(names[1]).waitFor();
    }

    private static void writeMainScript() throws IOException{
        File vbsFile = createTempFile(FILE_PREFIX,FILE_SUFFIX_VBS);
        script.append(gl(CMD_VBS_SH_INIT,1));
        if(nonEmpty(names[1])) script.append(glph(CMD_VBS_RUN,1,names[1])).append(glph(CMD_VBS_RUN_DEL,1,names[1])).append(glph(CMD_VBS_SLEEP,1,countWaitTime(game.getBeforeWait())));
        if(isEmpty(game.getAgentExe())) script.append(glph(CMD_VBS_RUN_GAME,1,game.getPath(),game.getExe(),game.getArgs()));
        else if(isEmpty(game.getAgentPath())) script.append(glph(CMD_VBS_RUN_GAME,1,game.getPath(),game.getAgentExe(),game.getAgentArgs()));
        else script.append(glph(CMD_VBS_RUN_GAME,1,game.getAgentPath(),game.getAgentExe(),game.getAgentArgs()));
        if(nonEmpty(game.getPriority())) script.append(glph(CMD_VBS_SLEEP,1,countWaitTime(WAIT_TIME))).append(glph(CMD_VBS_RUN_PROC,1,game.getExe(),game.getPriority()));
        if(nonEmpty(names[2])) script.append(glph(CMD_VBS_SLEEP,1,countWaitTime(game.getAfterWait()))).append(glph(CMD_VBS_RUN,1,names[2])).append(glph(CMD_VBS_RUN_DEL,1,names[2]));
        if(nonEmpty(names[3])) script.append(glph(CMD_VBS_SLEEP,1,countWaitTime(WAIT_TIME))).append(glph(CMD_VBS_RUN,1,names[3])).append(glph(CMD_VBS_RUN_DEL,1,names[3]));
        names[0] = vbsFile.getCanonicalPath();
        script.append(glph(CMD_VBS_RUN_DEL,1,names[0]));
        names[names.length - 1] = script.toString();
        CS.sl(names[0]).sl(names[names.length - 1]);
        write(new BufferedWriter(new FileWriter(vbsFile)),names[names.length - 1]);
        names[names.length - 1] = gsph(CMD_CS_RUN,names[0]);
        CS.s(gl(names[names.length - 1],2));
    }

    private static void writeOtherScript() throws IOException{
        Pattern pattern = Pattern.compile(REG_SPRT_CMD);
        writeScript(pattern.split(game.getBefore()),1);
        writeScript(pattern.split(game.getAfter()),2);
        if(nonEmpty(game.getWatches()) && nonEmpty(game.getWatches().get(0))){
            File batFile = createTempFile(FILE_PREFIX,FILE_SUFFIX_BAT);
            StringBuilder builder = new StringBuilder();
            game.getWatches().forEach(watch->{
                watch = watch.replaceAll(REG_SPRT_PATH,gs(SPRT_FILE,4));
                if(watch.contains(SPRT_FILE)) builder.append(glph(CMD_BAT_PROC_DEL_BY_PATH,1,watch));
                else builder.append(glph(CMD_BAT_PROC_DEL_BY_NAME,1,watch));
            });
            script.append(glph(CMD_BAT_WATCH,1,game.getExe() + FILE_SUFFIX_EXE,game.getWatchWait(),builder.toString()));
            names[3] = batFile.getCanonicalPath();
            names[names.length - 1] = script.toString();
            CS.sl(names[3]).sl(names[names.length - 1]);
            write(new BufferedWriter(new FileWriter(batFile)),names[names.length - 1]);
            script.delete(0,script.length());
        }
    }

    private static void writeScript(String[] cmd, int index) throws IOException{
        if(nonEmpty(cmd[0])){
            File batFile = createTempFile(FILE_PREFIX,FILE_SUFFIX_BAT);
            for(int i = 0;i < cmd.length;i++)
                script.append(gl(cmd[i].trim(),1));
            names[index] = batFile.getCanonicalPath();
            names[names.length - 1] = script.toString();
            CS.sl(names[index]).sl(names[names.length - 1]);
            write(new BufferedWriter(new FileWriter(batFile)),names[names.length - 1]);
            script.delete(0,script.length());
        }
    }

    private static void write(BufferedWriter writer, String s) throws IOException{
        writer.write(s);
        writer.flush();
        writer.close();
    }

    private static String countWaitTime(String time){
        return String.valueOf(Integer.parseInt(time) * 1000);
    }
}
