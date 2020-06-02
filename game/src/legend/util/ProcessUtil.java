package legend.util;

import static legend.util.ConsoleUtil.CS;
import static legend.util.StringUtil.concat;
import static legend.util.StringUtil.gsph;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import legend.util.intf.IProcessUtil;

public class ProcessUtil implements IProcessUtil{
    private static final ExecutorService ES;
    private static final Consumer<Process> PH;
    static{
        ES = Executors.newWorkStealingPool();
        PH = new ProcessHandler();
    }

    private ProcessUtil(){}

    public static void handleProcess(String... cmds){
        handleProcess(PH,cmds);
    }

    public static void handleProcess(Consumer<Process> handler, String... cmds){
        try{
            ProcessBuilder builder = new ProcessBuilder(cmds);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            ES.execute(()->handler.accept(process));
            process.waitFor();
        }catch(Exception e){
            CS.sl(gsph(ERR_EXEC_CMD_SPEC,concat(cmds,S_SPACE),gsph(ERR_EXEC_PROC,e.toString())));
        }
    }

    private static class ProcessHandler implements Consumer<Process>{
        @Override
        public void accept(Process process){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                StringBuilder builder = new StringBuilder();
                reader.lines().forEach(line->builder.append(line + SPRT_LINE));
                CS.s(builder.toString());
            }catch(Exception e){
                CS.sl(gsph(ERR_DEAL_PROC,e.toString()));
            }
        }
    }
}
