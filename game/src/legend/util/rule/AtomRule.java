package legend.util.rule;

import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.nonEmpty;
import static legend.util.rule.ReplaceRuleStrategy.provideStrategy;
import java.util.regex.Matcher;

import legend.intf.IValue;

public class AtomRule extends ReplaceRule implements IValue<AtomRule>{
    protected AtomRule(ReplaceRuleEngine engine, int index, String rule){
        super(engine,index,rule);
    }

    @Override
    protected String[] execute(String data){
        return strategy.apply(this,data);
    }

    @Override
    protected void refreshRule(String rule){
        Matcher matcher = compile(REG_RULE_ATOM).matcher(rule);
        if(matcher.matches()){
            name = matcher.group(1).toUpperCase();
            strategy = provideStrategy(name);
            String arg = matcher.group(3);
            if(nonEmpty(arg)) args = arg.split(SPRT_ARG);
            else args = null;
        }
        this.rule = rule;
    }

    @Override
    public AtomRule cloneValue(){
        return new AtomRule(engine,index,rule);
    }
}
