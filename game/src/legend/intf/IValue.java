package legend.intf;

import java.util.function.Supplier;

public interface IValue<T>extends Cloneable{
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

    default boolean validate(Supplier<Object> supplier){
        return true;
    }

    static Object clone(Object o){
        Object object = null;
        Class<?> c = o.getClass();
        try{
            object = c.getDeclaredConstructor().newInstance((Object[])null);
        }catch(Exception e){
            e.printStackTrace();
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    static <T> T cloneValue(T t){
        return (T)clone(t);
    }
}
