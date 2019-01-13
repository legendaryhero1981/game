package legend.util.engine.intf;

import java.util.List;

public interface IReplaceRuleEngine{
    public List<String> execute();

    public void refreshEngine(String replaceRule, List<String> rows, String... colSplitRegex);

    public void refreshRule(String replaceRule);

    public void refreshData(List<String> rows, String... colSplitRegex);
}
