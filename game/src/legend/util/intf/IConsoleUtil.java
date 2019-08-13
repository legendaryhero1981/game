package legend.util.intf;

import legend.intf.ICommon;

public interface IConsoleUtil extends ICommon{
    enum UNIT_TYPE{
        NON,B,KB,MB,GB,TB
    }

    int UNIT_SIZE = 0x400;
    String SIZE_TB = "TB";
    String SIZE_GB = "GB";
    String SIZE_MB = "MB";
    String SIZE_KB = "KB";
    String SIZE_B = "B";
    String ERR_FILE_MERGE = V_EXEC + N_FILE_MERGE + N_CMD + V_FAIL + N_ERR_INFO + PH_ARG0;
}
