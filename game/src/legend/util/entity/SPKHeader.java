package legend.util.entity;

import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.nonEmpty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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
    @XmlTransient
    protected int size;

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
        if(nonEmpty(headerSize) && !PTN_SPK_SIZE.matcher(headerSize).matches()){
            errorInfo = gsph(ERR_CONF_SPKH_EXPR,"headerSize");
            return false;
        }else if(nonEmpty(filePathExpr) && !PTN_SPK_SIZE_EXPR.matcher(filePathExpr).matches()){
            errorInfo = gsph(ERR_CONF_SPKH_EXPR,"filePathExpr");
            return false;
        }else if(nonEmpty(fileSizeExpr) && !PTN_SPK_SIZE_EXPR.matcher(fileSizeExpr).matches()){
            errorInfo = gsph(ERR_CONF_SPKH_EXPR,"fileSizeExpr");
            return false;
        }else if(nonEmpty(fileStartPosExpr) && !PTN_SPK_SIZE_EXPR.matcher(fileStartPosExpr).matches()){
            errorInfo = gsph(ERR_CONF_SPKH_EXPR,"fileStartPosExpr");
            return false;
        }
        size = Integer.parseInt(headerSize);
        return true;
    }

    public int getSize(){
        return size;
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
