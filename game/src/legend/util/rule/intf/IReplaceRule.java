package legend.util.rule.intf;

import legend.intf.ICommon;

public interface IReplaceRule extends ICommon{
    String FLAG_COL_REPL_COMP = "-";
    String RULE_REGENROW = "REGENROW";
    String RULE_LOWER = "LOWER";
    String RULE_UPPER = "UPPER";
    String RULE_REPLACE = "REPLACE";
    String REG_RULE_LOWER = "(?i)(" + RULE_LOWER + ")(\\((.*)\\))?";
    String REG_RULE_UPPER = "(?i)(" + RULE_UPPER + ")(\\((.*)\\))?";
    String REG_RULE_REPLACE = "(?i)(" + RULE_REPLACE + ")\\((.+)" + REG_SPRT_ARG + "(.*)\\)";
    String REG_RULE_REGENROW = "(?i)(" + RULE_REGENROW + ")\\((.+)\\)";
    String REG_RULE_ATOM = "(?i)(.+)(\\((.*)\\))?";
    String REG_COL_NUM = "([1-9]\\d*)(-([1-9]\\d*))?,?+";
    String REG_COL_REPL_ATOM = PLACE_HOLDER + "([1-9]\\d*)\\.(0|[1-9]\\d*)" + PLACE_HOLDER;
    String REG_COL_REPL_COMP = PLACE_HOLDER + "([1-9]\\d*)" + FLAG_COL_REPL_COMP + "([1-9]\\d*)(\\.([1-9]\\d*))?" + PLACE_HOLDER;
    String REG_COL_REPL = REG_COL_REPL_ATOM + "|" + REG_COL_REPL_COMP;
    String ERR_RULE_FMT = "列字符串替换表达式格式错误！";
}
