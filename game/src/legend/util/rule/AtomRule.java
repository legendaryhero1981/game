package legend.util.rule;

import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.quote;
import static legend.util.StringUtil.brph;
import static legend.util.StringUtil.concat;
import static legend.util.ValueUtil.nonEmpty;
import static legend.util.rule.ReplaceRuleStrategy.ProvideRuleStrategy;

import java.util.regex.Matcher;

import legend.intf.IValue;

public class AtomRule extends ReplaceRule implements IValue<AtomRule>{
    protected AtomRule(ReplaceRuleEngine engine, String rule){
        super(engine,rule);
    }

    @Override
    protected String[] execute(String data){
        return strategy.apply(this,data);
    }

    @Override
    protected void refreshRule(String rule){
        Matcher matcher = PTRN_RULE_ATOM.matcher(rule);
        if(matcher.matches()){
            name = matcher.group(1).toUpperCase();
            if(!RULE_CON_MAP.containsKey(name)) return;
            condition = RULE_CON_MAP.get(name);
            strategy = ProvideRuleStrategy(name);
            String arg = matcher.group(2);
            if(nonEmpty(arg)){
                StringBuilder sb = new StringBuilder();
                Matcher mnul = PTRN_SPC_NUL.matcher(S_EMPTY);
                args = arg.split(REG_SPRT_ARGS);
                for(int i = 0;i < args.length;i++){
                    args[i] = brph(args[i],RULE_REG_MAP.get(condition));
                    mnul.reset(args[i]);
                    while(mnul.find() && !engine.quotesCache.isEmpty()) if(meetCondition(TMNT_RULE)) mnul.appendReplacement(sb,quoteReplacement(engine.quotesCache.remove()));
                    else if(0 == i) mnul.appendReplacement(sb,quoteReplacement(quote(engine.quotesCache.remove())));
                    else mnul.appendReplacement(sb,quoteReplacement(quoteReplacement(engine.quotesCache.remove())));
                    args[i] = mnul.appendTail(sb).toString();
                    sb.delete(0,sb.length());
                }
                matcher = PTRN_RULE_ATOM_QUOTE.matcher(rule);
                if(matcher.matches()) rule = matcher.group(1).concat(concat(args,SPRT_ARGS)).concat(matcher.group(2));
            }
        }
        this.rule = rule;
    }

    @Override
    public AtomRule cloneValue(){
        return new AtomRule(engine,rule);
    }
}
