package legend.util.entity.intf;

import static legend.util.StringUtil.getAppPath;
import static legend.util.StringUtil.gs;

import legend.util.intf.IFileUtil;

public interface IZip7 extends IFileUtil{
    long ZIP7_VOL_SIZE_DEF = 1l << 30;
    String REG_ZIP7_MODE = MODE_ZIP + "|" + MODE_UNZIP;
    String REG_ZIP7_COMP = "[013579]";
    String REG_ZIP7_VOL = "([1-9]\\d*)([bkmgBKMG])";
    String ZIP7_ARG_ZIP = "a";
    String ZIP7_ARG_UNZIP = "x";
    String ZIP7_ARG_LIST_FILE = "@";
    String ZIP7_ARG_YES_ALL = "-y";
    String ZIP7_ARG_OUT = "-o";
    String ZIP7_ARG_PW = "-p";
    String ZIP7_ARG_SPF = "-spf2";
    String ZIP7_ARG_VOL = "-v";
    String ZIP7_ARG_VOL_DEF = ZIP7_ARG_VOL + "1g";
    String ZIP7_ARG_COMP = "-mx";
    String ZIP7_VAL_COMP_DEF = "9";
    String ZIP7_ARG_COMP_DEF = ZIP7_ARG_COMP + ZIP7_VAL_COMP_DEF;
    String ZIP7_ARG_SFX = "-sfx";
    String ZIP7_ARG_SFX_GUI = ZIP7_ARG_SFX + "7z.sfx";
    String ZIP7_ARG_SFX_CON = ZIP7_ARG_SFX + "7zCon.sfx";
    String ZIP7_EXEC_PATH = getAppPath() + "/tools/7-Zip/7z.exe";
    String N_ZIP7_CONF = "7-Zip任务处理配置文件";
    String ST_FILE_SPK_CONF = V_GNRT + N_ZIP7_CONF + S_DQM + CONF_FILE_7ZIP + S_DQM + S_BANG;
    String ERR_ZIP7_EXEC_NON = "Zip7节点下的zip7ExecutablePath子节点值" + V_BY_NUL;
    String ERR_ZIP7_TASK_NON = "Zip7::Zip7Task节点下的queryRegex或queryPath或mode子节点值" + V_BY_NUL;
    String FILE_ZIP7_COMMENT = "\n" + gs(4) + "Zip7配置节点结构说明：\n"
    + gs(4);
}
