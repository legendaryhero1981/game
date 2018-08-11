package legend.intf;

@FunctionalInterface
public interface IValue<T>extends Cloneable{
    T cloneValue();

    static Object clone(Object o){
        Object object = null;
        Class<?> c = o.getClass();
        try{
            object = c.newInstance();
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
