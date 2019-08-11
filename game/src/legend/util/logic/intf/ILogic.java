package legend.util.logic.intf;

import legend.intf.ICommon;

public interface ILogic<T> extends ICommon{
    void execute(T t);
}
