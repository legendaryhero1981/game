package legend.game.test;

import org.junit.Before;
import org.junit.Test;

import legend.game.run.Main;
import legend.test.TestBase;

public class TestRun extends TestBase{
    public static void main(String[] args){
        Main.main(args);
    }

    @Test
    public void testMain(){
        Main.main(args);
    }

    @Before
    @Override
    public void monkParam(){
        String s = "";
//        s = "-c  ew  F:/games/The Evil Within  EvilWithin  恶灵附身";
//        s = "-v";
//        s = "-x  sg";
//        s = "-la";
//        s = "-l  ew";
//        s = "-d  44";
//        s = "-a  44  F:/games/Resident Evil 4/Bin32  bio4  生化危机4";
        super.monkParam(s);
    }
}
