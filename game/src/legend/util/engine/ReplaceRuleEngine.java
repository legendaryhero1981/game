package legend.util.engine;

import static legend.util.strategy.ReplaceRuleStrategy.ComplexStrategy;
import static legend.util.strategy.ReplaceRuleStrategy.LowerStrategy;
import static legend.util.strategy.ReplaceRuleStrategy.UpperStrategy;
import static legend.util.strategy.ReplaceRuleStrategy.ReplaceStrategy;
import static legend.util.strategy.ReplaceRuleStrategy.RegenRowStrategy;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import legend.intf.IValue;
import legend.util.engine.intf.IReplaceRuleEngine;
import legend.util.intf.IFileUtil;
import legend.util.rule.AtomRule;
import legend.util.rule.ComplexRule;

public final class ReplaceRuleEngine implements IReplaceRuleEngine,IFileUtil,IValue<ReplaceRuleEngine>{
    private Map<Integer,AtomRule> atomRuleMap = new Hashtable<>();
    private Map<Integer,ComplexRule> complexRuleMap = new Hashtable<>();
    private List<Integer> colIndexs;
    private List<String> results;
    private String[][][] atomsCache;
    private String[][][] complexsCache;
    private String replaceRule;
    private String colSplit;
    private String colSplitRegex = REG_SPRT_COL;
    private String fieldSplit = SPRT_FIELD;
    private String ruleSplit = SPRT_RULE;
    private int rulesCount;

    public ReplaceRuleEngine(String replaceRule){
        refreshRule(replaceRule);
    }

    @Override
    public ReplaceRuleEngine cloneValue(){
        return new ReplaceRuleEngine(replaceRule);
    }

    public List<String> execute(){
        AtomRule atomRule = atomRuleMap.get(0);
        ComplexRule complexRule = complexRuleMap.get(0);
        atomRule.execute(LowerStrategy,atomsCache[0][1][0]);
        complexRule.execute(ComplexStrategy,complexsCache[0][1][0]);
        return null;
    }

    public void refreshEngine(String replaceRule, List<String> rows, String... colSplitRegex){
        refreshRule(replaceRule);
        refreshData(rows,colSplitRegex);
    }

    public void refreshRule(String replaceRule){}

    public void refreshData(List<String> rows, String... colSplitRegex){}
}
