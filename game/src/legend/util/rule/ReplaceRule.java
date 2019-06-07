package legend.util.rule;

import java.util.function.BiFunction;

import legend.util.rule.intf.IReplaceRule;

public abstract class ReplaceRule implements IReplaceRule{
    protected BiFunction<ReplaceRule,String,String[]> strategy;
    protected ReplaceRuleEngine engine;
    protected String[] args;
    protected String rule;
    protected String name;

    protected ReplaceRule(ReplaceRuleEngine engine, String rule){
        this.engine = engine;
        refreshRule(rule);
    }

    protected abstract String[] execute(String data);

    protected abstract void refreshRule(String rule);

    @Override
    public String toString(){
        return rule;
    }
}
