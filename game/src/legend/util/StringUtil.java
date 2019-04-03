package legend.util;

import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.nonEmpty;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;

import legend.intf.ICommon;
import legend.util.param.SingleValue;

public final class StringUtil implements ICommon{
    private StringUtil(){}

    public static String concat(Object[] objects, String join){
        if(0 == objects.length) return "";
        String r = "";
        for(int i = 0;i < objects.length - 1;i++)
            r = r.concat(objects[i].toString()).concat(join);
        return r.concat(objects[objects.length - 1].toString());
    }

    public static String concat(Object[] objects){
        return concat(objects,"");
    }

    public static String concat(Collection<Object> collection, String join){
        return concat(collection.toArray(new Object[0]),join);
    }

    public static String concat(Collection<Object> collection){
        return concat(collection,"");
    }

    public static String concat(String[] s, String join){
        if(0 == s.length) return "";
        String r = "";
        for(int i = 0;i < s.length - 1;i++)
            r = r.concat(s[i]).concat(join);
        return r.concat(s[s.length - 1]);
    }

    public static String concat(String[] s){
        return concat(s,"");
    }

    public static String brph(String s, Map<String,String> map){
        SingleValue<String> value = new SingleValue<>(s);
        if(nonEmpty(map)) map.entrySet().stream().forEach(entry->value.set(brph(value.get(),entry.getKey(),entry.getValue())));
        return value.get();
    }

    public static String brph(String s, String regex, String repl){
        StringBuilder builder = new StringBuilder();
        Matcher matcher = compile(regex).matcher(s);
        while(matcher.find()){
            String r = repl;
            String match = matcher.group(1);
            if(nonEmpty(match)){
                for(int i = 1,n = Integer.valueOf(match);i < n;i++)
                    r += repl;
            }
            matcher.appendReplacement(builder,r);
        }
        return matcher.appendTail(builder).toString();
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
