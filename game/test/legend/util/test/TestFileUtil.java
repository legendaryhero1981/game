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
        s = "-fddsa*  .  0  G:/games/Severance  10";
        super.monkParam(s);
    }
}
