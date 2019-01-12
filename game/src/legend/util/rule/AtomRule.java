package legend.util.rule;

import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;

import legend.intf.ICommon;
import legend.intf.IValue;

public class AtomRule implements ICommon,IValue<AtomRule>{
    private String[] args;
    private String rule;
    private String arg;
    private String name;
    private String argSplit = SPRT_ARG;

    public AtomRule(String rule){
        refreshRule(rule);
    }

    @Override
    public AtomRule cloneValue(){
        return new AtomRule(rule);
    }

    public void refreshRule(String rule){
        Matcher matcher = compile(REG_RULE_ATOM).matcher(rule);
        if(matcher.matches()){
            name = matcher.group(1);
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

    public String getRule(){
        return rule;
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
