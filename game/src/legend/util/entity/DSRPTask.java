package legend.util.entity;

import static java.nio.file.Paths.get;
import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.param.FileParam.convertParam;

import java.util.regex.Matcher;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import legend.util.entity.intf.IDSRP;
import legend.util.param.FileParam;

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
    private String dataFileRegex = S_EMPTY;
    @XmlElement
    private String dcxFileRegex = S_EMPTY;
    @XmlTransient
    private int level = Integer.MAX_VALUE;
    @XmlTransient
    private FileParam dataParam;
    @XmlTransient
    private FileParam dcxParam;

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
        dcxParam = new FileParam();
        dcxParam.setCmd(CMD_FND_PTH_ABS);
        dcxParam.setOpt(OPT_INSIDE);
        dcxParam.setPattern(compile(convertParam(dcxFileRegex,true)));
        dcxParam.setSrcPath(get(queryPath));
        dcxParam.setLevel(level);
        dataParam = dcxParam.cloneValue();
        dataParam.setPattern(compile(convertParam(dataFileRegex,true)));
        return true;
    }

    public String getMode(){
        return mode;
    }

    public FileParam getDataParam(){
        return dataParam;
    }

    public FileParam getDcxParam(){
        return dcxParam;
    }

    protected void setMode(String mode){
        this.mode = mode;
    }

    protected void setDataFileRegex(String dataFileRegex){
        this.dataFileRegex = dataFileRegex;
    }

    protected void setDcxFileRegex(String dcxFileRegex){
        this.dcxFileRegex = dcxFileRegex;
    }
}
