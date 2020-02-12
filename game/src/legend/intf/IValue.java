package legend.intf;

import static legend.util.ConsoleUtil.CS;
import static legend.util.StringUtil.gsph;

public interface IValue<T>extends ICommon{
    default T cloneValue(){
        return null;
    }

    default T getValue(){
        return null;
    }

    default void setValue(T t){}

    default T trim(){
        return getValue();
    }

    default boolean validate(){
        return true;
    }

    default <V> boolean validate(V v){
        return true;
    }
    
    static <T> T cloneValue(T t){
        return (T)clone(t);
    }

    @SuppressWarnings("unchecked")
    static <V> V clone(V v){
        return (V)newInstance(v.getClass());
    }

    static <T> T newInstance(Class<T> c){
        try{
            return c.getDeclaredConstructor().newInstance((Object[])null);
        }catch(Exception e){
            CS.sl(gsph(ERR_INFO,e.toString()));
        }
        return null;
    }
}
