package legend.helper.test;

import static legend.helper.ILHelper.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestILHelper extends TestBase{
    @Test
    public void test(){
        main(args);
    }

    @BeforeAll
    @Override
    public void monkParam(){
        String s = "";
        // s = "-i";
        // s = "-i ldc.i4";
        // s = "-s";
        // s = "-s instance";
        super.monkParam(s);
    }
}
