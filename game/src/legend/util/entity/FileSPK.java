package legend.util.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IFileSPK;

@XmlRootElement(name = "FileSPK")
@XmlType(propOrder = {"comment","SPKCode"})
public class FileSPK extends BaseEntity<FileSPK> implements IFileSPK{
    @XmlElement
    private String comment = FILE_SPK_COMMENT;
    @XmlElementRef
    private List<SPKCode> codes = new ArrayList<>();

    @Override
    public FileSPK trim(){
        codes.parallelStream().forEach(c->c.trim());
        return this;
    }

    @Override
    public boolean validate(){
        if(codes.parallelStream().anyMatch(c->{
            if(!c.validate()){
                errorInfo = c.errorInfo;
                return true;
            }
            return false;
        })) return false;
        return true;
    }

    public List<SPKCode> getCodes(){
        return codes;
    }
}
