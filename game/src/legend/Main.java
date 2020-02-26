package legend;

import static java.util.Arrays.copyOfRange;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ValueUtil.isEmpty;

import legend.intf.IMain;

public final class Main implements IMain{
    public static void main(String[] args){
        CS.showHelp(HELP_MAIN,()->isEmpty(args));
        final String cmd = args[0].toLowerCase();
        String[] s = copyOfRange(args,1,args.length);
        switch(cmd){
            case MAIN_IL:
            legend.helper.ILHelper.main(s);
            break;
            case MAIN_FILE:
            legend.util.FileUtil.main(s);
            break;
            case MAIN_RUN:
            legend.game.run.Main.main(s);
            break;
            case MAIN_EOC:
            legend.game.dos2.Main.main(s);
            break;
            case MAIN_KCD:
            legend.game.kcd.Main.main(s);
            break;
            case MAIN_POE:
            legend.game.poe2.Main.main(s);
            break;
            default:
            CS.checkError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT});
        }
    }
}
