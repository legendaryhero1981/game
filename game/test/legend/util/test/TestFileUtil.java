package legend.util.test;

import static legend.util.FileUtil.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestFileUtil extends TestBase{
    @Test
    public void testMain(){
        main(args);
    }

    @BeforeAll
    @Override
    public void monkParam(){
        String s = "";
//        s = "-f*  #SQM#  d:/games  0  1";
//        s = "-bs*  .  E:/Java/MyEclipse 2018/plugins  E:/Java/myeclise-2018破解文件/patch/plugins  E:/Java/myeclise-2018破解文件/备份  ";
//        s = "-rf*  \\Atemp1\\.txt$  E:/Decompile/DLL-ildasm  1##LOWER;;UPPER=>REPLACE(\\.,,_);;REGENROW(String INST_#1-1# = #DQM##1.1##DQM#;)  \\t+  1";
//        s = "-rf*  \\Atemp1\\.txt$  E:/Decompile/DLL-ildasm  1##UPPER=>REPLACE(\\.,,_);;REGENROW(addInstruction(INST_#1-1#,#DQM##2.0##DQM#,#DQM=2#);)  \\t+  1";
        s = "-fdf*@::-d*  .::*  F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data::*  D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data::?";
        super.monkParam(s);
    }
}
