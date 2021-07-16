package legend.util.rule;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.CS;
import static legend.util.StringUtil.concat;
import static legend.util.ValueUtil.arrayToUniqueCollection;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.isNull;
import static legend.util.ValueUtil.limitValue;
import static legend.util.ValueUtil.nonEmpty;
import static legend.util.rule.ReplaceLogicStrategy.ProvideReplaceLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.regex.Matcher;

import legend.util.rule.intf.IReplaceRule;

public final class ReplaceRuleStrategy implements IReplaceRule{
    private static final Map<String,BiFunction<ReplaceRule,String,String[]>> strategiesCache;
    static{
        strategiesCache = ofEntries(entry(SPRT_ATOMS,(rule, data)->{
            AtomRule[] atomRules = ((ComplexRule)rule).atomRules;
            String[] results = new String[atomRules.length];
            for(int i = 0;i < atomRules.length;i++){
                results[i] = atomRules[i].execute(data)[0];
                data = results[i];
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
            String[] results = new String[1], args = rule.args;
            if(1 < args.length){
                Matcher queryMatcher = compile(args[0]).matcher(data), logicMatcher = compile(REG_LOGIC_FUNC).matcher(args[1]);
                if(queryMatcher.find()){
                    if(logicMatcher.find()){
                        StringBuilder queryBuilder = new StringBuilder(), logicBuilder = new StringBuilder();
                        doReplaceLogic(queryMatcher,logicMatcher,queryBuilder,logicBuilder,args[1]);
                        while(queryMatcher.find()) doReplaceLogic(queryMatcher,logicMatcher,queryBuilder,logicBuilder,args[1]);
                        queryMatcher.appendTail(queryBuilder);
                        results[0] = queryBuilder.toString();
                    }else results[0] = queryMatcher.replaceAll(args[1]);
                }else if(2 == args.length) results[0] = data;
                else results[0] = args[2];
            }else results[0] = data;
            return results;
        }),entry(RULE_SINGLEROW,ReplaceRuleStrategy::everyRow),entry(RULE_FINALSINGLEROW,ReplaceRuleStrategy::finalRow),entry(RULE_DISTFINALSINGLEROW,ReplaceRuleStrategy::distFinalRow),entry(RULE_MULTIROW,ReplaceRuleStrategy::everyRow),entry(RULE_FINALMULTIROW,ReplaceRuleStrategy::finalRow),entry(RULE_DISTFINALMULTIROW,ReplaceRuleStrategy::distFinalRow));
    }

    protected static BiFunction<ReplaceRule,String,String[]> ProvideRuleStrategy(String name){
        return strategiesCache.get(name);
    }

    private static void doReplaceLogic(Matcher queryMatcher, Matcher logicMatcher, StringBuilder queryBuilder, StringBuilder logicBuilder, String logicExpr){
        int groupCount = queryMatcher.groupCount();
        logicMatcher.reset(logicExpr);
        while(logicMatcher.find()){
            String name = logicMatcher.group(1).toUpperCase(), variable = logicMatcher.group(3);
            BiFunction<String,String,String> logicStrategy = ProvideReplaceLogic(name);
            CS.checkError(ERR_LOGIC_FUNC,new String[]{name,ERR_LOGIC_INVALID},()->isNull(logicStrategy));
            int group = Integer.valueOf(logicMatcher.group(2));
            CS.checkError(ERR_LOGIC_FUNC,new String[]{name,ERR_LOGIC_GROUP},()->groupCount < group);
            logicMatcher.appendReplacement(logicBuilder,logicStrategy.apply(queryMatcher.group(group),variable));
        }
        logicMatcher.appendTail(logicBuilder);
        queryMatcher.appendReplacement(queryBuilder,logicBuilder.toString());
        logicBuilder.delete(0,logicBuilder.length());
    }

    private static String[] finalRow(ReplaceRule rule, String data){
        return finalRow(rule,()->everyRow(rule,data));
    }

    private static String[] distFinalRow(ReplaceRule rule, String data){
        return finalRow(rule,()->arrayToUniqueCollection(everyRow(rule,data)));
    }

    private static <T> String[] finalRow(ReplaceRule rule, Supplier<T> supplier){
        String[] results = new String[1], args = rule.args;
        T rows = supplier.get();
        switch(args.length){
            case 2:
            results[0] = concat(rows,args[1],true);
            break;
            case 3:
            results[0] = args[2] + concat(rows,args[1],true);
            break;
            case 4:
            results[0] = args[2] + concat(rows,args[1],true) + args[3];
            break;
            default:
            results[0] = concat(rows,S_EMPTY);
        }
        return results;
    }

    private static String[] everyRow(ReplaceRule rule, String data){
        Map<Integer,String[][]> atomsCache = rule.engine.atomsCache;
        Map<Integer,String[][][]> complexesCache = rule.engine.complexesCache;
        final int size = atomsCache.size(), colSize = atomsCache.get(0).length, dataSize = data.length();
        String[] results = new String[size];
        Matcher matcher = PTRN_COL_REPL.matcher(data);
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
                matchList.add(S_EMPTY);
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
                results[i] = S_EMPTY;
                for(int j = 0;j < indexes.length;j++){
                    int x = indexes[j][0], y, z;
                    switch(indexes[j].length){
                        case 3:
                        if(0 < complexes[x].length){
                            y = limitValue(indexes[j][1],1,complexes[x].length) - 1;
                            z = limitValue(indexes[j][2],1,complexes[x][y].length) - 1;
                            results[i] += complexes[x][y][z];
                        }
                        break;
                        case 2:
                        if(0 < atoms[x].length){
                            y = limitValue(indexes[j][1],0,atoms[x].length - 1);
                            results[i] += atoms[x][y];
                        }
                        break;
                        default:
                        results[i] += matches[x];
                    }
                }
            });
        }
        return results;
    }
}
