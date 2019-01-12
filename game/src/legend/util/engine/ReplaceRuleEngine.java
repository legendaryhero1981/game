package legend.util.engine;

import java.util.List;

import legend.intf.IValue;
import legend.util.intf.IFileUtil;
import legend.util.rule.AtomRule;
import legend.util.rule.ComplexRule;

public class ReplaceRuleEngine implements IFileUtil,IValue<ReplaceRuleEngine>{
    private List<AtomRule> atomRules;
    private List<ComplexRule> complexRules;
    private List<Integer> colIndexs;
    private List<String> results;
    private String[][][] atomsCache;
    private String[][][] complexsCache;
    private String replaceRule;
    private String colSplit;
    private String colSplitRegex = REG_SPRT_COL;
    private String fieldSplit = SPRT_FIELD;
    private String ruleSplit = SPRT_RULE;

    public ReplaceRuleEngine(String replaceRule){}

    @Override
    public ReplaceRuleEngine cloneValue(){
        return new ReplaceRuleEngine(replaceRule);
    }

    public List<String> execute(){
        return null;
    }

    public void refreshEngine(String replaceRule, List<String> rows, String... colSplitRegex){
        refreshRule(replaceRule);
        refreshData(rows,colSplitRegex);
    }

    public void refreshRule(String replaceRule){}

    public void refreshData(List<String> rows, String... colSplitRegex){}
}
