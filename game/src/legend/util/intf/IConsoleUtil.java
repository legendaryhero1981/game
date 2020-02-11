package legend.util.intf;

import legend.intf.ICommon;

public interface IConsoleUtil extends ICommon{
    enum UnitType{
        NON,B,KB,MB,GB,TB
    }

    int UNIT_SIZE = 0x400;
    String SIZE_TB = "TB";
    String SIZE_GB = "GB";
    String SIZE_MB = "MB";
    String SIZE_KB = "KB";
    String SIZE_B = "B";
}
