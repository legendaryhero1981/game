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
    String MOD_INT = "MOD-INT";
    String MOD_LONG = "MOD-LONG";
    String MOD_FLOAT = "MOD-FLOAT";
    String MOD_DOUBLE = "MOD-DOUBLE";
    String POW_INT = "POW-INT";
    String POW_LONG = "POW-LONG";
    String POW_FLOAT = "POW-FLOAT";
    String POW_DOUBLE = "POW-DOUBLE";
    String REG_LOGIC_FUNC = "(?i)" + PH_ARGS + "(.+?)\\((\\d)" + SPRT_ARGS_FUNC + "(.+?)\\)" + PH_ARGS;
    String ERR_LOGIC_FUNC = "执行逻辑方法" + PH_ARGS0 + "时出错！" + N_ERR_INFO + PH_ARGS1;
    String ERR_LOGIC_INVALID = "无效的逻辑方法！";
    String ERR_LOGIC_GROUP = "参数表达式中捕获组编号大于查询表达式中指定的捕获组个数！";
    String ERR_LOGIC_NUM = "整型参数格式错误！";
    String ERR_LOGIC_REAL = "浮点型参数格式错误！";
}
