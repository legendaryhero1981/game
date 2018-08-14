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
        s = "-fddsa*  .  G:/games/Severance  1mb  10";
//        s = "-cd@`::-md`  .::*  G:/cache/temp/FINAL FANTASY XV::G:/cache/temp/src/FINAL FANTASY XV  G:/cache/temp/src::G:/cache/temp/backup";
        super.monkParam(s);
    }
}
