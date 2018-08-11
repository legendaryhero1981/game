package legend.util.test;

import static legend.util.FileUtil.main;

import org.junit.Before;
import org.junit.Test;

import legend.test.TestBase;

public class TestFileUtil extends TestBase{
    @Test
    public void testMain(){
        main(args);
    }

    @Before
    @Override
    public void monkParam(){
        String s = "";
        s = "-rdu*  (?i)(e).*\\.(e).*$  D:/games";
//        s = "-fd*  .  G:/cache/temp/src/FINAL FANTASY XV";
//        s = "-dd*  .  G:/cache/temp/src/FINAL FANTASY XV";
//        s = "-ddn*  .  G:/cache/temp/src/FINAL FANTASY XV";
//        s = "-cd*  .  G:/cache/temp/src/FINAL FANTASY XV  G:/cache/temp";
//        s = "-md*  .  G:/cache/temp/src/FINAL FANTASY XV  G:/cache/temp";
        super.monkParam(s);
    }
}
