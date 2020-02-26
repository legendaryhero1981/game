package legend.util;

import static java.nio.file.Paths.get;
import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;

import legend.intf.IValue;
import legend.util.intf.IStringUtil;
import legend.util.param.SingleValue;

public final class StringUtil implements IStringUtil{
    private StringUtil(){}

    /**
     * 返回当前程序可执行文件所在的绝对路径名
     * 
     * @return 返回当前程序可执行文件所在的绝对路径名
     */
    public static String getAppPath(){
        return get(S_EMPTY).toAbsolutePath().toString().replace(SPRT_FILE,SPRT_FILE_ZIP);
    }

    /**
     * getClassPath需要一个当前程序使用的Java类的class属性参数，它可以返回打包过的
     * Java可执行文件（jar，war）所处的系统目录名或非打包Java程序所处的目录
     * 
     * @param clazz
     *            Class类型
     * @return 返回值为该类所在的Java程序运行的目录
     */
    public static String getClassPath(Class<?> clazz){
        ClassLoader loader = clazz.getClassLoader();
        if(isEmpty(loader)) return S_EMPTY;
        // 获得类的全限定名
        String name = clazz.getName() + EXT_CLASS;
        // 获得传入参数所在的包
        Package pack = clazz.getPackage();
        String path = S_EMPTY;
        // 如果不是匿名包，将包名转化为路径
        if(nonEmpty(pack)){
            name = name.substring(pack.getName().length() + 1);
            path = pack.getName().replaceAll(SPRT_PKG,SPRT_FILE_ZIP) + SPRT_FILE_ZIP;
        }
        // 调用ClassLoader的getResource方法，传入包含路径信息的类文件名
        URL url = loader.getResource(path + name);
        // 从URL对象中获取路径信息
        path = url.getPath();
        // 去掉路径信息中的文件协议名前缀和jar文件协议路径
        path = path.replaceFirst(REG_PATH_URL,REP_PATH_URL).replaceFirst(REG_PATH_JAR,REP_PATH_URL);
        try{
            // 解码以还原路径中的所有中文和空格等字符
            path = URLDecoder.decode(path,CHARSET_UTF8);
        }catch(Exception e){
            CS.sl(gsph(ERR_EXEC_CMD,e.toString()));
        }
        return path;
    }

    public static String getFileNameWithSuffix(String path){
        String result = S_EMPTY;
        Matcher matcher = PTRN_PATH_NAME.matcher(path);
        if(matcher.find()) result = matcher.group(2);
        return isEmpty(result) ? S_EMPTY : path;
    }

    public static String getFileNameWithoutSuffix(String path){
        String result = S_EMPTY;
        Matcher matcher = PTRN_PATH_NAME.matcher(path);
        if(matcher.find()) result = matcher.group(2);
        else result = path;
        return isEmpty(result) ? S_EMPTY : result.replaceFirst(REG_FILE_SUFFIX,S_EMPTY);
    }

    public static byte[] fillBytes(int n, int size){
        return fillBytes((byte)n,size);
    }

    public static byte[] fillBytes(byte b, int size){
        byte[] bytes = new byte[size];
        for(int i = 0;i < size;i++)
            bytes[i] = b;
        return bytes;
    }

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

    public static <T> String concat(T[] objects, String join){
        if(0 == objects.length) return S_EMPTY;
        String r = S_EMPTY;
        for(int i = 0;i < objects.length - 1;i++)
            r = r.concat(objects[i].toString()).concat(join);
        return r.concat(objects[objects.length - 1].toString());
    }

    public static <T> String concat(T[] objects){
        return concat(objects,S_EMPTY);
    }

    public static <T> String concat(Collection<T> collection, String join){
        return concat(collection.toArray(new Object[0]),join);
    }

    public static <T> String concat(Collection<T> collection){
        return concat(collection,S_EMPTY);
    }

    public static String concat(String[] s, String join){
        if(0 == s.length) return S_EMPTY;
        String r = S_EMPTY;
        for(int i = 0;i < s.length - 1;i++)
            r = r.concat(s[i]).concat(join);
        return r.concat(s[s.length - 1]);
    }

    public static String concat(String[] s){
        return concat(s,S_EMPTY);
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
