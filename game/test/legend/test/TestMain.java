package legend.test;

import static legend.Main.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestMain extends TestBase{
    @Test
    public void test(){
        main(args);
    }

    @BeforeAll
    @Override
    public void monkParam(){
        String cmd = "";
//        cmd = "il";
//        cmd += "file";
        cmd += "run";
//        cmd += "eoc";
//        cmd += "kcd";
//        cmd += "poe";
        String arg = "";
        arg = "  -k  ew";
        cmd += arg;
        super.monkParam(cmd);
    }
}
