package legend.game.test;

import static legend.game.kcd.Main.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestKCD extends TestBase{
    @Test
    public void testMain(){
        main(args);
    }

    @BeforeAll
    @Override
    public void monkParam(){
        String s = "";
        s = "-lm  text_ui_soul\\.xml$  F:/games/KingdomComeDeliverance/修改/Mods/UltimateRealismOverhaul/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/merge";
//        s = "-lc  text_ui_soul\\.xml$  F:/games/KingdomComeDeliverance/修改/Mods/UltimateRealismOverhaul/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/diff";
//        s = "-lm  (?i)\\.xml$  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/diff  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/merge";
//        s = "-ld  \\.xml$  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/debug";
//        s = "-lr  \\.xml$  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/debug  F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses";
//        s = "-mc  F:/games/KingdomComeDeliverance  F:/games/KingdomComeDeliverance/修改/Mods  F:/games/KingdomComeDeliverance/修改/Merge  F:/tools/KDiff3/kdiff3.exe";
//        s = "-mmf";
//        s = "-mmc";
//        s = "-mp";
//        s = "-mu";
//        s = "-mmo";
//        s = "-mmu";
//        s = "-mma";
        super.monkParam(s);
    }
}
