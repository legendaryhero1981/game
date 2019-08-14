package legend.util.entity;

import static legend.util.ValueUtil.nonEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import legend.util.logic.intf.IFileMerge;

@XmlRootElement(name = "FileMerge")
@XmlType(propOrder = {"comment","path","path2","path3","mergeExecutablePath","queryRegex","pathMd5","merges"})
public class FileMerge implements IFileMerge{
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

    public boolean validate(){
        return nonEmpty(path) && nonEmpty(path2) && nonEmpty(path3) && nonEmpty(mergeExecutablePath) && nonEmpty(queryRegex);
    }

    public FileMerge trim(){
        path = path.trim();
        path2 = path2.trim();
        path3 = path3.trim();
        mergeExecutablePath = mergeExecutablePath.trim();
        queryRegex = queryRegex.trim();
        pathMd5 = pathMd5.trim();
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
