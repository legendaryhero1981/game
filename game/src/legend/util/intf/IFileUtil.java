package legend.util.intf;

import static legend.intf.ICommon.gl;

import legend.intf.ICommon;

public interface IFileUtil extends ICommon{
    int RECURSION_LEVEL = 100;
    int PROGRESS_POSITION = 50;
    float PROGRESS_SCALE = (100 - PROGRESS_POSITION) / 100f;
    String RES_LEVEL = "100";
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
    String CMD_FND_PTH_DIR_OLY_SRC = "-fpdos";
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
    String FILE_LOG = "./file.log";
    String REG_FLE_SIZ = "(0|[1-9]\\d*)([TGMKtgmk]?[Bb])?[,;-]?+";
    String REG_REN_UP_FST = "[a-zA-Z]\\w*";
    String ST_ASK_CONT = "输入n或N跳过，否则继续，按回车键确认：";
    String ERR_DIR_VST = V_VST + N_DIR + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_VST = V_VST + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_DEL = V_DEL + N_FLE + S_SPACE + PH_ARG0 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG1;
    String ERR_FLE_CPY = V_CPY + N_FLE + S_SPACE + PH_ARG0 + V_TO + PH_ARG1 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG2;
    String ERR_FLE_MOV = V_MOV + N_FLE + S_SPACE + PH_ARG0 + V_TO + PH_ARG1 + S_SPACE + V_FAIL + N_ERR_INFO + PH_ARG2;
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
}
