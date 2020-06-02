package legend.util.entity;

import static legend.util.ValueUtil.isEmpty;

import java.util.regex.Matcher;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import legend.util.entity.intf.IDSRP;

@XmlRootElement(name = "DSRPTask")
@XmlType(propOrder = {"mode","queryPath","queryLevel","dataFileRegex","dcxFileRegex"})
public class DSRPTask extends BaseEntity<DSRPTask> implements IDSRP{
    @XmlElement
    private String mode = MODE_ZIP;
    @XmlElement
    private String queryPath = S_EMPTY;
    @XmlElement
    private String queryLevel = S_EMPTY;
    @XmlElement
    private String dataFileRegex = REG_DSRP_DATA;
    @XmlElement
    private String dcxFileRegex = REG_DSRP_DCX;
    @XmlTransient
    private int level = Integer.MAX_VALUE;

    @Override
    public DSRPTask trim(){
        mode = mode.strip();
        queryPath = queryPath.strip();
        queryLevel = queryLevel.strip();
        dataFileRegex = dataFileRegex.strip();
        dcxFileRegex = dcxFileRegex.strip();
        return this;
    }

    @Override
    public boolean validate(){
        if(isEmpty(mode) || isEmpty(queryPath) || isEmpty(dataFileRegex) || isEmpty(dcxFileRegex)){
            errorInfo = ERR_DSRP_TASK_NUL;
            return false;
        }
        Matcher matcher = PTRN_DSRP_MODE.matcher(mode);
        if(!matcher.matches()) mode = MODE_ZIP;
        matcher = PTRN_NUM_NATURAL.matcher(queryLevel);
        if(isEmpty(queryLevel) || !matcher.matches()) level = Integer.MAX_VALUE;
        else level = Integer.valueOf(queryLevel);
        return true;
    }

    public String getMode(){
        return mode;
    }

    public String getQueryPath(){
        return queryPath;
    }

    public String getQueryLevel(){
        return queryLevel;
    }

    public String getDataFileRegex(){
        return dataFileRegex;
    }

    public String getDcxFileRegex(){
        return dcxFileRegex;
    }

    public int getLevel(){
        return level;
    }
}
