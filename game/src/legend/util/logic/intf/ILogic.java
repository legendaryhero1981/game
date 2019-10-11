package legend.util.logic.intf;

@FunctionalInterface
public interface ILogic<T>{
    void execute(T t);
}
