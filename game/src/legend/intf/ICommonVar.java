package legend.intf;

import static java.util.regex.Matcher.quoteReplacement;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.io.File;
import java.util.Optional;

import legend.util.param.SingleValue;

public interface ICommonVar{
    // common
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
    static String gs(String[] ss,String sprt){
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
    static String esc(String s,String... args){
        if(isEmpty(s)) return "";
        if(isEmpty(args)) return s;
        SingleValue<String> value = new SingleValue<>(s);
        Optional<SingleValue<String>> opt = Optional.of(value);
        for(String arg : args)
            switch(arg){
                case XML_AND:
                opt.filter(v->v.get().contains(XML_AND)).ifPresent(v->v.set(v.get().replaceAll(XML_AND,ESCAPE_AND)));
                break;
                case XML_QUOTE_S:
                opt.filter(v->v.get().contains(XML_QUOTE_S)).ifPresent(v->v.set(v.get().replaceAll(XML_QUOTE_S,ESCAPE_QUOTE_S)));
                break;
                case XML_QUOTE_D:
                opt.filter(v->v.get().contains(XML_QUOTE_D)).ifPresent(v->v.set(v.get().replaceAll(XML_QUOTE_D,ESCAPE_QUOTE_D)));
                break;
                case XML_CUSP_L:
                opt.filter(v->v.get().contains(XML_CUSP_L)).ifPresent(v->v.set(v.get().replaceAll(XML_CUSP_L,ESCAPE_CUSP_L)));
                break;
                case XML_CUSP_R:
                opt.filter(v->v.get().contains(XML_CUSP_R)).ifPresent(v->v.set(v.get().replaceAll(XML_CUSP_R,ESCAPE_CUSP_R)));
            }
        return value.get();
    }
    // legend.Main
    String SPRT_LINE = System.lineSeparator();
    String SPRT_FILE = File.separator;
    String SPRT_FILE_ZIP = "/";
    String AUTHOR = "作者：李允" + gl(1);
    String VERSION = "版本：V2.0" + gl(3);
    String APP_INFO = AUTHOR + VERSION;
    String MAIN_FILE = "file";
    String MAIN_RUN = "run";
    String MAIN_EOC = "eoc";
    String MAIN_KCD = "kcd";
    String MAIN_POE = "poe";
    String HELP_MAIN = APP_INFO + "参数说明：" + gl(2)
    + "game file|run|eoc|kcd|poe" + gl(2)
    + "命令列表：" + gl(2)
    + "file\t游戏文件处理命令，通过正则匹配批量查询和处理目录和文件。" + gl(2)
    + "run\t参数化运行游戏，通过配置文件对所有游戏可执行文件进行统一管理。" + gl(2)
    + "eoc\t神界：原罪2 汉化文件处理。" + gl(2)
    + "kcd\t天国：拯救 汉化文件和Mod整合处理。" + gl(2)
    + "poe\t永恒之柱2：死火 汉化文件和Mod文件处理。";
    // legend.util.TimeUtil
    int UNIT_MINUTE = 60;
    int UNIT_SECOND = 60;
    int UNIT_MILLI = 1000;
    // legend.util.FileUtil
    enum UNIT_TYPE{
        NON,B,KB,MB,GB,TB
    }
    int UNIT_SIZE = 0x400;
    int RECURSION_LEVEL = 100;
    int PROGRESS_POSITION = 50;
    float PROGRESS_SCALE = (100 - PROGRESS_POSITION) / 100f;
    String RES_LEVEL = "100";
    String SIZE_TB = "TB";
    String SIZE_GB = "GB";
    String SIZE_MB = "MB";
    String SIZE_KB = "KB";
    String SIZE_B = "B";
    String CMD_FIND = "-f";
    String CMD_FND_DIR = "-fd";
    String CMD_FND_DIR_OLY = "-fdo";
    String CMD_FND_PTH_ABS = "-fpa";
    String CMD_FND_PTH_RLT = "-fpr";
    String CMD_FND_PTH_SRC = "-fps";
    String CMD_FND_PTH_DIR_ABS = "-fpda";
    String CMD_FND_PTH_DIR_RLT = "-fpdr";
    String CMD_FND_PTH_DIR_SRC = "-fpds";
    String CMD_FND_PTH_DIR_OLY_ABS = "-fpdoa";
    String CMD_FND_PTH_DIR_OLY_RLT = "-fpdor";
    String CMD_FND_PTH_DIR_OLY_SRC= "-fpdos";
    String CMD_FND_SIZ = "-fs";
    String CMD_FND_SIZ_ASC = "-fsa";
    String CMD_FND_SIZ_DSC = "-fsd";
    String CMD_FND_DIR_SIZ = "-fds";
    String CMD_FND_DIR_SIZ_ASC = "-fdsa";
    String CMD_FND_DIR_SIZ_DSC = "-fdsd";
    String CMD_FND_DIR_OLY_SIZ_ASC = "-fdosa";
    String CMD_FND_DIR_OLY_SIZ_DSC = "-fdosd";
    String CMD_FND_DIR_DIR_SIZ_ASC = "-fddsa";
    String CMD_FND_DIR_DIR_SIZ_DSC = "-fddsd";
    String CMD_RENAME = "-r";
    String CMD_REN_DIR = "-rd";
    String CMD_REN_UP = "-ru";
    String CMD_REN_DIR_UP = "-rdu";
    String CMD_REN_LOW = "-rl";
    String CMD_REN_DIR_LOW = "-rdl";
    String CMD_REN_UP_FST = "-ruf";
    String CMD_REN_DIR_UP_FST = "-rduf";
    String CMD_COPY = "-c";
    String CMD_CPY_DIR = "-cd";
    String CMD_DELETE = "-d";
    String CMD_DEL_DIR = "-dd";
    String CMD_DEL_DIR_NUL = "-ddn";
    String CMD_MOVE = "-m";
    String CMD_MOV_DIR = "-md";
    String CMD_BACKUP = "-b";
    String CMD_BAK_DIR = "-bd";
    String CMD_BAK_UGD = "-bu";
    String CMD_BAK_RST = "-br";
    String CMD_UPGRADE = "-u";
    String CMD_UGD_DIR = "-ud";
    String CMD_ZIP_DEF = "-zd";
    String CMD_ZIP_DIR_DEF = "-zdd";
    String CMD_ZIP_INF = "-zi";
    String CMD_PAK_DEF = "-pd";
    String CMD_PAK_DIR_DEF = "-pdd";
    String CMD_PAK_INF = "-pi";
    String CMD_OPT_NONE = "";
    String OPT_INSIDE = "`";
    String OPT_DETAIL = "+";
    String OPT_SIMULATE = "*";
    String OPT_ASK = "?";
    String OPT_CACHE = "@";
    String EXT_ZIP = ".zip";
    String EXT_PAK = ".pak";
    String SPRT_ARG = "::";
    String FILE_LOG = "./file.log";
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
    String REG_NON_PROG = ".*?[" + OPT_DETAIL + OPT_SIMULATE  + OPT_INSIDE + "]+$";
    String REG_OPT = "(.*?)([" + OPT_DETAIL + OPT_SIMULATE  + OPT_ASK  + OPT_CACHE + OPT_INSIDE + "]+)$";
    String REG_ANY = ".";  
    String REG_ASK_NO = "\\A[nN]$";
    String REG_PH_ARG = "\\A[" + OPT_ASK + "]+$";
    String REG_RPT_ARG = "\\A[" + OPT_SIMULATE + "]+(.*)";
    String REG_SPRT_ARG = SPRT_ARG + "+";
    String REG_SPRT_FILE = "\\\\";
    String REG_FLE_SIZ = "(0|[1-9]\\d*)([TGMKtgmk]?[Bb])?[,;-]?+";
    String REG_REN_UP_FST = "[a-zA-Z]\\w*";
    String S_SPACE = " ";
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
    String N_DIR = "目录";
    String N_DIR_NUL = "空目录";
    String N_SPEC_ID = "给定ID";
    String N_SIZE = "大小";
    String N_TIME = "耗时";
    String N_CMD = "命令";
    String N_PRG = "程序";
    String N_NUM = "个数";
    String N_OTW = "否则";
    String N_STM = "流";
    String N_OR = "或";
    String N_AN = "个";
    String N_IN = "中";
    String N_FMT = "格式";
    String V_NON_EXISTS = "不存在！";
    String V_ALREADY_EXISTS = "已存在！";
    String V_BY_NUL = "为空！";
    String V_FAIL = "失败！";
    String V_ERR = "错误！";
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
    String V_CLS = "关闭";
    String V_TO = " 到 ";
    String V_BY = " 为 ";
    String ST_ASK_CONT = "输入n或N跳过，否则继续，按回车键确认：";
    String ST_ARG_START = V_START + V_ANLS + N_CMD + N_ARG + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + S_ELLIPSIS;
    String ST_ARG_DONE = N_CMD + N_ARG + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + V_ANLS + V_DONE + S_PERIOD;
    String ST_ARG_ERR = N_ARG + N_NUM + N_OR + N_ARG + N_FMT + V_ERR;
    String ST_CMD_START = V_START + V_EXEC + N_CMD + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + S_ELLIPSIS;
    String ST_CMD_DONE = N_CMD + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + V_EXEC + V_DONE + S_PERIOD;
    String ST_PRG_START = V_START + V_EXEC + N_PRG + S_ELLIPSIS;
    String ST_PRG_DONE = N_PRG + V_EXEC + V_DONE + S_PERIOD;
    String ERR_CMD_EXEC = N_CMD + V_EXEC + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_RES_CLS = V_CLS + N_STM + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_ARG_ANLS = V_ANLS + N_ARG + V_FAIL + N_ERR_INFO +  PH_ARG0;
    String ERR_DIR_VST = V_VST + N_DIR + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_VST = V_VST + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_DEL = V_DEL + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_CPY = V_CPY + N_FLE + S_SPACE + PH_ARG0 + V_TO + PH_ARG1 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG2;
    String ERR_FLE_MOV = V_MOV + N_FLE + S_SPACE + PH_ARG0 + V_TO + PH_ARG1 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG2;
    String ERR_LOG_FLE_CRT = V_CRT + N_LOG + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_EXTR = V_EXTR + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_DCPRS = V_DCPRS + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_CRT = V_CRT + V_CPRS + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_WRT = V_WRITE + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_CPY = V_CPRS + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_NUL_CPY = V_CPRS + N_FLE_NUL + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_DIR_NUL_CPY = V_CPRS + N_DIR_NUL + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String HELP_FILE = APP_INFO + "参数说明：" + gl(2)
    + "file -f[+*@?]|-fd[+*@?]|-fdo[+*@?]|-fpa[+*@?]|-fpr[+*@?]|-fps[+*@?]|-fpda[+*@?]|-fpdr[+*@?]|-fpds[+*@?]|-fpdoa[+*@?]|-fpdor[+*@?]|-fpdos[+*@?]|-fs[+*@?]|-fsa[+*@?]|-fsd[+*@?]|-fds[+*@?]|-fdsa[+*@?]|-fdsd[+*@?]|-fdosa[+*@?]|-fdosd[+*@?]|-fddsa[+*@?]|-fddsd[+*@?]|-r[+*@?]|-rl[+*@?]|-ru[+*@?]|-ruf[+*@?]|-rd[+*@?]|-rdl[+*@?]|-rdu[+*@?]|-rduf[+*@?]|-c[+*@?]|-cd[+*@?]|-d[+*@?]|-dd[+*@?]|-ddn[+*@?]|-m[+*@?]|-md[+*@?]|-b[+*@?]|-bd[+*@?]|-bu[+*@?]|-br[+*@?]|-u[+*@?]|-ud[+*@?]|-zd[+*@?]|-zdd[+*@?]|-zi[+*@?]|-pd[+*@?]|-pdd[+*@?]|-pi[+*@?] regex src [dest] [backup] [sizeExpr] [replacement] [limit] [zipName] [zipLevel] [level]" + gl(2)
    + "命令参数：" + gl(2)
    + "regex\t\t文件名查询正则表达式，.匹配任意文件名和目录名。" + gl(2)
    + "sizeExpr\t\t文件大小表达式，匹配的正则表达式为：" + REG_FLE_SIZ + "；取值范围为：0~9223372036854775807B，指定0或不指定则取默认值9223372036854775807B；例如：100B（不小于100字节），10KB（不小于10千字节），1-100MB（介于1兆字节到100兆字节之间），500MB;1GB（介于500兆字节到1千兆字节之间），2,1GB（介于2千兆字节到1千兆字节之间），1024,1024（等于1024字节）。" + gl(2)
    + "replacement\t文件名替换正则表达式。" + gl(2)
    + "src\t\t输入文件目录。" + gl(2)
    + "dest\t\t输出文件目录。" + gl(2)
    + "backup\t\t备份文件目录。" + gl(2)
    + "limit\t\t查询类命令（即命令选项以-f开头的命令）的查询结果显示数量限制，即显示前limit条记录；取值范围为：0~2147483647，指定0或不指定则取默认值2147483647。" + gl(2)
    + "zipName\t\t压缩文件名（程序会根据命令选项自动添加文件扩展名" + EXT_ZIP + "或" + EXT_PAK + "）。" + gl(2)
    + "zipLevel\t\t文件压缩级别，取值-1：程序智能选择最佳压缩率；0：不压缩，1~9：1为最低压缩率，9为最高压缩率；不指定则取默认值-1。" + gl(2)
    + "level\t\t文件目录最大查询层数，不指定则取默认值100层。" + gl(2)
    + "命令选项：" + gl(2)
    + "+ 可添加在命令选项末尾，表示输出详细信息；可与@或?连用；例如：-f+@?。" + gl(2)
    + "* 可添加在命令选项末尾，表示模拟执行命令，不进行实际操作，仅输出详细信息；可与@或?连用；例如：-f*?@。" + gl(2)
    + "@ 可添加在命令选项末尾，表示缓存该命令的查询结果，供后面的命令复用；某些命令不能缓存或复用查询结果，程序将智能忽略掉；复用查询结果的命令将忽略与查询相关的命令参数regex和src；当后面某个命令使用了@时，则重新缓存查询结果；可与+或*或?连用；例如：-f@*?或-f@+?。" + gl(2)
    + "? 可添加在命令选项末尾，表示命令开始执行前启用询问模式（" + ST_ASK_CONT + "）；可与+或*或@连用；例如：-f+@?或-f@*?。" + gl(2)
    + "组合命令：" + gl(2)
    + "可以组合多个命令选项和命令参数，一次连续执行多条命令；命令选项与各命令参数的个数必须相等；各命令选项及各命令参数使用" + SPRT_ARG + "分隔；可使用" + OPT_SIMULATE  + "复用最近一个明确的命令选项或命令参数，将其当作该命令的前缀使用，例如：-f::*d::*ds等价于-f::-fd::-fds，g:/games::*/1::*/2等价于g:/games::g:/games/1::g:/games/2；单条命令未用到的命令参数使用" + OPT_ASK + "占位。" + gl(2)
    + "组合命令示例：" + gl(2)
    + "file -cd*@::*::* .::*::* g:/games::*::* d:/::e:/::f:/" + gl(2)
    + "file -zdd+::-c+@?::* .::\\.zip$::* g:/games::g:/file::* g:/file::*/1::*/2 games::?::? 0::?::? ?::1::*" + gl(2)
    + "file -zi*::-cd@*::* \\.zip$::.::* g:/file::g:/games::* g:/::e:/::f:/ 1::?::?" + gl(2)
    + "单条命令：" + gl(2)
    + "file -f[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件。" + gl(2)
    + "file -fd[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件和子目录及其中所有文件，相对-f增加了目录名匹配，若目录名匹配，则该目录中所有文件和子目录都自动被匹配。" + gl(2)
    + "file -fdo[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的目录。" + gl(2)
    + "file -fpa[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件的绝对路径名。" + gl(2)
    + "file -fpr[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件的相对路径名（不包含src目录名称）。" + gl(2)
    + "file -fps[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件的相对路径名（包含src目录名称）。" + gl(2)
    + "file -fpda[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件和子目录及其中所有文件（同-fd），显示文件或子目录的绝对路径名。" + gl(2)
    + "file -fpdr[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件和子目录及其中所有文件（同-fd），显示文件或子目录的相对路径名（不包含src目录名称）。" + gl(2)
    + "file -fpds[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件和子目录及其中所有文件（同-fd），显示文件或子目录的相对路径名（包含src目录名称）。" + gl(2)
    + "file -fpdoa[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的子目录（同-fdo），显示子目录的绝对路径名。" + gl(2)
    + "file -fpdor[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的子目录（同-fdo），显示子目录的相对路径名（不包含src目录名称）。" + gl(2)
    + "file -fpdos[+*@?] regex src [limit] [level]" + gl(1) + "根据regex查找src中的子目录（同-fdo），显示子目录的相对路径名（包含src目录名称）。" + gl(2)
    + "file -fs[+*@?] regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件。" + gl(2)
    + "file -fsa[+*@?] regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件，按文件大小递增排序。" + gl(2)
    + "file -fsd[+*@?] regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件，按文件大小递减排序。" + gl(2)
    + "file -fds[+*@?] regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件和子目录；若匹配到目录，则其中所有文件只需匹配sizeExpr；也适用于-fdsa和-fdsd。" + gl(2)
    + "file -fdsa[+*@?] regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件和子目录，按文件大小递增排序。" + gl(2)
    + "file -fdsd[+*@?] regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件和子目录，按文件大小递减排序。" + gl(2)
    + "file -fdosa[+*@?] regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的第一级子目录，按子目录大小递增排序。" + gl(2)
    + "file -fdosd[+*@?] regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的第一级子目录，按子目录大小递减排序。" + gl(2)
    + "file -fddsa[+*@?] regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件和第一级子目录，按子文件或目录大小递增排序。" + gl(2)
    + "file -fddsd[+*@?] regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件和第一级子目录，按子文件或目录大小递减排序。" + gl(2)
    + "file -d[+*@?] regex src [level]" + gl(1) + "根据regex删除src中所有匹配文件。" + gl(2)
    + "file -dd[+*@?] regex src [level]" + gl(1) + "根据regex删除src中所有匹配文件和子目录及其中所有文件。" + gl(2)
    + "file -ddn[+*@?] regex src [level]" + gl(1) + "根据regex删除src中所有匹配的空文件和空目录。" + gl(2)
    + "file -r[+*@?] regex src replacement [level]" + gl(1) + "根据regex和replacement重命名src中的文件。" + gl(2)
    + "file -rl[+*@?] regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名中英文字母替换为小写；regex可最多指定9个捕获组，最左边为第1个捕获组，程序只会替换捕获组中的子串，如：(.*\\.)txt$ 表示只替换文件名，不会替换扩展名txt；.*\\.txt$则文件名和扩展名都会被替换；也适用于-ru和-ruf。" + gl(2)
    + "file -ru[+*@?] regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名中英文字母替换为大写。" + gl(2)
    + "file -ruf[+*@?] regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名中英文单词首字母替换为大写。" + gl(2)
    + "file -rd[+*@?] regex src replacement [level]" + gl(1) + "根据regex和replacement重命名src中的文件和子目录。" + gl(2)
    + "file -rdl[+*@?] regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名和目录名中英文字母替换为小写；regex可最多指定9个捕获组，最左边为第1个捕获组，程序只会替换捕获组中的子串，如：(.*\\.)txt$ 表示只替换文件名，不会替换扩展名txt；.*\\.txt$则文件名和扩展名都会被替换；也适用于-rdu和-rduf。" + gl(2)
    + "file -rdu[+*@?] regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名和目录名中英文字母替换为大写。" + gl(2)
    + "file -rduf[+*@?] regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名和目录名中英文单词首字母替换为大写。" + gl(2)
    + "file -c[+*@?] regex src dest [level]" + gl(1) + "根据regex复制src中文件到dest中。" + gl(2)
    + "file -cd[+*@?] regex src dest [level]" + gl(1) + "根据regex复制src中所有匹配文件和子目录及其中所有文件到dest中。" + gl(2)
    + "file -m[+*@?] regex src dest [level]" + gl(1) + "根据regex移动src中文件到dest中。" + gl(2)
    + "file -md[+*@?] regex src dest [level]" + gl(1) + "根据regex移动src中所有匹配文件和子目录及其中所有文件到dest中。" + gl(2)
    + "file -b[+*@?] regex src dest backup [level]" + gl(1) + "根据regex获得src中所有匹配文件，检查这些文件在dest中是否存在，将不存在的文件备份到backup中。" + gl(2)
    + "file -bd[+*@?] regex src dest backup [level]" + gl(1) + "根据regex获得src中所有匹配文件和子目录及其中所有文件，检查这些文件和子目录在dest中是否存在，将不存在的文件和子目录备份到backup中。" + gl(2)
    + "file -bu[+*@?] regex src dest backup [level]" + gl(1) + "根据regex获得src中所有匹配文件，检查这些文件在dest中是否能找到文件名称是以该文件名称为前缀的文件，若存在则先将dest中匹配的文件移动到backup中，再将该文件移动到dest中。" + gl(2)
    + "file -br[+*@?] regex src dest backup [level]" + gl(1) + "根据regex获得src中所有匹配文件，检查这些文件在dest中是否能找到文件名称是该文件名称的前缀的文件，若存在则先将dest中匹配的文件移动到backup中，再将该文件移动到dest中。" + gl(2)
    + "file -u[+*@?] regex src dest backup [level]" + gl(1) + "根据regex将src中所有匹配文件更新到dest中，更新时会先检查dest中是否已存在该文件，若存在则先将该文件备份到backup中，再更新之。" + gl(2)
    + "file -ud[+*@?] regex src dest backup [level]" + gl(1) + "根据regex将src中所有匹配文件和子目录及其中所有文件更新到dest中，更新时会先检查dest中是否已存在该文件，若存在则先将该文件备份到backup中，再更新之。" + gl(2)
    + "file -zd[+*@?] regex src dest zipName [zipLevel] [level]" + gl(1) + "根据regex将src中所有匹配文件压缩到dest/zipName" + EXT_ZIP + "文件中。" + gl(2)
    + "file -zdd[+*@?] regex src dest zipName [zipLevel] [level]" + gl(1) + "根据regex将src中所有匹配文件和子目录及其中所有文件压缩到dest/zipName" + EXT_ZIP + "文件中。" + gl(2)
    + "file -zi[+*@?] regex src dest [level]" + gl(1) + "根据regex将src中所有匹配文件解压缩到dest中。" + gl(2)
    + "file -pd[+*@?] regex src dest zipName [level]" + gl(1) + "根据regex将src中所有匹配文件打包到dest/zipName" + EXT_PAK + "文件中。" + gl(2)
    + "file -pdd[+*@?] regex src dest zipName [level]" + gl(1) + "根据regex将src中所有匹配文件和子目录及其中所有文件打包到dest/zipName" + EXT_PAK + "文件中。" + gl(2)
    + "file -pi[+*@?] regex src [level]" + gl(1) + "根据regex将src中所有匹配文件解包到该文件所在目录中。" + gl(2)
    + "单条命令示例：" + gl(2)
    + "file -f+ (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\"" + gl(1) + "查询该目录中名称以_cn.strings（忽略大小写）结尾的所有文件，.与strings中间可以包含0到2个任意字符。" + gl(2)
    + "file -fd+ (?i)strings$ \"F:/games/Fallout 4\"" + gl(1) + "查询该目录中名称以strings（忽略大小写）结尾的所有文件和子目录及其中所有文件。" + gl(2)
    + "file -fdo+ . \"F:/games/KingdomComeDeliverance/修改/Mods\" 0 1" + gl(1) + "查询该目录中的第一级子目录。" + gl(2)
    + "file -fpa+ . \"F:/games/DARK SOULS REMASTERED\" 20" + gl(1) + "查询该目录中的所有文件；显示文件的绝对路径名，且只显示前20条记录。" + gl(2)
    + "file -fpr+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的所有文件，显示文件的相对路径名（不包含该目录名称）。" + gl(2)
    + "file -fps+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的所有文件，显示文件的相对路径名（包含该目录名称）。" + gl(2)
    + "file -fpda+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的文件和子目录及其中所有文件，显示文件或子目录的绝对路径名。" + gl(2)
    + "file -fpdr+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的文件和子目录及其中所有文件，显示文件或子目录的相对路径名（不包含该目录名称）。" + gl(2)
    + "file -fpds+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的文件和子目录及其中所有文件，显示文件或子目录的相对路径名（包含该目录名称）。" + gl(2)
    + "file -fpdoa+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的所有子目录，显示子目录的绝对路径名。" + gl(2)
    + "file -fpdor+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的所有子目录，显示子目录的相对路径名（不包含该目录名称）。" + gl(2)
    + "file -fpdos+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的所有子目录，显示子目录的相对路径名（包含该目录名称）。" + gl(2)
    + "file -fs+ . \"F:/games/FINAL FANTASY XV\" 1MB-1GB" + gl(1) + "查询该目录中大小介于1兆字节到1千兆字节之间的所有文件。" + gl(2)
    + "file -fsa+ . \"F:/games/FINAL FANTASY XV\" 1MB,1GB" + gl(1) + "先查询（作用同-fs），再按文件大小递增排序。" + gl(2)
    + "file -fsd+ . \"F:/games/FINAL FANTASY XV\" 1MB;1GB" + gl(1) + "先查询（作用同-fs），再按文件大小递减排序。" + gl(2)
    + "file -fds+ \\Ajp$ \"F:/games/FINAL FANTASY XV\" 1MB-1GB" + gl(1) + "查询该目录中所有子目录名为jp的目录中大小介于1兆字节到1千兆字节之间的所有文件。" + gl(2)
    + "file -fdsa+ \\Ajp$ \"F:/games/FINAL FANTASY XV\" 1MB,1GB" + gl(1) + "先查询（作用同-fds），再按文件大小递增排序。" + gl(2)
    + "file -fdsd+ \\Ajp$ \"F:/games/FINAL FANTASY XV\" 1MB;1GB" + gl(1) + "先查询（作用同-fds），再按文件大小递减排序。" + gl(2)
    + "file -fdosa+ \\Ajp$ \"F:/games/DARK SOULS REMASTERED\" 100KB;10MB" + gl(1) + "先查询该目录中的第一级子目录，再按子目录大小递增排序。" + gl(2)
    + "file -fdosd+ \\Ajp$ \"F:/games/DARK SOULS REMASTERED\" 100KB;10MB" + gl(1) + "先查询该目录中的第一级子目录，再按子目录大小递减排序。" + gl(2)
    + "file -fddsa+ \\Ajp$ \"F:/games/DARK SOULS REMASTERED\" 100KB;10MB" + gl(1) + "先查询该目录中的文件和第一级子目录，再按子目录大小递增排序。" + gl(2)
    + "file -fddsd+ \\Ajp$ \"F:/games/DARK SOULS REMASTERED\" 100KB;10MB" + gl(1) + "先查询该目录中的文件和第一级子目录，再按子目录大小递减排序。" + gl(2)
    + "file -d (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\"" + gl(1) + "先查询（作用同-f）再删除该目录中所有匹配文件。" + gl(2)
    + "file -dd \"\\Ade$|\\Afr$|\\Aru$|\\Aus$\" \"F:/games/FINAL FANTASY XV\"" + gl(1) + "先查询（作用同-fd）再删除该目录中所有匹配文件和子目录及其中所有文件。" + gl(2)
    + "file -ddn . \"F:/games/FINAL FANTASY XV\"" + gl(1) + "先查询（作用同-fd）再删除该目录中所有匹配的空文件和空目录。" + gl(2)
    + "file -r (.*_)(?i)cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\" $1en$2" + gl(1) + "先查询（作用同-f）再以en替换掉所有匹配文件名中的cn（其余字符不变）。" + gl(2)
    + "file -rl (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\"" + gl(1) + "先查询（作用同-f）再将该目录中所有匹配文件名中英文字母替换为小写。" + gl(2)
    + "file -ru (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\"" + gl(1) + "先查询（作用同-f）再将该目录中所有匹配文件名中英文字母替换为大写。" + gl(2)
    + "file -ruf (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\"" + gl(1) + "先查询（作用同-f）再将该目录中所有匹配文件名中英单词首字母替换为大写。" + gl(2)
    + "file -rd (.*_)(?i)cn(\\..{0,2}strings$) \"F:/games/Fallout 4\" $1en$2" + gl(1) + "先查询（作用同-fd）再以en替换掉所有匹配文件名和目录名中的cn（其余字符不变）。" + gl(2)
    + "file -rdl (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4\"" + gl(1) + "先查询（作用同-fd）再将该目录中所有匹配文件名和目录名中英文字母替换为小写。" + gl(2)
    + "file -rdu (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4\"" + gl(1) + "先查询（作用同-fd）再将该目录中所有匹配文件名和目录名中英文字母替换为大写。" + gl(2)
    + "file -rduf (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4\"" + gl(1) + "先查询（作用同-fd）再将该目录中所有匹配文件名和目录名中英单词首字母替换为大写。" + gl(2)
    + "file -c (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\" \"F:/games/Fallout 4/备份\"" + gl(1) + "先查询（作用同-f）再将 .../Strings 中所有匹配文件复制到 .../备份 目录中。" + gl(2)
    + "file -cd (?i).{0,2}strings$ \"F:/games/Fallout 4/Data\" \"F:/games/Fallout 4/备份\"" + gl(1) + "先查询（作用同-fd）再将 .../Data 中所有匹配文件和子目录及其中所有文件复制到 .../备份 目录中。" + gl(2)
    + "file -m (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\" \"F:/games/Fallout 4/备份\"" + gl(1) + "先查询（作用同-f）再将 .../Strings中所有匹配文件移动到 .../备份 目录中。" + gl(2)
    + "file -md (?i).{0,2}strings$ \"F:/games/Fallout 4/Data\" \"F:/games/Fallout 4/备份\"" + gl(1) + "先查询（作用同-fd）再将 .../Data 中所有匹配文件和子目录及其中所有文件移动到 .../备份 目录中。" + gl(2)
    + "file -b . \"F:/games/FINAL FANTASY XV\" \"F:/迅雷下载/FINAL FANTASY XV\" \"F:/备份\"" + gl(1) + "先查询（作用同-f）获得 F:/games/FINAL FANTASY XV 目录中所有匹配文件，检查这些文件在 F:/迅雷下载/FINAL FANTASY XV 目录中是否存在，将不存在的文件备份到 F:/备份 目录中。" + gl(2)
    + "file -bd \\Adatas$ \"F:/games/FINAL FANTASY XV\" \"F:/迅雷下载/FINAL FANTASY XV\" \"F:/备份\"" + gl(1) + "先查询（作用同-fd）获得 F:/games/FINAL FANTASY XV 目录中所有匹配文件和子目录及其中所有文件，检查这些文件和子目录在 F:/迅雷下载/FINAL FANTASY XV 目录中是否存在，将不存在的文件和子目录备份到 F:/备份 目录中。" + gl(2)
    + "file -bu . \"F:/games/Resident Evil 4/修改/BIO4\" \"F:/games/Resident Evil 4/BIO4\" \"F:/games/Resident Evil 4/备份/BIO4\"" + gl(1) + "先查询（作用同-f）获得 F:/games/Resident Evil 4/修改/BIO4 目录中所有匹配文件，检查这些文件在 F:/games/Resident Evil 4/BIO4 目录中是否能找到文件名称是以该文件名称为前缀的文件，若存在则先将 F:/games/Resident Evil 4/BIO4 目录中匹配的文件移动到 F:/games/Resident Evil 4/备份/BIO4 目录中，再将该文件移动到 F:/games/Resident Evil 4/BIO4 目录中。" + gl(2)
    + "file -br . \"F:/games/Resident Evil 4/备份/BIO4\" \"F:/games/Resident Evil 4/BIO4\" \"F:/games/Resident Evil 4/修改/BIO4\"" + gl(1) + "先查询（作用同-f）获得 F:/games/Resident Evil 4/备份/BIO4 目录中所有匹配文件，检查这些文件在 F:/games/Resident Evil 4/BIO4 目录中是否能找到文件名称是该文件名称的前缀的文件，若存在则先将 F:/games/Resident Evil 4/BIO4 目录中匹配的文件移动到 F:/games/Resident Evil 4/修改/BIO4 目录中，再将该文件移动到 F:/games/Resident Evil 4/BIO4 目录中。" + gl(2)
    + "file -u \"F:/games/FINAL FANTASY XV\" \"F:/迅雷下载/FINAL FANTASY XV\" \"F:/备份\"" + gl(1) + "先查询（作用同-f）再将 F:/games/FINAL FANTASY XV 目录中所有匹配文件更新到 F:/迅雷下载/FINAL FANTASY XV 中，若存在同名文件则先将该文件备份到 F:/备份 目录中，再更新之。" + gl(2)
    + "file -ud \\Adatas$ \"F:/games/FINAL FANTASY XV\" \"F:/迅雷下载/FINAL FANTASY XV\" \"F:/备份\"" + gl(1) + "先查询（作用同-fd）再将 F:/games/FINAL FANTASY XV 目录中所有匹配文件和子目录及其中所有文件更新到 F:/迅雷下载/FINAL FANTASY XV 中，若存在同名文件则先将该文件备份到 F:/备份 目录中，再更新之。" + gl(2)
    + "file -zd (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\" \"F:/games/Fallout 4/备份\" strings 1" + gl(1) + "先查询（作用同-f）再将 .../Strings 目录中所有匹配文件按压缩级别1压缩到 .../备份/strings" + EXT_ZIP + " 文件中。" + gl(2)
    + "file -zdd (?i).{0,2}strings$ \"F:/games/Fallout 4/Data\" \"F:/games/Fallout 4/备份\" strings 1" + gl(1) + "先查询（作用同-fd）再将 .../Data 目录中所有匹配文件和子目录及其中所有文件按压缩级别1压缩到 .../备份/strings" + EXT_ZIP + " 文件中。" + gl(2)
    + "file -zi (?i)\\.zip$ \"F:/games/Fallout 4/备份\" \"F:/games/Fallout 4/Data\"" + gl(1) + "先查询（作用同-f）再将 .../备份 目录中所有匹配文件解压缩到 .../Data 目录中。" + gl(2)
    + "file -pd . \"F:/games/KingdomComeDeliverance/修改/Merge/Data\" \"F:/games/KingdomComeDeliverance/Mods/Merge/Data\" merge" + gl(1) + "先查询（作用同-f）再将 .../修改/Merge/Data 目录中所有匹配文件打包到 .../Mods/Merge/Data/merge" + EXT_PAK + " 文件中。" + gl(2)
    + "file -pdd . \"F:/games/KingdomComeDeliverance/修改/Merge/Data\" \"F:/games/KingdomComeDeliverance/Mods/Merge/Data\" merge" + gl(1) + "先查询（作用同-fd）再将 .../修改/Merge/Data 目录中所有匹配文件和子目录及其中所有文件打包到 .../Mods/Merge/Data/merge" + EXT_PAK + " 文件中。" + gl(2)
    + "file -pi (?i)\\.pak$ \"F:/games/KingdomComeDeliverance/修改/Mods\"" + gl(1) + "先查询（作用同-f）再将 .../Mods 目录中所有匹配文件解包到该文件所在目录中。";
    // legend.util.MD5Util
    int BUFFER_SIZE = 0x8000;
    String N_MD5 = "MD5编码";
    String ERR_MD5_CRT = V_CRT + N_MD5 + V_FAIL + N_ERR_INFO + PH_ARG0;
    // legend.util.JaxbUtil
    String ENCODING_UTF8 = "UTF-8";
    String ENCODING_GBK = "GBK";
    String XML_QUOTE_D = "\"";
    String XML_AND = "&";
    String XML_QUOTE_S = "'";
    String XML_CUSP_L = "<";
    String XML_CUSP_R = ">";
    String ESCAPE_QUOTE_S = "&quot;";
    String ESCAPE_AND = "&amp;";
    String ESCAPE_QUOTE_D = "&apos;";
    String ESCAPE_CUSP_L = "&lt;";
    String ESCAPE_CUSP_R = "&gt;";
    String ERR_ANLS_XML = "XML" + N_FLE + V_ANLS + V_FAIL + N_ERR_INFO + PH_ARG0;
    // legend.util.JsonUtil
    char C_ESCAPE = '\\';
    char C_QUOT_D = '"';
    char C_COMMA = ',';
    char C_BRACE_L = '{';
    char C_BRACE_R = '}';
    char C_SQUARE_L = '[';
    char C_SQUARE_R = ']';
    String N_JSON = "JSON字符串";
    String ERR_JSON_PARSE = V_ANLS + N_JSON + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_JSON_FMT = V_ANLS + N_JSON + V_FAIL + N_ERR_INFO + N_JSON + N_FMT + V_ERR;
    // legend.util.ProgressUtil
    String N_PROGRESS = "进度条";
    String V_RUN = "运行";
    String V_STOP = "停止";
    String V_RESUME = "恢复";
    String V_FINISH = "完成";
    String V_RESET = "重置";
    String V_UPDATE = "更新";
    String ERR_RUN = V_RUN + N_PROGRESS + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_STOP = V_STOP + N_PROGRESS + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_RESUME = V_RESUME + N_PROGRESS + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_FINISH = V_FINISH + N_PROGRESS + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_RESET = V_RESET + N_PROGRESS + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_UPDATE = V_UPDATE + N_PROGRESS + V_FAIL + N_ERR_INFO + PH_ARG0;
    // legend.game.poe2.Main
    String POE_DATA_ENCODE = "-e";
    String POE_DATA_DECODE = "-d";
    String POE_OBJ_GUID = "-g";
    String HELP_POE = APP_INFO + "参数说明：" + gl(2)
    + "poe -d|-e|-g regex src string" + gl(2)
    + "-d\t解码（即格式化）JSON格式文件" + gl(2)
    + "-e\t编码（即压缩为一行）JSON格式文件" + gl(2)
    + "-g\t给自定义MOD对象生成36位的GUID字符串" + gl(2)
    + "regex\t文件名查询正则表达式，.匹配任意文件名和目录名。" + gl(2)
    + "src\t文件输入目录" + gl(2)
    + "string\t自定义MOD对象名称字符串" + gl(2)
    + "单条命令：" + gl(2)
    + "poe -d regex src" + gl(2)
    + "poe -e regex src" + gl(2)
    + "poe -g string" + gl(2)
    + "示例：" + gl(2)
    + "poe -d (?i)\\..*bundle$ \"F:/games/Pillars of Eternity II/PillarsOfEternityII_Data/exported/design/gamedata\"" + gl(1)
    + "将 .../gamedata 目录中文件扩展名以bundle结尾（忽略大小写）的所有文件进行解码。" + gl(2)
    + "poe -e (?i)\\..*bundle$ \"F:/games/Pillars of Eternity II/PillarsOfEternityII_Data/exported/design/gamedata\"" + gl(1)
    + "将 .../gamedata 目录中文件扩展名以bundle结尾（忽略大小写）的所有文件进行编码。" + gl(2)
    + "poe -g Great_Sword_WarGod" + gl(1)
    + "获得自定义MOD对象名称Great_Sword_WarGod的GUID字符串。";
    // legend.game.kcd.Main
    String KCD_FILE_CONFIG = "./kcd.xml";
    String KCD_FILE_ORDER = "mod_order.txt";
    String N_KCD_CONFIG = "Mod配置文件";
    String N_KCD_CFG = "Config节点内有任一个节点";
    String N_KCD_MOD_PATH = "Config节点内的modPath节点对应的目录";
    String N_KCD_MOD = "Mods节点内有任一个Mod节点的mod节点";
    String N_KCD_MERGE = "合并文件";
    String MOD_ORDER_MERGE = "0";
    String MOD_ORDER_CONFLICT = "1";
    String MOD_ORDER_INGNORE = "999";
    String MOD_INGNORE_DESC = "该Mod中无任何文件与其它Mod冲突，具有最低的Mod排序优先级，不会添加到Mod排序文件 " + KCD_FILE_ORDER + " 中。";
    String MOD_MERGE_DESC = "整合Mod，包含所有其它Mod的非冲突文件，具有最高的Mod排序优先级。";
    String MOD_MODS = "Mods";
    String MOD_MERGE = "Merge";
    String MOD_REPAIR = "Repair";
    String MOD_CONFLICT = "Conflict";
    String MOD_DATA = "data";
    String MOD_LOCAL = "localization";
    String MOD_CHS = "chineses";
    String MOD_ENG = "english";
    String MOD_LOCAL_CHS = MOD_LOCAL + SPRT_FILE + MOD_CHS;
    String MOD_LOCAL_ENG = MOD_LOCAL + SPRT_FILE + MOD_ENG;
    String PAK_CHINESES = "chineses_xml" + EXT_PAK;
    String PAK_ENGLISH = "english_xml" + EXT_PAK;
    String PAK_MERGE = MOD_MERGE + EXT_PAK;
    String REG_PATH = ".*[/\\\\]";
    String REG_MOD_LOCAL = "(?i).*_xml\\.pak$";
    String REG_MOD_PAK = "(?i)\\.pak$";
    String REG_MOD_NOT_PAK = ".*(?i)(?<!\\.pak)$";
    String REG_ORDER = "\\d|[1-9]\\d|[1-9]\\d\\d";
    String XML_NOTE_START = "<!--";
    String XML_NOTE_END = "-->";
    String XML_NOTE = XML_NOTE_START + PH_ARG0 + XML_NOTE_END + SPRT_LINE;
    String EXEC_KDIFF_F2 = "\"" + PH_ARG0 + "\" \"" + PH_ARG1 + "\" \"" + PH_ARG2 + "\" -o \"" + PH_ARG3 + "\"";
    String EXEC_KDIFF_F3 = "\"" + PH_ARG0 + "\" \"" + PH_ARG1 + "\" \"" + PH_ARG2 + "\" \"" + PH_ARG3 + "\" -o \"" + PH_ARG4 + "\"";
    String KCD_LOC_MRG = "-lm";
    String KCD_LOC_MRG_A = "-lma";
    String KCD_LOC_MRG_U = "-lmu";
    String KCD_LOC_CMP = "-lc";
    String KCD_LOC_CMP_A = "-lca";
    String KCD_LOC_CMP_U = "-lcu";
    String KCD_LOC_DBG = "-ld";
    String KCD_LOC_RLS = "-lr";
    String KCD_MOD_CRT = "-mc";
    String KCD_MOD_PAK = "-mp";
    String KCD_MOD_UNPAK = "-mu";
    String KCD_MOD_MRG_A = "-mma";
    String KCD_MOD_MRG_F = "-mmf";
    String KCD_MOD_MRG_C = "-mmc";
    String KCD_MOD_MRG_O = "-mmo";
    String KCD_MOD_MRG_U = "-mmu";
    String ERR_NOT_FIND = "没找到任何匹配的文件！";
    String ERR_KCD_NON = N_KCD_CONFIG + "\"" + KCD_FILE_CONFIG + "\"" + V_NON_EXISTS;
    String ERR_KCD_NUL_CFG = N_KCD_CONFIG + "\"" + KCD_FILE_CONFIG + "\"" + N_IN + N_KCD_CFG + V_BY_NUL;
    String ERR_KCD_MOD_PATH = N_KCD_CONFIG + "\"" + KCD_FILE_CONFIG + "\"" + N_IN + N_KCD_MOD_PATH + V_NON_EXISTS;
    String ERR_KCD_NUL_MOD = N_KCD_CONFIG + "\"" + KCD_FILE_CONFIG + "\"" + N_IN + N_KCD_MOD + V_BY_NUL;
    String ERR_KCD_MERGE = V_EXEC + N_KCD_MERGE + N_CMD + V_FAIL + N_ERR_INFO + PH_ARG0;
    String HELP_KCD = APP_INFO + "参数说明：" + gl(2)
    + "kcd -lm|-lma|-lmu|-lc|-lca|-lcu|-ld|-lr|-mc|-mp|-mu|-mma|-mmf|-mmc|-mmo|-mmu regex src dest merge gamePath modPath mergePath mergeExecutablePath" + gl(2)
    + "-lm\t\t\t全量合并翻译文件，合并所有；包含原有的、新增的和更新的记录。" + gl(2)
    + "-lma\t\t\t全量合并翻译文件，合并新增；包含原有的和新增的记录。" + gl(2)
    + "-lmu\t\t\t全量合并翻译文件，合并更新；包含原有的和更新的记录。" + gl(2)
    + "-lc\t\t\t差量合并翻译文件，合并所有；包含新增的和更新的记录，不包含原有的记录。" + gl(2)
    + "-lca\t\t\t差量合并翻译文件，合并新增；包含新增的记录，不包含原有的记录。" + gl(2)
    + "-lcu\t\t\t差量合并翻译文件，合并更新；包含更新的记录，不包含原有的记录。" + gl(2)
    + "-ld\t\t\t调试翻译模式。" + gl(2)
    + "-lr\t\t\t发布翻译模式。" + gl(2)
    + "-mc\t\t\t创建Mod配置文件" + KCD_FILE_CONFIG + "。" + gl(2)
    + "-mp\t\t\t先清空gamePath/Mods目录，再重新打包mergePath目录中文件到gamePath/Mods目录，并重新生成Mod排序文件" + KCD_FILE_ORDER + "。" + gl(2)
    + "-mu\t\t\t先清空modPath目录，再重新解包该目录中所有Mod。" + gl(2)
    + "-mmf\t\t\t重新整合modPath目录中所有Mod，并重新合并所有冲突文件（即多个Mod共有的同名文件）。" + gl(2)
    + "-mma\t\t\t重新整合modPath目录中所有Mod，只重新合并变化了的冲突文件（即同名文件有更新、新增或删除）。" + gl(2)
    + "-mmc\t\t\t只合并Mod配置文件" + KCD_FILE_CONFIG + "中配置的所有冲突文件，不打包到gamePath/Mods目录。" + gl(2)
    + "-mmo\t\t\t根据Mod配置文件" + KCD_FILE_CONFIG + "中配置的Mod排序信息重新生成Mod排序文件" + KCD_FILE_ORDER + "。" + gl(2)
    + "-mmu\t\t\t在mergePath目录中重新生成Mod配置文件" + KCD_FILE_CONFIG + "中配置的所有唯一文件（即非冲突文件），并更新这些文件的MD5码。" + gl(2)
    + "regex\t\t\t文件名查询正则表达式，.匹配任意文件名。" + gl(2)
    + "src\t\t\t文件输入目录。" + gl(2)
    + "dest\t\t\t文件输入或输出目录。" + gl(2)
    + "merge\t\t\t文件整合目录。" + gl(2)
    + "gamePath\t\t游戏目录。" + gl(2)
    + "modPath\t\t\tMod目录。" + gl(2)
    + "mergePath\t\tMod整合目录。" + gl(2)
    + "mergeExecutablePath\tMod整合工具KDiff3的可执行文件路径名。" + gl(2)
    + "合并翻译文件说明：先根据regex获得src和dest中匹配的所有文件，再将同名文件数据合并保存到merge中；全量合并与差量合并的区别是前者得到的合并文件是满足游戏数据规范的文件（即可以直接作为Mod使用的文件），而后者得到的是中间文件，需要进行再编辑以满足游戏数据规范。" + gl(2)
    + "单条命令：" + gl(2)
    + "kcd -lm regex src dest merge" + gl(2)
    + "kcd -lma regex src dest merge" + gl(2)
    + "kcd -lmu regex src dest merge" + gl(2)
    + "kcd -lc regex src dest merge" + gl(2)
    + "kcd -lca regex src dest merge" + gl(2)
    + "kcd -lcu regex src dest merge" + gl(2)
    + "kcd -ld regex src dest" + gl(2)
    + "kcd -lr regex src dest" + gl(2)
    + "kcd -mc gamePath modPath mergePath mergeExecutablePath" + gl(2)
    + "kcd -mp" + gl(2)
    + "kcd -mu" + gl(2)
    + "kcd -mmf" + gl(2)
    + "kcd -mma" + gl(2)
    + "kcd -mmc" + gl(2)
    + "kcd -mmo" + gl(2)
    + "kcd -mmu" + gl(2)
    + "示例：" + gl(2)
    + "kcd -lm (?i)\\.xml$ F:/games/KingdomComeDeliverance/修改/Mods/UltimateRealismOverhaul/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/merge" + gl(2)
    + "kcd -lc (?i)\\.xml$ F:/games/KingdomComeDeliverance/修改/Mods/UltimateRealismOverhaul/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/diff" + gl(2)
    + "kcd -ld (?i)\\.xml$ F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/debug" + gl(2)
    + "kcd -lr (?i)\\.xml$ F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/debug F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses" + gl(2)
    + "kcd -mc F:/games/KingdomComeDeliverance F:/games/KingdomComeDeliverance/修改/Mods F:/games/KingdomComeDeliverance/修改/Merge F:/tools/KDiff3/kdiff3.exe";
    // legend.game.dos2.Main
    String FLAG_DEBUG = "#";
    String FLAG_MOD = "*";
    String FLAG_ADD = "+";
    String REG_RELEASE = FLAG_DEBUG + ".*?" + FLAG_DEBUG;
    String REG_DEBUG = FLAG_DEBUG + "\\d+" + FLAG_DEBUG;
    String REG_MOD = FLAG_DEBUG + "\\" + FLAG_MOD + "\\d+" + FLAG_DEBUG;
    String REG_ADD = FLAG_DEBUG + "\\" + FLAG_ADD + "(\\d+)" + FLAG_DEBUG + ".*";
    String REP_ADD = FLAG_DEBUG + "$1" + FLAG_DEBUG;
    String DOS2_DEBUG = "-d";
    String DOS2_RELEASE = "-r";
    String DOS2_UPDATE = "-u";
    String HELP_EOC = APP_INFO + "参数说明：" + gl(2)
    + "eoc -d|-r|-u src dest" + gl(2)
    + "-d\t调试模式" + gl(2)
    + "-r\t发布模式" + gl(2)
    + "-u\t更新模式" + gl(2)
    + "src\t输入文件路径名" + gl(2)
    + "dest\t输出文件路径名" + gl(2)
    + "示例：" + gl(2)
    + "eoc -d \"F:/games/Divinity Original Sin 2/修改/release/english.xml\" \"F:/games/Divinity Original Sin 2/修改/debug/english.xml\"" + gl(2)
    + "eoc -r \"F:/games/Divinity Original Sin 2/修改/debug/english.xml\" \"F:/games/Divinity Original Sin 2/修改/release/english.xml\"" + gl(2)
    + "eoc -u \"F:/games/Divinity Original Sin 2/修改/release/english.xml\" \"F:/games/Divinity Original Sin 2/修改/debug/english.xml\"" + gl(2)
    + "更新模式说明：" + gl(2)
    + "主要针对调试模式的批量更新，会逐行检查调试模式中每个 content 的值，根据最开头的编号模式进行处理，如下：" + gl(2)
    + "1、编号模式形如 #12345#：替换编号后面的值；" + gl(2)
    + "2、编号模式形如 #" + FLAG_MOD + "12345#：保留原值不替换；" + gl(2)
    + "3、编号模式形如 #" + FLAG_ADD + "12345#：去掉编号左边的" + FLAG_ADD + "号，再替换掉编号后面的值；" + gl(2)
    + "4、编号模式为其他形式：去掉编号并替换编号后面的值；" + gl(2)
    + "5、如果是新增的 content 记录（即找不到对应的 contentuid），将添加新纪录并自动生成新编号（编号值从 content记录数+1 开始累加），新编号形如 #" + FLAG_ADD + "12345#。";
    // legend.game.run.Main
    String RUN_FILE_LOG = "./run.log";
    String RUN_FILE_CONFIG = "./run.xml";
    String TIME_SECOND_MIN = "1";
    String TIME_SECOND_MAX = "60";
    String WAIT_TIME = "10";
    String SLEEP_TIME = "1000";
    String REG_TIME = "60|[1-9]|[1-5]\\d";
    String REG_SPRT_CMD = "(?m)\n";
    String REG_SPRT_PATH = "[/" + gs(SPRT_FILE,2) + "]";
    String N_GAME_CONFIG = "游戏配置文件";
    String N_FILE_SCRIPT = "脚本文件";
    String N_EXE = "对应的name或path或exe节点";
    String N_VALIDATE = "存在id或name或path或exe为空的game节点！";
    String ST_CHOICE_ID = "请输入一个游戏ID（按回车键确认）：";
    String FILE_PREFIX = "run";
    String FILE_SUFFIX_BAT = ".bat";
    String FILE_SUFFIX_VBS = ".vbs";
    String FILE_SUFFIX_EXE = ".exe";
    String FILE_SUFFIX_LNK = ".lnk";
    String CMD_CREATE = "-c";
    String CMD_ADD = "-a";
    String CMD_DEL = "-d";
    String CMD_VIEW = "-v";
    String CMD_EXEC = "-x";
    String CMD_LINK = "-l";
    String CMD_LINK_ALL = "-la";
    String CMD_CS_RUN = "cscript \"" + PH_ARG0 + "\"";
    String CMD_VBS_SH_INIT = "dim sh" + gl(1)
    + "set sh=WScript.CreateObject(\"WScript.Shell\")";
    String CMD_VBS_SLEEP = "WScript.Sleep " + PH_ARG0;
    String CMD_VBS_RUN = "sh.Run \"" + PH_ARG0 + "\",0,true";
    String CMD_VBS_RUN_DEL = "sh.Run \"cmd /c del /q \"\"" + PH_ARG0 + "\"\">nul 2>nul\",0,true";
    String CMD_VBS_RUN_GAME = "sh.Run \"cmd /c start /high /D \"\"" + PH_ARG0 + "\"\" \"\"\"\" \"\"" + PH_ARG1 + FILE_SUFFIX_EXE + "\"\" " + PH_ARG2 + "\",0,true";
    String CMD_VBS_RUN_PROC = "sh.Run \"cmd /c wmic process where \"\"name='" + PH_ARG0 + FILE_SUFFIX_EXE + "'\"\" call SetPriority " + PH_ARG1 + "\",0,true";
    String CMD_VBS_WMI_INIT = "dim wmi,run" + gl(1)
    + "set wmi=GetObject(\"WinMgmts:\\\\.\\root\\CIMV2\")" + gl(1)
    + "set run=wmi.Execquery(\"Select * From Win32_Process Where Name='run.exe'\").ItemIndex(0)";
    String CMD_VBS_SC = "dim shortcut,path" + gl(1)
    + "path=sh.SpecialFolders(\"Desktop\")&\"" + SPRT_FILE + PH_ARG0 + FILE_SUFFIX_LNK + "\"" + gl(1)
    + "set shortcut=sh.Createshortcut(path)";
    String CMD_VBS_SC_ARG = "shortcut.Arguments=\"" + CMD_EXEC + " " + PH_ARG0 + "\"";
    String CMD_VBS_SC_IL = "shortcut.IconLocation=\"" + PH_ARG0 + SPRT_FILE + PH_ARG1 + ",0\"";
    String CMD_VBS_SC_DESC = "shortcut.Description=\"" + PH_ARG0 + "\"";
    String CMD_VBS_SC_WD = "shortcut.WorkingDirectory=\"" + PH_ARG0 + "\"";
    String CMD_VBS_SC_TP = "shortcut.TargetPath=run.ExecutablePath";
    String CMD_VBS_SC_WS = "shortcut.WindowStyle=1";
    String CMD_VBS_SC_SAVE = "shortcut.Save";
    String CMD_BAT_PROC_DEL_BY_NAME = "wmic process where \"name='" + PH_ARG0 + "'\" delete";
    String CMD_BAT_PROC_DEL_BY_PATH = "wmic process where \"executablepath='" + PH_ARG0 + "'\" delete";
    String CMD_BAT_WATCH = "setlocal enableextensions" + gl(1)
    + "setlocal enabledelayedexpansion" + gl(1)
    + ":watch" + gl(1)
    + "set pid=" + gl(1)
    + "for /f \"usebackq skip=1\" %%i in (`wmic process where \"name='" + PH_ARG0 + "'\" get processid`) do if \"!pid!\"==\"\" set pid=%%i" + gl(1)
    + "if not \"%pid%\"==\"\" (" + gl(1)
    + "choice /c y /d y /t " + PH_ARG1 + " >nul 2>nul" + gl(1)
    + "goto watch ) else (" + gl(1)
    + PH_ARG2
    + "goto quit )" + gl(1)
    + ":quit" + gl(1)
    + "exit /b 0";
    String ERR_CONFIG_NON = N_GAME_CONFIG + "\"" + RUN_FILE_CONFIG + "\"" + V_NON_EXISTS;
    String ERR_CONFIG_NUL = N_GAME_CONFIG + "\"" + RUN_FILE_CONFIG + "\"" + V_BY_NUL;
    String ERR_ID_NON = N_GAME_CONFIG + "\"" + PH_ARG0 + "\"" + N_IN + N_SPEC_ID + "\"" + PH_ARG1 + "\"" + V_NON_EXISTS;
    String ERR_ID_EXISTS = N_GAME_CONFIG + "\"" + PH_ARG0 + "\"" + N_IN + N_SPEC_ID + "\"" + PH_ARG1 + "\"" + V_ALREADY_EXISTS;
    String ERR_VALIDATE = N_GAME_CONFIG + "\"" + PH_ARG0 + "\"" + N_IN + N_VALIDATE;
    String ERR_EXE_NUL = N_GAME_CONFIG + "\"" + PH_ARG0 + "\"" + N_IN + N_SPEC_ID + "\"" + PH_ARG1 + "\"" + N_EXE + V_BY_NUL;
    String ERR_CREATE_FAIL = V_CRT + N_FILE_SCRIPT + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_RUN_FAIL = V_EXEC + N_FILE_SCRIPT + V_FAIL + N_ERR_INFO + PH_ARG0;
    String GAMES_COMMENT = gl(1) + gs(4) + "游戏配置集节点结构说明：" + gl(1)
    + gs(4) + "Games节点由一个唯一节点comment和多个Game节点按顺序组成，comment节点必须在最前面。" + gl(1)
    + gs(4) + "Games::comment\t游戏配置集节点结构说明，对执行游戏无影响，仅此说明而已。" + gl(1)
    + gs(4) + "Games::Game\t\t游戏配置节点，包括执行游戏的命令行参数配置及执行游戏前和执行游戏后的BAT脚本命令配置。" + gl(1)
    + gs(4) + "Game节点由comment、name、id、path、exe、args、priority、icon、agentPath、agentExe、agentArgs、before、after、beforeWait、afterWait、watchWait、watch节点按顺序组成；comment节点必须在最前面，watch节点可以有多个。" + gl(1)
    + gs(4) + "Game::comment\t\t游戏快捷方式说明，默认值同Game::name。" + gl(1)
    + gs(4) + "Game::name\t\t游戏快捷方式名称，一般使用游戏中文名称。" + gl(1)
    + gs(4) + "Game::id\t\t游戏唯一标识。" + gl(1)
    + gs(4) + "Game::path\t\t游戏可执行文件路径，也是Game::icon的路径。" + gl(1)
    + gs(4) + "Game::exe\t\t游戏可执行文件名称，不包含文件扩展名" + FILE_SUFFIX_EXE + "；若Game::agentExe非空，则优先使用Game::agentExe启动游戏。" + gl(1)
    + gs(4) + "Game::args\t\tGame::exe的命令行参数。" + gl(1)
    + gs(4) + "Game::priority\t\t游戏进程的优先级，可选值为：32（标准），64（低），128（高），256（实时），16384（低于标准），32768（高于标准）；若Game::agentExe非空且Game::agentArgs已指定优先级，则应把该节点值置空，否则优先使用该节点值。" + gl(1)
    + gs(4) + "Game::icon\t\t游戏快捷方式的图标文件完整名称（包含文件扩展名）；若为空则使用游戏可执行文件中图标。" + gl(1)
    + gs(4) + "Game::agentPath\tGame::exe的代理可执行文件路径；若为空则取值为Game::path。" + gl(1)
    + gs(4) + "Game::agentExe\t\tGame::exe的代理可执行文件名称，不包含文件扩展名" + FILE_SUFFIX_EXE + "；适用于使用游戏插件启动游戏的情况，例如：上古卷轴5的skse。" + gl(1)
    + gs(4) + "Game::agentArgs\tGame::agentExe的命令行参数。" + gl(1)
    + gs(4) + "Game::before\t\t在游戏执行前需要执行的BAT脚本命令。" + gl(1)
    + gs(4) + "Game::after\t\t在游戏执行后需要执行的BAT脚本命令。" + gl(1)
    + gs(4) + "Game::beforeWait\tGame::before命令执行完后等待beforeWait秒，再执行游戏；仅当Game::before不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。" + gl(1)
    + gs(4) + "Game::afterWait\t\t执行游戏后等待afterWait秒，再执行Game::after命令；仅当Game::after不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。"  + gl(1)
    + gs(4) + "Game::watchWait\t游戏监控进程的等待时间，每隔watchWait秒后检测一次游戏进程是否存在；仅当Game::watch不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。"  + gl(1)
    + gs(4) + "Game::watch\t\t由Game::before或Game::after脚本启动的进程的名称（例如：editplus.exe）或进程的可执行文件路径名（例如：F:/tools/EditPlus/editplus.exe），在游戏进程结束后监控程序会自动关闭之。" + gl(1) + gs(4);
    String HELP_RUN = APP_INFO + "参数说明：" + gl(2)
    + "run -c|-a|-d|-v|-x|-l|-la id path exe name [comment]" + gl(2)
    + "id\t\t游戏标识，在" + RUN_FILE_CONFIG + "文件中唯一标识一个游戏配置节点。" + gl(2)
    + "path\t\t游戏可执行文件路径。" + gl(2)
    + "exe\t\t游戏可执行文件名称（不包含扩展名" + FILE_SUFFIX_EXE + "）。" + gl(2)
    + "name\t\t游戏中文名称。" + gl(2)
    + "comment\t\t游戏快捷方式说明。" + gl(2)
    + "-c id path exe name [comment] 新建游戏配置文件" + RUN_FILE_CONFIG + "，并生成一个游戏配置节点。" + gl(2)
    + "-a id path exe name [comment] 添加一个游戏配置节点到游戏配置文件" + RUN_FILE_CONFIG + "中。" + gl(2)
    + "-d id 根据id删除游戏配置文件" + RUN_FILE_CONFIG + "中对应的一个游戏配置节点。" + gl(2)
    + "-v 显示游戏配置文件" + RUN_FILE_CONFIG + "中所有的游戏配置节点的id列表，显示格式为：id\t\tcomment。" + gl(2)
    + "-x [id] 根据id执行游戏配置文件" + RUN_FILE_CONFIG + "中对应的游戏；如果不指定id程序则会先显示id列表（同-v），再提示输入一个id，根据id执行对应的游戏。" + gl(2)
    + "-l [id] 根据id获得" + RUN_FILE_CONFIG + "中对应的游戏，并创建游戏快捷方式到桌面；如果不指定id程序则会先显示id列表（同-v），再提示输入一个id，根据id创建游戏快捷方式。" + gl(2)
    + "-la 批量创建游戏配置文件" + RUN_FILE_CONFIG + "中所有游戏的快捷方式到桌面。" + gl(2)
    + "示例：" + gl(2)
    + "run -c ew \"F:/games/The Evil Within\" EvilWithin 恶灵附身：开发者模式" + gl(2)
    + "run -a ew2 \"F:/games/The Evil Within 2\" TEW2 恶灵附身2：开发者模式" + gl(2)
    + "run -a lotf \"F:/games/Lords Of The Fallen/bin\" LordsOfTheFallen 堕落之王：开发者模式" + gl(2)
    + "run -a sg \"F:/games/The Surge/bin\" TheSurge 迸发：开发者模式" + gl(2)
    + "run -a skse \"F:/games/Skyrim Special Edition\" SkyrimSE 上古卷轴5：开发者模式" + gl(2)
    + "run -a poe2 \"F:/games/Pillars of Eternity II\" PillarsOfEternityII 永恒之柱2" + gl(2)
    + "run -a poe2-d \"F:/games/Pillars of Eternity II\" PillarsOfEternityII 永恒之柱2：开发者模式" + gl(2)
    + "run -d ew2" + gl(2)
    + "run -v" + gl(2)
    + "run -x" + gl(2)
    + "run -x ew" + gl(2)
    + "run -l" + gl(2)
    + "run -l ew" + gl(2)
    + "run -la";
}
