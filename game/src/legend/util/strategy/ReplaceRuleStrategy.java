package legend.util.strategy;

import java.util.List;
import java.util.function.BiFunction;

import legend.util.intf.IFileUtil;
import legend.util.rule.AtomRule;
import legend.util.rule.ComplexRule;

public final class ReplaceRuleStrategy implements IFileUtil{
    public static final BiFunction<ComplexRule,String,List<String>> ComplexStrategy;
    public static final BiFunction<AtomRule,String,String> LowerStrategy;
    public static final BiFunction<AtomRule,String,String> UpperStrategy;
    public static final BiFunction<AtomRule,String,String> ReplaceStrategy;
    public static final BiFunction<AtomRule,String,String> RegenRowStrategy;
    static{
        ComplexStrategy = new ComplexStrategy();
        LowerStrategy = new LowerStrategy();
        UpperStrategy = new UpperStrategy();
        ReplaceStrategy = new ReplaceStrategy();
        RegenRowStrategy = new RegenRowStrategy();
    }

    private static class ComplexStrategy implements BiFunction<ComplexRule,String,List<String>>{
        @Override
        public List<String> apply(ComplexRule complexRule, String data){
            return null;
        }
    }

    private static class LowerStrategy implements BiFunction<AtomRule,String,String>{
        @Override
        public String apply(AtomRule atomRule, String data){
            return null;
        }
    }

    private static class UpperStrategy implements BiFunction<AtomRule,String,String>{
        @Override
        public String apply(AtomRule atomRule, String data){
            return null;
        }
    }

    private static class ReplaceStrategy implements BiFunction<AtomRule,String,String>{
        @Override
        public String apply(AtomRule atomRule, String data){
            return null;
        }
    }

    private static class RegenRowStrategy implements BiFunction<AtomRule,String,String>{
        @Override
        public String apply(AtomRule atomRule, String data){
            return null;
        }
    }
}
