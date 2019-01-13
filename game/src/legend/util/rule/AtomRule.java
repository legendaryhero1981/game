package legend.util.rule;

import static java.util.regex.Pattern.compile;

import java.util.function.BiFunction;
import java.util.regex.Matcher;

import legend.intf.IValue;

public class AtomRule extends ReplaceRule<AtomRule,String,String> implements IValue<AtomRule>{
    private String[] args;
    private String arg;
    private String name;
    private String argSplit = SPRT_ARG;

    public AtomRule(int index, String rule){
        super(index,rule);
        refreshRule(rule);
    }

    @Override
    public String execute(BiFunction<AtomRule,String,String> strategy, String data){
        return strategy.apply(this,data);
    }

    @Override
    public AtomRule cloneValue(){
        return new AtomRule(index,rule);
    }

    public void refreshRule(String rule){
        Matcher matcher = compile(REG_RULE_ATOM).matcher(rule);
        if(matcher.matches()){
            name = matcher.group(1).toUpperCase();
            arg = matcher.group(3);
            refreshArgs();
        }
        this.rule = rule;
    }

    public String[] refreshArgs(){
        return args = arg.split(argSplit);
    }

    public String[] getArgs(){
        return args;
    }

    public String getArg(){
        return arg;
    }

    public String getName(){
        return name;
    }

    public String getArgSplit(){
        return argSplit;
    }

    public void setArgSplit(String argSplit){
        this.argSplit = argSplit;
    }
}
