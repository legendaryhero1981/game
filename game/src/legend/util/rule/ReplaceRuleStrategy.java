package legend.util.rule;

import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

import legend.util.rule.intf.IReplaceRule;

public final class ReplaceRuleStrategy implements IReplaceRule{
    private static final Map<String,BiFunction<ComplexRule,String,String[]>> complexStrategiesCache;
    private static final Map<String,BiFunction<AtomRule,String,String>> atomStrategiesCache;
    static{
        complexStrategiesCache = new ConcurrentHashMap<>();
        complexStrategiesCache.put(RULE_COMPLEX,(rule, data)->{
            AtomRule[] atomRules = rule.getAtomRules();
            String[] results = new String[atomRules.length];
            for(int i = 0;i < atomRules.length;i++)
                results[i] = atomRules[i].execute(provideAtomStrategy(atomRules[i].name),data);
            return results;
        });
        atomStrategiesCache = new ConcurrentHashMap<>();
        atomStrategiesCache.put(RULE_LOWER,(rule, data)->{
            String[] args = rule.getArgs();
            if(isEmpty(args)) return data.toLowerCase();
            else{
                Matcher matcher = compile(args[0]).matcher(data);
                if(matcher.find()) return data.toLowerCase();
            }
            return data;
        });
        atomStrategiesCache.put(RULE_UPPER,(rule, data)->{
            String[] args = rule.getArgs();
            if(isEmpty(args)) return data.toUpperCase();
            else{
                Matcher matcher = compile(args[0]).matcher(data);
                if(matcher.find()) return data.toUpperCase();
            }
            return data;
        });
        atomStrategiesCache.put(RULE_REPLACE,(rule, data)->{
            String[] args = rule.getArgs();
            if(nonEmpty(args) && 1 < args.length) return data.replaceAll(args[0],quoteReplacement(args[1]));
            return data;
        });
        atomStrategiesCache.put(RULE_REGENROW,(rule, data)->{
            ReplaceRuleEngine engine = rule.engine;
            List<String[][]> atomsCache = engine.atomsCache;
            List<String[][][]> complexesCache = engine.complexesCache;
            return data;
        });
    }

    static BiFunction<AtomRule,String,String> provideAtomStrategy(String name){
        return atomStrategiesCache.get(name);
    }

    static BiFunction<ComplexRule,String,String[]> provideComplexStrategy(String name){
        return complexStrategiesCache.get(name);
    }
}
