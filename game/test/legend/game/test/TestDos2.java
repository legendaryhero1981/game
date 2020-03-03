package legend.game.test;

import static legend.game.dos2.Main.main;

import org.junit.jupiter.api.Test;

import legend.test.TestBase;

public class TestDos2 extends TestBase{
    @Test
    public void test(){
        main(args);
    }

    @Override
    public String monk(){
        String s = "";
        s="-d  F:/games/Divinity Original Sin 2/修改/release/english.xml  F:/games/Divinity Original Sin 2/修改/debug/english.xml";
//        s="-r  F:/games/Divinity Original Sin 2/修改/debug/english.xml  F:/games/Divinity Original Sin 2/修改/release/english.xml";
//        s="-u  F:/games/Divinity Original Sin 2/修改/release/english.xml  F:/games/Divinity Original Sin 2/修改/debug/english.xml";
        return s;
    }
}
