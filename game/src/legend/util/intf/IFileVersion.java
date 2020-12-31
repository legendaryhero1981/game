package legend.util.intf;

import java.util.List;

import legend.intf.ICommon;

public interface IFileVersion<T,R> extends ICommon{
    String REG_FILE_VER = "(.*)" + PH_ARGS0 + "(\\d+\\..*)";
    String REG_FILE_VER_DEF = "(.*?)(\\d+\\..*)";
    
    public List<List<R>> getSortedFileVersions(T t);
}
