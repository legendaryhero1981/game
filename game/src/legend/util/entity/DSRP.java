package legend.util.entity;

import static java.nio.file.Paths.get;
import static legend.util.FileUtil.existsPath;
import static legend.util.ValueUtil.isEmpty;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import legend.util.entity.intf.IDSRP;

@XmlRootElement(name = "DSRP")
@XmlType(propOrder = {"comment","dataRepackerExecutablePath","dcxRepackerExecutablePath","tasks"})
public class DSRP extends BaseEntity<DSRP> implements IDSRP{
    @XmlElement
    private String comment = FILE_DSRP_COMMENT;
    @XmlElement
    private String dataRepackerExecutablePath = DATA_EXEC_PATH;
    @XmlElement
    private String dcxRepackerExecutablePath = DCX_EXEC_PATH;
    @XmlElementRef
    private List<DSRPTask> tasks;

    public DSRP(){
        tasks = new ArrayList<>();
        tasks.add(new DSRPTask());
    }

    @Override
    public DSRP trim(){
        dataRepackerExecutablePath = dataRepackerExecutablePath.strip();
        dcxRepackerExecutablePath = dcxRepackerExecutablePath.strip();
        tasks.parallelStream().forEach(t->t.trim());
        return this;
    }

    @Override
    public boolean validate(){
        if(isEmpty(dataRepackerExecutablePath) || isEmpty(dcxRepackerExecutablePath)){
            errorInfo = ERR_DSRP_EXEC_NUL;
            return false;
        }else if(!existsPath(get(dataRepackerExecutablePath)) || !existsPath(get(dcxRepackerExecutablePath))){
            errorInfo = ERR_DSRP_EXEC_NON;
            return false;
        }
        return tasks.stream().anyMatch(t->{
            if(!t.validate()){
                errorInfo = t.errorInfo;
                return false;
            }else return true;
        });
    }

    public String getDataRepackerExecutablePath(){
        return dataRepackerExecutablePath;
    }

    public String getDcxRepackerExecutablePath(){
        return dcxRepackerExecutablePath;
    }

    public List<DSRPTask> getTasks(){
        return tasks;
    }
}
