package legend.util.entity;

import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.isEmpty;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IFileSPK;

@XmlRootElement(name = "STCFormat")
@XmlType(propOrder = {"headerInfo","bodyInfo","listInfo"})
public class STCFormat extends BaseEntity<STCFormat> implements IFileSPK{
    @XmlElement(name = "HeaderInfo")
    private SPKHeader headerInfo = new SPKHeader();
    @XmlElement(name = "BodyInfo")
    private SPKHeader bodyInfo = new SPKHeader();
    @XmlElement(name = "ListInfo")
    private SPKHeader listInfo = new SPKHeader();

    @Override
    public STCFormat trim(){
        headerInfo = headerInfo.trim();
        bodyInfo = bodyInfo.trim();
        listInfo = listInfo.trim();
        return this;
    }

    @Override
    public boolean validate(){
        if(isEmpty(headerInfo.getHeaderSize()) || isEmpty(headerInfo.getHeaderFlag())){
            errorInfo = gsph(ERR_SPKH_NODE_NON,N_STCF_HEADER_INFO);
            return false;
        }else if(isEmpty(bodyInfo.getHeaderSize()) || isEmpty(listInfo.getHeaderFlag()) || isEmpty(bodyInfo.getFileSizeExpr()) || isEmpty(bodyInfo.getFileStartPosExpr())){
            errorInfo = gsph(ERR_SPKH_NODE_NON,N_STCF_BODY_INFO);
            return false;
        }else if(isEmpty(listInfo.getHeaderSize()) || isEmpty(listInfo.getHeaderFlag())){
            errorInfo = gsph(ERR_SPKH_NODE_NON,N_STCF_LIST_INFO);
            return false;
        }else if(!headerInfo.validate()){
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
