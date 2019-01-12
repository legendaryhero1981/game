package legend.util.rule;

import java.util.ArrayList;
import java.util.List;

import legend.intf.ICommon;
import legend.intf.IValue;

public class ComplexRule implements ICommon,IValue<ComplexRule>{
    private List<AtomRule> atomRules = new ArrayList<>();
    private String rule;
    private String atomSplit = SPRT_ATOM;

    public ComplexRule(String rule){
        refreshAtomRules(rule);
    }

    @Override
    public ComplexRule cloneValue(){
        return new ComplexRule(rule);
    }

    public List<AtomRule> refreshAtomRules(String rule){
        atomRules.clear();
        String[] rules = rule.split(atomSplit);
        for(int i = 0;i < rules.length;i++){
            AtomRule atomRule = new AtomRule(rules[i]);
            atomRules.add(atomRule);
        }
        this.rule = rule;
        return atomRules;
    }

    public List<AtomRule> getAtomRules(){
        return atomRules;
    }

    public String getRule(){
        return rule;
    }

    public String getAtomSplit(){
        return atomSplit;
    }

    public void setAtomSplit(String atomSplit){
        this.atomSplit = atomSplit;
    }
}
