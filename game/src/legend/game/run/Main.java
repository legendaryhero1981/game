package legend.game.run;

import static java.io.File.createTempFile;
import static java.lang.Thread.sleep;
import static java.nio.file.Paths.get;
import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ConsoleUtil.IN;
import static legend.util.JaxbUtil.convertToJavaBean;
import static legend.util.JaxbUtil.convertToXml;
import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.glph;
import static legend.util.StringUtil.gs;
import static legend.util.StringUtil.gsph;
import static legend.util.TimeUtil.runWithConsole;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import legend.game.run.entity.Game;
import legend.game.run.entity.Games;
import legend.game.run.intf.IMain;

public final class Main implements IMain{
    private static final Path config;
    private static final StringBuilder script;
    private static final String[] caches;
    private static PrintStream PS;
    private static File vbsFile;
    private static Game game;
    static{
        config = get(RUN_FILE_CONFIG);
        script = new StringBuilder();
        caches = new String[]{"","","","",""};
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
            case CMD_KILL:
            kill();
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

    private static void dealParam(String[] args){
        try{
            switch(args[0]){
                case CMD_EXEC:
                case CMD_KILL:
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

    private static void exec(){
        try{
            // 根据ID获得需要被执行的游戏
            loadData();
            // 处理游戏进程的优先级数值，游戏执行前、游戏执行后需要执行的BAT脚本以及游戏进程监控的等待时间
            dealIntegerValues();
            // 生成游戏执行前、游戏执行后需要执行的BAT脚本文件
            writeOtherScript();
            // 执行启动游戏进程的VBS主脚本文件
            runVbsScript(false,t->writeMainScript());
        }catch(IOException e){
            CS.showError(ERR_CREATE_FAIL,new String[]{e.toString()});
        }catch(Exception e){
            CS.showError(ERR_RUN_FAIL,new String[]{e.toString()});
        }
    }

    private static void kill(){
        try{
            // 根据ID获得需要被终止的游戏
            loadData();
            // 执行终止游戏进程的VBS主脚本文件
            runVbsScript(false,t->script.append(glph(CMD_VBS_GAME_KILL,1,game.getExe())));
        }catch(IOException e){
            CS.showError(ERR_CREATE_FAIL,new String[]{e.toString()});
        }catch(Exception e){
            CS.showError(ERR_RUN_FAIL,new String[]{e.toString()});
        }
    }

    private static void linkAll(){
        // 数据验证
        List<Game> games = loadModel().getGames();
        games.parallelStream().forEach(g->CS.showError(ERR_INVALIDATE,new String[]{RUN_FILE_CONFIG},()->g.trim().validate()));
        // 执行脚本批量生成游戏快捷方式
        try{
            runVbsScript(true,t->{
                script.append(gl(CMD_VBS_SC_INIT,1));
                games.stream().forEach(g->cacheLinkScript(g));
            });
            sleep(SLEEP_TIME);
        }catch(IOException e){
            CS.showError(ERR_CREATE_FAIL,new String[]{e.toString()});
        }catch(Exception e){
            CS.showError(ERR_RUN_FAIL,new String[]{e.toString()});
        }
    }

    private static void link(){
        try{
            loadData();
            runVbsScript(true,t->{
                script.append(gl(CMD_VBS_SC_INIT,1));
                cacheLinkScript(game);
            });
            sleep(SLEEP_TIME);
        }catch(IOException e){
            CS.showError(ERR_CREATE_FAIL,new String[]{e.toString()});
        }catch(Exception e){
            CS.showError(ERR_RUN_FAIL,new String[]{e.toString()});
        }
    }

    private static void view(){
        loadModel().sortGames().stream().forEach(game->CS.s(gl(game.toString(),2)));
    }

    private static void del(){
        Games games = loadModel();
        CS.showError(ERR_ID_NON,new String[]{RUN_FILE_CONFIG,game.getId()},()->!games.getGameMap().containsKey(game.getId()));
        games.sortGames().remove(games.getGameMap().get(game.getId()));
        saveModel(games);
    }

    private static void add(){
        Games games = loadModel();
        CS.showError(ERR_ID_EXISTS,new String[]{RUN_FILE_CONFIG,game.getId()},()->games.getGameMap().containsKey(game.getId()));
        games.getGames().add(game);
        games.sortGames();
        saveModel(games);
    }

    private static void create(){
        Games games = new Games();
        games.getGames().add(game);
        saveModel(games);
    }

    private static void loadData(){
        ConcurrentMap<String,Game> gameMap = loadModel().getGameMap();
        String id = game.getId();
        if(isEmpty(id)){
            view();
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
    }

    private static void saveModel(Games games){
        convertToXml(config,games);
    }

    private static Games loadModel(){
        CS.showError(ERR_CONFIG_NON,null,()->!config.toFile().isFile());
        Games games = convertToJavaBean(config,Games.class);
        CS.showError(ERR_CONFIG_NUL,null,()->isEmpty(games.getGames()));
        ConcurrentMap<String,AtomicInteger> gameMap = new ConcurrentHashMap<>();
        games.getGames().parallelStream().forEach(g->gameMap.computeIfAbsent(g.getId(),k->new AtomicInteger()).addAndGet(1));
        gameMap.entrySet().parallelStream().filter(entry->entry.getValue().get() > 1).forEach(entry->CS.s(glph(ST_REPEAT_ID,2,entry.getKey())));
        CS.showError(ERR_CONFIG_REPEAT,null,()->gameMap.values().parallelStream().anyMatch(v->v.get() > 1));
        return games;
    }

    private static void cacheLinkScript(Game game){
        script.append(glph(CMD_VBS_SC_CRT,1,game.getName()));
        script.append(glph(CMD_VBS_SC_ARG,1,game.getId()));
        script.append(glph(CMD_VBS_SC_IL,1,game.getPath(),nonEmpty(game.getIcon()) ? game.getIcon() : game.getExe() + FILE_SUFFIX_EXE));
        script.append(glph(CMD_VBS_SC_DESC,1,game.getComment()));
        script.append(glph(CMD_VBS_SC_WD,1,game.getPath()));
        script.append(gl(CMD_VBS_SC_TP,1));
        script.append(gl(CMD_VBS_SC_WS,1));
        script.append(gl(CMD_VBS_SC_SAVE,1));
    }

    private static void runVbsScript(boolean waitFor, Consumer<Object> consumer) throws Exception{
        createVbsFile();
        consumer.accept(null);
        writeVbsFile();
        runVbsFile(waitFor);
    }

    private static void writeMainScript(){
        if(nonEmpty(caches[1])) script.append(glph(CMD_VBS_RUN,1,caches[1])).append(glph(CMD_VBS_RUN_DEL,1,caches[1])).append(glph(CMD_VBS_SLEEP,1,countWaitTime(game.getBeforeWait())));
        Matcher matcher = compile(REG_PATH_NAME).matcher(game.getAgentExecutablePath());
        if(!isEmpty(game.getAgentExecutablePath()) && matcher.find()) script.append(glph(CMD_VBS_RUN_AGENT,1,matcher.group(1),matcher.group(2),game.getAgentArgs()));
        else script.append(glph(CMD_VBS_RUN_GAME,1,game.getPath(),game.getExe(),game.getArgs()));
        if(nonEmpty(game.getPriority())) script.append(glph(CMD_VBS_SLEEP,1,countWaitTime(WAIT_TIME))).append(glph(CMD_VBS_GAME_PRIORITY,1,game.getExe(),game.getPriority()));
        if(nonEmpty(caches[2])) script.append(glph(CMD_VBS_SLEEP,1,countWaitTime(game.getAfterWait()))).append(glph(CMD_VBS_RUN,1,caches[2])).append(glph(CMD_VBS_RUN_DEL,1,caches[2]));
        // BAT脚本方式实现进程监控
        // if(nonEmpty(caches[3]))
        // script.append(glph(CMD_VBS_SLEEP,1,countWaitTime(WAIT_TIME))).append(glph(CMD_VBS_RUN,1,caches[3])).append(glph(CMD_VBS_RUN_DEL,1,caches[3]));
        // VBS脚本方式实现进程监控
        if(nonEmpty(game.getWatches()) && nonEmpty(game.getWatches().get(0))){
            StringBuilder names = new StringBuilder(), paths = new StringBuilder();
            game.getWatches().stream().forEach(watch->{
                watch = watch.replaceAll(REG_SPRT_PATH,gs(SPRT_FILE,4));
                if(watch.contains(SPRT_FILE)) paths.append(watch + SPRT_CMD);
                else names.append(watch + SPRT_CMD);
            });
            script.append(glph(CMD_VBS_GAME_WATCH,1,countWaitTime(game.getWatchWait()),game.getExe(),names.toString(),paths.toString()));
        }
    }

    private static void writeOtherScript() throws IOException{
        Pattern pattern = compile(REG_SPRT_CODE);
        writeScript(pattern.split(game.getBefore()),1);
        writeScript(pattern.split(game.getAfter()),2);
        // BAT脚本方式实现进程监控
        // if(nonEmpty(game.getWatches()) && nonEmpty(game.getWatches().get(0))){
        // File batFile = createTempFile(FILE_PREFIX,FILE_SUFFIX_BAT);
        // StringBuilder builder = new StringBuilder();
        // game.getWatches().stream().forEach(watch->{
        // watch = watch.replaceAll(REG_SPRT_PATH,gs(SPRT_FILE,4));
        // if(watch.contains(SPRT_FILE)) builder.append(glph(CMD_BAT_PROC_DEL_BY_PATH,1,watch));
        // else builder.append(glph(CMD_BAT_PROC_DEL_BY_NAME,1,watch));
        // });
        // script.append(glph(CMD_BAT_GAME_WATCH,1,game.getExe() +
        // FILE_SUFFIX_EXE,game.getWatchWait(),builder.toString()));
        // caches[3] = batFile.getCanonicalPath();
        // caches[caches.length - 1] = script.toString();
        // CS.sl(caches[3]).sl(caches[caches.length - 1]);
        // write(new BufferedWriter(new FileWriter(batFile)),caches[caches.length - 1]);
        // script.delete(0,script.length());
        // }
    }

    private static void writeScript(String[] cmds, int index) throws IOException{
        if(nonEmpty(cmds[0])){
            File batFile = createTempFile(FILE_PREFIX,FILE_SUFFIX_BAT);
            for(String cmd : cmds)
                script.append(gl(cmd.trim(),1));
            caches[index] = batFile.getCanonicalPath();
            caches[caches.length - 1] = script.toString();
            CS.sl(caches[index]).sl(caches[caches.length - 1]);
            write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(batFile),CHARSET_UTF8)),caches[caches.length - 1]);
            script.delete(0,script.length());
        }
    }

    private static void write(BufferedWriter writer, String s) throws IOException{
        writer.write(s);
        writer.flush();
        writer.close();
    }

    private static void createVbsFile() throws IOException{
        vbsFile = createTempFile(FILE_PREFIX,FILE_SUFFIX_VBS);
        caches[0] = vbsFile.getCanonicalPath();
        script.append(gl(CMD_VBS_SH_INIT,1));
    }

    private static void writeVbsFile() throws IOException{
        script.append(glph(CMD_VBS_RUN_DEL,1,caches[0]));
        caches[caches.length - 1] = script.toString();
        CS.sl(caches[0]).sl(caches[caches.length - 1]);
        write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(vbsFile),CHARSET_GBK)),caches[caches.length - 1]);
    }

    private static void runVbsFile(boolean waitFor) throws Exception{
        caches[caches.length - 1] = gsph(CMD_CS_RUN,caches[0]);
        CS.s(gl(caches[caches.length - 1],2));
        if(waitFor) Runtime.getRuntime().exec(caches[caches.length - 1]).waitFor();
        else Runtime.getRuntime().exec(caches[caches.length - 1]);
    }

    private static void dealIntegerValues(){
        Matcher matcher = compile(REG_PRIORITY).matcher(game.getPriority());
        if(!matcher.matches()) game.setPriority(PRIORITY_HIGH);
        matcher = compile(REG_TIME).matcher(game.getBeforeWait());
        if(!matcher.matches()) game.setBeforeWait(WAIT_TIME);
        if(!matcher.reset(game.getAfterWait()).matches()) game.setAfterWait(WAIT_TIME);
        if(!matcher.reset(game.getWatchWait()).matches()) game.setWatchWait(WAIT_TIME);
    }

    private static String countWaitTime(String time){
        return String.valueOf(Integer.parseInt(time) * 1000);
    }
}
