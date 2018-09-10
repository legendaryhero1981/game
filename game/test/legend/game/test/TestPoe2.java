package legend.game.test;

import static legend.game.poe2.Main.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestPoe2 extends TestBase{
    @Test
    public void testMain(){
        main(args);
    }

    @BeforeAll
    @Override
    public void monkParam(){
        String s = "";
        s = "-d  (?i)\\..*bundle$  F:/games/Pillars of Eternity II/PillarsOfEternityII_Data/exported/design/gamedata";
        s = "-d  (?i)\\Aglobal.gamedatabundle$  F:/games/Pillars of Eternity II/PillarsOfEternityII_Data/exported/design/gamedata";
        super.monkParam(s);
    }
}
