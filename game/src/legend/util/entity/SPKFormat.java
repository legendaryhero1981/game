package legend.util.entity;

import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.isEmpty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IFileSPK;

@XmlRootElement(name = "SPKFormat")
@XmlType(propOrder = {"bodyInfo","listInfo","tailInfo"})
public class SPKFormat extends BaseEntity<SPKFormat> implements IFileSPK{
    @XmlElement(name = "BodyInfo")
    private SPKHeader bodyInfo = new SPKHeader();
    @XmlElement(name = "ListInfo")
    private SPKHeader listInfo = new SPKHeader();
    @XmlElement(name = "TailInfo")
    private SPKHeader tailInfo = new SPKHeader();

    @Override
    public SPKFormat trim(){
        bodyInfo = bodyInfo.trim();
        listInfo = listInfo.trim();
        tailInfo = tailInfo.trim();
        return this;
    }

    @Override
    public boolean validate(){
        if(isEmpty(bodyInfo.getHeaderSize()) || isEmpty(bodyInfo.getHeaderFlag()) || isEmpty(bodyInfo.getFilePathExpr()) || isEmpty(bodyInfo.getFileSizeExpr())){
            errorInfo = gsph(ERR_CONF_SPKH_NODE_NON,N_SPKF_BODY_INFO);
            return false;
        }else if(isEmpty(listInfo.getHeaderSize()) || isEmpty(listInfo.getHeaderFlag()) || isEmpty(listInfo.getFilePathExpr()) || isEmpty(listInfo.getFileSizeExpr())){
            errorInfo = gsph(ERR_CONF_SPKH_NODE_NON,N_SPKF_LIST_INFO);
            return false;
        }else if(isEmpty(tailInfo.getHeaderSize()) || isEmpty(tailInfo.getHeaderFlag())){
            errorInfo = gsph(ERR_CONF_SPKH_NODE_NON,N_SPKF_TAIL_INFO);
            return false;
        }else if(!bodyInfo.validate()){
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
