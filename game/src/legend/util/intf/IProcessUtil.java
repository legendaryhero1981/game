package legend.util.intf;

import legend.intf.ICommon;

public interface IProcessUtil extends ICommon{
    String ERR_EXEC_PROC = V_EXEC + N_PROC + V_FAIL + S_BANG + PH_ARGS0;
    String ERR_DEAL_PROC = V_DEAL + N_PROC + V_FAIL + S_BANG + ERR_INFO;
}
