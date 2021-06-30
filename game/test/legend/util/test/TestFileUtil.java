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
        s = "-dov!*  `.jar`$  E:/Java/apache-maven  ";
        s = "-ddoov!*~  \\d+\\.  E:/Java/apache-maven  ";
        s = "-fdo^*!  \\A(修改|备份|Mods)$  f:/games/Pathfinder Kingmaker Definitive Edition";
        s = "-rfspk+  (?i)`file-spk.xml`$  D:/360安全浏览器下载/GF  1";
//        s = "-dsrp+  (?i)`file-dsrp.xml`$  .  1";
//        s = "-rfbt*  (?i)`武器うんちく.fmg.xml`$  G:/cache/temp/ds3  REPLACE(\\A(<text id=#DQM#\\d+#DQM#>).*,,$1,,#EMPTY#);;DIST-FINAL-MULTI-ROW(#1.1#,,#ENTER#)  \\n  1";
//        s = "-rfbt*  `repack-fmg.bat`$  G:/cache/temp/ds3  REPLACE(.+,,#DQM#$0#DQM#);;FINAL-MULTI-ROW(#1.1#,,` `,,@echo off#ENTER#chcp 65001#ENTER##DQM#G:/games/DSParamEditor/DSDataRepacker#DQM# )  \\n  1";
//        s = "-7zip+  (?i)`file-7zip.xml`$  .  1";
        return s;
    }
}
