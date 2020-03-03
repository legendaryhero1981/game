package legend.game.test;

import static legend.game.poe2.Main.main;

import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestPoe2 extends TestBase{
    @Test
    public void testMain(){
        main(args);
    }

    @Override
    public String monk(){
        String s = "";
        s = "-e  (?i)\\..*bundle$  G:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data/exported/design/gamedata";
//        s = "-d  (?i)\\..*bundle$  G:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data/exported/design/gamedata";
        return s;
    }
}
