package legend.game.test;

import static legend.game.run.Main.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestRun extends TestBase{
    @Test
    public void testMain(){
        main(args);
    }

    @BeforeAll
    @Override
    public void monkParam(){
        String s = "";
        // s = "-c ew F:/games/The Evil Within EvilWithin 恶灵附身";
        // s = "-v";
        // s = "-x sg";
        // s = "-la";
        // s = "-l ew";
        // s = "-d 44";
        // s = "-a 44 F:/games/Resident Evil 4/Bin32 bio4 生化危机4";
        super.monkParam(s);
    }
}
