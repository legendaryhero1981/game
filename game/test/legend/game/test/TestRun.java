package legend.game.test;

import static legend.game.run.Main.main;

import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestRun extends TestBase{
    @Test
    public void testMain(){
        main(args);
    }

    @Override
    public String monk(){
        String s = "";
        // s = "-c ew F:/games/The Evil Within EvilWithin 恶灵附身";
        s = "-v";
        // s = "-x sg";
        // s = "-la";
        // s = "-l ew";
        // s = "-d bio4";
        s = "-a  remn  F:/games/Remnant/Remnant/Binaries/Win64  Remnant-Win64-Shipping  遗迹：灰烬重生";
        // s = "-k ew";
        return s;
    }
}
