package legend.util;

import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.write;
import static legend.util.ConsoleUtil.CS;
import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.gs;
import static legend.util.StringUtil.gsph;
import static java.nio.charset.Charset.forName;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import legend.util.intf.IJsonUtil;

public final class JsonUtil implements IJsonUtil{
    private JsonUtil(){}

    public static void trimJson(Path path){
        try{
            List<String> lines = readAllLines(path,forName(CHARSET_GBK));
            write(path,trimJson(lines).getBytes(CHARSET_GBK),StandardOpenOption.TRUNCATE_EXISTING);
        }catch(IOException e){
            CS.sl(gsph(ERR_JSON_PARSE,e.toString()));
        }
    }

    public static String trimJson(List<String> lines){
        StringBuilder builder = new StringBuilder();
        lines.forEach(line->builder.append(line.trim()));
        return builder.toString();
    }

    public static void formatJson(Path path){
        try{
            String s = trimJson(readAllLines(path,forName(CHARSET_GBK)));
            write(path,formatJson(s).getBytes(CHARSET_GBK),StandardOpenOption.TRUNCATE_EXISTING);
        }catch(IOException e){
            CS.sl(gsph(ERR_JSON_PARSE,e.toString()));
        }
    }

    public static String formatJson(String s){
        char[] c = s.toCharArray();
        StringBuilder builder = new StringBuilder();
        int n = 0, m = 4;
        boolean format = true;
        try{
            for(int i = 0;i < c.length;i++)
                switch(c[i]){
                    case C_COMMA:
                    builder.append(c[i]);
                    if(format) builder.append(gl(1) + gs(m * n));
                    break;
                    case C_BRACE_L:
                    case C_SQUARE_L:
                    builder.append(c[i]);
                    if(format) builder.append(gl(1) + gs(m * ++n));
                    break;
                    case C_BRACE_R:
                    case C_SQUARE_R:
                    if(format) builder.append(gl(1) + gs(m * --n));
                    builder.append(c[i]);
                    break;
                    case C_QUOT_D:
                    if(C_ESCAPE != c[i - 1]) format = !format;
                    default:
                    builder.append(c[i]);
                }
        }catch(Exception e){
            CS.sl(gsph(ERR_JSON_PARSE,e.toString()));
            return s;
        }
        if(!format || 0 != n){
            CS.sl(ERR_JSON_FMT);
            return s;
        }
        return builder.toString();
    }
}
