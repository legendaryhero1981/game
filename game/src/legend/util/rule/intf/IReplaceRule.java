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
    String REG_RULE_ATOM = "(?i)(.+?)(\\((.*)\\))?";
    String REG_RULE_ATOM_QUOTE = "(?i)(.+?\\().+(\\))";
    String REG_COL_NUM = "([1-9]\\d*)(-([1-9]\\d*))?,?+";
    String REG_COL_REPL_ATOM = PLACE_HOLDER + "([1-9]\\d*)\\.(0|[1-9]\\d*)" + PLACE_HOLDER;
    String REG_COL_REPL_COMP = PLACE_HOLDER + "([1-9]\\d*)" + FLAG_COL_REPL_COMP + "([1-9]\\d*)\\.?([1-9]\\d*)?" + PLACE_HOLDER;
    String REG_COL_REPL = REG_COL_REPL_ATOM + "|" + REG_COL_REPL_COMP;
    String ERR_RULE_ANLS = "列字符串替换表达式解析错误！" + N_ERR_INFO + PH_ARG0;
    String ERR_RULE_FMT = "字符串替换表达式格式错误！";
    String ERR_RULE_COL_NUM = "列号表达式无效！";
    String ERR_RULE_TMNT = "字符串替换规则" + RULE_REGENROW + "只能作为最后一条原子规则！";
    String ERR_RULE_INVALID = "无效的字符串替换规则：" + PH_ARG0 + "！";
    String ERR_DATA_ANLS = "二维表数据解析错误！" + N_ERR_INFO + PH_ARG0;
    String ERR_DATA_COL_NUM = "存在列数不相同的行！";
}
