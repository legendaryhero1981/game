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
        s = "-dov!*  .  E:/Java/eclipse/SpringToolSuite/plugins  _   ";
        s = "-ddov!*  .  E:/Java/eclipse/SpringToolSuite/plugins  _   ";
        s = "-ddoov!*  .  E:/Java/eclipse/SpringToolSuite/plugins  _  ";
//        s = "-fddsd!*  .  d:/games";
//        s = "-dsrp+  (?i)`file-dsrp.xml`$  .  1";
//        s = "-rfbt*  (?i)`武器うんちく.fmg.xml`$  G:/cache/temp/ds3  REPLACE(\\A(<text id=#DQM#\\d+#DQM#>).*,,$1,,#EMPTY#);;DIST-FINAL-MULTI-ROW(#1.1#,,#ENTER#)  \n  1";
//        s = "-rfbt*  `repack-fmg.bat`$  G:/cache/temp/ds3  REPLACE(.+,,#DQM#$0#DQM#);;FINAL-MULTI-ROW(#1.1#,,` `,,@echo off#ENTER#chcp 65001#ENTER##DQM#G:/games/DSParamEditor/DSDataRepacker#DQM# )  \n  1";
//        s = "-7zip+  (?i)`file-7zip.xml`$  .  1";
//        s = "-u!*::*::*  `.class`$::*::*  D:/games/classes/jaxb/jakarta.xml.bind-api/META-INF/versions/9::D:/games/classes/jaxb/jaxb-core/META-INF/versions/9::D:/games/classes/jaxb/jaxb-impl/META-INF/versions/9  D:/games/classes/jaxb/jakarta.xml.bind-api::D:/games/classes/jaxb/jaxb-core::D:/games/classes/jaxb/jaxb-impl";
//        s = "-ddo+  `META-INF`$  D:/games/jmod/jaxb/3.0.0/unzip";
//        s = "-d~::*::-7zip+  games?`.7z`$::*::`file-7zip.xml`$  D:/360安全浏览器下载/7zip/zip::G:/cache/截图/game::D:/games  1::*::*";
//        s = "-zimd5+  `.jar`$  G:/cache/temp/7zip/cache  G:/cache/temp/7zip/unzip  1";
//        s = "-pimd5+  `.jar`$  G:/cache/temp/7zip/cache  1";
//        s = "-fdf*@::-d*  .::*  F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data::*  D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data::?";
//        s = "-fpr!+  `.java`$  D:/games/src  ";
//        s = "-fpr!+  `.class`$  D:/games/classes/11  ./run.log";
//        s = "-iu*  .  F:/games/Resident Evil 4/修改/BIO4  F:/games/Resident Evil 4/BIO4  F:/games/Resident Evil 4/备份/BIO4";
//        s = "-ir*  .  F:/games/Resident Evil 4/备份/BIO4  F:/games/Resident Evil 4/BIO4  F:/games/Resident Evil 4/修改/BIO4";
//        s = "-r*  file  d:/games  `$$$`$0`$$$`  1";
//        s = "-rfbt*  \\Atemp1`.txt`$  E:/Decompile/DLL-ildasm  1@@LOWER;;UPPER=>REPLACE(`.`,,`_$\\`);;SINGLE-ROW(String INST_#1-1# = #DQM##1.1##DQM#;#SQM3#$1\\2#BQ#)  \\t+  1";
//        s = "-rfbt*  \\Atemp1`.txt`$  E:/Decompile/DLL-ildasm  1@@UPPER=>REPLACE(\\.,,_);;SINGLE-ROW(addInstruction(INST_#1-1#,#DQM##2.0##DQM#,#DQM=2#);)  \\t+  1";
//        s = "-rfbt*  (?i)`native.log`$  d:/games  REPLACE(.*?--initialize-at-run-time=(.+?) .*,,$1,,#EMPTY#);;FINAL-SINGLE-ROW(#1.1#,,`,`,,--initialize-at-run-time=)  \\n  1";
//        s = "-rfbt*  (?i)`native.log`$  d:/games  REPLACE(.*?--initialize-at-run-time=(.+?) .*,,$1,,#EMPTY#);;DIST-FINAL-SINGLE-ROW(#1.1#,,`,`,,--initialize-at-run-time=)  \\n  1";
//        s = "-rfgbk*  mod  E:/Decompile/Code/IL/Pathfinder Kingmaker  D:/games/font_schinese.txt";
//        s = "-rfbig5*  .  D:/games/jaot  D:/games/font_tchinese.txt";
//        s = "-rfbig5*  zhCN  E:/Decompile/Code/IL/Pathfinder Kingmaker  D:/games/font_tchinese.txt";
//        s = "-fsmd5+  (?i)`.param`$  D:/Sekiro Shadows Die Twice/param/gameparam/gameparam-parambnd  G:/games/DSParamEditor/gameparam-parambnd";
//        s = "-fdfmd5+  (?i)`.param`$  D:/Sekiro Shadows Die Twice/param/gameparam/gameparam-parambnd  G:/games/DSParamEditor/gameparam-parambnd";
//        s = "-rfcs+  \\Atemp2`.txt`$  E:/Decompile/DLL-ildasm  utf8";
//        s = "-rfsn*  (?i)\\A`JetBrains.Platform.Shell.dll`$  E:/Decompile/ReSharper  C:/Users/liyun/AppData/Local/JetBrains/Installations  2";
//        s = "-rfsn*  (?i)`.dll`$  E:/Decompile/ReSharper  C:/Users/liyun/AppData/Local/JetBrains/Installations  2";
//        s = "-rfmeg+  (?i)`bot.xml`$  D:/360安全浏览器下载/PKM/源代码  1";
//        s = "-rfil+  (?i)`assembly-csharp.il`$  E:/Decompile/DLL-ildasm";
//        s = "-rfspk+  (?i)`file-spk.xml`$  .  1";
//        s = "-ml8+  (?i)`assembly-csharp.dll`$  F:/games/Pathfinder Kingmaker Beneath the Stolen Lands";
//        s = "-ml16+  (?i)`assembly-csharp.dll`  F:/games/Pathfinder Kingmaker Beneath the Stolen Lands/Kingmaker_Data/Managed";
//        s = "-ml32+  (?i)`assembly-csharp.dll`  F:/games/Pathfinder Kingmaker Beneath the Stolen Lands/Kingmaker_Data/Managed";
        return s;
    }
}
