package legend.util.test;

import static legend.util.ConsoleUtil.CS;
import static java.nio.file.Paths.get;

import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import legend.intf.ICommonVar;
import legend.util.test.model.GCDModel;

public class TestArithmetic implements ICommonVar{
    public static void main(String[] args){
        new TestArithmetic().test();
    }

    @Test
    public void test(){
        Path a = get("G:/cache/temp/src");
        Path b = get("G:/cache/temp/FINAL FANTASY XV");
        CS.sl(a.relativize(b).toString());
    }

    // @Test
    public void testJZT(){
        Scanner scanner = new Scanner(System.in);
        Pattern pattern = Pattern.compile("[A-Z]");
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
            for(j = 0;j < l;j++)
                CS.s(1);
            // 打印该层的英文字符串
            for(;j <= r;j++)
                CS.s(String.valueOf((char)(c + i)));
            // 打印换行
            CS.l(1);
        }
    }

    // @Test
    public void testGCD(){
        Scanner scanner = new Scanner(System.in);
        // 正则匹配任意一个自然数
        Pattern pattern = Pattern.compile("^[1-9]\\d*$");
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
}
