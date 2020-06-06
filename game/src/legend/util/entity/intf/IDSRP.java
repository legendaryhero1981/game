package legend.util.entity.intf;

import static java.util.regex.Pattern.compile;
import static legend.util.StringUtil.getAppPath;
import static legend.util.StringUtil.gs;

import java.util.regex.Pattern;

import legend.util.intf.IFileUtil;

public interface IDSRP extends IFileUtil{
    String REG_DSRP_MODE = MODE_ZIP + "|" + MODE_UNZIP;
    String REG_DSRP_DATA_REPACK = ".*(?i)`.`.*(?:`bnd`|`tpf`)$|(?:`.fmg.xml`)$";
    String REG_DSRP_DCX_REPACK = "(?i)`.`.*(?:`undcx`|`bnd`|`tpf`)$";
    String REG_DSRP_DATA_UNPACK = ".*(?i)`.`.*(?:`bnd`|`tpf`|`fmg`|`luagnl`|`luainfo`)$";
    String REG_DSRP_DCX_UNPACK = "(?i)`.dcx`$";
    String REG_DSRP_DOT = "\\.";
    String REP_DSRP_HYPHEN = "-";
    String DATA_REPACKER = "DSDataRepacker";
    String DCX_REPACKER = "DSDCXRepacker";
    String CONTEXT_PATH = getAppPath() + "/tools/DSRP/";
    String DATA_EXEC_PATH = CONTEXT_PATH + DATA_REPACKER + EXT_EXE;
    String DCX_EXEC_PATH = CONTEXT_PATH + DCX_REPACKER + EXT_EXE;
    String N_DSRP_CONF = "《黑暗之魂》系列游戏数据文件解包打包处理配置文件";
    String ST_FILE_DSRP_CONF = V_GNRT + N_DSRP_CONF + S_DQM + CONF_FILE_DSRP + S_DQM + S_BANG;
    String ERR_DSRP_EXEC_NON = DATA_REPACKER + N_OR + DCX_REPACKER + N_A + N_EXEC_PATH + V_NON_EXISTS + S_BANG;
    String ERR_DSRP_EXEC_NUL = "DSRP节点下的dataRepackerExecutablePath或dcxRepackerExecutablePath子节点值" + V_BY_NUL + S_BANG;
    String ERR_DSRP_TASK_NUL = "DSRP::DSRPTask节点下的mode或queryPath或dataFileRegex或dcxFileRegex子节点值" + V_BY_NUL + S_BANG;
    String FILE_DSRP_COMMENT = "\n" + gs(4) + "DSPR配置节点结构说明：\n"
    + gs(4) + "DSRP节点由comment、dataRepackerExecutablePath、dcxRepackerExecutablePath、DSRPTask节点按顺序组成，comment节点必须在最前面，DSRPTask节点可以有多个。\n"
    + gs(4) + "DSRP::comment" + gs(23) + "DSRP配置节点结构说明，对执行文件解包打包任务无影响，仅此说明而已。\n"
    + gs(4) + "DSRP::dataRepackerExecutablePath" + gs(4) + "黑暗之魂系列游戏数据文件解包打包工具" + DATA_REPACKER + "的可执行文件路径名，用来与DSRP::dcxRepackerExecutablePath联合执行每一个DSRP::DSRPTask任务。\n"
    + gs(4) + "DSRP::dcxRepackerExecutablePath" + gs(5) + "黑暗之魂系列游戏数据文件DCX专用打包解包工具" + DCX_REPACKER + "的可执行文件路径名，用来与DSRP::dataRepackerExecutablePath联合执行每一个DSRP::DSRPTask任务。\n"
    + gs(4) + "DSRP::DSRPTask" + gs(22) + "DSRPTask任务对象，包含了执行每一条文件解包打包命令所需的全部参数。\n"
    + gs(4) + "DSRPTask节点由节点mode、queryPath、queryLevel、dataFileRegex、dcxFileRegex按顺序组成。\n"
    + gs(4) + "DSRPTask::mode" + gs(22) + "文件解包打包模式，取值范围为：0,1；取0表示打包，取1表示解解包；若指定的值超过取值范围程序会取默认值0。\n"
    + gs(4) + "DSRPTask::queryPath" + gs(17) + "文件查询目录，即待解包打包的文件所在的根目录。\n"
    + gs(4) + "DSRPTask::queryLevel" + gs(16) + "文件目录最大查询层数；取值范围为：1~" + Integer.MAX_VALUE + "，不指定或超过取值范围则取默认值" + Integer.MAX_VALUE + "。\n"
    + gs(4) + "DSRPTask::dataFileRegex" + gs(13) + DATA_REPACKER + "需要处理的文件名正则查询表达式，程序只会处理匹配的文件。\n"
    + gs(4) + "DSRPTask::dcxFileRegex" + gs(14) + DCX_REPACKER + "需要处理的文件名正则查询表达式，程序只会处理匹配的文件。\n"
    + gs(4) + "特别说明：通常情况下，对于每一个DSRPTask节点，dataFileRegex和dcxFileRegex子节点的值受mode子节点值影响；当mode值为0时，dataFileRegex默认值为“" + REG_DSRP_DATA_REPACK + "”，dcxFileRegex默认值为“" + REG_DSRP_DCX_REPACK + "”；当mode值为1时，dataFileRegex默认值为“" + REG_DSRP_DATA_UNPACK + "”，dcxFileRegex默认值为“" + REG_DSRP_DCX_UNPACK + "”；鉴于程序是并发调用外部程序执行任务的，为了避免读写文件冲突，对于每一个mode子节点值不同的DSRPTask节点，应该确保queryPath子节点值不同。\n" + gs(4);
    Pattern PTRN_DSRP_MODE = compile(REG_DSRP_MODE);
}
