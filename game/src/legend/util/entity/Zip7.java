package legend.util.entity;

import static java.nio.file.Paths.get;
import static java.util.regex.Pattern.compile;
import static legend.util.FileUtil.dealFiles;
import static legend.util.FileUtil.existsPath;
import static legend.util.FileUtil.makeDirs;
import static legend.util.FileUtil.writeFile;
import static legend.util.MD5Util.getMD5L32;
import static legend.util.StringUtil.getFileNameWithoutSuffix;
import static legend.util.StringUtil.gs;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.param.FileParam.convertParam;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
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
    private Queue<String[]> cmds;

    public Zip7(){
        tasks = new ArrayList<>();
        tasks.add(new Zip7Task());
        cmds = new ConcurrentLinkedQueue<>();
    }

    @Override
    public Zip7 trim(){
        zip7ExecutablePath = zip7ExecutablePath.strip();
        tasks.parallelStream().forEach(t->t.trim());
        return this;
    }

    @Override
    public boolean validate(){
        if(isEmpty(zip7ExecutablePath)){
            errorInfo = ERR_ZIP7_EXEC_NUL;
            return false;
        }else if(!existsPath(get(zip7ExecutablePath))){
            errorInfo = ERR_ZIP7_EXEC_NON;
            return false;
        }
        cmds.clear();
        return !tasks.stream().anyMatch(t->{
            if(!t.validate()){
                errorInfo = t.errorInfo;
                return true;
            }
            makeDirs(get(t.getFilePath()));
            final boolean isZipMode = ZIP7_ARG_ZIP.equals(t.getMode()), isZipModeDefault = MODE_ZIP_DEF.equals(t.getZipMode());
            FileParam fp = new FileParam();
            fp.setCmd(isZipMode ? isZipModeDefault ? CMD_FND_PTH_DIR_RLT : CMD_FND_PTH_DIR_ABS : CMD_FND_PTH_ABS);
            fp.setOpt(OPT_INSIDE);
            fp.setPattern(compile(convertParam(t.getQueryRegex(),true)));
            fp.setSrcPath(get(t.getQueryPath()));
            fp.setDestPath(get(t.getListFilePath()));
            fp.setLevel(t.getLevel());
            dealFiles(fp);
            t.cmd.addFirst(zip7ExecutablePath);
            if(isZipMode){
                if(isZipModeDefault){
                    Path path = get(t.getQueryPath());
                    path = isEmpty(path.getParent()) ? path : path.getParent();
                    String[] context = gsph(ZIP7_CONTEXT,path.toString()).split(SPC_NUL);
                    for(int i = context.length - 1;i >= 0;i--) t.cmd.addFirst(context[i]);
                }else t.cmd.add(MODE_ZIP_SPF.equals(t.getZipMode()) ? ZIP7_ARG_SPF : ZIP7_ARG_SPF2);
                cmds.add(t.cmd.toArray(new String[t.cmd.size()]));
            }else{
                Queue<String> caches = new ConcurrentLinkedQueue<>();
                fp.getPathsCache().parallelStream().forEach(p->{
                    Deque<String> cmd = new ArrayDeque<>(t.cmd);
                    String name = t.getUnzipMode();
                    if(MODE_UNZIP_MD5.equals(name)) name = p.getFileName().toString() + REG_ANY + getMD5L32(p);
                    else if(MODE_UNZIP_DIR.equals(name)) name = getFileNameWithoutSuffix(p.toString());
                    else name = S_EMPTY;
                    cmd.add(ZIP7_ARG_OUT + get(t.getFilePath()).resolve(name));
                    caches.add(S_EMPTY.equals(name) ? p.toString() : name + gs(4) + p.toString());
                    cmd.add(p.toString());
                    cmd.add(t.getMoreArgs());
                    cmds.add(cmd.toArray(new String[cmd.size()]));
                });
                writeFile(fp.getDestPath(),caches);
            }
            return false;
        });
    }

    public String getZip7ExecutablePath(){
        return zip7ExecutablePath;
    }

    public List<Zip7Task> getTasks(){
        return tasks;
    }

    public Queue<String[]> getCmds(){
        return cmds;
    }
}
