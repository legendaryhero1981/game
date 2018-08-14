package legend.util;

import static java.lang.System.exit;
import static legend.intf.ICommon.gl;
import static legend.intf.ICommon.gs;
import static legend.intf.ICommon.gsph;
import static legend.util.ValueUtil.isNull;
import static legend.util.ValueUtil.nonEmpty;
import static legend.util.ValueUtil.nonNull;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.BooleanSupplier;

import legend.util.intf.IConsoleUtil;

public class ConsoleUtil implements IConsoleUtil{
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

    ConsoleUtil(){}

    public void showHelp(String help, BooleanSupplier... suppliers){
        if(nonEmpty(suppliers)) for(BooleanSupplier supplier : suppliers)
            if(!supplier.getAsBoolean()) return;
        sl(help);
        exit(0);
    }

    public void showError(String error, String[] args, BooleanSupplier... suppliers){
        if(nonEmpty(suppliers)) for(BooleanSupplier supplier : suppliers)
            if(!supplier.getAsBoolean()) return;
        sl(gsph(error,args));
        exit(0);
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

    public ConsoleUtil l(int n){
        return l(true,n);
    }

    public ConsoleUtil f(String format, Object... args){
        return format(true,format,args);
    }

    public ConsoleUtil fl(String format, Object... args){
        return format(true,format + "%n",args);
    }

    public ConsoleUtil formatSize(long size, UNIT_TYPE type){
        long b = size;
        long kb = FS.divideSize(size,1);
        long mb = FS.divideSize(size,2);
        long gb = FS.divideSize(size,3);
        long tb = FS.divideSize(size,4);
        switch(type){
            case B:
            format(true,"%5d%s",b,SIZE_B);
            break;
            case KB:
            format(true,"%5d%s%5d%s",kb,SIZE_KB,FS.modSize(b),SIZE_B);
            break;
            case MB:
            format(true,"%5d%s%5d%s%5d%s",mb,SIZE_MB,FS.modSize(kb),SIZE_KB,FS.modSize(b),SIZE_B);
            break;
            case GB:
            format(true,"%5d%s%5d%s%5d%s%5d%s",gb,SIZE_GB,FS.modSize(mb),SIZE_MB,FS.modSize(kb),SIZE_KB,FS.modSize(b),SIZE_B);
            break;
            case TB:
            default:
            format(true,"%d%s%5d%s%5d%s%5d%s%5d%s",tb,SIZE_TB,FS.modSize(gb),SIZE_GB,FS.modSize(mb),SIZE_MB,FS.modSize(kb),SIZE_KB,FS.modSize(b),SIZE_B);
        }
        return this;
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

    public ConsoleUtil formatSize(boolean flag, long size, UNIT_TYPE type){
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

    public void cacheStream(PrintStream stream){
        this.stream = stream;
    }

    public void clearStream(){
        this.stream = null;
    }

    private ConsoleUtil print(boolean flag, String s){
        if(flag) OUT.print(s);
        if(nonNull(stream)) stream.print(s);
        return this;
    }

    private ConsoleUtil print(boolean flag, String s, int n){
        if(flag) OUT.print(gs(s,n));
        if(nonNull(stream)) stream.print(gs(s,n));
        return this;
    }

    private ConsoleUtil format(boolean flag, String format, Object... args){
        if(flag) OUT.format(format,args);
        if(nonNull(stream)) stream.format(format,args);
        return this;
    }

    static class FileSizeUtil{
        private FileSizeUtil(){}

        public UNIT_TYPE matchType(String type){
            if(isNull(type)) return UNIT_TYPE.NON;
            switch(type){
                case SIZE_B:
                return UNIT_TYPE.B;
                case SIZE_KB:
                return UNIT_TYPE.KB;
                case SIZE_MB:
                return UNIT_TYPE.MB;
                case SIZE_GB:
                return UNIT_TYPE.GB;
                case SIZE_TB:
                return UNIT_TYPE.TB;
                default:
                return UNIT_TYPE.NON;
            }
        }

        public long matchSize(long size, UNIT_TYPE type){
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
