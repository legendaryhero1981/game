package legend.helper.test;

import static legend.helper.ILHelper.main;

import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestILHelper extends TestBase{
    @Test
    public void test(){
        main(args);
    }

    @Override
    public String monk(){
        String s = "";
        // s = "-i";
        // s = "-i ldc.i4";
        // s = "-s";
        // s = "-s instance";
        return s;
    }
}
