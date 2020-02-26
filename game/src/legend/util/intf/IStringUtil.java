package legend.util.intf;

import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

import legend.intf.ICommon;

public interface IStringUtil extends ICommon{
    String REG_PATH_URL = "\\A(?:.*?/+)(.+/.+?)(?:/.+)";
    String REG_PATH_JAR = "(.+/.+?)(?:/.+!.+)";
    String REP_PATH_URL = "$1";
    Pattern PTRN_PATH_NAME = compile(REG_PATH_NAME);
}
