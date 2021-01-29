package legend.util.intf;

import legend.intf.ICommon;

public interface ITimeUtil extends ICommon{
    int RADIX_MINUTE = 60;
    int RADIX_SECOND = 60;
    int RADIX_MILLI = 1000;
    String UNIT_HOUR = "时";
    String UNIT_MINUTE = "分";
    String UNIT_SECOND = "秒";
    String UNIT_MILLI = "毫秒";
}
