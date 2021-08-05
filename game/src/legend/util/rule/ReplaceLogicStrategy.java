package legend.util.rule;

import static java.lang.Math.pow;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static legend.util.ConsoleUtil.CS;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

import legend.util.rule.intf.IReplaceLogic;

public class ReplaceLogicStrategy implements IReplaceLogic{
    private static final Map<String,BiFunction<String,String,String>> strategiesCache;
    static{
        strategiesCache = ofEntries(entry(ADD_INT,(value, variable)->{
            checkErrorFromNumber(ADD_INT,value,variable);
            int val = Integer.valueOf(value), var = Integer.valueOf(variable);
            val += var;
            return val + S_EMPTY;
        }),entry(ADD_LONG,(value, variable)->{
            checkErrorFromNumber(ADD_LONG,value,variable);
            long val = Long.valueOf(value), var = Long.valueOf(variable);
            val += var;
            return val + S_EMPTY;
        }),entry(ADD_FLOAT,(value, variable)->{
            checkErrorFromReal(ADD_FLOAT,value,variable);
            float val = Float.valueOf(value), var = Float.valueOf(variable);
            val += var;
            return val + S_EMPTY;
        }),entry(ADD_DOUBLE,(value, variable)->{
            checkErrorFromReal(ADD_DOUBLE,value,variable);
            double val = Double.valueOf(value), var = Double.valueOf(variable);
            val += var;
            return val + S_EMPTY;
        }),entry(SUB_INT,(value, variable)->{
            checkErrorFromNumber(SUB_INT,value,variable);
            int val = Integer.valueOf(value), var = Integer.valueOf(variable);
            val -= var;
            return val + S_EMPTY;
        }),entry(SUB_LONG,(value, variable)->{
            checkErrorFromNumber(SUB_LONG,value,variable);
            long val = Long.valueOf(value), var = Long.valueOf(variable);
            val -= var;
            return val + S_EMPTY;
        }),entry(SUB_FLOAT,(value, variable)->{
            checkErrorFromReal(SUB_FLOAT,value,variable);
            float val = Float.valueOf(value), var = Float.valueOf(variable);
            val -= var;
            return val + S_EMPTY;
        }),entry(SUB_DOUBLE,(value, variable)->{
            checkErrorFromReal(SUB_DOUBLE,value,variable);
            double val = Double.valueOf(value), var = Double.valueOf(variable);
            val -= var;
            return val + S_EMPTY;
        }),entry(MUL_INT,(value, variable)->{
            checkErrorFromNumber(MUL_INT,value,variable);
            int val = Integer.valueOf(value), var = Integer.valueOf(variable);
            val *= var;
            return val + S_EMPTY;
        }),entry(MUL_LONG,(value, variable)->{
            checkErrorFromNumber(MUL_LONG,value,variable);
            long val = Long.valueOf(value), var = Long.valueOf(variable);
            val *= var;
            return val + S_EMPTY;
        }),entry(MUL_FLOAT,(value, variable)->{
            checkErrorFromReal(MUL_FLOAT,value,variable);
            float val = Float.valueOf(value), var = Float.valueOf(variable);
            val *= var;
            return val + S_EMPTY;
        }),entry(MUL_DOUBLE,(value, variable)->{
            checkErrorFromReal(MUL_DOUBLE,value,variable);
            double val = Double.valueOf(value), var = Double.valueOf(variable);
            val *= var;
            return val + S_EMPTY;
        }),entry(DIV_INT,(value, variable)->{
            checkErrorFromNumber(DIV_INT,value,variable);
            int val = Integer.valueOf(value), var = Integer.valueOf(variable);
            val /= var;
            return val + S_EMPTY;
        }),entry(DIV_LONG,(value, variable)->{
            checkErrorFromNumber(DIV_LONG,value,variable);
            long val = Long.valueOf(value), var = Long.valueOf(variable);
            val /= var;
            return val + S_EMPTY;
        }),entry(DIV_FLOAT,(value, variable)->{
            checkErrorFromReal(DIV_FLOAT,value,variable);
            float val = Float.valueOf(value), var = Float.valueOf(variable);
            val /= var;
            return val + S_EMPTY;
        }),entry(DIV_DOUBLE,(value, variable)->{
            checkErrorFromReal(DIV_DOUBLE,value,variable);
            double val = Double.valueOf(value), var = Double.valueOf(variable);
            val /= var;
            return val + S_EMPTY;
        }),entry(MOD_INT,(value, variable)->{
            checkErrorFromNumber(MOD_INT,value,variable);
            int val = Integer.valueOf(value), var = Integer.valueOf(variable);
            val %= var;
            return val + S_EMPTY;
        }),entry(MOD_LONG,(value, variable)->{
            checkErrorFromNumber(MOD_LONG,value,variable);
            int val = Integer.valueOf(value), var = Integer.valueOf(variable);
            val %= var;
            return val + S_EMPTY;
        }),entry(MOD_FLOAT,(value, variable)->{
            checkErrorFromReal(MOD_FLOAT,value,variable);
            float val = Float.valueOf(value), var = Float.valueOf(variable);
            val %= var;
            return val + S_EMPTY;
        }),entry(MOD_DOUBLE,(value, variable)->{
            checkErrorFromReal(MOD_DOUBLE,value,variable);
            double val = Double.valueOf(value), var = Double.valueOf(variable);
            val %= var;
            return val + S_EMPTY;
        }),entry(POW_INT,(value, variable)->{
            checkErrorFromNumber(POW_INT,value,variable);
            int val = Integer.valueOf(value), var = Integer.valueOf(variable);
            return (int)pow(val,var) + S_EMPTY;
        }),entry(POW_LONG,(value, variable)->{
            checkErrorFromNumber(POW_LONG,value,variable);
            int val = Integer.valueOf(value), var = Integer.valueOf(variable);
            return (long)pow(val,var) + S_EMPTY;
        }),entry(POW_FLOAT,(value, variable)->{
            checkErrorFromReal(POW_FLOAT,value,variable);
            float val = Float.valueOf(value), var = Float.valueOf(variable);
            return (float)pow(val,var) + S_EMPTY;
        }),entry(POW_DOUBLE,(value, variable)->{
            checkErrorFromReal(POW_DOUBLE,value,variable);
            double val = Double.valueOf(value), var = Double.valueOf(variable);
            return pow(val,var) + S_EMPTY;
        }));
    }

    protected static BiFunction<String,String,String> ProvideReplaceLogic(String name){
        return strategiesCache.get(name);
    }

    private static void checkErrorFromNumber(String name, String... vars){
        for(String var : vars){
            Matcher matcher = PTRN_NUM_ROUND.matcher(var);
            CS.checkError(ERR_LOGIC_FUNC,new String[]{name,ERR_LOGIC_NUM},()->!matcher.matches());
        }
    }

    private static void checkErrorFromReal(String name, String... vars){
        for(String var : vars){
            Matcher matcher = PTRN_NUM_REAL.matcher(var);
            CS.checkError(ERR_LOGIC_FUNC,new String[]{name,ERR_LOGIC_REAL},()->!matcher.matches());
        }
    }
}
