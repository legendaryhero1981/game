package legend.util.rule;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.limitValue;
import static legend.util.ValueUtil.nonEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

import legend.util.rule.intf.IReplaceRule;

public final class ReplaceRuleStrategy implements IReplaceRule{
    private static final Map<String,BiFunction<ReplaceRule,String,String[]>> strategiesCache;
    static{
        strategiesCache = ofEntries(entry(SPRT_ATOM,(rule, data)->{
            AtomRule[] atomRules = ((ComplexRule)rule).atomRules;
            String[] results = new String[atomRules.length];
            for(int i = 0;i < atomRules.length;i++){
                results[i] = atomRules[i].execute(data)[0];
                data = results[i];
            }
            return results;
        }),entry(RULE_REGENROW,(rule, data)->{
            Map<Integer,String[][]> atomsCache = rule.engine.atomsCache;
            Map<Integer,String[][][]> complexesCache = rule.engine.complexesCache;
            final int size = atomsCache.size(), colSize = atomsCache.get(0).length,
                            dataSize = data.length();
            String[] results = new String[size];
            Matcher matcher = compile(REG_COL_REPL).matcher(data);
            if(!matcher.find()) for(int i = 0;i < results.length;results[i++] = data);
            else{
                List<String> matchList = new ArrayList<>();
                List<Integer[]> indexList = new ArrayList<>();
                int start = 0, end = 0, index = 0;
                do{
                    end = matcher.start();
                    if(start < end){
                        matchList.add(data.substring(start,end));
                        indexList.add(new Integer[]{index++});
                    }
                    start = matcher.end();
                    matchList.add("");
                    String match = matcher.group();
                    if(match.contains(FLAG_COL_REPL_COMP)){
                        int x = limitValue(Integer.parseInt(matcher.group(3)),1,colSize) - 1;
                        int y = Integer.parseInt(matcher.group(4));
                        int z = nonEmpty(matcher.group(5)) ? Integer.parseInt(matcher.group(5)) : Integer.MAX_VALUE;
                        indexList.add(new Integer[]{x,y,z});
                    }else{
                        int x = limitValue(Integer.parseInt(matcher.group(1)),1,colSize) - 1;
                        int y = Integer.parseInt(matcher.group(2));
                        indexList.add(new Integer[]{x,y});
                    }
                    index++;
                }while(matcher.find());
                if(start < dataSize){
                    matchList.add(data.substring(start,dataSize));
                    indexList.add(new Integer[]{index});
                }
                String[] matches = matchList.toArray(new String[matchList.size()]);
                Integer[][] indexes = indexList.toArray(new Integer[indexList.size()][]);
                atomsCache.entrySet().parallelStream().forEach(entry->{
                    int i = entry.getKey();
                    String[][] atoms = entry.getValue();
                    String[][][] complexes = complexesCache.get(i);
                    results[i] = "";
                    for(int j = 0;j < indexes.length;j++){
                        int x = indexes[j][0];
                        switch(indexes[j].length){
                            case 3:
                            int y = limitValue(indexes[j][1],1,complexes[x].length) - 1;
                            int z = limitValue(indexes[j][2],1,complexes[x][y].length) - 1;
                            results[i] += complexes[x][y][z];
                            break;
                            case 2:
                            y = limitValue(indexes[j][1],0,atoms[x].length - 1);
                            results[i] += atoms[x][y];
                            break;
                            default:
                            results[i] += matches[x];
                        }
                    }
                });
            }
            return results;
        }),entry(RULE_LOWER,(rule, data)->{
            String[] args = rule.args;
            if(isEmpty(args)) return new String[]{data.toLowerCase()};
            else{
                Matcher matcher = compile(args[0]).matcher(data);
                if(matcher.find()) return new String[]{data.toLowerCase()};
            }
            return new String[]{data};
        }),entry(RULE_UPPER,(rule, data)->{
            String[] args = rule.args;
            if(isEmpty(args)) return new String[]{data.toUpperCase()};
            else{
                Matcher matcher = compile(args[0]).matcher(data);
                if(matcher.find()) return new String[]{data.toUpperCase()};
            }
            return new String[]{data};
        }),entry(RULE_REPLACE,(rule, data)->{
            String[] args = rule.args;
            if(isEmpty(args)) return new String[]{data};
            else if(1 == args.length) return new String[]{data.replaceAll(args[0],"")};
            else return new String[]{data.replaceAll(args[0],quoteReplacement(args[1]))};
        }));
    }

    protected static BiFunction<ReplaceRule,String,String[]> provideStrategy(String name){
        return strategiesCache.get(name);
    }
}
