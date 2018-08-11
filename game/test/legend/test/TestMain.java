package legend.test;

import static legend.Main.main;

import org.junit.Before;
import org.junit.Test;

public class TestMain extends TestBase{
    @Test
    public void test(){
        main(args);
    }

    @Before
    @Override
    public void monkParam(){
        String cmd = "";
        cmd = "kcd  ";
        String arg = "";
//       arg = "-lm  \\.xml$  F:/games/KingdomComeDeliverance/修改/Mods/UltimateRealismOverhaul/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/merge";
       arg = "-lcu  text_ui_dialog\\.xml$  F:/games/KingdomComeDeliverance/修改/Mods/UltimateRealismOverhaul/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/diff";
//       arg = "-ld  \\.xml$  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/debug";
//        arg = "-lr  \\.xml$  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/debug  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses";
        cmd += arg;
        super.monkParam(cmd);
    }
}
