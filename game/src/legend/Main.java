package legend;

import static legend.util.ConsoleUtil.CS;
import static legend.util.ValueUtil.isEmpty;

import legend.intf.ICommonVar;

public final class Main implements ICommonVar{
    public static void main(String[] args){
        CS.showHelp(HELP_MAIN,()->isEmpty(args));
        String[] sa = moveLeft(args,1);
        final String cmd = args[0].toLowerCase();
        switch(cmd){
            case MAIN_FILE:
            legend.util.FileUtil.main(sa);
            break;
            case MAIN_RUN:
            legend.game.run.Main.main(sa);
            break;
            case MAIN_EOC:
            legend.game.dos2.Main.main(sa);
            break;
            case MAIN_KCD:
            legend.game.kcd.Main.main(sa);
            break;
            case MAIN_POE:
            legend.game.poe2.Main.main(sa);
            break;
            default:
            CS.showError(ERR_ARG_ANLS,new String[]{ST_ARG_ERR});
        }
    }

    private static String[] moveLeft(String[] args, int n){
        if(n >= args.length) return null;
        String[] sa = new String[args.length - n];
        for(int i = n;i < args.length;i++)
            sa[i - n] = args[i];
        return sa;
    }
}
