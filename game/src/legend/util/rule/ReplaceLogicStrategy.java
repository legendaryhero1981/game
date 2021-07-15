package legend.util.rule;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

import java.util.Map;
import java.util.function.BiFunction;

import legend.util.rule.intf.IReplaceLogic;

public class ReplaceLogicStrategy implements IReplaceLogic{
    private static final Map<String,BiFunction<String,String,String>> strategiesCache;
    static{
        strategiesCache = ofEntries(entry(ADD_INT,(data, logic)->{
            return data;
        }),entry(ADD_LONG,(data, logic)->{
            return data;
        }),entry(ADD_FLOAT,(data, logic)->{
            return data;
        }),entry(ADD_DOUBLE,(data, logic)->{
            return data;
        }),entry(SUB_INT,(data, logic)->{
            return data;
        }),entry(SUB_LONG,(data, logic)->{
            return data;
        }),entry(SUB_FLOAT,(data, logic)->{
            return data;
        }),entry(SUB_DOUBLE,(data, logic)->{
            return data;
        }),entry(MUL_INT,(data, logic)->{
            return data;
        }),entry(MUL_LONG,(data, logic)->{
            return data;
        }),entry(MUL_FLOAT,(data, logic)->{
            return data;
        }),entry(MUL_DOUBLE,(data, logic)->{
            return data;
        }),entry(DIV_INT,(data, logic)->{
            return data;
        }),entry(DIV_LONG,(data, logic)->{
            return data;
        }),entry(DIV_FLOAT,(data, logic)->{
            return data;
        }),entry(DIV_DOUBLE,(data, logic)->{
            return data;
        }));
    }

    protected static BiFunction<String,String,String> ProvideReplaceLogic(String name){
        return strategiesCache.get(name);
    }
}
