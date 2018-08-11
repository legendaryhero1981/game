package legend.game.test;

import static legend.game.dos2.Main.main;

import org.junit.Before;
import org.junit.Test;

import legend.test.TestBase;

public class TestDos2 extends TestBase{
    @Test
    public void test(){
        main(args);
    }

    @Before
    @Override
    public void monkParam(){
        String s = "-d  ";
        super.monkParam(s);
    }
}
