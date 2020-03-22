package legend.util.intf;

import legend.intf.ICommon;

public interface IJsonUtil extends ICommon{
    char C_ESCAPE = '\\';
    char C_QUOT_D = '"';
    char C_COMMA = ',';
    char C_BRACE_L = '{';
    char C_BRACE_R = '}';
    char C_SQUARE_L = '[';
    char C_SQUARE_R = ']';
    String N_JSON = "JSON字符串";
    String ERR_JSON_PARSE = V_ANLS + N_JSON + V_FAIL + S_BANG + N_ERR_INFO + PH_ARGS0;
    String ERR_JSON_FMT = V_ANLS + N_JSON + V_FAIL + S_BANG + N_ERR_INFO + N_JSON + N_FMT + V_ERR + S_BANG;
}
