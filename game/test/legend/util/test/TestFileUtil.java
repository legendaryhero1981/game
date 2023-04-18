package legend.util.test;

import static legend.util.FileUtil.main;

import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestFileUtil extends TestBase{
    @Test
    public void testMain(){
        main(args);
    }

    @Override
    public String monk(){
        String s = "";
//        s = "-dov!*  `.jar`$  E:/Java/apache-maven  ";
//        s = "-ddoov!*  \\d$  E:/Java/apache-maven  ";
//        s = "-rfil+  (?i)`assembly-csharp.il`$  E:/Decompile/DLL-ildasm  E:/Decompile/Code/IL/Pathfinder Wrath of the Righteous/Assembly-CSharp.il.xml  1";
//        s = "-fdo^*!  \\A(修改|备份|Mods)$  f:/games/Pathfinder Kingmaker Definitive Edition";
//        s = "-rfspk+  (?i)`file-spk.xml`$  .  1";
//        s = "-rfspk+  (?i)`file-spk.xml`$  D:/360安全浏览器下载/GF  1";
//        s = "-dsrp+  (?i)`file-dsrp.xml`$  .  1";
//        s = "-7zip+  (?i)`file-7zip.xml`$  .  1";
//        s = "-cmd+  (?i)`curl.txt`$  F:/我的云盘/游戏/Wartales  1";
            s = "-fs@+::-fpr!*  .::*  F:\\我的云盘\\游戏\\Divinity Original Sin 2\\Mods\\神界2整合包V1.5\\DefEd\\Data\\Public::*  F:\\games\\Divinity Original Sin 2\\DefEd\\Data\\Public::?";
//        s = "-rfbt*  `temp2.txt`$  E:/Decompile/DLL-ildasm  REPLACE(-,,_);;MULTI-ROW(String #1.1# = #DQM##1.0##DQM#;#ENTER#String #2.1# = #DQM##2.0##DQM#;#ENTER#String #3.1# = #DQM##3.0##DQM#;#ENTER#String #4.1# = #DQM##4.0##DQM#;)  \\s  1";
//        s = "-rfbt*  `temp2.txt`$  E:/Decompile/DLL-ildasm  REPLACE(value=#DQM#(-?([.]?\\d+|\\d+[.]\\d*))#DQM#,,value=#DQM##POW-FLOAT(1,0.75)##DQM#)  \\n  1";
        return s;
    }
}
