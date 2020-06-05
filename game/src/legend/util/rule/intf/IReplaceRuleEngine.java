package legend.util.rule.intf;

import java.util.Collection;

public interface IReplaceRuleEngine extends IReplaceRule{
    Collection<String> execute(Collection<String> datas, String colSplitRegex);

    void refreshRule(String replaceRule);
}
