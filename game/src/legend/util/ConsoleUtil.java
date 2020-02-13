package legend.util;

import static java.lang.System.exit;
import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.gs;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import legend.util.intf.IConsoleUtil;

public final class ConsoleUtil implements IConsoleUtil{
    public static final ConsoleUtil CS;
    public static final FileSizeUtil FS;
    public static final Scanner IN;
    private static final PrintStream OUT;
    private PrintStream stream;
    static{
        CS = new ConsoleUtil();
        FS = new FileSizeUtil();
        IN = new Scanner(System.in);
        OUT = System.out;
    }

    public static void exec(String cmd, String error){
        exec(cmd,error,true);
    }

    public static void exec(String cmd, String error, boolean waitFor){
        try{
            if(waitFor) Runtime.getRuntime().exec(cmd).waitFor();
            else Runtime.getRuntime().exec(cmd);
        }catch(InterruptedException | IOException e){
            CS.showError(error,new String[]{e.toString()});
        }
    }

    public void showHelp(String help, BooleanSupplier... suppliers){
        if(meetCondition(suppliers)){
            sl(help);
            exit(0);
        }
    }

    public void showError(String error, String[] args, BooleanSupplier... suppliers){
        if(showException(error,args,suppliers)) exit(0);
    }

    public void showError(String error, List<Supplier<String>> args, BooleanSupplier... suppliers){
        if(showException(error,args,suppliers)) exit(0);
    }

    public boolean showException(String error, String[] args, BooleanSupplier... suppliers){
        if(meetCondition(suppliers)){
            sl(gsph(error,args));
            return true;
        }else return false;
    }

    public boolean showException(String error, List<Supplier<String>> args, BooleanSupplier... suppliers){
        if(meetCondition(suppliers)){
            sl(gsph(error,args));
            return true;
        }else return false;
    }

    public ConsoleUtil s(int n){
        return s(true,n);
    }

    public ConsoleUtil s(String s){
        return s(true,s);
    }

    public ConsoleUtil s(String s, int n){
        return s(true,s,n);
    }

    public ConsoleUtil sl(String s, int n){
        return sl(true,s,n);
    }

    public ConsoleUtil sl(String s){
        return sl(true,s);
    }

    public ConsoleUtil sl(Collection<String> c, int n){
        c.stream().forEach(s->sl(s));
        return l(n);
    }

    public ConsoleUtil sl(Collection<String> c){
        return sl(c,1);
    }

    public ConsoleUtil l(int n){
        return l(true,n);
    }

    public ConsoleUtil f(String format, Object... args){
        return format(true,format,args);
    }

    public ConsoleUtil fl(String format, Object... args){
        return format(true,format + "%n",args);
    }

    public ConsoleUtil s(boolean flag, int n){
        return print(flag,gs(n));
    }

    public ConsoleUtil s(boolean flag, String s, int n){
        return print(flag,s,n);
    }

    public ConsoleUtil s(boolean flag, String s){
        return print(flag,s);
    }

    public ConsoleUtil sl(boolean flag, String s, int n){
        return print(flag,s + gl(n));
    }

    public ConsoleUtil sl(boolean flag, String s){
        return sl(flag,s,1);
    }

    public ConsoleUtil l(boolean flag, int n){
        return sl(flag,"",n);
    }

    public ConsoleUtil f(boolean flag, String format, Object... args){
        return format(flag,format,args);
    }

    public ConsoleUtil fl(boolean flag, String format, Object... args){
        return format(flag,format + "%n",args);
    }

    public ConsoleUtil formatSize(boolean flag, long size, UnitType type){
        long b = size;
        long kb = FS.divideSize(size,1);
        long mb = FS.divideSize(size,2);
        long gb = FS.divideSize(size,3);
        long tb = FS.divideSize(size,4);
        switch(type){
            case B:
            format(flag,"%5d%s",b,SIZE_B);
            break;
            case KB:
            format(flag,"%5d%s%5d%s",kb,SIZE_KB,FS.modSize(b),SIZE_B);
            break;
            case MB:
            format(flag,"%5d%s%5d%s%5d%s",mb,SIZE_MB,FS.modSize(kb),SIZE_KB,FS.modSize(b),SIZE_B);
            break;
            case GB:
            format(flag,"%5d%s%5d%s%5d%s%5d%s",gb,SIZE_GB,FS.modSize(mb),SIZE_MB,FS.modSize(kb),SIZE_KB,FS.modSize(b),SIZE_B);
            break;
            case TB:
            default:
            format(flag,"%d%s%5d%s%5d%s%5d%s%5d%s",tb,SIZE_TB,FS.modSize(gb),SIZE_GB,FS.modSize(mb),SIZE_MB,FS.modSize(kb),SIZE_KB,FS.modSize(b),SIZE_B);
        }
        return this;
    }

    public ConsoleUtil formatSize(long size, UnitType type){
        return formatSize(true,size,type);
    }

    public void cacheStream(PrintStream stream){
        this.stream = stream;
    }

    public void clearStream(){
        this.stream = null;
    }

    private boolean meetCondition(BooleanSupplier... suppliers){
        if(nonEmpty(suppliers)) for(BooleanSupplier supplier : suppliers)
            if(!supplier.getAsBoolean()) return false;
        return true;
    }

    private ConsoleUtil print(boolean flag, String s){
        if(flag) OUT.print(s);
        if(nonEmpty(stream)) stream.print(s);
        return this;
    }

    private ConsoleUtil print(boolean flag, String s, int n){
        if(flag) OUT.print(gs(s,n));
        if(nonEmpty(stream)) stream.print(gs(s,n));
        return this;
    }

    private ConsoleUtil format(boolean flag, String format, Object... args){
        if(flag) OUT.format(format,args);
        if(nonEmpty(stream)) stream.format(format,args);
        return this;
    }

    public static class FileSizeUtil{
        private FileSizeUtil(){}

        public UnitType matchType(String type){
            if(isEmpty(type)) return UnitType.NON;
            switch(type.toUpperCase()){
                case SIZE_B:
                return UnitType.B;
                case SIZE_KB:
                return UnitType.KB;
                case SIZE_MB:
                return UnitType.MB;
                case SIZE_GB:
                return UnitType.GB;
                case SIZE_TB:
                return UnitType.TB;
                default:
                return UnitType.NON;
            }
        }

        public long matchSize(long size, UnitType type){
            switch(type){
                case KB:
                return multiplySize(size,1);
                case MB:
                return multiplySize(size,2);
                case GB:
                return multiplySize(size,3);
                case TB:
                return multiplySize(size,4);
                case B:
                default:
                return size;
            }
        }

        public long divideSize(long size, int n){
            for(int i = 0;i < n;i++)
                size /= UNIT_SIZE;
            return size;
        }

        public long multiplySize(long size, int n){
            for(int i = 0;i < n;i++)
                size *= UNIT_SIZE;
            return size;
        }

        public long modSize(long size){
            return size % UNIT_SIZE;
        }
    }
}
