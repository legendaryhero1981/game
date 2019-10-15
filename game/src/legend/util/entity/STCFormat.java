package legend.util.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IFileSPK;

@XmlRootElement(name = "STCFormat")
@XmlType(propOrder = {"HeaderInfo","BodyInfo","ListInfo"})
public class STCFormat extends BaseEntity<STCFormat> implements IFileSPK{
    @XmlElement(name = "HeaderInfo")
    private SPKHeader headerInfo;
    @XmlElement(name = "BodyInfo")
    private SPKHeader bodyInfo;
    @XmlElement(name = "ListInfo")
    private SPKHeader listInfo;

    @Override
    public STCFormat trim(){
        headerInfo = headerInfo.trim();
        bodyInfo = bodyInfo.trim();
        listInfo = listInfo.trim();
        return this;
    }

    @Override
    public boolean validate(){
        if(!headerInfo.validate()){
            errorInfo = headerInfo.errorInfo;
            return false;
        }else if(!bodyInfo.validate()){
            errorInfo = bodyInfo.errorInfo;
            return false;
        }else if(!listInfo.validate()){
            errorInfo = listInfo.errorInfo;
            return false;
        }else return true;
    }

    public SPKHeader getHeaderInfo(){
        return headerInfo;
    }

    public SPKHeader getBodyInfo(){
        return bodyInfo;
    }

    public SPKHeader getListInfo(){
        return listInfo;
    }
}
