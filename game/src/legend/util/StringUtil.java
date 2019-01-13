package legend.util;

import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.nonEmpty;

import java.util.regex.Matcher;

import legend.intf.ICommon;
import legend.util.param.SingleValue;

public final class StringUtil implements ICommon{
    public static final StringUtil SU;
    static{
        SU = new StringUtil();
    }

    private StringUtil(){}

    public StringUtil rph(SingleValue<String> value, String regex, String repl){
        Matcher matcher = compile(regex).matcher(value.get());
        while(matcher.find()){
            String ph = matcher.group(), m = matcher.group(1);
            rph(value,ph,repl,nonEmpty(m) ? Integer.parseInt(m) : 1);
        }
        return SU;
    }

    public StringUtil rph(SingleValue<String> value, String ph, String repl, int n){
        String r = repl;
        for(int i = 1;i < n;i++)
            r += repl;
        value.set(value.get().replaceAll(ph,quoteReplacement(r)));
        return SU;
    }

    public static String rph(String s, String ph, String repl, int n){
        String r = repl;
        for(int i = 1;i < n;i++)
            r += repl;
        return s.replaceAll(ph,quoteReplacement(r));
    }

    public static String rph(String s, String ph, String repl){
        return rph(s,ph,repl,1);
    }

    public static String gl(String s, int n){
        String r = nonEmpty(s) ? s : S_EMPTY;
        for(int i = 0;i < n;i++)
            r += SPRT_LINE;
        return r;
    }

    public static String gl(int n){
        return gl(S_EMPTY,n);
    }

    public static String gs(String s, int n){
        String r = S_EMPTY;
        if(nonEmpty(s)) for(int i = 0;i < n;i++)
            r += s;
        return r;
    }

    public static String gs(int n){
        return gs(S_SPACE,n);
    }

    public static String gs(String[] ss, String sprt){
        String r = S_EMPTY;
        for(String s : ss)
            r += s + sprt;
        return r.isEmpty() ? r : r.substring(0,r.length() - sprt.length());
    }

    public static String gs(String[] ss){
        return gs(ss,S_SPACE);
    }

    public static String glph(String s, int n, String... ph){
        String r = gl(s,n);
        if(nonEmpty(ph)) for(int i = 0;i < ph.length;i++)
            r = r.replaceAll(gph(i),quoteReplacement(ph[i]));
        return r;
    }

    public static String gsph(String s, String... ph){
        return glph(s,0,ph);
    }

    public static String gph(int n){
        return PLACE_HOLDER + String.valueOf(n) + PLACE_HOLDER;
    }
}