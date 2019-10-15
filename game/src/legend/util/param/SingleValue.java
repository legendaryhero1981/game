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
        if(value instanceof IValue) return ((IValue<T>)value).cloneValue();
        return IValue.cloneValue(value);
    }

    @SuppressWarnings("unchecked")
    public SingleValue<T> clone(){
        return (SingleValue<T>)IValue.clone(this);
    }

    @Override
    public T getValue(){
        return value;
    }

    @Override
    public void setValue(T value){
        this.value = value;
    }

    public String toString(){
        return value.toString();
    }
}
