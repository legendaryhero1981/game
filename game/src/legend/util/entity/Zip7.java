package legend.util.entity;

import static java.nio.file.Paths.get;
import static java.util.regex.Pattern.compile;
import static legend.util.FileUtil.dealFiles;
import static legend.util.FileUtil.existsPath;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.param.FileParam.convertParam;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IZip7;
import legend.util.param.FileParam;

@XmlRootElement(name = "Zip7")
@XmlType(propOrder = {"comment","zip7ExecutablePath","tasks"})
public class Zip7 extends BaseEntity<Zip7> implements IZip7{
    @XmlElement
    private String comment = FILE_ZIP7_COMMENT;
    @XmlElement
    private String zip7ExecutablePath = ZIP7_EXEC_PATH;
    @XmlElementRef
    private List<Zip7Task> tasks;
    @XmlTransient
    private Queue<Deque<String>> cmds;

    public Zip7(){
        tasks = new ArrayList<>();
        tasks.add(new Zip7Task());
        cmds = new ConcurrentLinkedQueue<>();
    }

    @Override
    public Zip7 trim(){
        zip7ExecutablePath = zip7ExecutablePath.trim();
        tasks.parallelStream().forEach(t->t.trim());
        return this;
    }

    @Override
    public boolean validate(){
        if(isEmpty(zip7ExecutablePath)){
            errorInfo = ERR_ZIP7_EXEC_NON;
            return false;
        }else if(!existsPath(get(zip7ExecutablePath))){
            errorInfo = ERR_7ZIP_EXEC_NON;
            return false;
        }
        if(tasks.parallelStream().anyMatch(t->{
            if(!t.validate()){
                errorInfo = t.errorInfo;
                return true;
            }
            final boolean isZipMode = ZIP7_ARG_ZIP.equals(t.getMode());
            FileParam fp = new FileParam();
            fp.setCmd(isZipMode ? CMD_FND_PTH_DIR_ABS : CMD_FND_PTH_ABS);
            fp.setOpt(OPT_INSIDE);
            fp.setPattern(compile(convertParam(t.getQueryRegex(),true)));
            fp.setSrcPath(get(t.getQueryPath()));
            fp.setDestPath(get(t.getListFilePath()));
            dealFiles(fp);
            t.cmd.addFirst(zip7ExecutablePath);
            if(isZipMode) cmds.add(t.cmd);
            else fp.getPathCaches().parallelStream().forEach(p->{
                Deque<String> cmd = new ArrayDeque<>(t.cmd);
                cmd.add(p.toString());
                cmds.add(cmd);
            });
            return false;
        })) return false;
        return true;
    }

    public String getZip7ExecutablePath(){
        return zip7ExecutablePath;
    }

    public List<Zip7Task> getTasks(){
        return tasks;
    }

    public Queue<Deque<String>> getCmds(){
        return cmds;
    }
}
