package legend.util.rule;

import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

import legend.intf.IValue;
import legend.util.rule.intf.IReplaceRuleEngine;

public final class ReplaceRuleEngine implements IReplaceRuleEngine,IValue<ReplaceRuleEngine>{
    protected Map<Integer,String[][]> atomsCache = new ConcurrentHashMap<>();
    protected Map<Integer,String[][][]> complexesCache = new ConcurrentHashMap<>();
    private Set<Integer> colIndexesCache = new HashSet<>();
    private ReplaceRule[] rules;
    private String colSplit;
    private String colNumber;
    private String rule;
    private int atomsSize;
    private int complexesSize;
    private boolean hasTerminationRule;

    public static IReplaceRuleEngine ProvideRuleEngine(String rule){
        return new ReplaceRuleEngine(rule);
    }

    protected ReplaceRuleEngine(String rule){
        refreshRule(rule);
    }

    @Override
    public ReplaceRuleEngine cloneValue(){
        return new ReplaceRuleEngine(rule);
    }

    @Override
    public List<String> execute(List<String> datas, String colSplitRegex){
        refreshData(datas,colSplitRegex);
        excuteRules();
        return dealResults();
    }

    @Override
    public void refreshRule(String replaceRule){
        String[] s = replaceRule.split(REG_SPRT_FIELD);
        CS.showError(ERR_RULE_FMT,null,()->s.length > 2 || isEmpty(s[s.length - 1]));
        if(s.length == 2){
            colNumber = s[0];
            rule = s[1];
        }else{
            colNumber = "";
            rule = s[0];
        }
        String[] r = rule.split(REG_SPRT_RULE);
        rules = new ReplaceRule[r.length];
        atomsSize = complexesSize = 0;
        for(int i = 0;i < r.length;i++){
            if(r[i].contains(SPRT_ATOM)){
                ComplexRule complexRule = new ComplexRule(this,i + 1,r[i]);
                for(int j = 0;j < complexRule.atomRules.length;j++)
                    validateRule(complexRule.atomRules[j],j,complexRule.atomRules.length);
                rules[i] = complexRule;
                complexesSize++;
            }else{
                rules[i] = new AtomRule(this,i + 1,r[i]);
                atomsSize++;
            }
            validateRule(rules[i],i + 1,r.length);
        }
        hasTerminationRule = RULE_REGENROW.equals(rules[rules.length - 1].name) ? true : false;
    }

    private void refreshData(List<String> datas, String colSplitRegex){
        clearCache();
        if(nonEmpty(datas)){
            final int datasSize = datas.size();
            String[] data = datas.toArray(new String[datasSize]);
            for(int i = 0,j,k,l;i < datasSize;i++){
                String[] s = data[i].split(colSplitRegex);
                String[][] atoms = new String[s.length][atomsSize + 1];
                atoms[0][0] = String.valueOf(i);
                for(j = 0;j < s.length;j++)
                    atoms[j][0] = s[j];
                atomsCache.put(i,atoms);
                String[][][] complexes = new String[s.length][complexesSize][];
                for(k = l = 0;l < rules.length;){
                    ReplaceRule rule = rules[l++];
                    if(rule instanceof ComplexRule) complexes[j][k++] = new String[((ComplexRule)rule).atomRules.length];
                }
                complexesCache.put(i,complexes);
            }
            final int size = atomsCache.get(0).length;
            CS.showError(ERR_RULE_FMT,null,()->atomsCache.values().parallelStream().anyMatch(v->size != v.length));
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
                        colIndexesCache.add(i);
                }
                CS.showError(ERR_RULE_FMT,null,()->colIndexesCache.isEmpty());
            }else for(int i = 0;i < size;i++)
                colIndexesCache.add(i);
            Matcher matcher = compile(colSplitRegex).matcher(data[0]);
            if(compile(colSplitRegex).matcher(data[0]).find()) colSplit = matcher.group();
            else colSplit = "";
        }
    }

    private void excuteRules(){
        final int length = hasTerminationRule ? rules.length - 1 : rules.length;
        if(length < 1) return;
        atomsCache.entrySet().parallelStream().forEach(entry->{
            String[][] atoms = entry.getValue();
            String[][][] complexes = complexesCache.get(entry.getKey());
            colIndexesCache.parallelStream().forEach(index->{
                String data = atoms[index][0];
                for(int i = 0,j = 0,k = 0;i < length;i++,k++){
                    ReplaceRule replaceRule = rules[i];
                    if(replaceRule instanceof AtomRule){
                        atoms[index][++j] = replaceRule.execute(data)[0];
                        data = atoms[index][j];
                    }else{
                        complexes[index][k] = replaceRule.execute(data);
                        data = complexes[index][k][complexes[index][k].length - 1];
                    }
                }
            });
        });
    }

    private List<String> dealResults(){
        List<String> results = new ArrayList<>(atomsCache.size());
        ReplaceRule replaceRule = rules[rules.length - 1];
        if(hasTerminationRule){
            String[] s = replaceRule.execute(replaceRule.args[0]);
            for(int i = 0;i < s.length;i++)
                results.add(s[i]);
        }else{
            final int length = atomsCache.get(0).length;
            if(replaceRule instanceof AtomRule){
                atomsCache.values().stream().forEach(atoms->{
                    String data = "";
                    for(int i = 0;i < length;i++)
                        data = data.concat(atoms[i][atomsSize]).concat(colSplit);
                    results.add(data.substring(0,data.length() - colSplit.length()));
                });
            }else{
                complexesCache.values().stream().forEach(complexes->{
                    String data = "";
                    for(int i = 0;i < length;i++)
                        data = data.concat(complexes[i][complexesSize - 1][complexes[i][complexesSize - 1].length - 1]).concat(colSplit);
                    results.add(data.substring(0,data.length() - colSplit.length()));
                });
            }
        }
        return results;
    }

    private void validateRule(ReplaceRule replaceRule, int index, int length){
        if(isEmpty(replaceRule.strategy)) CS.showError(ERR_RULE_FMT,new String[]{replaceRule.name});
        if(RULE_REGENROW.equals(replaceRule.name) && index < length) CS.showError(ERR_RULE_FMT,null);
    }

    private void clearCache(){
        atomsCache.clear();
        complexesCache.clear();
        colIndexesCache.clear();
    }
}
