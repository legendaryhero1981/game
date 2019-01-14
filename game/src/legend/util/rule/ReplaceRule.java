package legend.util.rule;

import java.util.function.BiFunction;

import legend.util.rule.intf.IReplaceRule;

public abstract class ReplaceRule<T1,T2,R> implements IReplaceRule{
    protected ReplaceRuleEngine engine;
    protected int index;
    protected String rule;
    protected String name;

    protected ReplaceRule(ReplaceRuleEngine engine, int index, String rule){
        this.engine = engine;
        this.index = index;
        this.rule = rule;
    }

    abstract R execute(BiFunction<T1,T2,R> strategy, T2 data);
}
