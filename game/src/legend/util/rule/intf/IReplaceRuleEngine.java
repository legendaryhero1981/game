package legend.util.rule.intf;

import java.util.List;

public interface IReplaceRuleEngine extends IReplaceRule{
    public List<String> execute(List<String> datas, String colSplitRegex);

    public void refreshRule(String replaceRule);
}
