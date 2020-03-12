package legend.util.intf;

import legend.intf.ICommon;

public interface IStringUtil extends ICommon{
    String REG_PATH_URL = "\\A(?:.*?/+)(.+/.+?)(?:/.+)";
    String REG_PATH_JAR = "(.+/.+?)(?:/.+!.+)";
    String REP_PATH_URL = "$1";
    String REP_SPRT_PKG = "\\.";
}
