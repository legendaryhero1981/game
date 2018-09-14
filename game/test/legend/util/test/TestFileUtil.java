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
//        s = "-zdd!*  .  G:\\cache\\temp\\src  G:\\cache\\temp\\dest  src";
//        s = "-c!*  \\.bat$  d:\\games\\jlink  d:\\games\\jlink\\game\\bin  1";
        s = "-dd`  .  d:\\games\\jlink\\game";
        super.monkParam(s);
    }
}
