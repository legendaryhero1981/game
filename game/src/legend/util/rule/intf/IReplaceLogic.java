package legend.util.rule.intf;

import legend.intf.ICommon;

public interface IReplaceLogic extends ICommon{
    String ADD_INT = "ADD-INT";
    String ADD_LONG = "ADD-LONG";
    String ADD_FLOAT = "ADD-FLOAT";
    String ADD_DOUBLE = "ADD-DOUBLE";
    String SUB_INT = "SUB-INT";
    String SUB_LONG = "SUB-LONG";
    String SUB_FLOAT = "SUB-FLOAT";
    String SUB_DOUBLE = "SUB-DOUBLE";
    String MUL_INT = "MUL-INT";
    String MUL_LONG = "MUL-LONG";
    String MUL_FLOAT = "MUL-FLOAT";
    String MUL_DOUBLE = "MUL-DOUBLE";
    String DIV_INT = "DIV-INT";
    String DIV_LONG = "DIV-LONG";
    String DIV_FLOAT = "DIV-FLOAT";
    String DIV_DOUBLE = "DIV-DOUBLE";
    String REG_ADD_INT = "(?i)(" + ADD_INT + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_NATURAL + ")\\)";
    String REG_ADD_LONG = "(?i)(" + ADD_LONG + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_NATURAL + ")\\)";
    String REG_ADD_FLOAT = "(?i)(" + ADD_FLOAT + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_REAL + ")\\)";
    String REG_ADD_DOUBLE = "(?i)(" + ADD_DOUBLE + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_REAL + ")\\)";
    String REG_SUB_INT = "(?i)(" + SUB_INT + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_NATURAL + ")\\)";
    String REG_SUB_LONG = "(?i)(" + SUB_LONG + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_NATURAL + ")\\)";
    String REG_SUB_FLOAT = "(?i)(" + SUB_FLOAT + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_REAL + ")\\)";
    String REG_SUB_DOUBLE = "(?i)(" + SUB_DOUBLE + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_REAL + ")\\)";
    String REG_MUL_INT = "(?i)(" + MUL_INT + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_NATURAL + ")\\)";
    String REG_MUL_LONG = "(?i)(" + MUL_LONG + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_NATURAL + ")\\)";
    String REG_MUL_FLOAT = "(?i)(" + MUL_FLOAT + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_REAL + ")\\)";
    String REG_MUL_DOUBLE = "(?i)(" + MUL_DOUBLE + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_REAL + ")\\)";
    String REG_DIV_INT = "(?i)(" + DIV_INT + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_NATURAL + ")\\)";
    String REG_DIV_LONG = "(?i)(" + DIV_LONG + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_NATURAL + ")\\)";
    String REG_DIV_FLOAT = "(?i)(" + DIV_FLOAT + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_REAL + ")\\)";
    String REG_DIV_DOUBLE = "(?i)(" + DIV_DOUBLE + ")\\((\\d)" + REG_SPRT_ARGS + "(" + REG_NUM_REAL + ")\\)";
    String ERR_REPL_LOGIC = "替换逻辑" + PH_ARGS0 + "执行时出错！" + N_ERR_INFO + PH_ARGS1;
    String ERR_LOGIC_ANLS = "逻辑方法" + PH_ARGS0 + "参数表达式解析错误！";
}
