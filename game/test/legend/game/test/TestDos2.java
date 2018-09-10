package legend.game.test;

import static legend.game.dos2.Main.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestDos2 extends TestBase{
    @Test
    public void test(){
        main(args);
    }

    @BeforeAll
    @Override
    public void monkParam(){
        String s = "-d  ";
        super.monkParam(s);
    }
}
