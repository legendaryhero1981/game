package legend.intf;

import static java.util.regex.Pattern.compile;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.gs;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

public interface ICommon extends Cloneable{
    int BLOCK_SIZE_FILE = 1 << 20;
    int MAX_SIZE_FILE_PATH = 256;
    byte[] BOM_UTF16LE = {(byte)0xff,(byte)0xfe};
    byte[] BOM_UTF16BE = {(byte)0xfe,(byte)0xff};
    byte[] BOM_UTF8 = {(byte)0xef,(byte)0xbb,(byte)0xbf};
    char[] CHAR_HEX = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    String SPRT_LINE = System.lineSeparator();
    String SPRT_FILE = File.separator;
    String SPRT_FILE_ZIP = "/";
    String VERSION = "版本：V7.0";
    String AUTHOR = "作者：李允";
    String HOME_PAGE = "主页：知乎 https://www.zhihu.com/people/legendaryhero1981";
    String APP_INFO = VERSION + gl(1) + AUTHOR + gl(1) + HOME_PAGE + gl(3);
    String CHARSET_GBK = "GBK";
    String CHARSET_BIG5 = "BIG5";
    String CHARSET_UTF8 = "UTF-8";
    String CHARSET_UTF8_BOM = "UTF-8B";
    String CHARSET_UTF16LE = "UTF-16LE";
    String CHARSET_UTF16BE = "UTF-16BE";
    String CONTEXT_CMD_PREFIX = "cmd /c start /wait \"\" ";
    String EXT_BAT = ".bat";
    String EXT_VBS = ".vbs";
    String EXT_EXE = ".exe";
    String EXT_LNK = ".lnk";
    String EXT_IL = ".il";
    String EXT_PAK = ".pak";
    String EXT_SPK = ".spk";
    String EXT_STC = ".stc";
    String EXT_DCX = ".dcx";
    String EXT_XML = ".xml";
    String EXT_ZIP = ".zip";
    String EXT_CLASS = ".class";
    String S_EMPTY = "";
    String S_ENTER = SPRT_LINE;
    String S_SPACE = " ";
    String S_SQM = "'";
    String S_DQM = "\"";
    String S_BQ = "`";
    String S_PAUSE = "、";
    String S_COMMA = "，";
    String S_SEMICOLON = "；";
    String S_PERIOD = "。";
    String S_BANG = "！";
    String S_ELLIPSIS = "…";
    String S_COLON = "：";
    String S_BRACKET_L = "（";
    String S_BRACKET_R = "）";
    String S_DQM_L = "“";
    String S_DQM_R = "”";
    String N_ARG = "参数";
    String N_LOG = "日志";
    String N_ERR_INFO = "错误信息：";
    String N_FILE = "文件";
    String N_FILE_NUL = "空文件";
    String N_VER_NEW = "新版本";
    String N_VER_OLD = "旧版本";
    String N_FILE_MERGE = "文件整合";
    String N_PATH_NAME = "路径名";
    String N_DIR = "目录";
    String N_DIR_NUL = "空目录";
    String N_DIR_SUB = "子目录";
    String N_SPEC_ID = "给定ID";
    String N_SIZE = "大小";
    String N_TIME = "耗时";
    String N_CMD = "命令";
    String N_EXTN = "外部";
    String N_PRG = "程序";
    String N_PROC = "进程";
    String N_NUM = "个数";
    String N_OTW = "否则";
    String N_STM = "流";
    String N_A = "的";
    String N_AN = "个";
    String N_AND = "和";
    String N_OR = "或";
    String N_IN = "中";
    String N_FMT = "格式";
    String N_DEAL = "处理了";
    String N_EXEC_PATH = "可执行文件路径名";
    String V_DEAL = "处理";
    String V_INPUT = "输入";
    String V_OUTPUT = "输出";
    String V_IMPORT = "导入";
    String V_EXPORT = "导出";
    String V_NON_EXISTS = "不存在";
    String V_ARD_EXISTS = "已存在";
    String V_BY_NUL = "为空";
    String V_FAIL = "失败";
    String V_ERR = "错误";
    String V_SAME = "相同";
    String V_EXISTS = "存在";
    String V_CAN_NOT = "不能";
    String V_READ = "读取";
    String V_WRITE = "写入";
    String V_SMLT = "模拟";
    String V_EXEC = "执行";
    String V_START = "开始";
    String V_DONE = "完毕";
    String V_FIND = "找到";
    String V_ANLS = "解析";
    String V_GNRT = "生成";
    String V_VST = "访问";
    String V_DEL = "删除";
    String V_CPY = "复制";
    String V_MOV = "移动";
    String V_BAK = "备份";
    String V_UPD = "更新";
    String V_REPL = "替换";
    String V_ADD = "新增";
    String V_REN = "重命名";
    String V_EXTR = "提取";
    String V_CRT = "创建";
    String V_CPRS = "压缩";
    String V_DCPRS = "解压缩";
    String V_TSC = "转码";
    String V_ENC = "编码";
    String V_DEC = "解码";
    String V_CLS = "关闭";
    String V_IS = "是";
    String V_TO = " 到 ";
    String V_BY = " 为 ";
    String OPT_INSIDE = "~";
    String OPT_DETAIL = "+";
    String OPT_SIMULATE = "*";
    String OPT_EXCLUDE_ROOT = "!";
    String OPT_DIFF = "^";
    String OPT_CACHE = "@";
    String OPT_ASK = "?";
    String PH_ARGS = "#";
    String PH_ARGS0 = PH_ARGS + "0" + PH_ARGS;
    String PH_ARGS1 = PH_ARGS + "1" + PH_ARGS;
    String PH_ARGS2 = PH_ARGS + "2" + PH_ARGS;
    String PH_ARGS3 = PH_ARGS + "3" + PH_ARGS;
    String PH_ARGS4 = PH_ARGS + "4" + PH_ARGS;
    String PH_ARGS5 = PH_ARGS + "5" + PH_ARGS;
    String PH_ARGS6 = PH_ARGS + "6" + PH_ARGS;
    String PH_ARGS7 = PH_ARGS + "7" + PH_ARGS;
    String PH_ARGS8 = PH_ARGS + "8" + PH_ARGS;
    String PH_ARGS9 = PH_ARGS + "9" + PH_ARGS;
    String PH_CG = "$";
    String PH_CG0 = PH_CG + "0";
    String PH_CG1 = PH_CG + "1";
    String PH_CG2 = PH_CG + "2";
    String PH_CG3 = PH_CG + "3";
    String PH_CG4 = PH_CG + "4";
    String PH_CG5 = PH_CG + "5";
    String PH_CG6 = PH_CG + "6";
    String PH_CG7 = PH_CG + "7";
    String PH_CG8 = PH_CG + "8";
    String PH_CG9 = PH_CG + "9";
    String FLAG_DEBUG = "#";
    String FLAG_MOD = "*";
    String FLAG_ADD = "+";
    String SPRT_CMDS = "::";
    String SPRT_FIELDS = "@@";
    String SPRT_RULES = ";;";
    String SPRT_ATOMS = "=>";
    String SPRT_ARGS = ",,";
    String SPRT_ARGS_FUNC = ",";
    String SPC_NUL = new String(new byte[]{0});
    String SPC_SQM = "SQM";
    String SPC_DQM = "DQM";
    String SPC_BQ = "BQ";
    String SPC_ENTER = "ENTER";
    String SPC_EMPTY = "EMPTY";
    String EXEC_KDIFF_F2 = "\"" + PH_ARGS0 + "\" \"" + PH_ARGS1 + "\" \"" + PH_ARGS2 + "\" -o \"" + PH_ARGS3 + "\"";
    String EXEC_KDIFF_F3 = "\"" + PH_ARGS0 + "\" \"" + PH_ARGS1 + "\" \"" + PH_ARGS2 + "\" \"" + PH_ARGS3 + "\" -o \"" + PH_ARGS4 + "\"";
    String XML_NOTE_START = "<!--";
    String XML_NOTE_END = "-->";
    String XML_NOTE = XML_NOTE_START + PH_ARGS0 + XML_NOTE_END;
    String XML_CDATA_START = "<![CDATA[";
    String XML_CDATA_END = "]]>";
    String XML_CDATA = XML_CDATA_START + PH_ARGS0 + XML_CDATA_END;
    String REP_ADD = FLAG_DEBUG + "$1" + FLAG_DEBUG;
    String REG_WHOLE_MATCH = "\\A" + PH_ARGS0 + "$";
    String REG_WHOLE_MATCH_IC = "(?i)" + REG_WHOLE_MATCH;
    String REG_UC_MC_GBK = "(?:[\\u3000\\ue766-\\ue76b\\ue76d-\\ue76f\\ue770-\\ue78c\\ue797-\\ue79f\\ue7a0-\\ue7bb\\ue7c9-\\ue7e1\\ue7fe-\\ue7ff\\ue800-\\ue814])";
    String REG_UC_MC_BIG5 = "(?:[\\u3000\\u007f\\ufffd])";
    String REG_UC_NON_CHS = "(?:[^\\u2e80-\\uffef])";
    String REG_SPC_SQM = "(?i)" + PH_ARGS + SPC_SQM + "=?([1-9]?)" + PH_ARGS;
    String REG_SPC_DQM = "(?i)" + PH_ARGS + SPC_DQM + "=?([1-9]?)" + PH_ARGS;
    String REG_SPC_BQ = "(?i)" + PH_ARGS + SPC_BQ + "=?([1-9]?)" + PH_ARGS;
    String REG_SPC_ENTER = "(?i)" + PH_ARGS + SPC_ENTER + "=?([1-9]?)" + PH_ARGS;
    String REG_SPC_EMPTY = "(?i)" + PH_ARGS + SPC_EMPTY + PH_ARGS;
    String REG_QUOTE_DMQ = S_DQM + "(.*?)" + S_DQM;
    String REG_QUOTE_BQ = S_BQ + "(.*?)" + S_BQ;
    String REG_SPRT_CMDS = SPRT_CMDS + "+";
    String REG_SPRT_FIELDS = SPRT_FIELDS + "+";
    String REG_SPRT_RULES = SPRT_RULES + "+";
    String REG_SPRT_ARGS = SPRT_ARGS + "+";
    String REG_SPRT_CODES = "(?m)\n+";
    String REG_SPRT_COLS = "[ \\t]+";
    String REG_SPRT_PATHS = "[/" + gs(SPRT_FILE,2) + "]";
    String REG_PATH_NAME = "(.*" + REG_SPRT_PATHS + ")(.*)";
    String REG_FILE_NAME = "(.*)(\\..*)";
    String REG_NUM = "\\d+";
    String REG_NUM_NATURAL = "[1-9]\\d*";
    String REG_NUM_ROUND = "-?(0|" + REG_NUM_NATURAL + ")";
    String REG_NUM_REAL = "-?([.]?" + REG_NUM + "|" + REG_NUM + "[.]\\d*)";
    String REG_ANY = ".";
    String REG_BLANK = "\\s+";
    String REG_NON_BLANK = "\\S+";
    String REG_XML_NOTE = XML_NOTE_START + "(.*)" + XML_NOTE_END;
    String REG_XML_CDATA = XML_CDATA_START + "(.*)" + XML_CDATA_END;
    String REG_LOCAL_RELEASE = FLAG_DEBUG + ".*?" + FLAG_DEBUG;
    String REG_LOCAL_DEBUG = FLAG_DEBUG + REG_NUM + FLAG_DEBUG;
    String REG_LOCAL_MOD = FLAG_DEBUG + "\\" + FLAG_MOD + REG_NUM + FLAG_DEBUG;
    String REG_LOCAL_ADD = FLAG_DEBUG + "\\" + FLAG_ADD + "(" + REG_NUM + ")" + FLAG_DEBUG + ".*";
    String ST_ARG_START = V_START + V_ANLS + N_CMD + N_ARG + S_DQM_L + PH_ARGS0 + S_DQM_R + S_ELLIPSIS;
    String ST_ARG_DONE = N_CMD + N_ARG + S_DQM_L + PH_ARGS0 + S_DQM_R + V_ANLS + V_DONE + S_PERIOD;
    String ST_CMD_START = V_START + V_EXEC + N_CMD + S_DQM_L + PH_ARGS0 + S_DQM_R + S_ELLIPSIS;
    String ST_CMD_DONE = N_CMD + S_DQM_L + PH_ARGS0 + S_DQM_R + V_EXEC + V_DONE + S_PERIOD;
    String ST_PRG_START = V_START + V_EXEC + N_PRG + S_ELLIPSIS;
    String ST_PRG_DONE = N_PRG + V_EXEC + V_DONE + S_PERIOD;
    String ST_PRG_EXTN_START = V_START + V_EXEC + N_EXTN + N_PRG + S_DQM_L + PH_ARGS0 + S_DQM_R + S_ELLIPSIS;
    String ST_PRG_EXTN_DONE = N_EXTN + N_PRG + S_DQM_L + PH_ARGS0 + S_DQM_R + V_EXEC + V_DONE + S_PERIOD;
    String ERR_INFO = N_ERR_INFO + PH_ARGS0;
    String ERR_EXEC_CMD = V_EXEC + N_CMD + V_FAIL + S_BANG + ERR_INFO;
    String ERR_EXEC_CMD_SPEC = V_EXEC + N_CMD + S_DQM_L + PH_ARGS0 + S_DQM_R + V_FAIL + S_BANG + N_ERR_INFO + PH_ARGS1;
    String ERR_EXEC_FILE_MERGE = V_EXEC + N_FILE_MERGE + N_CMD + V_FAIL + S_BANG + ERR_INFO;
    String ERR_KDIFF3_EXEC_NON = "文件整合工具KDiff3" + N_A + N_EXEC_PATH + V_NON_EXISTS + S_BANG;
    String ERR_RES_CLS = V_CLS + N_STM + V_FAIL + S_BANG + ERR_INFO;
    String ERR_ARG_ANLS = V_ANLS + N_ARG + V_FAIL + S_BANG + ERR_INFO;
    String ERR_ARG_FMT = N_ARG + N_NUM + N_OR + N_ARG + N_FMT + V_ERR + S_BANG;
    String ERR_LOG_FLE_CRT = V_CRT + N_LOG + N_FILE + S_SPACE + PH_ARGS0 + S_SPACE + V_FAIL + S_BANG + N_ERR_INFO + PH_ARGS1;
    Pattern PTRN_SPC_NUL = compile(SPC_NUL);
    Pattern PTRN_ANY = compile(REG_ANY);
    Pattern PTRN_NON_BLANK = compile(REG_NON_BLANK);
    Pattern PTRN_QUOTE_DMQ = compile(REG_QUOTE_DMQ);
    Pattern PTRN_QUOTE_BQ = compile(REG_QUOTE_BQ);
    Pattern PTRN_NUM = compile(REG_NUM);
    Pattern PTRN_NUM_NATURAL = compile(REG_NUM_NATURAL);
    Pattern PTRN_NUM_ROUND = compile(REG_NUM_ROUND);
    Pattern PTRN_NUM_REAL = compile(REG_NUM_REAL);
    Pattern PTRN_PATH_NAME = compile(REG_PATH_NAME);
    Pattern PTRN_FILE_NAME = compile(REG_FILE_NAME);
    Pattern PTRN_SPRT_CODE = compile(REG_SPRT_CODES);
    Pattern PTRN_UC_NON_CHS = compile(REG_UC_NON_CHS);
    Pattern PTRN_XML_CDATA = compile(REG_XML_CDATA);
    Pattern PTRN_LOCAL_DEBUG = compile(REG_LOCAL_DEBUG);
    Pattern PTRN_LOCAL_RELEASE = compile(REG_LOCAL_RELEASE);
    Pattern PTRN_LOCAL_MOD = compile(REG_LOCAL_MOD);
    Pattern PTRN_LOCAL_ADD = compile(REG_LOCAL_ADD);
    Map<String,String> SPH_MAP = ofEntries(entry(REG_SPC_SQM,S_SQM),entry(REG_SPC_DQM,S_DQM),entry(REG_SPC_BQ,S_BQ));
}
