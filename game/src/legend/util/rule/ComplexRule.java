package legend.util.rule;

import java.util.function.BiFunction;

import legend.intf.IValue;

public class ComplexRule extends ReplaceRule<ComplexRule,String,String[]> implements IValue<ComplexRule>{
    private AtomRule[] atomRules;
    private String atomSplit = SPRT_ATOM;

    ComplexRule(ReplaceRuleEngine engine, int index, String rule){
        super(engine,index,rule);
        name = RULE_COMPLEX;
        refreshAtomRules(rule);
    }

    @Override
    String[] execute(BiFunction<ComplexRule,String,String[]> strategy, String data){
        return strategy.apply(this,data);
    }

    @Override
    public ComplexRule cloneValue(){
        return new ComplexRule(engine,index,rule);
    }

    AtomRule[] refreshAtomRules(String rule){
        String[] rules = rule.split(atomSplit);
        atomRules = new AtomRule[rules.length];
        for(int i = 0;i < rules.length;i++){
            AtomRule atomRule = new AtomRule(engine,i + 1,rules[i]);
            atomRules[i] = atomRule;
        }
        this.rule = rule;
        return atomRules;
    }

    AtomRule[] getAtomRules(){
        return atomRules;
    }

    String getAtomSplit(){
        return atomSplit;
    }

    void setAtomSplit(String atomSplit){
        this.atomSplit = atomSplit;
    }
}
