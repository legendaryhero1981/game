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
//        s = "-fddsa*  .  G:/games/Severance  1mb  10";
//        s = "-fddsa*  .  F:/games";
        s = "-cd*  .  G:/games/Severance  G:/games/1";
        super.monkParam(s);
    }
}
