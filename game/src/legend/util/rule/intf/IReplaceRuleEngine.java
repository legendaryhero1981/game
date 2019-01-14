package legend.util.rule.intf;

import java.util.List;

public interface IReplaceRuleEngine extends IReplaceRule{
    public List<String> execute();

    public void refreshEngine(String replaceRule, List<String> datas);

    public void refreshEngine(String replaceRule, List<String> datas, String colSplitRegex);

    public void refreshRule(String replaceRule);

    public void refreshData(List<String> datas);

    public void refreshData(List<String> datas, String colSplitRegex);
}
