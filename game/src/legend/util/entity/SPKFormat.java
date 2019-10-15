package legend.util.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IFileSPK;

@XmlRootElement(name = "SPKFormat")
@XmlType(propOrder = {"BodyInfo","ListInfo","TailInfo"})
public class SPKFormat extends BaseEntity<SPKFormat> implements IFileSPK{
    @XmlElement(name = "BodyInfo")
    private SPKHeader bodyInfo;
    @XmlElement(name = "ListInfo")
    private SPKHeader listInfo;
    @XmlElement(name = "TailInfo")
    private SPKHeader tailInfo;

    @Override
    public SPKFormat trim(){
        bodyInfo = bodyInfo.trim();
        listInfo = listInfo.trim();
        tailInfo = tailInfo.trim();
        return this;
    }

    @Override
    public boolean validate(){
        if(!bodyInfo.validate()){
            errorInfo = bodyInfo.errorInfo;
            return false;
        }else if(!listInfo.validate()){
            errorInfo = listInfo.errorInfo;
            return false;
        }else if(!tailInfo.validate()){
            errorInfo = tailInfo.errorInfo;
            return false;
        }else return true;
    }

    public SPKHeader getBodyInfo(){
        return bodyInfo;
    }

    public SPKHeader getListInfo(){
        return listInfo;
    }

    public SPKHeader getTailInfo(){
        return tailInfo;
    }
}
