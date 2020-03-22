package legend.util.intf;

import legend.intf.ICommon;

public interface IMD5Util extends ICommon{
    int BUFFER_SIZE = 0x8000;
    String N_MD5 = "MD5编码";
    String ERR_MD5_CRT = V_CRT + N_MD5 + V_FAIL + S_BANG + N_ERR_INFO + PH_ARGS0;

    enum Mode{
        CN8,CN16,CN32
    }
}
