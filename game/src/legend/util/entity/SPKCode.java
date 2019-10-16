package legend.util.entity;

import static legend.util.ValueUtil.isEmpty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IFileSPK;

@XmlRootElement(name = "SPKCode")
@XmlType(propOrder = {"unpackPath","filePath","fileName","queryRegex","SPKFormat","STCFormat"})
public class SPKCode extends BaseEntity<SPKCode> implements IFileSPK{
    @XmlElement
    private String unpackPath = S_EMPTY;
    @XmlElement
    private String filePath = S_EMPTY;
    @XmlElement
    private String fileName = S_EMPTY;
    @XmlElement
    private String queryRegex = REG_ANY;
    @XmlElement(name = "SPKFormat")
    private SPKFormat spkFormat;
    @XmlElement(name = "STCFormat")
    private STCFormat stcFormat;

    @Override
    public SPKCode trim(){
        unpackPath = unpackPath.trim();
        filePath = filePath.trim();
        fileName = fileName.trim();
        queryRegex = queryRegex.trim();
        spkFormat = spkFormat.trim();
        stcFormat = stcFormat.trim();
        return this;
    }

    @Override
    public boolean validate(){
        if(isEmpty(unpackPath) || isEmpty(filePath) || isEmpty(fileName) || isEmpty(queryRegex)){
            errorInfo = ERR_CONF_SPKC_NON;
            return false;
        }else if(!spkFormat.validate()){
            errorInfo = spkFormat.errorInfo;
            return false;
        }else if(!stcFormat.validate()){
            errorInfo = stcFormat.errorInfo;
            return false;
        }else return true;
    }

    public String getUnpackPath(){
        return unpackPath;
    }

    public String getFilePath(){
        return filePath;
    }

    public String getFileName(){
        return fileName;
    }

    public String getQueryRegex(){
        return queryRegex;
    }

    public SPKFormat getSpkFormat(){
        return spkFormat;
    }

    public STCFormat getStcFormat(){
        return stcFormat;
    }
}
