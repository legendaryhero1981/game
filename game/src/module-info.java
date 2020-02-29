open module legend{
    requires transitive java.xml.bind;
    requires static org.junit.jupiter.api;
    
    exports legend;
    exports legend.game.dos2;
    exports legend.game.dos2.entity;
    exports legend.game.dos2.intf;
    exports legend.game.kcd;
    exports legend.game.kcd.entity;
    exports legend.game.kcd.entity.local;
    exports legend.game.kcd.entity.mod;
    exports legend.game.kcd.intf;
    exports legend.game.poe2;
    exports legend.game.poe2.intf;
    exports legend.game.run;
    exports legend.game.run.entity;
    exports legend.game.run.intf;
    exports legend.helper;
    exports legend.helper.entity;
    exports legend.helper.intf;
    exports legend.intf;
    exports legend.util;
    exports legend.util.adapter;
    exports legend.util.chardet;
    exports legend.util.entity;
    exports legend.util.entity.intf;
    exports legend.util.intf;
    exports legend.util.logic;
    exports legend.util.logic.intf;
    exports legend.util.param;
    exports legend.util.rule;
    exports legend.util.rule.intf;
}