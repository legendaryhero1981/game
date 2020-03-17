package legend.util.rule;

import static legend.util.StringUtil.concat;
import static legend.util.rule.ReplaceRuleStrategy.provideStrategy;

import legend.intf.IValue;

public class ComplexRule extends ReplaceRule implements IValue<ComplexRule>{
    protected AtomRule[] atomRules;

    protected ComplexRule(ReplaceRuleEngine engine, String rule){
        super(engine,rule);
        name = SPRT_ATOMS;
        strategy = provideStrategy(name);
    }

    @Override
    protected String[] execute(String data){
        return strategy.apply(this,data);
    }

    @Override
    protected void refreshRule(String rule){
        String[] rules = rule.split(SPRT_ATOMS);
        atomRules = new AtomRule[rules.length];
        for(int i = 0;i < rules.length;i++){
            AtomRule atomRule = new AtomRule(engine,rules[i]);
            atomRules[i] = atomRule;
        }
        this.rule = concat(atomRules,SPRT_ATOMS);
    }

    @Override
    public ComplexRule cloneValue(){
        return new ComplexRule(engine,rule);
    }
}
