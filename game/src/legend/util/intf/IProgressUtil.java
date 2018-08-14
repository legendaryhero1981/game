package legend.util.intf;

import legend.intf.ICommon;

public interface IProgressUtil extends ICommon{
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
}
