package legend.util.rule.intf;

import java.util.List;

public interface IReplaceRuleEngine extends IReplaceRule{
    List<String> execute(List<String> datas, String colSplitRegex);

    void refreshRule(String replaceRule);
}
