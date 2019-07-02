package legend.util.rule;

import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;
import static legend.util.StringUtil.concat;
import static legend.util.ValueUtil.nonEmpty;
import static legend.util.rule.ReplaceRuleStrategy.provideStrategy;

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
        Matcher matom = compile(REG_RULE_ATOM).matcher(rule);
        if(matom.matches()){
            name = matom.group(1).toUpperCase();
            strategy = provideStrategy(name);
            String s = matom.group(3);
            if(nonEmpty(s)){
                args = s.split(SPRT_ARG);
                StringBuilder sb = new StringBuilder();
                Matcher mnul = compile(SPC_NUL).matcher(S_EMPTY);
                for(int i = 0;i < args.length;i++){
                    sb.delete(0,sb.length());
                    mnul.reset(args[i]);
                    while(mnul.find() && !engine.quotesCache.isEmpty())
                        switch(name){
                            case RULE_REGENROW:
                            mnul.appendReplacement(sb,quoteReplacement(engine.quotesCache.remove()));
                            break;
                            default:
                            if(0 == i) mnul.appendReplacement(sb,quoteReplacement(quote(engine.quotesCache.remove())));
                            else mnul.appendReplacement(sb,quoteReplacement(quoteReplacement(engine.quotesCache.remove())));
                        }
                    args[i] = mnul.appendTail(sb).toString();
                }
                Matcher maq = compile(REG_RULE_ATOM_QUOTE).matcher(rule);
                if(maq.matches()) rule = maq.group(1).concat(concat(args,SPRT_ARG)).concat(maq.group(2));
            }
        }
        this.rule = rule;
    }

    @Override
    public AtomRule cloneValue(){
        return new AtomRule(engine,rule);
    }
}
