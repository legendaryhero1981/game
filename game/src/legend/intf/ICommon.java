package legend.intf;

import static java.util.regex.Matcher.quoteReplacement;
import static legend.util.ValueUtil.nonEmpty;

import java.io.File;

public interface ICommon{
    String SPRT_LINE = System.lineSeparator();
    String SPRT_FILE = File.separator;
    String SPRT_FILE_ZIP = "/";
    String AUTHOR = "作者：李允";
    String VERSION = "版本：V3.0";
    String APP_INFO = AUTHOR + gl(1) + VERSION + gl(3);
    String ENCODING_UTF8 = "UTF-8";
    String ENCODING_GBK = "GBK";
    String EXT_ZIP = ".zip";
    String EXT_PAK = ".pak";
    String S_SPACE = " ";
    String S_QUOTATION = "\"";
    String S_COMMA = "，";
    String S_SEMICOLON = "；";
    String S_PERIOD = "。";
    String S_BANG = "！";
    String S_ELLIPSIS = "…";
    String S_COLON = "：";
    String S_BRACKET_L = "（";
    String S_BRACKET_R = "）";
    String S_QUOTATION_L = "“";
    String S_QUOTATION_R = "”";
    String N_ARG = "参数";
    String N_LOG = "日志";
    String N_ERR_INFO = "错误信息：";
    String N_FLE = "文件";
    String N_FLE_NUL = "空文件";
    String N_PATH_NAME = "路径名";
    String N_DIR = "目录";
    String N_DIR_NUL = "空目录";
    String N_DIR_SUB = "子目录";
    String N_SPEC_ID = "给定ID";
    String N_SIZE = "大小";
    String N_TIME = "耗时";
    String N_CMD = "命令";
    String N_PRG = "程序";
    String N_NUM = "个数";
    String N_OTW = "否则";
    String N_STM = "流";
    String N_A = "的";
    String N_AN = "个";
    String N_AND = "和";
    String N_OR = "或";
    String N_IN = "中";
    String N_FMT = "格式";
    String V_INPUT = "输入";
    String V_OUTPUT = "输出";
    String V_IMPORT = "导入";
    String V_EXPORT = "导出";
    String V_NON_EXISTS = "不存在！";
    String V_ARD_EXISTS = "已存在！";
    String V_BY_NUL = "为空！";
    String V_FAIL = "失败！";
    String V_ERR = "错误！";
    String V_SAME = "相同";
    String V_EXISTS = "存在";
    String V_CAN_NOT = "不能";
    String V_WRITE = "写入";
    String V_SMLT = "模拟";
    String V_EXEC = "执行";
    String V_START = "开始";
    String V_DONE = "完毕";
    String V_DEAL = "处理了";
    String V_FIND = "找到";
    String V_ANLS = "解析";
    String V_VST = "访问";
    String V_DEL = "删除";
    String V_CPY = "复制";
    String V_MOV = "移动";
    String V_BAK = "备份";
    String V_UPD = "更新";
    String V_ADD = "新增";
    String V_REN = "重命名";
    String V_EXTR = "提取";
    String V_CRT = "创建";
    String V_CPRS = "压缩";
    String V_DCPRS = "解压缩";
    String V_ENC = "编码";
    String V_DEC = "解码";
    String V_CLS = "关闭";
    String V_IS = "是";
    String V_TO = " 到 ";
    String V_BY = " 为 ";
    String OPT_NONE = "";
    String OPT_INSIDE = "`";
    String OPT_DETAIL = "+";
    String OPT_SIMULATE = "*";
    String OPT_EXCLUDE_ROOT = "!";
    String OPT_CACHE = "@";
    String OPT_ASK = "?";
    String SPRT_ARG = "::";
    String REG_ANY = ".";
    String REG_ASK_NO = "\\A[nN]$";
    String REG_SPRT_ARG = SPRT_ARG + "+";
    String REG_PH_ARG = "\\A[" + OPT_ASK + "]+$";
    String REG_RPT_ARG = "\\A[" + OPT_SIMULATE + "]+(.*)";
    String REG_NON_PROG = ".*?[" + OPT_DETAIL + OPT_SIMULATE + OPT_INSIDE + "].*?";
    String REG_OPT = "(.*?)([" + OPT_INSIDE + OPT_DETAIL + OPT_SIMULATE + OPT_EXCLUDE_ROOT + OPT_CACHE + OPT_ASK + "]+)$";
    String PLACE_HOLDER = "#";
    String PH_ARG0 = PLACE_HOLDER + "0" + PLACE_HOLDER;
    String PH_ARG1 = PLACE_HOLDER + "1" + PLACE_HOLDER;
    String PH_ARG2 = PLACE_HOLDER + "2" + PLACE_HOLDER;
    String PH_ARG3 = PLACE_HOLDER + "3" + PLACE_HOLDER;
    String PH_ARG4 = PLACE_HOLDER + "4" + PLACE_HOLDER;
    String PH_ARG5 = PLACE_HOLDER + "5" + PLACE_HOLDER;
    String PH_ARG6 = PLACE_HOLDER + "6" + PLACE_HOLDER;
    String PH_ARG7 = PLACE_HOLDER + "7" + PLACE_HOLDER;
    String PH_ARG8 = PLACE_HOLDER + "8" + PLACE_HOLDER;
    String PH_ARG9 = PLACE_HOLDER + "9" + PLACE_HOLDER;
    String XML_NOTE_START = "<!--";
    String XML_NOTE_END = "-->";
    String XML_NOTE = XML_NOTE_START + PH_ARG0 + XML_NOTE_END;
    String ST_ARG_START = V_START + V_ANLS + N_CMD + N_ARG + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + S_ELLIPSIS;
    String ST_ARG_DONE = N_CMD + N_ARG + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + V_ANLS + V_DONE + S_PERIOD;
    String ST_CMD_START = V_START + V_EXEC + N_CMD + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + S_ELLIPSIS;
    String ST_CMD_DONE = N_CMD + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + V_EXEC + V_DONE + S_PERIOD;
    String ST_PRG_START = V_START + V_EXEC + N_PRG + S_ELLIPSIS;
    String ST_PRG_DONE = N_PRG + V_EXEC + V_DONE + S_PERIOD;
    String ERR_LOG_FLE_CRT = V_CRT + N_LOG + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_CMD_EXEC = N_CMD + V_EXEC + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_RES_CLS = V_CLS + N_STM + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_ARG_ANLS = V_ANLS + N_ARG + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_ARG_FMT = N_ARG + N_NUM + N_OR + N_ARG + N_FMT + V_ERR;
    String FLAG_DEBUG = "#";
    String FLAG_MOD = "*";
    String FLAG_ADD = "+";
    String REG_XML_NOTE = XML_NOTE_START + "(.*)" + XML_NOTE_END;
    String REG_RELEASE = FLAG_DEBUG + ".*?" + FLAG_DEBUG;
    String REG_DEBUG = FLAG_DEBUG + "\\d+" + FLAG_DEBUG;
    String REG_MOD = FLAG_DEBUG + "\\" + FLAG_MOD + "\\d+" + FLAG_DEBUG;
    String REG_ADD = FLAG_DEBUG + "\\" + FLAG_ADD + "(\\d+)" + FLAG_DEBUG + ".*";
    String REP_ADD = FLAG_DEBUG + "$1" + FLAG_DEBUG;

    static String gl(String s, int n){
        String r = nonEmpty(s) ? s : "";
        for(int i = 0;i < n;i++)
            r += SPRT_LINE;
        return r;
    }

    static String gl(int n){
        return gl("",n);
    }

    static String gs(String s, int n){
        String r = "";
        if(nonEmpty(s)) for(int i = 0;i < n;i++)
            r += s;
        return r;
    }

    static String gs(int n){
        return gs(S_SPACE,n);
    }

    static String gs(String[] ss, String sprt){
        String r = "";
        for(String s : ss)
            r += s + sprt;
        return r.isEmpty() ? r : r.substring(0,r.length() - sprt.length());
    }

    static String gs(String[] ss){
        return gs(ss,S_SPACE);
    }

    static String glph(String s, int n, String... ph){
        String r = gl(s,n);
        if(nonEmpty(ph)) for(int i = 0;i < ph.length;i++)
            r = r.replaceAll(gph(i),quoteReplacement(ph[i]));
        return r;
    }

    static String gsph(String s, String... ph){
        return glph(s,0,ph);
    }

    static String gph(int n){
        return PLACE_HOLDER + String.valueOf(n) + PLACE_HOLDER;
    }
}
