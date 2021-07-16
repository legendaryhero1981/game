package legend.util.rule.intf;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.regex.Pattern.compile;

import java.util.Map;
import java.util.regex.Pattern;

public interface IReplaceRule extends IReplaceLogic{
    long ATOM_RULE = 0l;
    long TMNT_RULE = 1l;
    long MULTI_LINE = 1l << 1;
    String FLAG_COL_REPL_COMP = "-";
    String RULE_LOWER = "LOWER";
    String RULE_UPPER = "UPPER";
    String RULE_REPLACE = "REPLACE";
    String RULE_SINGLEROW = "SINGLE-ROW";
    String RULE_FINALSINGLEROW = "FINAL-SINGLE-ROW";
    String RULE_DISTFINALSINGLEROW = "DIST-FINAL-SINGLE-ROW";
    String RULE_MULTIROW = "MULTI-ROW";
    String RULE_FINALMULTIROW = "FINAL-MULTI-ROW";
    String RULE_DISTFINALMULTIROW = "DIST-FINAL-MULTI-ROW";
    String REG_RULE_LOWER = "(?i)(" + RULE_LOWER + ")(\\((.*)\\))?";
    String REG_RULE_UPPER = "(?i)(" + RULE_UPPER + ")(\\((.*)\\))?";
    String REG_RULE_REPLACE = "(?i)(" + RULE_REPLACE + ")\\((.+)\\)";
    String REG_RULE_SINGLEROW = "(?i)(" + RULE_SINGLEROW + ")\\((.+)\\)";
    String REG_RULE_FINALSINGLEROW = "(?i)(" + RULE_FINALSINGLEROW + ")\\((.+)\\)";
    String REG_RULE_ATOM = "(?i)(.+?)(?:\\((.*)\\))?";
    String REG_RULE_ATOM_QUOTE = "(?i)(.+?\\().+(\\))";
    String REG_COL_NUM = "(" + REG_NUM_NATURAL + ")(?:-(" + REG_NUM_NATURAL + "))?,?";
    String REG_COL_REPL_ATOM = PH_ARGS + "(" + REG_NUM_NATURAL + ")\\.(0|" + REG_NUM_NATURAL + ")" + PH_ARGS;
    String REG_COL_REPL_COMP = PH_ARGS + "(" + REG_NUM_NATURAL + ")" + FLAG_COL_REPL_COMP + "(" + REG_NUM_NATURAL + ")(?:\\.(" + REG_NUM_NATURAL + "))?" + PH_ARGS;
    String REG_COL_REPL = REG_COL_REPL_ATOM + "|" + REG_COL_REPL_COMP;
    String ERR_RULE_ANLS = "列字符串替换表达式解析错误！" + ERR_INFO;
    String ERR_RULE_FMT = "字符串替换表达式格式错误！";
    String ERR_RULE_COL_NUM = "列号表达式无效！";
    String ERR_RULE_TMNT = "字符串替换规则" + PH_ARGS0 + "只能作为最后一条原子规则！";
    String ERR_RULE_INVALID = "无效的字符串替换规则：" + PH_ARGS0 + "！";
    String ERR_DATA_ANLS = "二维表数据解析错误！" + ERR_INFO;
    String ERR_DATA_COL_NUM = "存在列数不相同的行！";
    Pattern PTRN_RULE_ATOM = compile(REG_RULE_ATOM);
    Pattern PTRN_RULE_ATOM_QUOTE = compile(REG_RULE_ATOM_QUOTE);
    Pattern PTRN_COL_NUM = compile(REG_COL_NUM);
    Pattern PTRN_COL_REPL = compile(REG_COL_REPL);
    Map<String,Long> RULE_CON_MAP = ofEntries(entry(RULE_LOWER,ATOM_RULE),entry(RULE_UPPER,ATOM_RULE),entry(RULE_REPLACE,ATOM_RULE),entry(RULE_SINGLEROW,TMNT_RULE),entry(RULE_FINALSINGLEROW,TMNT_RULE),entry(RULE_DISTFINALSINGLEROW,TMNT_RULE),entry(RULE_MULTIROW,TMNT_RULE | MULTI_LINE),entry(RULE_FINALMULTIROW,TMNT_RULE | MULTI_LINE),entry(RULE_DISTFINALMULTIROW,TMNT_RULE | MULTI_LINE));
    Map<Long,Map<String,String>> RULE_REG_MAP = ofEntries(entry(ATOM_RULE,ofEntries(entry(REG_SPC_EMPTY,S_EMPTY))),entry(TMNT_RULE,ofEntries(entry(REG_SPC_EMPTY,S_EMPTY))),entry(TMNT_RULE | MULTI_LINE,ofEntries(entry(REG_SPC_EMPTY,S_EMPTY),entry(REG_SPC_ENTER,S_ENTER))));
}
