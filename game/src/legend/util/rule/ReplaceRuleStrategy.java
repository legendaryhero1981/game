package legend.util.rule;

import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

import legend.util.rule.intf.IReplaceRule;

public final class ReplaceRuleStrategy implements IReplaceRule{
    private static final Map<String,BiFunction<ReplaceRule,String,String[]>> strategiesCache;
    static{
        strategiesCache = new ConcurrentHashMap<>();
        strategiesCache.put(SPRT_ATOM,(rule, data)->{
            if(rule instanceof ComplexRule){
                AtomRule[] atomRules = ((ComplexRule)rule).atomRules;
                String[] results = new String[atomRules.length];
                for(int i = 0;i < atomRules.length;i++)
                    results[i] = atomRules[i].execute(data)[0];
                return results;
            }
            return new String[]{data};
        });
        strategiesCache.put(RULE_REGENROW,(rule, data)->{
            ReplaceRuleEngine engine = rule.engine;
            return new String[]{data};
        });
        strategiesCache.put(RULE_LOWER,(rule, data)->{
            String[] args = rule.args;
            if(isEmpty(args)) return new String[]{data.toLowerCase()};
            else{
                Matcher matcher = compile(args[0]).matcher(data);
                if(matcher.find()) return new String[]{data.toLowerCase()};
            }
            return new String[]{data};
        });
        strategiesCache.put(RULE_UPPER,(rule, data)->{
            String[] args = rule.args;
            if(isEmpty(args)) return new String[]{data.toUpperCase()};
            else{
                Matcher matcher = compile(args[0]).matcher(data);
                if(matcher.find()) return new String[]{data.toUpperCase()};
            }
            return new String[]{data};
        });
        strategiesCache.put(RULE_REPLACE,(rule, data)->{
            String[] args = rule.args;
            if(nonEmpty(args) && 1 < args.length) return new String[]{data.replaceAll(args[0],quoteReplacement(args[1]))};
            return new String[]{data};
        });
    }

    protected static BiFunction<ReplaceRule,String,String[]> provideStrategy(String name){
        return strategiesCache.get(name);
    }
}
