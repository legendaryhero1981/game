package legend.util.test;

import static java.nio.file.Paths.get;
import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.CS;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.StringUtil.getAppPath;
import static legend.util.StringUtil.getClassPath;
import static legend.util.StringUtil.getFileNameWithSuffix;
import static legend.util.StringUtil.getFileNameWithoutSuffix;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import legend.intf.ICommon;
import legend.util.entity.Zip7;
import legend.util.test.model.GCDModel;

public class TestArithmetic implements ICommon{
    @Test
    public void test(){
        testJaxbUtil();
    }

    // @Test
    public void testJaxbUtil(){
        Zip7 zip7 = convertToObject(get("file-7zip.xml"),Zip7.class);
        CS.sl(zip7.toString());
    }

    // @Test
    public void testFileName(){
        String s = "d:/abc/1.2.3/javax.inject_1.0.0.v20091030.jar";
        CS.sl(getFileNameWithSuffix(s)).sl(getFileNameWithoutSuffix(s));
    }

    // @Test
    public void testAppPath(){
        CS.sl(getAppPath());
    }

    // @Test
    public void testClassPath(){
        CS.sl(getClassPath(Test.class)).sl(getClassPath(GCDModel.class));
    }

    // @Test
    public void testGBK(){
        StringBuilder sb = new StringBuilder();
        byte[] by = new byte[2];
        for(int b1 = 0xa1;b1 <= 0xa9;b1++){
            by[0] = (byte)b1;
            for(int b2 = 0xa1;b2 <= 0xfe;b2++){
                by[1] = (byte)b2;
                String str = S_EMPTY;
                try{
                    str = new String(by,CHARSET_GBK);
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                sb.append(str);
            }
        }
        for(int b1 = 0xb0;b1 <= 0xf7;b1++){
            by[0] = (byte)b1;
            for(int b2 = 0xa1;b2 <= 0xfe;b2++){
                by[1] = (byte)b2;
                String str = S_EMPTY;
                try{
                    str = new String(by,CHARSET_GBK);
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                sb.append(str);
            }
        }
        String s = sb.toString().replaceAll(REG_UC_MC_GBK,S_EMPTY);
        Matcher matcher = compile(REG_UC_NON_CHS).matcher(s);
        CS.s(matcher.replaceAll(S_EMPTY));
    }

    // @Test
    public void testBIG5(){
        StringBuilder sb = new StringBuilder();
        byte[] by = new byte[2];
        for(int b1 = 0xa1;b1 <= 0xa3;b1++){
            by[0] = (byte)b1;
            for(int b2 = 0x40;b2 <= 0xbf;b2++){
                by[1] = (byte)b2;
                String str = S_EMPTY;
                try{
                    str = new String(by,CHARSET_BIG5);
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                sb.append(str);
            }
        }
        for(int b1 = 0xa4;b1 <= 0xc6;b1++){
            by[0] = (byte)b1;
            for(int b2 = 0x40;b2 <= 0x7e;b2++){
                by[1] = (byte)b2;
                String str = S_EMPTY;
                try{
                    str = new String(by,CHARSET_BIG5);
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                sb.append(str);
            }
        }
        for(int b1 = 0xc9;b1 <= 0xf9;b1++){
            by[0] = (byte)b1;
            for(int b2 = 0x40;b2 <= 0xd5;b2++){
                by[1] = (byte)b2;
                String str = S_EMPTY;
                try{
                    str = new String(by,CHARSET_BIG5);
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                sb.append(str);
            }
        }
        String s = sb.toString().replaceAll(REG_UC_MC_BIG5,S_EMPTY);
        Matcher matcher = compile(REG_UC_NON_CHS).matcher(s);
        CS.s(matcher.replaceAll(S_EMPTY));
    }

    // @Test
    public void testJZT(){
        Scanner scanner = new Scanner(System.in);
        Pattern pattern = compile("[A-Z]");
        Matcher matcher = null;
        // 正则匹配输入，直到输入字符串中包含至少一个英文大写字母为止
        do{
            CS.sl("请输入任意一个英文大写字母");
        }while(!(matcher = pattern.matcher(scanner.next())).find());
        scanner.close();
        char c = 'A';
        // n为金字塔顶横坐标，l为每层输出英文字符串的起始横坐标，r为每层输出英文字符串的结束横坐标
        int n = matcher.group().charAt(0) - c, l = n, r = n;
        for(int i = 0,j;i <= n;i++,l--,r++){
            // 打印该层的空格字符串
            for(j = 0;j < l;j++) CS.s(1);
            // 打印该层的英文字符串
            for(;j <= r;j++) CS.s(String.valueOf((char)(c + i)));
            // 打印换行
            CS.l(1);
        }
    }

    // @Test
    public void testGCD(){
        Scanner scanner = new Scanner(System.in);
        // 正则匹配任意一个自然数
        Pattern pattern = compile("^[1-9]\\d*$");
        Matcher matcher = null;
        // 正则匹配输入，直到输入任意一个自然数为止
        do{
            CS.sl("请输入第一个自然数");
        }while(!(matcher = pattern.matcher(scanner.next())).find());
        int n = Integer.parseInt(matcher.group());
        // 正则匹配输入，直到输入任意一个自然数为止
        do{
            CS.sl("请输入第二个自然数");
        }while(!(matcher = pattern.matcher(scanner.next())).find());
        int m = Integer.parseInt(matcher.group());
        scanner.close();
        // 构建这两个自然数的计算模型
        GCDModel model = new GCDModel(n,m);
        // 计算这两个自然数的最大公约数和最小公倍数
        model.gcd();
        // 打印计算结果
        CS.sl("最大公约数为：" + model.getMax() + "\n最小公倍数为：" + model.getMin());
    }

    // @Test
    /**
     * Oracle 的标准Java HotSpot VM的默认设置-XX:TieredStopAtLevel=4，
     * 会导致后面打印出/而不是-1，而字符/是字符0的ascii码-1；
     * 手动设置为0到3可以正常打印，取0最慢，取1最快。
     * <p>
     * 这是C2编译器处理int类型转字符类型的错误，自java8起一直未修复。
     */
    public void testC2Error(){
        C2Error hello = new C2Error();
        for(int i = 0;i < 50_000;i++) hello.test();
    }

    private class C2Error{
        public void test(){
            int i = 8;
            while((i -= 3) > 0);
            CS.sl("i = " + i);
        }
    }

    // @Test
    public void testClassVersion(){
        ClassVersionChecker.checkClassVersion(S_EMPTY);
    }

    private static class ClassVersionChecker{
        private static void checkClassVersion(String filename){
            try{
                DataInputStream in = new DataInputStream(new FileInputStream(filename));
                int magic = in.readInt();
                if(magic != 0xcafebabe){
                    CS.sl(filename + " is not a valid class!");
                }
                int minor = in.readUnsignedShort();
                int major = in.readUnsignedShort();
                CS.sl(filename + ": " + major + " . " + minor);
                in.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
