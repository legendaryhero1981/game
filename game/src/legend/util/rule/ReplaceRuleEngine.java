package legend.util.rule;

import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ValueUtil.nonEmpty;
import static legend.util.rule.ReplaceRuleStrategy.provideAtomStrategy;
import static legend.util.rule.ReplaceRuleStrategy.provideComplexStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;

import legend.intf.IValue;
import legend.util.rule.intf.IReplaceRuleEngine;

public final class ReplaceRuleEngine implements IReplaceRuleEngine,IValue<ReplaceRuleEngine>{
    private Map<Integer,AtomRule> atomRulesCache = new ConcurrentHashMap<>();
    private Map<Integer,ComplexRule> complexRulesCache = new ConcurrentHashMap<>();
    protected List<String[][]> atomsCache = new CopyOnWriteArrayList<>();
    protected List<String[][][]> complexesCache = new CopyOnWriteArrayList<>();
    private List<String> resultsCache = new CopyOnWriteArrayList<>();
    private Set<Integer> colIndexsCache = new HashSet<>();
    private String colNumber;
    private String rule;
    private String colSplit;
    private String colSplitRegex = REG_SPRT_COL;
    private int rulesSize;

    public ReplaceRuleEngine(String replaceRule){
        refreshRule(replaceRule);
    }

    @Override
    public ReplaceRuleEngine cloneValue(){
        return new ReplaceRuleEngine(rule);
    }

    public List<String> execute(){
        atomsCache.parallelStream().forEach(atoms->{
            colIndexsCache.parallelStream().forEach(i->{
                excuteRules(atoms[i][0],atoms[i],complexesCache.get(Integer.parseInt(atoms[0][0]))[i]);
            });
        });
        return resultsCache;
    }

    public void refreshEngine(String replaceRule, List<String> datas){
        refreshRule(replaceRule);
        refreshData(datas);
    }

    public void refreshEngine(String replaceRule, List<String> datas, String colSplitRegex){
        refreshRule(replaceRule);
        refreshData(datas,colSplitRegex);
    }

    public void refreshRule(String replaceRule){
        clearRuleCache();
        String[] s = replaceRule.split(REG_SPRT_FIELD);
        CS.showError(ERR_RULE_FMT,null,()->s.length > 2);
        if(s.length == 2){
            colNumber = s[0];
            rule = s[1];
        }else{
            colNumber = "";
            rule = s[0];
        }
        String[] r = rule.split(REG_SPRT_RULE);
        rulesSize = r.length;
        for(int i = 0;i < rulesSize;i++){
            if(r[i].contains(SPRT_ATOM)) complexRulesCache.put(i + 1,new ComplexRule(this,i + 1,r[i]));
            else atomRulesCache.put(i + 1,new AtomRule(this,i + 1,r[i]));
        }
    }

    public void refreshData(List<String> datas){
        clearDataCache();
        if(nonEmpty(datas)){
            final int datasSize = datas.size(), atomsSize = atomRulesCache.size(),
                            complexesSize = complexRulesCache.size();
            String[] data = datas.toArray(new String[datasSize]);
            for(int i = 0,j;i < datasSize;i++){
                String[] s = data[i].split(colSplitRegex);
                String[][] atoms = new String[s.length + 1][atomsSize + 1];
                String[][][] complexes = new String[s.length + 1][complexesSize + 1][];
                atoms[0][0] = String.valueOf(i);
                for(j = 0;j < s.length;j++){
                    atoms[j + 1][0] = s[j];
                    complexes[j + 1][0] = new String[1];
                    complexes[j + 1][0][0] = s[j];
                }
                atomsCache.add(atoms);
                complexesCache.add(complexes);
            }
            final int size = complexesCache.stream().findFirst().get().length;
            CS.showError(ERR_RULE_FMT,null,()->complexesCache.parallelStream().anyMatch(c->size != c.length));
            if(nonEmpty(colNumber)){
                Matcher matcher = compile(REG_COL_NUM).matcher(colNumber);
                while(matcher.find()){
                    int start = Integer.parseInt(matcher.group(1));
                    String s = matcher.group(3);
                    int end = nonEmpty(s) ? Integer.parseInt(s) : start;
                    if(start > end){
                        start += end;
                        end = start - end;
                        start -= end;
                    }
                    if(end > size) end = size;
                    for(int i = start;i < end;i++)
                        colIndexsCache.add(i);
                }
                CS.showError(ERR_RULE_FMT,null,()->colIndexsCache.isEmpty());
            }else for(int i = 0;i < size;i++)
                colIndexsCache.add(i);
        }
    }

    public void refreshData(List<String> datas, String colSplitRegex){
        this.colSplitRegex = colSplitRegex;
        refreshData(datas);
    }

    private void excuteRules(String data, String[] atoms, String[][] complexes){
        for(int i = 1;i <= rulesSize;i++){
            AtomRule atomRule = atomRulesCache.get(i);
            if(nonEmpty(atomRule)){
                atoms[i] = atomRule.execute(provideAtomStrategy(atomRule.name),data);
            }else{
                ComplexRule complexRule = complexRulesCache.get(i);
                complexes[i] = complexRule.execute(provideComplexStrategy(complexRule.name),data);
            }
        }
    }

    private void clearRuleCache(){
        atomRulesCache.clear();
        complexRulesCache.clear();
    }

    private void clearDataCache(){
        atomsCache.clear();
        complexesCache.clear();
        resultsCache.clear();
        colIndexsCache.clear();
    }
}
