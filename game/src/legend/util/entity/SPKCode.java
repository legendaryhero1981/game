package legend.util.entity;

import static legend.util.ValueUtil.isEmpty;
import static legend.util.param.FileParam.convertParam;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IFileSPK;

@XmlRootElement(name = "SPKCode")
@XmlType(propOrder = {"unpackPath","repackPath","filePath","fileName","queryRegex","stcFormat","spkFormat"})
public class SPKCode extends BaseEntity<SPKCode> implements IFileSPK{
    @XmlElement
    private String unpackPath = S_EMPTY;
    @XmlElement
    private String repackPath = S_EMPTY;
    @XmlElement
    private String filePath = S_EMPTY;
    @XmlElement
    private String fileName = S_EMPTY;
    @XmlElement
    private String queryRegex = REG_ANY;
    @XmlElement(name = "STCFormat")
    private STCFormat stcFormat = new STCFormat();
    @XmlElement(name = "SPKFormat")
    private SPKFormat spkFormat = new SPKFormat();

    @Override
    public SPKCode trim(){
        unpackPath = unpackPath.strip();
        repackPath = repackPath.strip();
        filePath = filePath.strip();
        fileName = fileName.strip();
        queryRegex = queryRegex.strip();
        spkFormat = spkFormat.trim();
        stcFormat = stcFormat.trim();
        return this;
    }

    @Override
    public boolean validate(){
        if(isEmpty(unpackPath) || isEmpty(repackPath) || isEmpty(filePath) || isEmpty(fileName) || isEmpty((queryRegex = convertParam(queryRegex,true)))){
            errorInfo = ERR_SPKC_NODE_NUL;
            return false;
        }else if(repackPath.equalsIgnoreCase(filePath)){
            errorInfo = ERR_SPKC_PATH_SAME;
            return false;
        }else if(!stcFormat.validate()){
            errorInfo = stcFormat.errorInfo;
            return false;
        }else if(!spkFormat.validate()){
            errorInfo = spkFormat.errorInfo;
            return false;
        }else return true;
    }

    public String getUnpackPath(){
        return unpackPath;
    }

    public String getRepackPath(){
        return repackPath;
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

    public STCFormat getStcFormat(){
        return stcFormat;
    }

    public SPKFormat getSpkFormat(){
        return spkFormat;
    }
}
