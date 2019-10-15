package legend.util.entity;

import static legend.util.ValueUtil.isEmpty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IFileSPK;

@XmlRootElement(name = "SPKHeader")
@XmlType(propOrder = {"headerSize","headerFlag","filePathExpr","fileSizeExpr","fileStartPosExpr"})
public class SPKHeader extends BaseEntity<SPKHeader> implements IFileSPK{
    @XmlElement
    private String headerSize;
    @XmlElement
    private String headerFlag;
    @XmlElement
    private String filePathExpr;
    @XmlElement
    private String fileSizeExpr;
    @XmlElement
    private String fileStartPosExpr;

    @Override
    public SPKHeader trim(){
        headerFlag = headerFlag.trim();
        headerSize = headerSize.trim();
        filePathExpr = filePathExpr.trim();
        fileSizeExpr = fileSizeExpr.trim();
        fileStartPosExpr = fileStartPosExpr.trim();
        return this;
    }

    @Override
    public boolean validate(){
        if(isEmpty(headerSize)){
            errorInfo = S_EMPTY;
            return false;
        }
        return true;
    }

    public String getHeaderSize(){
        return headerSize;
    }

    public String getHeaderFlag(){
        return headerFlag;
    }

    public String getFilePathExpr(){
        return filePathExpr;
    }

    public String getFileSizeExpr(){
        return fileSizeExpr;
    }

    public String getFileStartPosExpr(){
        return fileStartPosExpr;
    }
}
