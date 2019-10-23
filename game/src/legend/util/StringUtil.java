package legend.util;

import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.nonEmpty;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;

import legend.intf.ICommon;
import legend.intf.IValue;
import legend.util.param.SingleValue;

public final class StringUtil implements ICommon{
    private StringUtil(){}

    public static int bytesIndexOfBytes(byte[] lb, byte[] sb, boolean reverse){
        if(reverse) return bytesIndexOfBytes(lb,sb,lb.length - 1,reverse);
        else return bytesIndexOfBytes(lb,sb,0,reverse);
    }

    public static int bytesIndexOfBytes(byte[] lb, byte[] sb, int lbOffset, boolean reverse){
        if(lb.length < sb.length || 0 > lbOffset || lb.length < lbOffset + 1) return -1;
        if(reverse) for(int i = lbOffset,j,k;i >= 0;i--){
            for(j = sb.length - 1,k = i;j >= 0 && i >= 0 && sb[j--] == lb[i--];);
            if(j == -1) return k - sb.length + 1;
            i = k;
        }
        else for(int i = lbOffset,j,k;i < lb.length;i++){
            for(j = 0,k = i;j < sb.length && i < lb.length && sb[j++] == lb[i++];);
            if(j == sb.length) return k;
            i = k;
        }
        return -1;
    }

    public static String bytesToHex(byte[] bytes){
        char[] cache = new char[bytes.length * 2];
        for(int i = 0;i < bytes.length;i++){
            cache[i] = CHAR_HEX[bytes[i] >> 4 & 0xf];
            cache[++i] = CHAR_HEX[bytes[i] & 0xf];
        }
        return new String(cache);
    }

    public static byte[] hexToBytes(String hex){
        final int size = hex.length() / 2;
        byte[] bytes = new byte[size];
        for(int i = 0;i < size;i++){
            String subStr = hex.substring(i * 2,i * 2 + 2);
            bytes[i] = (byte)Integer.parseInt(subStr,16);
        }
        return bytes;
    }

    public static String stringToHex(String string){
        return bytesToHex(string.getBytes());
    }

    public static String hexToString(String hex){
        return new String(hexToBytes(hex));
    }

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
        IValue<String> value = new SingleValue<>(s);
        if(nonEmpty(map)) map.entrySet().stream().forEach(entry->value.setValue(brph(value.getValue(),entry.getKey(),entry.getValue())));
        return value.getValue();
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

    public static String glph(String s, int n, List<Supplier<String>> suppliers){
        String r = gl(s,n);
        if(nonEmpty(suppliers)) for(int i = 0;i < suppliers.size();i++)
            r = r.replaceAll(gph(i),quoteReplacement(suppliers.get(i).get()));
        return r;
    }

    public static String gsph(String s, String... ph){
        return glph(s,0,ph);
    }

    public static String gsph(String s, List<Supplier<String>> suppliers){
        return glph(s,0,suppliers);
    }

    public static String gph(int n){
        return PLACE_HOLDER + String.valueOf(n) + PLACE_HOLDER;
    }
}
