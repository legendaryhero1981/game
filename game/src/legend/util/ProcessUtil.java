package legend.util;

import static legend.util.ConsoleUtil.CS;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.nonEmpty;

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
        ProcessBuilder builder = new ProcessBuilder(cmds);
        builder.redirectErrorStream(true);
        try{
            Process process = builder.start();
            ES.execute(()->handler.accept(process));
            process.waitFor();
            process.destroy();
        }catch(Exception e){
            CS.sl(gsph(ERR_EXEC_PROC,e.toString()));
        }
    }

    private static class ProcessHandler implements Consumer<Process>{
        @Override
        public void accept(Process process){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                for(String line;nonEmpty(line = reader.readLine());CS.sl(line));
            }catch(Exception e){
                CS.sl(gsph(ERR_DEAL_PROC,e.toString()));
            }
        }
    }
}
