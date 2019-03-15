package legend.util.intf;

import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.gs;
import static legend.util.rule.intf.IReplaceRule.REG_COL_NUM;
import static legend.util.rule.intf.IReplaceRule.REG_COL_REPL_COMP;
import static legend.util.rule.intf.IReplaceRule.REG_COL_REPL_ATOM;
import static legend.util.rule.intf.IReplaceRule.REG_RULE_LOWER;
import static legend.util.rule.intf.IReplaceRule.REG_RULE_REGENROW;
import static legend.util.rule.intf.IReplaceRule.REG_RULE_REPLACE;
import static legend.util.rule.intf.IReplaceRule.REG_RULE_UPPER;
import static legend.util.rule.intf.IReplaceRule.RULE_LOWER;
import static legend.util.rule.intf.IReplaceRule.RULE_REGENROW;
import static legend.util.rule.intf.IReplaceRule.RULE_REPLACE;
import static legend.util.rule.intf.IReplaceRule.RULE_UPPER;

import legend.intf.ICommon;

public interface IFileUtil extends ICommon{
    long EXEC_CMD = 1l;
    long SHOW_PROGRESS = 1l << 1;
    long SHOW_DETAIL = 1l << 2;
    long EXCLUDE_ROOT = 1l << 3;
    long MATCH_FILE_ONLY = 1l << 4;
    long MATCH_DIR_ONLY = 1l << 5;
    long IS_QUERY_COMMAND = 1l << 6;
    long NEED_REPATH = 1l << 7;
    long ORDER_ASC = 1l << 8;
    long COMPARE_SAME = 1l << 9;
    long PATH_RELATIVE = 1l << 10;
    long INTERCHANGE_UPGRADE = 1l << 11;
    long ZIP_UNZIP = 1l << 12;
    long IGNORE_REGEX = 1l << 13;
    long ENABLE_CACHE = 1l << 50;
    long CAN_USE_CACHE = 1l << 51;
    long CAN_SAVE_CACHE = 1l << 52;
    long NEED_CLEAR_CACHE = 1l << 53;
    int SIZE_IL_PARTITION = 10000;
    int SIZE_IL_HEADER = 7;
    int PROGRESS_POSITION = 50;
    float PROGRESS_SCALE = (100 - PROGRESS_POSITION) / 100f;
    String CMD = "file ";
    String CMD_FIND = "-f";
    String CMD_FND_DIR = "-fd";
    String CMD_FND_DIR_OLY = "-fdo";
    String CMD_FND_SAM = "-fs";
    String CMD_FND_DIR_SAM = "-fds";
    String CMD_FND_DIR_OLY_SAM = "-fdos";
    String CMD_FND_DIF = "-fdf";
    String CMD_FND_DIR_DIF = "-fddf";
    String CMD_FND_DIR_OLY_DIF = "-fdodf";
    String CMD_FND_PTH_ABS = "-fpa";
    String CMD_FND_PTH_RLT = "-fpr";
    String CMD_FND_PTH_DIR_ABS = "-fpda";
    String CMD_FND_PTH_DIR_RLT = "-fpdr";
    String CMD_FND_PTH_DIR_OLY_ABS = "-fpdoa";
    String CMD_FND_PTH_DIR_OLY_RLT = "-fpdor";
    String CMD_FND_SIZ_ASC = "-fsa";
    String CMD_FND_SIZ_DSC = "-fsd";
    String CMD_FND_DIR_SIZ_ASC = "-fdsa";
    String CMD_FND_DIR_SIZ_DSC = "-fdsd";
    String CMD_FND_DIR_DIR_SIZ_ASC = "-fddsa";
    String CMD_FND_DIR_DIR_SIZ_DSC = "-fddsd";
    String CMD_FND_DIR_OLY_SIZ_ASC = "-fdosa";
    String CMD_FND_DIR_OLY_SIZ_DSC = "-fdosd";
    String CMD_RENAME = "-r";
    String CMD_REN_LOW = "-rl";
    String CMD_REN_UP = "-ru";
    String CMD_REN_UP_FST = "-ruf";
    String CMD_REN_DIR = "-rd";
    String CMD_REN_DIR_LOW = "-rdl";
    String CMD_REN_DIR_UP = "-rdu";
    String CMD_REN_DIR_UP_FST = "-rduf";
    String CMD_REN_DIR_OLY = "-rdo";
    String CMD_REN_DIR_OLY_LOW = "-rdol";
    String CMD_REN_DIR_OLY_UP = "-rdou";
    String CMD_REN_DIR_OLY_UP_FST = "-rdouf";
    String CMD_REP_FILE_BT = "-rfbt";
    String CMD_REP_FILE_IL = "-rfil";
    String CMD_REG_FILE_GBK = "-rfgbk";
    String CMD_REG_FILE_BIG5 = "-rfbig5";
    String CMD_COPY = "-c";
    String CMD_CPY_DIR = "-cd";
    String CMD_CPY_DIR_OLY = "-cdo";
    String CMD_DELETE = "-d";
    String CMD_DEL_DIR = "-dd";
    String CMD_DEL_DIR_OLY = "-ddo";
    String CMD_DEL_NUL = "-dn";
    String CMD_DEL_DIR_NUL = "-ddn";
    String CMD_DEL_DIR_OLY_NUL = "-ddon";
    String CMD_MOVE = "-m";
    String CMD_MOV_DIR = "-md";
    String CMD_MOV_DIR_OLY = "-mdo";
    String CMD_ITCHG_UGD = "-iu";
    String CMD_ITCHG_RST = "-ir";
    String CMD_UPGRADE = "-u";
    String CMD_UGD_DIR = "-ud";
    String CMD_ZIP_DEF = "-zd";
    String CMD_ZIP_DIR_DEF = "-zdd";
    String CMD_ZIP_INF = "-zi";
    String CMD_PAK_DEF = "-pd";
    String CMD_PAK_DIR_DEF = "-pdd";
    String CMD_PAK_INF = "-pi";
    String CMD_GUID_L32 = "-gl32";
    String CMD_GUID_U32 = "-gu32";
    String CMD_MD5_L16 = "-ml16";
    String CMD_MD5_U16 = "-mu16";
    String CMD_MD5_L32 = "-ml32";
    String CMD_MD5_U32 = "-mu32";
    String CMD_JSON_ENC = "-je";
    String CMD_JSON_DEC = "-jd";
    String OPTIONS = "[" + OPT_DETAIL + OPT_SIMULATE + OPT_EXCLUDE_ROOT + OPT_CACHE + OPT_ASK + "] ";
    String FILE_LOG = "./file.log";
    String CONFIG_FILE_IL = "./file-il.xml";
    String REG_FLE_SIZ = "(0|[1-9]\\d*)([TGMKtgmk]?[Bb])?[,;-]?+";
    String REG_REN_UP_FST = "[a-zA-Z]\\w*";
    String ST_ASK_CONT = "输入n或N跳过，否则继续，按回车键确认：";
    String ST_FILE_IL_MISMATCH = "IL配置文件与IL源文件内容不匹配！";
    String ST_FILE_IL_CONF = "已生成替换IL源文件" + S_DQM + PH_ARG0 + S_DQM + "内容所需的IL配置文件" + S_DQM + PH_ARG1 + S_DQM + S_BANG;
    String ERR_DIR_VST = V_VST + N_DIR + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_VST = V_VST + N_FLE + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_DEL = V_DEL + N_FLE + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_CPY = V_CPY + N_FLE + S_DQM + PH_ARG0 + V_TO + PH_ARG1 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG2;
    String ERR_FLE_MOV = V_MOV + N_FLE + S_DQM + PH_ARG0 + V_TO + PH_ARG1 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG2;
    String ERR_FLE_WRT = V_WRITE + N_FLE + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_READ = V_READ + N_FLE + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_REPL = V_REPL + N_FLE + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_EXTR = V_EXTR + N_FLE + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_DCPRS = V_DCPRS + N_FLE + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_CRT = V_CRT + V_CPRS + N_FLE + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_CPY = V_CPRS + N_FLE + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FLE_NUL_CPY = V_CPRS + N_FLE_NUL + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_DIR_NUL_CPY = V_CPRS + N_DIR_NUL + S_DQM + PH_ARG0 + S_DQM + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_ZIP_FILE_SAME = V_CPRS + N_OR + V_DCPRS + N_A + N_FLE + N_IN + V_EXISTS + N_AND + V_CPRS + N_FLE + N_PATH_NAME + S_DQM + PH_ARG0 + S_DQM + V_SAME + N_A + N_FLE + S_BANG;
    String HELP_FILE = APP_INFO + "命令参数：" + gl(2)
    + "regex" + gs(7) + "文件名正则查询表达式，.匹配任意文件名和目录名；引号等特殊字符可使用占位符表达式；" + gl(2)
    + "目前支持的所有特殊字符占位符表达式（英文字母不区分大小写）如下：" + gl(2)
    + "#SQM=n#" + gs(1) + "英文单引号（'）占位符表达式，匹配的正则表达式为：" + REG_SPC_SQM + "；SQM表示单引号，n为个数，=可以不写；基于性能考虑，n的取值范围限定为1~9，表示替换为n个单引号；例如：#SQM#（替换为1个单引号）,#SQM1#（替换为1个单引号）,#SQM=2#（替换为2个单引号）。" + gl(2)
    + "#DQM=n#" + gs(1) + "英文双引号（\"）占位符表达式，匹配的正则表达式为：" + REG_SPC_DQM + "；DQM表示双引号，n为个数，=可以不写；基于性能考虑，n的取值范围限定为1~9，表示替换为n个双引号；例如：#DQM#（替换为1个双引号）,#DQM1#（替换为1个双引号）,#DQM=2#（替换为2个双引号）。" + gl(2)
    + "src" + gs(9) + "文件输入目录；可使用特殊字符占位符表达式（见regex参数）。" + gl(2)
    + "dest" + gs(8) + "文件输入输出目录或输入文件路径名；可使用特殊字符占位符表达式（见regex参数）。" + gl(2)
    + "backup" + gs(6) + "文件备份输出目录；可使用特殊字符占位符表达式（见regex参数）。" + gl(2)
    + "zipName" + gs(5) + "压缩文件名（程序会根据命令选项自动添加文件扩展名" + EXT_ZIP + "或" + EXT_PAK + "）；可使用特殊字符占位符表达式（见regex参数）。" + gl(2)
    + "zipLevel" + gs(4) + "文件压缩级别，取值0：不压缩，1~9：1为最低压缩率，9为最高压缩率；不指定则程序智能选择最佳压缩率。" + gl(2)
    + "limit" + gs(7) + "查询类命令（即命令选项以-f开头的命令）的查询结果显示数量限制，即显示前limit条记录；取值范围为：1~2147483647，不指定或指定一个非正整数则取默认值2147483647。" + gl(2)
    + "level" + gs(7) + "文件目录最大查询层数；取值范围为：1~2147483647，不指定则取默认值2147483647层。" + gl(2)
    + "sizeExpr" + gs(4) + "文件大小表达式，匹配的正则表达式为：" + REG_FLE_SIZ + "；取值范围为：0~9223372036854775807B，不指定则取默认值0B；例如：100B（不小于100字节），10KB（不小于10千字节），1-100MB（介于1兆字节到100兆字节之间），500MB;1GB（介于500兆字节到1千兆字节之间），2,1GB（介于2千兆字节到1千兆字节之间），800,800（等于800字节）。" + gl(2)
    + "split" + gs(7) + "二维表格式文件中的列分隔符正则表达式，例如：[,;| \\t]+；不指定则取默认值[ \\t]+，即只使用空格或制表符作为列分隔符；可使用特殊字符占位符表达式（见regex参数）。" + gl(2)
    + "replacement" + gs(1) + "字符串替换表达式，可作为文件名正则替换表达式（可使用特殊字符占位符表达式（见regex参数））；也可作为二维表格式文件中的列字符串替换表达式，格式为：[列号表达式" + SPRT_FIELD + "]规则1(参数列表)" + SPRT_RULE + "[规则2(参数列表) ... " + SPRT_RULE + "规则n(参数列表)]；若不指定列号表达式则对所有列执行指定的规则；规则具备事务性，简单规则仅由1个原子规则组成，复合规则由多个原子规则组成且不能包含原子规则" + RULE_REGENROW + "；各事务性规则通过" + SPRT_RULE + "分隔，复合规则中各原子规则通过" + SPRT_ATOM + "分隔，各参数通过" + SPRT_ARG + "分隔；列号表达式匹配的正则表达式为：" + REG_COL_NUM + "；例如：1（取第1列）；1,3,5（取1、3、5列）；1-3（取1、2、3列）；1,4-6（取1、4、5、6列）；" + gl(2)
    + "目前支持的所有原子规则（英文字母不区分大小写）如下：" + gl(2)
    + RULE_LOWER + "(qstring)" + gs(12) + "将匹配qstring的列字符串中英文字母替换为小写，匹配的正则表达式为：" + REG_RULE_LOWER + "；可以不传参数qstring，即" + RULE_LOWER + "与" + RULE_LOWER + "(.)等效但更高效；qstring为正则查询表达式，可使用特殊字符占位符表达式（见regex参数）；" + gl(2)
    + RULE_UPPER + "(qstring)" + gs(12) + "将匹配qstring的列字符串中英文字母替换为大写，匹配的正则表达式为：" + REG_RULE_UPPER + "；可以不传参数qstring，即" + RULE_UPPER + "与" + RULE_UPPER + "(.)等效但更高效；qstring为正则查询表达式，可使用特殊字符占位符表达式（见regex参数）；" + gl(2)
    + RULE_REPLACE + "(qstring" + SPRT_ARG + "rstring)" + gs(1) + "将匹配qstring的列字符串的子串替换为rstring，匹配的正则表达式为：" + REG_RULE_REPLACE + "；qstring为正则查询表达式，rstring为正则替换表达式，可使用特殊字符占位符表达式（见regex参数）；" + gl(2)
    + RULE_REGENROW + "(rstring)" + gs(10) + "根据rstring重新生成每一行数据，匹配的正则表达式为：" + REG_RULE_REGENROW + "；此规则只能作为最后一条原子规则使用，即只能放在规则列表的最后面；rstring为行数据正则替换表达式，可使用特殊字符占位符表达式（见regex参数）和列数据占位符表达式；" + gl(2)
    + "目前支持的所有列数据占位符表达式如下：" + gl(2)
    + "#n.m#" + gs(1) + "提取通过执行原子规则获得的列数据，匹配的正则表达式为：" + REG_COL_REPL_ATOM + "；n为列号，m为原子规则执行顺序号；m的最小值为0，最大值为原子规则执行总数；m取0表示提取第n列的原始数据；例如：#1.0#（提取第1列的原始数据），#1.1#(提取对第1列执行了第1条原子规则后得到的数据)；" + gl(2)
    + "#n-m1.m2#" + gs(1) + "提取通过执行复合规则获得的列数据，匹配的正则表达式为：" + REG_COL_REPL_COMP + "；n为列号，m1为复合规则执行顺序号，m2为m1中原子规则的执行顺序号；m1的最小值为1，最大值为复合规则执行总数；m2的最小值为1，最大值为m1中原子规则执行总数，m2不指定则取最大值（即#n-m1#与#n-m1.max(m2)#等效）；例如：#1-1#（提取对第1列执行了第1条复合规则后得到的数据）；#1-1.1#（提取对第1列执行了第1条复合规则中的第1条原子规则后得到的数据）。" + gl(3)
    + "命令选项：" + gl(2)
    + "+ 可添加在命令选项末尾，表示输出详细信息；可与!或@或?连用；例如：-fd!+@?。" + gl(2)
    + "* 可添加在命令选项末尾，表示模拟执行命令，不进行实际操作，仅输出详细信息；可与!或@或?连用；例如：-fd*?@!。" + gl(2)
    + "! 可添加在命令选项末尾，表示不匹配查询的根目录，可与+或*或@或?连用；例如：file -fd!+ . d:/games 不匹配games目录，只匹配该目录中的任意文件和子目录名称。" + gl(2)
    + "@ 可添加在命令选项末尾，表示缓存该命令的查询结果，供后面的命令复用；某些命令不能缓存或复用查询结果，程序将智能忽略掉；复用查询结果的命令将忽略与查询相关的命令参数regex和src；当后面某个命令使用了@时，则重新缓存查询结果；可与!或+或*或?连用；例如：-fd!@*?。" + gl(2)
    + "? 可添加在命令选项末尾，表示命令开始执行前启用询问模式（" + ST_ASK_CONT + "）；可与!或+或*或@连用；例如：-fd!+@?。" + gl(3)
    + "组合命令：" + gl(2)
    + "可以组合多个命令选项和命令参数，一次连续执行多条命令；命令选项与各命令参数的个数必须相等；各命令选项及各命令参数使用" + SPRT_CMD + "分隔；可使用" + OPT_SIMULATE  + "复用最近一个明确的命令选项或命令参数，将其当作该命令的前缀使用，例如：-f::*d::*dsa等价于-f::-fd::-fdsa，g:/games::*/1::*/2等价于g:/games::g:/games/1::g:/games/2；单条命令未用到的命令参数使用" + OPT_ASK + "占位。" + gl(2)
    + "组合命令示例：" + gl(2)
    + CMD + "-cd*@::*::* .::*::* g:/games::*::* d:/::e:/::f:/" + gl(2)
    + CMD + "-zdd+::-c+@?::* .::\\.zip$::* g:/games::g:/file::* g:/file::*/1::*/2 games::?::? 0::?::? ?::1::*" + gl(2)
    + CMD + "-zi*::-cd@*::* \\.zip$::.::* g:/file::g:/games::* g:/::e:/::f:/ 1::?::?" + gl(3)
    + "单条命令：" + gl(2)
    + CMD + CMD_FIND + OPTIONS + "regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件。" + gl(2)
    + CMD + CMD_FND_DIR + OPTIONS + "regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件和目录及其中所有文件，相对-f增加了目录名匹配，若目录名匹配，则该目录中所有文件和目录都自动被匹配。" + gl(2)
    + CMD + CMD_FND_DIR_OLY + OPTIONS + "regex src [limit] [level]" + gl(1) + "根据regex查找src中的目录。" + gl(2)
    + CMD + CMD_FND_SAM + OPTIONS + "regex src dest [limit] [level]" + gl(1) + "根据regex查找src中的文件，且只选取在desc目录的同一相对路径中存在的同名文件。" + gl(2)
    + CMD + CMD_FND_DIR_SAM + OPTIONS + "regex src dest [limit] [level]" + gl(1) + "根据regex查找src中的文件和目录及其中所有文件，相对-f增加了目录名匹配，若目录名匹配，则该目录中所有文件和目录都自动被匹配；且只选取在desc目录的同一相对路径中存在的同名目录和文件。" + gl(2)
    + CMD + CMD_FND_DIR_OLY_SAM + OPTIONS + "regex src dest [limit] [level]" + gl(1) + "根据regex查找src中的目录，且只选取在desc目录的同一相对路径中存在的同名目录。" + gl(2)
    + CMD + CMD_FND_DIF + OPTIONS + "regex src dest [limit] [level]" + gl(1) + "根据regex查找src中的文件，且只选取在desc目录的同一相对路径中不存在的文件。" + gl(2)
    + CMD + CMD_FND_DIR_DIF + OPTIONS + "regex src dest [limit] [level]" + gl(1) + "根据regex查找src中的文件和目录及其中所有文件，相对-f增加了目录名匹配，若目录名匹配，则该目录中所有文件和目录都自动被匹配；且只选取在desc目录的同一相对路径中不存在的目录和文件。" + gl(2)
    + CMD + CMD_FND_DIR_OLY_DIF + OPTIONS + "regex src dest [limit] [level]" + gl(1) + "根据regex查找src中的目录，且只选取在desc目录的同一相对路径中不存在的目录。" + gl(2)
    + CMD + CMD_FND_PTH_ABS + OPTIONS + "regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件的绝对路径名。" + gl(2)
    + CMD + CMD_FND_PTH_RLT + OPTIONS + "regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件的相对路径名。" + gl(2)
    + CMD + CMD_FND_PTH_DIR_ABS + OPTIONS + "regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件和目录及其中所有文件（同-fd），显示文件或目录的绝对路径名。" + gl(2)
    + CMD + CMD_FND_PTH_DIR_RLT + OPTIONS + "regex src [limit] [level]" + gl(1) + "根据regex查找src中的文件和目录及其中所有文件（同-fd），显示文件或目录的相对路径名。" + gl(2)
    + CMD + CMD_FND_PTH_DIR_OLY_ABS + OPTIONS + "regex src [limit] [level]" + gl(1) + "根据regex查找src中的目录（同-fdo），显示目录的绝对路径名。" + gl(2)
    + CMD + CMD_FND_PTH_DIR_OLY_RLT + OPTIONS + "regex src [limit] [level]" + gl(1) + "根据regex查找src中的目录（同-fdo），显示目录的相对路径名。" + gl(2)
    + CMD + CMD_FND_SIZ_ASC + OPTIONS + "regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件，按文件大小递增排序。" + gl(2)
    + CMD + CMD_FND_SIZ_DSC + OPTIONS + "regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件，按文件大小递减排序。" + gl(2)
    + CMD + CMD_FND_DIR_SIZ_ASC + OPTIONS + "regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件和目录，按文件大小递增排序。" + gl(2)
    + CMD + CMD_FND_DIR_SIZ_DSC + OPTIONS + "regex src [sizeExpr] [limit] [level]" + gl(1) + "根据regex和sizeExpr查找src中的文件和目录，按文件大小递减排序。" + gl(2)
    + CMD + CMD_FND_DIR_DIR_SIZ_ASC + OPTIONS + "regex src [sizeExpr] [limit]" + gl(1) + "根据regex和sizeExpr查找src中的文件和第一级子目录，按文件和子目录大小递增排序。" + gl(2)
    + CMD + CMD_FND_DIR_DIR_SIZ_DSC + OPTIONS + "regex src [sizeExpr] [limit]" + gl(1) + "根据regex和sizeExpr查找src中的文件和第一级子目录，按文件和子目录大小递减排序。" + gl(2)
    + CMD + CMD_FND_DIR_OLY_SIZ_ASC + OPTIONS + "regex src [sizeExpr] [limit]" + gl(1) + "根据regex和sizeExpr查找src中的第一级子目录，按子目录大小递增排序。" + gl(2)
    + CMD + CMD_FND_DIR_OLY_SIZ_DSC + OPTIONS + "regex src [sizeExpr] [limit]" + gl(1) + "根据regex和sizeExpr查找src中的第一级子目录，按子目录大小递减排序。" + gl(2)
    + CMD + CMD_RENAME + OPTIONS + "regex src replacement [level]" + gl(1) + "根据regex和replacement重命名src中的文件。" + gl(2)
    + CMD + CMD_REN_LOW + OPTIONS + "regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名中英文字母替换为小写；regex可最多指定9个捕获组，最左边为第1个捕获组，程序只会替换捕获组中的子串，如：(.*\\.)txt$ 表示只替换文件名，不会替换扩展名txt；.*\\.txt$则文件名和扩展名都会被替换；也适用于-ru和-ruf。" + gl(2)
    + CMD + CMD_REN_UP + OPTIONS + "regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名中英文字母替换为大写。" + gl(2)
    + CMD + CMD_REN_UP_FST + OPTIONS + "regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名中英文单词首字母替换为大写。" + gl(2)
    + CMD + CMD_REN_DIR + OPTIONS + "regex src replacement [level]" + gl(1) + "根据regex和replacement重命名src中的文件和目录。" + gl(2)
    + CMD + CMD_REN_DIR_LOW + OPTIONS + "regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名和目录名中英文字母替换为小写；regex可最多指定9个捕获组，最左边为第1个捕获组，程序只会替换捕获组中的子串，如：(.*\\.)txt$ 表示只替换文件名，不会替换扩展名txt；.*\\.txt$则文件名和扩展名都会被替换；也适用于-rdu和-rduf。" + gl(2)
    + CMD + CMD_REN_DIR_UP + OPTIONS + "regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名和目录名中英文字母替换为大写。" + gl(2)
    + CMD + CMD_REN_DIR_UP_FST + OPTIONS + "regex src [level]" + gl(1) + "根据regex将src中所有匹配文件名和目录名中英文单词首字母替换为大写。" + gl(2)
    + CMD + CMD_REN_DIR_OLY + OPTIONS + "regex src replacement [level]" + gl(1) + "根据regex和replacement重命名src中的目录。" + gl(2)
    + CMD + CMD_REN_DIR_OLY_LOW + OPTIONS + "regex src [level]" + gl(1) + "根据regex将src中所有匹配的目录名中英文字母替换为小写。" + gl(2)
    + CMD + CMD_REN_DIR_OLY_UP + OPTIONS + "regex src [level]" + gl(1) + "根据regex将src中所有匹配的目录名中英文字母替换为大写。" + gl(2)
    + CMD + CMD_REN_DIR_OLY_UP_FST + OPTIONS + "regex src [level]" + gl(1) + "根据regex将src中所有匹配的目录名中英文单词首字母替换为大写。" + gl(2)
    + CMD + CMD_REP_FILE_BT + OPTIONS + "regex src replacement [split] [level]" + gl(1) + "根据regex和replacement替换src中所有匹配的二维表格式文件中所有匹配的列。" + gl(2)
    + CMD + CMD_REP_FILE_IL + OPTIONS + "regex src [dest] [level]" + gl(1) + "根据配置文件dest自动替换src中所有文件名匹配regex的文件内容；若不指定dest，则根据配置文件" + CONFIG_FILE_IL + "自动替换src中所有文件名匹配regex的文件内容，若配置文件" + CONFIG_FILE_IL + "不存在，则会自动生成一个与该文件同名且同格式的模版文件。" + gl(2)
    + CMD + CMD_COPY + OPTIONS + "regex src dest [level]" + gl(1) + "根据regex复制src中文件到dest中。" + gl(2)
    + CMD + CMD_CPY_DIR + OPTIONS + "regex src dest [level]" + gl(1) + "根据regex复制src中所有匹配文件和目录及其中所有文件到dest中。" + gl(2)
    + CMD + CMD_CPY_DIR_OLY + OPTIONS + "regex src dest [level]" + gl(1) + "根据regex复制src中所有匹配的目录及其中所有文件到dest中。" + gl(2)
    + CMD + CMD_DELETE + OPTIONS + "regex src [level]" + gl(1) + "根据regex删除src中所有匹配文件。" + gl(2)
    + CMD + CMD_DEL_DIR + OPTIONS + "regex src [level]" + gl(1) + "根据regex删除src中所有匹配文件和目录及其中所有文件。" + gl(2)
    + CMD + CMD_DEL_DIR_OLY + OPTIONS + "regex src [level]" + gl(1) + "根据regex删除src中所有匹配的目录及其中所有文件。" + gl(2)
    + CMD + CMD_DEL_NUL + OPTIONS + "regex src [level]" + gl(1) + "根据regex删除src中所有匹配的空文件。" + gl(2)
    + CMD + CMD_DEL_DIR_NUL + OPTIONS + "regex src [level]" + gl(1) + "根据regex删除src中所有匹配的空文件和空目录。" + gl(2)
    + CMD + CMD_DEL_DIR_OLY_NUL + OPTIONS + "regex src [level]" + gl(1) + "根据regex删除src中所有匹配的空目录。" + gl(2)
    + CMD + CMD_MOVE + OPTIONS + "regex src dest [level]" + gl(1) + "根据regex移动src中文件到dest中。" + gl(2)
    + CMD + CMD_MOV_DIR + OPTIONS + "regex src dest [level]" + gl(1) + "根据regex移动src中所有匹配文件和目录及其中所有文件到dest中。" + gl(2)
    + CMD + CMD_MOV_DIR_OLY + OPTIONS + "regex src dest [level]" + gl(1) + "根据regex移动src中所有匹配的目录及其中所有文件到dest中。" + gl(2)
    + CMD + CMD_ITCHG_UGD + OPTIONS + "regex src dest backup [level]" + gl(1) + "根据regex获得src中所有匹配文件，检查这些文件在dest中是否能找到文件名称是以该文件名称为前缀的文件，若存在则先将dest中匹配的文件移动到backup中，再将该文件移动到dest中。" + gl(2)
    + CMD + CMD_ITCHG_RST + OPTIONS + "regex src dest backup [level]" + gl(1) + "根据regex获得src中所有匹配文件，检查这些文件在dest中是否能找到文件名称是该文件名称的前缀的文件，若存在则先将dest中匹配的文件移动到backup中，再将该文件移动到dest中。" + gl(2)
    + CMD + CMD_UPGRADE + OPTIONS + "regex src dest backup [level]" + gl(1) + "根据regex将src中所有匹配文件更新到dest中，更新时会先检查dest中是否已存在该文件，若存在则先将该文件备份到backup中，再更新之。" + gl(2)
    + CMD + CMD_UGD_DIR + OPTIONS + "regex src dest backup [level]" + gl(1) + "根据regex将src中所有匹配文件和目录及其中所有文件更新到dest中，更新时会先检查dest中是否已存在该文件，若存在则先将该文件备份到backup中，再更新之。" + gl(2)
    + CMD + CMD_ZIP_DEF + OPTIONS + "regex src dest zipName [zipLevel] [level]" + gl(1) + "根据regex将src中所有匹配文件压缩到dest/zipName" + EXT_ZIP + "文件中。" + gl(2)
    + CMD + CMD_ZIP_DIR_DEF + OPTIONS + "regex src dest zipName [zipLevel] [level]" + gl(1) + "根据regex将src中所有匹配文件和目录及其中所有文件压缩到dest/zipName" + EXT_ZIP + "文件中。" + gl(2)
    + CMD + CMD_ZIP_INF + OPTIONS + "regex src dest [level]" + gl(1) + "根据regex将src中所有匹配文件解压缩到dest中。" + gl(2)
    + CMD + CMD_PAK_DEF + OPTIONS + "regex src dest zipName [zipLevel] [level]" + gl(1) + "根据regex将src中所有匹配文件打包到dest/zipName" + EXT_PAK + "文件中。" + gl(2)
    + CMD + CMD_PAK_DIR_DEF + OPTIONS + "regex src dest zipName [zipLevel] [level]" + gl(1) + "根据regex将src中所有匹配文件和目录及其中所有文件打包到dest/zipName" + EXT_PAK + "文件中。" + gl(2)
    + CMD + CMD_PAK_INF + OPTIONS + "regex src [level]" + gl(1) + "根据regex将src中所有匹配文件解包到该文件所在目录中。" + gl(2)
    + CMD + CMD_GUID_L32 + OPTIONS + "regex src [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件对应的36位GUID（英文字母全小写）。" + gl(2)
    + CMD + CMD_GUID_U32 + OPTIONS + "regex src [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件对应的36位GUID（英文字母全大写）。" + gl(2)
    + CMD + CMD_MD5_L16 + OPTIONS + "regex src [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件对应的16位MD5（英文字母全小写）。" + gl(2)
    + CMD + CMD_MD5_U16 + OPTIONS + "regex src [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件对应的16位MD5（英文字母全大写）。" + gl(2)
    + CMD + CMD_MD5_L32 + OPTIONS + "regex src [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件对应的32位MD5（英文字母全小写）。" + gl(2)
    + CMD + CMD_MD5_U32 + OPTIONS + "regex src [level]" + gl(1) + "根据regex查找src中的文件（同-f），显示文件对应的32位MD5（英文字母全大写）。" + gl(2)
    + CMD + CMD_JSON_ENC + OPTIONS + "regex src [level]" + gl(1) + "根据regex查找src中的文件（同-f），编码（即压缩为一行）JSON格式文件。" + gl(2)
    + CMD + CMD_JSON_DEC + OPTIONS + "regex src [level]" + gl(1) + "根据regex查找src中的文件（同-f），解码（即格式化）JSON格式文件。" + gl(3)
    + "单条命令示例：" + gl(2)
    + CMD + CMD_FIND + "+ (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\"" + gl(1) + "查询该目录中名称以_cn.strings（忽略大小写）结尾的所有文件，.与strings中间可以包含0到2个任意字符。" + gl(2)
    + CMD + CMD_FND_DIR + "+ (?i)strings$ \"F:/games/Fallout 4\"" + gl(1) + "查询该目录中名称以strings（忽略大小写）结尾的所有文件和目录及其中所有文件。" + gl(2)
    + CMD + CMD_FND_DIR_OLY + "+ . \"F:/games/KingdomComeDeliverance/修改/Mods\" 0 1" + gl(1) + "查询该目录中的第一级目录。" + gl(2)
    + CMD + CMD_FND_SAM + "+ \"F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\" \"D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\"" + gl(1) + "查询F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录中的所有文件；且只选取在D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录的同一相对路径中存在的同名文件。" + gl(2)
    + CMD + CMD_FND_DIR_SAM + "+ \"F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\" \"D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\"" + gl(1) + "查询F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录中的所有文件；且只选取在D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录的同一相对路径中存在的同名目录和文件。" + gl(2)
    + CMD + CMD_FND_DIR_OLY_SAM + "+ \"F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\" \"D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\"" + gl(1) + "查询F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录中的所有文件；且只选取在D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录的同一相对路径中存在的同名目录。" + gl(2)
    + CMD + CMD_FND_DIF + "+ \"F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\" \"D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\"" + gl(1) + "查询F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录中的所有文件；且只选取在D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录的同一相对路径中不存在的文件。" + gl(2)
    + CMD + CMD_FND_DIR_DIF + "+ \"F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\" \"D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\"" + gl(1) + "查询F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录中的所有文件；且只选取在D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录的同一相对路径中不存在的目录和文件。" + gl(2)
    + CMD + CMD_FND_DIR_OLY_DIF + "+ \"F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\" \"D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data\"" + gl(1) + "查询F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录中的所有文件；且只选取在D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data目录的同一相对路径中不存在的目录。" + gl(2)
    + CMD + CMD_FND_PTH_ABS + "+ . \"F:/games/DARK SOULS REMASTERED\" 20" + gl(1) + "查询该目录中的所有文件；显示文件的绝对路径名，且只显示前20条记录。" + gl(2)
    + CMD + CMD_FND_PTH_RLT + "+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的所有文件，显示文件的相对路径名。" + gl(2)
    + CMD + CMD_FND_PTH_DIR_ABS + "+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的文件和目录及其中所有文件，显示文件或目录的绝对路径名。" + gl(2)
    + CMD + CMD_FND_PTH_DIR_RLT + "+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的文件和目录及其中所有文件，显示文件或目录的相对路径名。" + gl(2)
    + CMD + CMD_FND_PTH_DIR_OLY_ABS + "+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的所有目录，显示目录的绝对路径名。" + gl(2)
    + CMD + CMD_FND_PTH_DIR_OLY_RLT + "+ . \"F:/games/DARK SOULS REMASTERED\"" + gl(1) + "查询该目录中的所有目录，显示目录的相对路径名。" + gl(2)
    + CMD + CMD_FND_SIZ_ASC + "+ . \"F:/games/FINAL FANTASY XV\" 1MB,1GB" + gl(1) + "查询该目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递增排序。" + gl(2)
    + CMD + CMD_FND_SIZ_DSC + "+ . \"F:/games/FINAL FANTASY XV\" 1MB;1GB" + gl(1) + "查询该目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递减排序。" + gl(2)
    + CMD + CMD_FND_DIR_SIZ_ASC + "+ \\Ajp$ \"F:/games/FINAL FANTASY XV\" 1MB,1GB" + gl(1) + "查询该目录中所有目录名为jp的目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递增排序。" + gl(2)
    + CMD + CMD_FND_DIR_SIZ_DSC + "+ \\Ajp$ \"F:/games/FINAL FANTASY XV\" 1MB;1GB" + gl(1) + "查询该目录中所有目录名为jp的目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递减排序。" + gl(2)
    + CMD + CMD_FND_DIR_DIR_SIZ_ASC + "+ \\Ajp$ \"F:/games/DARK SOULS REMASTERED\" 100KB;10MB" + gl(1) + "先查询该目录中的文件和第一级子目录，再按文件或目录大小递增排序。" + gl(2)
    + CMD + CMD_FND_DIR_DIR_SIZ_DSC + "+ \\Ajp$ \"F:/games/DARK SOULS REMASTERED\" 100KB;10MB" + gl(1) + "先查询该目录中的文件和第一级子目录，再按文件或目录大小递减排序。" + gl(2)
    + CMD + CMD_FND_DIR_OLY_SIZ_ASC + "+ \\Ajp$ \"F:/games/DARK SOULS REMASTERED\" 100KB;10MB" + gl(1) + "先查询该目录中的第一级子目录，再按目录大小递增排序。" + gl(2)
    + CMD + CMD_FND_DIR_OLY_SIZ_DSC + "+ \\Ajp$ \"F:/games/DARK SOULS REMASTERED\" 100KB;10MB" + gl(1) + "先查询该目录中的第一级子目录，再按目录大小递减排序。" + gl(2)
    + CMD + CMD_RENAME + " (.*_)(?i)cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\" $1en$2" + gl(1) + "先查询（作用同-f）再以en替换掉所有匹配文件名中的cn（其余字符不变）。" + gl(2)
    + CMD + CMD_REN_LOW + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\"" + gl(1) + "先查询（作用同-f）再将该目录中所有匹配文件名中英文字母替换为小写。" + gl(2)
    + CMD + CMD_REN_UP + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\"" + gl(1) + "先查询（作用同-f）再将该目录中所有匹配文件名中英文字母替换为大写。" + gl(2)
    + CMD + CMD_REN_UP_FST + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\"" + gl(1) + "先查询（作用同-f）再将该目录中所有匹配文件名中英单词首字母替换为大写。" + gl(2)
    + CMD + CMD_REN_DIR + " (.*_)(?i)cn(\\..{0,2}strings$) \"F:/games/Fallout 4\" $1en$2" + gl(1) + "先查询（作用同-fd）再以en替换掉所有匹配文件名和目录名中的cn（其余字符不变）。" + gl(2)
    + CMD + CMD_REN_DIR_LOW + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4\"" + gl(1) + "先查询（作用同-fd）再将该目录中所有匹配文件名和目录名中英文字母替换为小写。" + gl(2)
    + CMD + CMD_REN_DIR_UP + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4\"" + gl(1) + "先查询（作用同-fd）再将该目录中所有匹配文件名和目录名中英文字母替换为大写。" + gl(2)
    + CMD + CMD_REN_DIR_UP_FST + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4\"" + gl(1) + "先查询（作用同-fd）再将该目录中所有匹配文件名和目录名中英单词首字母替换为大写。" + gl(2)
    + CMD + CMD_REN_DIR_OLY + " (.*_)(?i)cn(\\..{0,2}strings$) \"F:/games/Fallout 4\" $1en$2" + gl(1) + "先查询（作用同-fd）再以en替换掉所有匹配的目录名中的cn（其余字符不变）。" + gl(2)
    + CMD + CMD_REN_DIR_OLY_LOW + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4\"" + gl(1) + "先查询（作用同-fd）再将该目录中所有匹配的目录名中英文字母替换为小写。" + gl(2)
    + CMD + CMD_REN_DIR_OLY_UP + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4\"" + gl(1) + "先查询（作用同-fd）再将该目录中所有匹配的目录名中英文字母替换为大写。" + gl(2)
    + CMD + CMD_REN_DIR_OLY_UP_FST + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4\"" + gl(1) + "先查询（作用同-fd）再将该目录中所有匹配的目录名中英单词首字母替换为大写。" + gl(2)
    + CMD + CMD_REP_FILE_BT + "* (?i)\\Atemp1\\.txt$ E:/Decompile/DLL-ildasm \"1##LOWER;;UPPER=>REPLACE(\\.,,_);;REGENROW(String INST_#1-1# = #DQM##1.1##DQM#;)\" \"\\t+\" 1" + gl(1)
    + "先查询（作用同-f）再对该目录中名称（忽略大小写）为temp1.txt的文件数据执行一系列有序的规则替换：" + gl(1)
    + "1、对每行的第1列数据执行原子规则：将英文字母全部替换为小写；" + gl(1)
    + "2、对每行的第1列数据执行复合规则：先将英文字母替换为大写，再将.替换为_；" + gl(1)
    + "3、对每行数据执行原子规则：将数据替换为String INST_#1-1# = #DQM##1.1##DQM#;；" + gl(1)
    + "例如：temp1.txt文件中有1行数据为：“Beq.S	如果两个值相等，则将控制转移到目标指令（短格式）。”，则执行命令后该文件数据变为：“String INST_BEQ_S = \"beq.s\"”。" + gl(2)
    + CMD + CMD_REP_FILE_BT + "* (?i)\\Atemp1\\.txt$ E:/Decompile/DLL-ildasm \"1##UPPER=>REPLACE(\\.,,_);;REGENROW(addInstruction(INST_#1-1#,#DQM##2.0##DQM#,#DQM=2#);)\" \"\\t+\" 1" + gl(1)
    + "先查询（作用同-f）再对该目录中名称（忽略大小写）为temp1.txt的文件数据执行一系列有序的规则替换：" + gl(1)
    + "1、对每行的第1列数据执行复合规则：先将英文字母替换为大写，再将.替换为_；" + gl(1)
    + "2、对每行数据执行原子规则：将数据替换为addInstruction(INST_#1-1#,#DQM##2.0##DQM#,#DQM=2#);；" + gl(1)
    + "例如：temp1.txt文件中有1行数据为：“Beq.S	如果两个值相等，则将控制转移到目标指令（短格式）。”，则执行命令后该文件数据变为：“addInstruction(INST_BEQ_S,\"如果两个值相等，则将控制转移到目标指令（短格式）。\",\"\");”。" + gl(2)
    + CMD + CMD_REP_FILE_IL + "* (?i)\\.il$ E:/Decompile/DLL-ildasm" + gl(1) + "根据配置文件" + CONFIG_FILE_IL + "自动替换E:/Decompile/DLL-ildasm目录中所有文件扩展名为.il的文件内容。" + gl(2)
    + CMD + CMD_REP_FILE_IL + "* (?i)\\.il$ E:/Decompile/DLL-ildasm E:/Decompile/DLL-ildasm/il.xml" + gl(1) + "根据配置文件il.xml自动替换E:/Decompile/DLL-ildasm目录中所有文件扩展名为.il的文件内容。" + gl(2)
    + CMD + CMD_COPY + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\" \"F:/games/Fallout 4/备份\"" + gl(1) + "先查询（作用同-f）再将 .../Strings 中所有匹配文件复制到 .../备份 目录中。" + gl(2)
    + CMD + CMD_CPY_DIR + " (?i).{0,2}strings$ \"F:/games/Fallout 4/Data\" \"F:/games/Fallout 4/备份\"" + gl(1) + "先查询（作用同-fd）再将 .../Data 中所有匹配文件和目录及其中所有文件复制到 .../备份 目录中。" + gl(2)
    + CMD + CMD_CPY_DIR_OLY + " (?i).{0,2}strings$ \"F:/games/Fallout 4/Data\" \"F:/games/Fallout 4/备份\"" + gl(1) + "先查询（作用同-fd）再将 .../Data 中所有匹配的目录及其中所有文件复制到 .../备份 目录中。" + gl(2)
    + CMD + CMD_DELETE + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\"" + gl(1) + "先查询（作用同-f）再删除该目录中所有匹配文件。" + gl(2)
    + CMD + CMD_DEL_DIR + " \"\\Ade$|\\Afr$|\\Aru$|\\Aus$\" \"F:/games/FINAL FANTASY XV\"" + gl(1) + "先查询（作用同-fd）再删除该目录中所有匹配文件和目录及其中所有文件。" + gl(2)
    + CMD + CMD_DEL_DIR_OLY + " \"\\Ade$|\\Afr$|\\Aru$|\\Aus$\" \"F:/games/FINAL FANTASY XV\"" + gl(1) + "先查询（作用同-fd）再删除该目录中所有匹配的目录及其中所有文件。" + gl(2)
    + CMD + CMD_DEL_NUL + " . \"F:/games/FINAL FANTASY XV\"" + gl(1) + "先查询（作用同-fd）再删除该目录中所有匹配的空文件。" + gl(2)
    + CMD + CMD_DEL_DIR_NUL + " . \"F:/games/FINAL FANTASY XV\"" + gl(1) + "先查询（作用同-fd）再删除该目录中所有匹配的空文件和空目录。" + gl(2)
    + CMD + CMD_DEL_DIR_OLY_NUL + " . \"F:/games/FINAL FANTASY XV\"" + gl(1) + "先查询（作用同-fd）再删除该目录中所有匹配的空目录。" + gl(2)
    + CMD + CMD_MOVE + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\" \"F:/games/Fallout 4/备份\"" + gl(1) + "先查询（作用同-f）再将 .../Strings中所有匹配文件移动到 .../备份 目录中。" + gl(2)
    + CMD + CMD_MOV_DIR + " (?i).{0,2}strings$ \"F:/games/Fallout 4/Data\" \"F:/games/Fallout 4/备份\"" + gl(1) + "先查询（作用同-fd）再将 .../Data 中所有匹配文件和目录及其中所有文件移动到 .../备份 目录中。" + gl(2)
    + CMD + CMD_MOV_DIR_OLY + " (?i).{0,2}strings$ \"F:/games/Fallout 4/Data\" \"F:/games/Fallout 4/备份\"" + gl(1) + "先查询（作用同-fd）再将 .../Data 中所有匹配的目录及其中所有文件移动到 .../备份 目录中。" + gl(2)
    + CMD + CMD_ITCHG_UGD + " . \"F:/games/Resident Evil 4/修改/BIO4\" \"F:/games/Resident Evil 4/BIO4\" \"F:/games/Resident Evil 4/备份/BIO4\"" + gl(1) + "先查询（作用同-f）获得 F:/games/Resident Evil 4/修改/BIO4 目录中所有匹配文件，检查这些文件在 F:/games/Resident Evil 4/BIO4 目录中是否能找到文件名称是以该文件名称为前缀的文件，若存在则先将 F:/games/Resident Evil 4/BIO4 目录中匹配的文件移动到 F:/games/Resident Evil 4/备份/BIO4 目录中，再将该文件移动到 F:/games/Resident Evil 4/BIO4 目录中。" + gl(2)
    + CMD + CMD_ITCHG_RST + " . \"F:/games/Resident Evil 4/备份/BIO4\" \"F:/games/Resident Evil 4/BIO4\" \"F:/games/Resident Evil 4/修改/BIO4\"" + gl(1) + "先查询（作用同-f）获得 F:/games/Resident Evil 4/备份/BIO4 目录中所有匹配文件，检查这些文件在 F:/games/Resident Evil 4/BIO4 目录中是否能找到文件名称是该文件名称的前缀的文件，若存在则先将 F:/games/Resident Evil 4/BIO4 目录中匹配的文件移动到 F:/games/Resident Evil 4/修改/BIO4 目录中，再将该文件移动到 F:/games/Resident Evil 4/BIO4 目录中。" + gl(2)
    + CMD + CMD_UPGRADE + " \"F:/games/FINAL FANTASY XV\" \"F:/迅雷下载/FINAL FANTASY XV\" \"F:/备份\"" + gl(1) + "先查询（作用同-f）再将 F:/games/FINAL FANTASY XV 目录中所有匹配文件更新到 F:/迅雷下载/FINAL FANTASY XV 中，若存在同名文件则先将该文件备份到 F:/备份 目录中，再更新之。" + gl(2)
    + CMD + CMD_UGD_DIR + " \\Adatas$ \"F:/games/FINAL FANTASY XV\" \"F:/迅雷下载/FINAL FANTASY XV\" \"F:/备份\"" + gl(1) + "先查询（作用同-fd）再将 F:/games/FINAL FANTASY XV 目录中所有匹配文件和目录及其中所有文件更新到 F:/迅雷下载/FINAL FANTASY XV 中，若存在同名文件则先将该文件备份到 F:/备份 目录中，再更新之。" + gl(2)
    + CMD + CMD_ZIP_DEF + " (?i)_cn(\\..{0,2}strings$) \"F:/games/Fallout 4/Data/Strings\" \"F:/games/Fallout 4/备份\" strings 1" + gl(1) + "先查询（作用同-f）再将 .../Strings 目录中所有匹配文件按压缩级别1压缩到 .../备份/strings" + EXT_ZIP + " 文件中。" + gl(2)
    + CMD + CMD_ZIP_DIR_DEF + " (?i).{0,2}strings$ \"F:/games/Fallout 4/Data\" \"F:/games/Fallout 4/备份\" strings 1" + gl(1) + "先查询（作用同-fd）再将 .../Data 目录中所有匹配文件和目录及其中所有文件按压缩级别1压缩到 .../备份/strings" + EXT_ZIP + " 文件中。" + gl(2)
    + CMD + CMD_ZIP_INF + " (?i)\\.zip$ \"F:/games/Fallout 4/备份\" \"F:/games/Fallout 4/Data\"" + gl(1) + "先查询（作用同-f）再将 .../备份 目录中所有匹配文件解压缩到 .../Data 目录中。" + gl(2)
    + CMD + CMD_PAK_DEF + " . \"F:/games/KingdomComeDeliverance/修改/Merge/Data\" \"F:/games/KingdomComeDeliverance/Mods/Merge/Data\" merge 1" + gl(1) + "先查询（作用同-f）再将 .../修改/Merge/Data 目录中所有匹配文件打包到 .../Mods/Merge/Data/merge" + EXT_PAK + " 文件中。" + gl(2)
    + CMD + CMD_PAK_DIR_DEF + " . \"F:/games/KingdomComeDeliverance/修改/Merge/Data\" \"F:/games/KingdomComeDeliverance/Mods/Merge/Data\" merge 1" + gl(1) + "先查询（作用同-fd）再将 .../修改/Merge/Data 目录中所有匹配文件和目录及其中所有文件打包到 .../Mods/Merge/Data/merge" + EXT_PAK + " 文件中。" + gl(2)
    + CMD + CMD_PAK_INF + " (?i)\\.pak$ \"F:/games/KingdomComeDeliverance/修改/Mods\"" + gl(1) + "先查询（作用同-f）再将 .../Mods 目录中所有匹配文件解包到该文件所在目录中。" + gl(2)   
    + CMD + CMD_GUID_L32 + "+ (?i)assembly-csharp\\.dll \"F:/games/Pathfinder Kingmaker/Kingmaker_Data/Managed\"" + gl(1) + "显示该目录中名称为Assembly-CSharp.dll的文件对应的36位GUID（英文字母全小写）。" + gl(2)
    + CMD + CMD_GUID_U32 + "+ (?i)assembly-csharp\\.dll \"F:/games/Pathfinder Kingmaker/Kingmaker_Data/Managed\"" + gl(1) + "显示该目录中名称为Assembly-CSharp.dll的文件对应的36位GUID（英文字母全大写）。" + gl(2)
    + CMD + CMD_MD5_L16 + "+ (?i)assembly-csharp\\.dll \"F:/games/Pathfinder Kingmaker/Kingmaker_Data/Managed\"" + gl(1) + "显示该目录中名称为Assembly-CSharp.dll的文件对应的16位MD5（英文字母全小写）。" + gl(2)
    + CMD + CMD_MD5_U16 + "+ (?i)assembly-csharp\\.dll \"F:/games/Pathfinder Kingmaker/Kingmaker_Data/Managed\"" + gl(1) + "显示该目录中名称为Assembly-CSharp.dll的文件对应的16位MD5（英文字母全大写）。" + gl(2)
    + CMD + CMD_MD5_L32 + "+ (?i)assembly-csharp\\.dll \"F:/games/Pathfinder Kingmaker/Kingmaker_Data/Managed\"" + gl(1) + "显示该目录中名称为Assembly-CSharp.dll的文件对应的32位MD5（英文字母全小写）。" + gl(2)
    + CMD + CMD_MD5_U32 + "+ (?i)assembly-csharp\\.dll \"F:/games/Pathfinder Kingmaker/Kingmaker_Data/Managed\"" + gl(1) + "显示该目录中名称为Assembly-CSharp.dll的文件对应的32位MD5（英文字母全大写）。" + gl(2)
    + CMD + CMD_JSON_ENC + " (?i)\\..*bundle$ \"g:/games/Pillars of Eternity II\"" + gl(1) + "查询该目录中名称以.bundle结尾（.与bundle之间可以包含0或多个字符）的所有文件，编码（即压缩为一行）JSON格式文件。" + gl(2)
    + CMD + CMD_JSON_DEC + " (?i)\\..*bundle$ \"g:/games/Pillars of Eternity II\"" + gl(1) + "查询该目录中名称以.bundle结尾（.与bundle之间可以包含0或多个字符）的所有文件，解码（即格式化）JSON格式文件。";
}
