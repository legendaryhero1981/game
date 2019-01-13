package legend.util.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import legend.intf.IValue;

public class ComplexRule extends ReplaceRule<ComplexRule,String,List<String>> implements IValue<ComplexRule>{
    private List<AtomRule> atomRules = new ArrayList<>();
    private String atomSplit = SPRT_ATOM;

    public ComplexRule(int index, String rule){
        super(index,rule);
        refreshAtomRules(rule);
    }

    @Override
    public List<String> execute(BiFunction<ComplexRule,String,List<String>> strategy, String data){
        return strategy.apply(this,data);
    }

    @Override
    public ComplexRule cloneValue(){
        return new ComplexRule(index,rule);
    }

    public List<AtomRule> refreshAtomRules(String rule){
        atomRules.clear();
        String[] rules = rule.split(atomSplit);
        for(int i = 0;i < rules.length;i++){
            AtomRule atomRule = new AtomRule(i + 1,rules[i]);
            atomRules.add(atomRule);
        }
        this.rule = rule;
        return atomRules;
    }

    public List<AtomRule> getAtomRules(){
        return atomRules;
    }

    public String getAtomSplit(){
        return atomSplit;
    }

    public void setAtomSplit(String atomSplit){
        this.atomSplit = atomSplit;
    }
}
