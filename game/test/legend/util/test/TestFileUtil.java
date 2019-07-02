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
        s = "-f*  #SQM#`$1`  d:/games  0  1";
        s = "-rfbt*  \\Atemp1\\.txt$  E:/Decompile/DLL-ildasm  1##LOWER;;UPPER=>REPLACE(`.`,,`_$\\`);;REGENROW(String INST_#1-1# = #DQM##1.1##DQM#;#SQM3#$1\\2#BQ#)  \\t+  1";
//        s = "-rfbt*  \\Atemp1\\.txt$  E:/Decompile/DLL-ildasm  1##UPPER=>REPLACE(\\.,,_);;REGENROW(addInstruction(INST_#1-1#,#DQM##2.0##DQM#,#DQM=2#);)  \\t+  1";
//        s = "-rfbt*  (?i)WepAbsorpPosParam.csv$  G:/cache/temp/Names  1##REPLACE((.+?) (.+),,`$1,$2`)  \\n  1";
//        s = "-fdf*@::-d*  .::*  F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data::*  D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data::?";
//        s = "-iu*  .  F:/games/Resident Evil 4/修改/BIO4  F:/games/Resident Evil 4/BIO4  F:/games/Resident Evil 4/备份/BIO4";
//        s = "-ir*  .  F:/games/Resident Evil 4/备份/BIO4  F:/games/Resident Evil 4/BIO4  F:/games/Resident Evil 4/修改/BIO4";
//        s = "-rfil*  (?i)assembly-csharp\\.il$  E:/Decompile/DLL-ildasm";
//        s = "-rfgbk*  mod  E:/Decompile/Code/IL/Pathfinder Kingmaker  D:/games/font_schinese.txt";
//        s = "-rfbig5*  .  D:/games/jaot  D:/games/font_tchinese.txt";
//        s = "-rfbig5*  zhCN  E:/Decompile/Code/IL/Pathfinder Kingmaker  D:/games/font_tchinese.txt";
//        s = "-fsmd5+  (?i)\\.param$  D:/Sekiro Shadows Die Twice/param/gameparam/gameparam-parambnd  G:/games/DSParamEditor/gameparam-parambnd";
//        s = "-fdfmd5+  (?i)\\.param$  D:/Sekiro Shadows Die Twice/param/gameparam/gameparam-parambnd  G:/games/DSParamEditor/gameparam-parambnd";
//        s = "-rfcs+  \\Atemp2\\.txt$  E:/Decompile/DLL-ildasm  utf8";
        super.monkParam(s);
    }
}
