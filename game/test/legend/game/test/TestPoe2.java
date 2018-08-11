package legend.game.test;

import org.junit.Before;
import org.junit.Test;

import legend.game.poe2.Main;
import legend.test.TestBase;

public class TestPoe2 extends TestBase{
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
        s = "-d  (?i)\\..*bundle$  F:/games/Pillars of Eternity II/PillarsOfEternityII_Data/exported/design/gamedata";
        s = "-d  (?i)\\Aglobal.gamedatabundle$  F:/games/Pillars of Eternity II/PillarsOfEternityII_Data/exported/design/gamedata";
        super.monkParam(s);
    }
}
