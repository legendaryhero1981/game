package legend.util.intf;

import static java.util.regex.Pattern.compile;

import java.util.Queue;
import java.util.regex.Pattern;

import legend.intf.ICommon;
import legend.util.param.FileParam;
import legend.util.param.FileVersion;

public interface IFileVersion extends ICommon{
    String REG_FILE_VER = "(.*)" + PH_ARGS0 + "(.*)";
    Pattern PTRN_FILE_VER = compile(REG_FILE_VER);
    
    public Queue<Queue<FileVersion>> getSortedFileVersions(FileParam param);
}
