package legend.util.rule;

import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.CS;
import static legend.util.StringUtil.brph;
import static legend.util.StringUtil.concat;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

import legend.intf.IValue;
import legend.util.param.SingleValue;
import legend.util.rule.intf.IReplaceRuleEngine;

public final class ReplaceRuleEngine implements IReplaceRuleEngine,IValue<ReplaceRuleEngine>{
    protected Map<Integer,String[][]> atomsCache = new ConcurrentHashMap<>();
    protected Map<Integer,String[][][]> complexesCache = new ConcurrentHashMap<>();
    protected Deque<String> quotesCache = new ArrayDeque<>();
    private Set<Integer> colIndexesCache = new HashSet<>();
    private ReplaceRule[] rules;
    private Matcher mbq = PTRN_QUOTE_BQ.matcher(S_EMPTY);
    private String colSplit;
    private String colNumber;
    private String rule;
    private int atomsSize;
    private int complexesSize;
    private long condition;

    public static IReplaceRuleEngine ProvideRuleEngine(String rule){
        return new ReplaceRuleEngine(rule);
    }

    @Override
    public ReplaceRuleEngine cloneValue(){
        return new ReplaceRuleEngine(rule);
    }

    @Override
    public Collection<String> execute(Collection<String> datas, String colSplitRegex){
        if(!refreshData(datas,colSplitRegex)) return datas;
        excuteRules();
        return dealResults();
    }

    @Override
    public void refreshRule(String replaceRule){
        quotesCache.clear();
        mbq.reset(replaceRule);
        while(mbq.find()) quotesCache.add(mbq.group(1));
        String[] s = brph(mbq.replaceAll(SPC_NUL),SPH_MAP).split(REG_SPRT_FIELDS);
        CS.checkError(ERR_RULE_ANLS,new String[]{ERR_RULE_FMT},()->s.length > 2 || isEmpty(s[s.length - 1]));
        if(s.length == 2){
            colNumber = s[0];
            Matcher matcher = PTRN_COL_NUM.matcher(colNumber);
            CS.checkError(ERR_RULE_ANLS,new String[]{ERR_RULE_COL_NUM},()->!matcher.find());
            rule = s[1];
        }else{
            colNumber = S_EMPTY;
            rule = s[0];
        }
        String[] r = rule.split(REG_SPRT_RULES);
        rules = new ReplaceRule[r.length];
        atomsSize = complexesSize = 0;
        IValue<Boolean> incomplete = new SingleValue<Boolean>(false);
        for(int i = 0;i < r.length;i++){
            if(r[i].contains(SPRT_ATOMS)){
                ComplexRule complexRule = new ComplexRule(this,r[i]);
                rules[i] = complexRule;
                complexesSize++;
            }else{
                rules[i] = new AtomRule(this,r[i]);
                atomsSize++;
            }
            incomplete.setValue(i < r.length - 1);
            validateRule(rules[i],incomplete);
        }
        rule = concat(rules,SPRT_RULES);
        condition = ATOM_RULE;
        if(rules[rules.length - 1].meetCondition(TMNT_RULE)) condition |= TMNT_RULE;
    }

    @Override
    public String toString(){
        return rule;
    }

    private ReplaceRuleEngine(String rule){
        refreshRule(rule);
    }

