package legend.util.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IFileSPK;

@XmlRootElement(name = "FileSPK")
@XmlType(propOrder = {"comment","codes"})
public class FileSPK extends BaseEntity<FileSPK> implements IFileSPK{
    @XmlElement
    private String comment = FILE_SPK_COMMENT;
    @XmlElementRef
    private List<SPKCode> codes;

    public FileSPK(){
        codes = new ArrayList<>();
        codes.add(new SPKCode());
    }

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
