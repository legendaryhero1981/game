package legend.util.entity;

import jakarta.xml.bind.annotation.XmlTransient;

import legend.intf.ICommon;
import legend.intf.IValue;

public abstract class BaseEntity<T> implements ICommon,IValue<T>{
    @XmlTransient
    protected String errorInfo = S_EMPTY;

    public String getErrorInfo(){
        return errorInfo;
    }
}