    private boolean refreshData(Collection<String> datas, String colSplit){
        atomsCache.clear();
        complexesCache.clear();
        colIndexesCache.clear();
        if(isEmpty(datas)) return false;
        final int datasSize = datas.size();
        String[] data = datas.toArray(new String[datasSize]);
        for(int i = 0,j,k,l;i < datasSize;i++){
            String[] s = data[i].split(colSplit);
            String[][] atoms = new String[s.length][atomsSize + 1];
            String[][][] complexes = new String[s.length][complexesSize][];
            for(j = 0;j < s.length;j++){
                atoms[j][0] = s[j];
                for(k = l = 0;l < rules.length;){
                    ReplaceRule rule = rules[l++];
                    if(rule instanceof ComplexRule) complexes[j][k++] = new String[((ComplexRule)rule).atomRules.length];
                }
            }
            atomsCache.put(i,atoms);
            complexesCache.put(i,complexes);
        }
        final int size = atomsCache.get(0).length;
        if(CS.checkException(ERR_DATA_ANLS,new String[]{ERR_DATA_COL_NUM},()->atomsCache.values().parallelStream().anyMatch(v->size != v.length))) return false;
        if(nonEmpty(colNumber)){
            Matcher matcher = PTRN_COL_NUM.matcher(colNumber);
            while(matcher.find()){
                int start = Integer.parseInt(matcher.group(1));
                if(start > size) start = size;
                int end = nonEmpty(matcher.group(2)) ? Integer.parseInt(matcher.group(2)) : start;
                if(end > size) end = size;
                if(start > end){
                    start += end;
                    end = start - end;
                    start -= end;
                }
                for(int i = start - 1;i < end;i++) colIndexesCache.add(i);
            }
        }else for(int i = 0;i < size;i++) colIndexesCache.add(i);
        Matcher matcher = compile(colSplit).matcher(data[0]);
        if(matcher.find()) this.colSplit = matcher.group();
        else this.colSplit = S_EMPTY;
        return true;
    }

    private void excuteRules(){
        final int length = meetCondition(TMNT_RULE) ? rules.length - 1 : rules.length;
        if(1 > length) return;
        atomsCache.entrySet().parallelStream().forEach(entry->{
            String[][] atoms = entry.getValue();
            String[][][] complexes = complexesCache.get(entry.getKey());
            colIndexesCache.parallelStream().forEach(index->{
                String data = atoms[index][0];
                for(int i = 0,j = 0,k = 0;i < length;){
                    ReplaceRule replaceRule = rules[i++];
                    if(replaceRule instanceof AtomRule){
                        atoms[index][++j] = replaceRule.execute(data)[0];
                        data = atoms[index][j];
                    }else{
                        complexes[index][k] = replaceRule.execute(data);
                        data = complexes[index][k][complexes[index][k++].length - 1];
                    }
                }
            });
        });
    }

    private List<String> dealResults(){
        List<String> results = new ArrayList<>(atomsCache.size());
        ReplaceRule replaceRule = rules[rules.length - 1];
        if(meetCondition(TMNT_RULE)){
            String[] s = replaceRule.execute(replaceRule.args[0]);
            for(int i = 0;i < s.length;i++) results.add(s[i]);
        }else{
            final int length = atomsCache.get(0).length;
            if(replaceRule instanceof AtomRule){
                atomsCache.values().stream().forEach(atoms->{
                    String data = atoms[0][atomsSize];
                    for(int i = 1;i < length;i++) data += colSplit + atoms[i][atomsSize];
                    results.add(data);
                });
            }else{
                complexesCache.values().stream().forEach(complexes->{
                    String data = complexes[0][complexesSize - 1][complexes[0][complexesSize - 1].length - 1];
                    for(int i = 1;i < length;i++) data += colSplit + complexes[i][complexesSize - 1][complexes[i][complexesSize - 1].length - 1];
                    results.add(data);
                });
            }
        }
        return results;
    }

    private boolean meetCondition(long condition){
        return condition == (condition & this.condition);
    }

    private void validateRule(ReplaceRule replaceRule, IValue<Boolean> incomplete){
        CS.checkError(ERR_RULE_ANLS,new String[]{gsph(ERR_RULE_INVALID,replaceRule.name)},()->isEmpty(replaceRule.strategy));
        CS.checkError(ERR_RULE_ANLS,new String[]{gsph(ERR_RULE_TMNT,replaceRule.name)},()->replaceRule.meetCondition(TMNT_RULE) && incomplete.getValue());
    }
}
