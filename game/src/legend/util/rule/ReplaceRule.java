package legend.util.rule;

import java.util.function.BiFunction;

import legend.intf.ICommon;

public abstract class ReplaceRule<T1,T2,R> implements ICommon{
    protected int index;
    protected String rule;

    protected ReplaceRule(int index, String rule){
        this.index = index;
        this.rule = rule;
    }

    public abstract R execute(BiFunction<T1,T2,R> strategy, T2 data);

    public int getIndex(){
        return index;
    }

    public String getRule(){
        return rule;
    }
}
