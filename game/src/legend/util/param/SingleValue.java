package legend.util.param;

import legend.intf.IValue;

public class SingleValue<T> implements IValue<T>{
    private T value;

    public SingleValue(T value){
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T cloneValue(){
        if(this.value instanceof IValue) return ((IValue<T>)this.value).cloneValue();
        return IValue.cloneValue(value);
    }

    @SuppressWarnings("unchecked")
    public SingleValue<T> clone(){
        return (SingleValue<T>)IValue.clone(this);
    }

    public T get(){
        return value;
    }

    public void set(T value){
        this.value = value;
    }
}
