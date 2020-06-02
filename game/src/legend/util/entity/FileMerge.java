package legend.util.entity;

import static java.nio.file.Paths.get;
import static legend.util.FileUtil.existsPath;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IFileMerge;

@XmlRootElement(name = "FileMerge")
@XmlType(propOrder = {"comment","path","path2","path3","mergeExecutablePath","queryRegex","pathMd5","merges"})
public class FileMerge extends BaseEntity<FileMerge> implements IFileMerge{
    @XmlTransient
    private ConcurrentMap<String,Merge> mergeMap = new ConcurrentHashMap<>();
    @XmlElement
    private String comment = FILE_MERGE_COMMENT;
    @XmlElement
    private String path = S_EMPTY;
    @XmlElement
    private String path2 = S_EMPTY;
    @XmlElement
    private String path3 = S_EMPTY;
    @XmlElement
    private String mergeExecutablePath = S_EMPTY;
    @XmlElement
    private String queryRegex = REG_ANY;
    @XmlElement
    private String pathMd5 = S_EMPTY;
    @XmlElementWrapper(name = "Merges")
    @XmlElementRef
    private List<Merge> merges = new ArrayList<>();

    @Override
    public boolean validate(){
        if(isEmpty(path) || isEmpty(path2) || isEmpty(path3) || isEmpty(mergeExecutablePath) || isEmpty((queryRegex))){
            errorInfo = ERR_MEG_NODE_NON;
            return false;
        }else if(!existsPath(get(mergeExecutablePath))){
            errorInfo = ERR_KDIFF3_EXEC_NON;
            return false;
        }
        return true;
    }

    @Override
    public FileMerge trim(){
        path = path.strip();
        path2 = path2.strip();
        path3 = path3.strip();
        mergeExecutablePath = mergeExecutablePath.strip();
        queryRegex = queryRegex.strip();
        pathMd5 = pathMd5.strip();
        merges.parallelStream().forEach(m->m.trim());
        return this;
    }

    public ConcurrentMap<String,Merge> refreshMergeMap(){
        mergeMap.clear();
        if(nonEmpty(merges)) merges.parallelStream().forEach(merge->mergeMap.put(merge.getPath().toLowerCase(),merge));
        return mergeMap;
    }

    public List<Merge> refreshMerges(){
        merges.clear();
        if(!mergeMap.isEmpty()) merges.addAll(mergeMap.values());
        return merges;
    }

    public String getPath(){
        return path;
    }

    public String getPath2(){
        return path2;
    }

    public String getPath3(){
        return path3;
    }

    public String getMergeExecutablePath(){
        return mergeExecutablePath;
    }

    public String getQueryRegex(){
        return queryRegex;
    }

    public String getPathMd5(){
        return pathMd5;
    }

    public void setPathMd5(String pathMd5){
        this.pathMd5 = pathMd5;
    }
}
