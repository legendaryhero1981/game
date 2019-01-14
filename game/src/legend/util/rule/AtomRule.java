package legend.util.rule;

import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.nonEmpty;

import java.util.function.BiFunction;
import java.util.regex.Matcher;

import legend.intf.IValue;

public class AtomRule extends ReplaceRule<AtomRule,String,String> implements IValue<AtomRule>{
    private String[] args;
    private String arg;
    private String argSplit = SPRT_ARG;

    AtomRule(ReplaceRuleEngine engine, int index, String rule){
        super(engine,index,rule);
        refreshRule(rule);
    }

    @Override
    String execute(BiFunction<AtomRule,String,String> strategy, String data){
        return strategy.apply(this,data);
    }

    @Override
    public AtomRule cloneValue(){
        return new AtomRule(engine,index,rule);
    }

    void refreshRule(String rule){
        Matcher matcher = compile(REG_RULE_ATOM).matcher(rule);
        if(matcher.matches()){
            name = matcher.group(1).toUpperCase();
            arg = matcher.group(3);
            if(nonEmpty(arg)) args = arg.split(argSplit);
            else args = null;
        }
        this.rule = rule;
    }

    String[] getArgs(){
        return args;
    }

    String getArg(){
        return arg;
    }

    String getArgSplit(){
        return argSplit;
    }

    void setArgSplit(String argSplit){
        this.argSplit = argSplit;
    }
}
