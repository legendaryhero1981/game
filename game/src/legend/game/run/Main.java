package legend.game.run;

import static java.io.File.createTempFile;
import static java.lang.Thread.sleep;
import static java.nio.file.Paths.get;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ConsoleUtil.IN;
import static legend.util.ConsoleUtil.exec;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.JaxbUtil.convertToXml;
import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.glph;
import static legend.util.StringUtil.gs;
import static legend.util.StringUtil.gsph;
import static legend.util.StringUtil.rph;
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
            run();
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
                case CMD_DEL:
                case CMD_EXEC:
                case CMD_KILL:
                case CMD_VIEW:
                case CMD_LINK:
                case CMD_LINK_ALL:
                if(args.length > 1) game.setId(args[1]);
                break;
                case CMD_ADD:
                case CMD_CREATE:
                game.setId(args[1]);
                game.setPath(args[2].replaceAll(REG_SPRT_PATHS,SPRT_FILE_ZIP));
                game.setExe(args[3]);
                game.setName(args[4]);
                if(args.length > 5) game.setComment(args[5]);
                else game.setComment(game.getName());
                break;
                default:
                CS.checkError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT});
            }
            game.trim();
        }catch(Exception e){
            CS.checkError(ERR_EXEC_CMD,new String[]{e.toString()});
        }
    }

    private static void run(){
        try{
            // 根据ID获得需要被执行的游戏
            loadAndValidate();
            // 处理游戏进程的优先级数值，游戏执行前、游戏执行后需要执行的BAT脚本以及游戏进程监控的等待时间
            dealIntegerValues();
            // 生成游戏执行前、游戏执行后需要执行的BAT脚本文件
            writeOtherScript();
            // 执行启动游戏进程的VBS主脚本文件
            runVbsScript(false,t->writeMainScript());
        }catch(Exception e){
            CS.checkError(ERR_CREATE_FILE,new String[]{e.toString()});
        }
    }

    private static void kill(){
        try{
            // 根据ID获得需要被终止的游戏
            loadAndValidate();
            // 执行终止游戏进程的VBS主脚本文件
            runVbsScript(false,t->script.append(glph(CMD_VBS_GAME_KILL,game.getExe())));
        }catch(IOException e){
            CS.checkError(ERR_CREATE_FILE,new String[]{e.toString()});
        }catch(Exception e){
            CS.checkError(ERR_RUN_FILE,new String[]{e.toString()});
        }
    }

    private static void linkAll(){
        // 数据验证
        List<Game> games = loadModel().getGames();
        games.parallelStream().forEach(g->CS.checkError(ERR_INVALIDATE,new String[]{RUN_FILE_CONFIG},()->!g.trim().validate()));
        // 执行脚本批量生成游戏快捷方式
        try{
            runVbsScript(true,t->{
                script.append(gl(CMD_VBS_SC_INIT));
                games.stream().forEach(g->cacheLinkScript(g));
            });
            sleep(SLEEP_TIME);
        }catch(IOException e){
            CS.checkError(ERR_CREATE_FILE,new String[]{e.toString()});
        }catch(Exception e){
            CS.checkError(ERR_RUN_FILE,new String[]{e.toString()});
        }
    }

    private static void link(){
        try{
            loadAndValidate();
            runVbsScript(true,t->{
                script.append(gl(CMD_VBS_SC_INIT));
                cacheLinkScript(game);
            });
            sleep(SLEEP_TIME);
        }catch(IOException e){
            CS.checkError(ERR_CREATE_FILE,new String[]{e.toString()});
        }catch(Exception e){
            CS.checkError(ERR_RUN_FILE,new String[]{e.toString()});
        }
    }

    private static void view(){
        loadModel().sortGames().stream().forEach(game->CS.s(gl(game.toString(),2)));
    }

    private static void del(){
        Games games = loadAndValidate();
        games.sortGames().remove(game);
        saveModel(games);
    }

    private static void add(){
        Games games = loadModel();
        CS.checkError(ERR_ID_EXISTS,new String[]{RUN_FILE_CONFIG,game.getId()},()->games.getGameMap().containsKey(game.getId()));
        games.getGames().add(game);
        games.sortGames();
        saveModel(games);
    }

    private static void create(){
        Games games = new Games();
        games.getGames().add(game);
        saveModel(games);
    }

    private static Games loadAndValidate(){
        Games games = loadModel();
        ConcurrentMap<String,Game> gameMap = games.getGameMap();
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
        CS.checkError(ERR_ID_NON,new String[]{RUN_FILE_CONFIG,id},()->isEmpty(game));
        CS.checkError(ERR_EXE_NUL,new String[]{RUN_FILE_CONFIG,id},()->!game.trim().validate());
        return games;
    }

    private static void saveModel(Games games){
        convertToXml(config,games);
    }

    private static Games loadModel(){
        CS.checkError(ERR_CONF_NON,(String[])null,()->!config.toFile().isFile());
        Games games = convertToObject(config,Games.class);
        CS.checkError(ERR_CONF_NUL,(String[])null,()->isEmpty(games.getGames()));
        ConcurrentMap<String,AtomicInteger> gameMap = new ConcurrentHashMap<>();
        games.getGames().parallelStream().forEach(g->gameMap.computeIfAbsent(g.getId(),k->new AtomicInteger()).addAndGet(1));
        gameMap.entrySet().parallelStream().filter(entry->entry.getValue().get() > 1).forEach(entry->CS.s(glph(ST_REPEAT_ID,2,entry.getKey())));
        CS.checkError(ERR_CONF_REPEAT,(String[])null,()->gameMap.values().parallelStream().anyMatch(v->v.get() > 1));
        return games;
    }

    private static void cacheLinkScript(Game game){
        script.append(glph(CMD_VBS_SC_CRT,game.getName()));
        script.append(glph(CMD_VBS_SC_ARG_AND_WD,game.getId(),game.getPath()));
        script.append(glph(CMD_VBS_SC_IL,game.getPath(),nonEmpty(game.getIcon()) ? game.getIcon() : game.getExe() + EXT_EXE));
        script.append(glph(CMD_VBS_SC_DESC,game.getComment()));
        script.append(gl(CMD_VBS_SC_TP));
        script.append(gl(CMD_VBS_SC_WS));
        script.append(gl(CMD_VBS_SC_SAVE));
    }

    private static void runVbsScript(boolean waitFor, Consumer<Object> consumer) throws Exception{
        createVbsFile();
        consumer.accept(null);
        writeVbsFile();
        runVbsFile(waitFor);
    }

    private static void writeMainScript(){
        if(nonEmpty(caches[1])) script.append(glph(CMD_VBS_RUN,caches[1])).append(glph(CMD_VBS_RUN_DEL,caches[1])).append(glph(CMD_VBS_SLEEP,countWaitTime(game.getBeforeWait())));
        Matcher matcher = PTRN_PATH_NAME.matcher(game.getAgentExecutablePath());
        if(nonEmpty(game.getAgentExecutablePath()) && matcher.find()) script.append(glph(CMD_VBS_RUN_AGENT,matcher.group(1),matcher.group(2),rph(game.getAgentArgs(),S_DQM,gs(S_DQM,2))));
        else script.append(glph(CMD_VBS_RUN_GAME,game.getPath(),game.getExe(),game.getArgs()));
        if(nonEmpty(caches[2])) script.append(glph(CMD_VBS_SLEEP,countWaitTime(game.getAfterWait()))).append(glph(CMD_VBS_RUN,caches[2])).append(glph(CMD_VBS_RUN_DEL,caches[2]));
        if(nonEmpty(game.getPriority())) script.append(glph(CMD_VBS_SLEEP,countWaitTime(TIME_SECOND_MAX))).append(glph(CMD_VBS_GAME_PRIORITY,game.getExe(),game.getPriority()));
        // BAT脚本方式实现进程监控
        // if(nonEmpty(caches[3]))
        // script.append(glph(CMD_VBS_SLEEP,countWaitTime(WAIT_TIME))).append(glph(CMD_VBS_RUN,caches[3])).append(glph(CMD_VBS_RUN_DEL,caches[3]));
        // VBS脚本方式实现进程监控
        if(nonEmpty(game.getWatches()) && nonEmpty(game.getWatches().get(0))){
            StringBuilder names = new StringBuilder(), paths = new StringBuilder();
            game.getWatches().stream().forEach(watch->{
                watch = watch.replaceAll(REG_SPRT_PATHS,gs(SPRT_FILE,4));
                if(watch.contains(SPRT_FILE)) paths.append(watch + SPRT_CMDS);
                else names.append(watch + SPRT_CMDS);
            });
            script.append(glph(CMD_VBS_GAME_WATCH,countWaitTime(game.getWatchWait()),game.getExe(),names.toString(),paths.toString()));
        }
        script.append(glph(CMD_VBS_RUN_DEL,caches[0]));
    }

    private static void writeOtherScript() throws IOException{
        writeScript(PTRN_SPRT_CODE.split(game.getBefore()),1);
        writeScript(PTRN_SPRT_CODE.split(game.getAfter()),2);
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
            File batFile = createTempFile(FILE_PREFIX,EXT_BAT);
            script.append(gl(CMD_BAT_CHCP_UTF8));
            for(String cmd : cmds) script.append(gl(cmd.trim()));
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
        vbsFile = createTempFile(FILE_PREFIX,EXT_VBS);
        caches[0] = vbsFile.getCanonicalPath();
        script.append(gl(CMD_VBS_SH_INIT));
    }

    private static void writeVbsFile() throws IOException{
        caches[caches.length - 1] = script.toString();
        CS.sl(caches[0]).sl(caches[caches.length - 1]);
        write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(vbsFile),CHARSET_GBK)),caches[caches.length - 1]);
    }

    private static void runVbsFile(boolean waitFor){
        caches[caches.length - 1] = gsph(CMD_CS_RUN,caches[0]);
        CS.s(gl(caches[caches.length - 1],2));
        exec(caches[caches.length - 1],ERR_RUN_FILE,waitFor);
    }

    private static void dealIntegerValues(){
        Matcher matcher = PTRN_PRIORITY.matcher(game.getPriority());
        if(!matcher.matches()) game.setPriority(PRIORITY_HIGH);
        matcher = PTRN_TIME.matcher(game.getBeforeWait());
        if(!matcher.matches()) game.setBeforeWait(WAIT_TIME);
        if(!matcher.reset(game.getAfterWait()).matches()) game.setAfterWait(WAIT_TIME);
        if(!matcher.reset(game.getWatchWait()).matches()) game.setWatchWait(WAIT_TIME);
    }

    private static String countWaitTime(String time){
        return String.valueOf(Integer.parseInt(time) * 1000);
    }
}
