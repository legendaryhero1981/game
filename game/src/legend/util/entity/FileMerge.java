package legend.util.entity;

import static legend.util.ValueUtil.isEmpty;
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

@XmlRootElement(name = "FileMerge")
@XmlType(propOrder = {"pathOne","pathTwo","pathThree","mergeExecutablePath","queryRegex","pathMd5"})
public class FileMerge{
    @XmlTransient
    private ConcurrentMap<String,Merge> mergeMap = new ConcurrentHashMap<>();
    @XmlElement
    private String pathOne = "";
    @XmlElement
    private String pathTwo = "";
    @XmlElement
    private String pathThree = "";
    @XmlElement
    private String mergeExecutablePath = "";
    @XmlElement
    private String queryRegex = "";
    @XmlElement
    private String pathMd5 = "";
    @XmlElementWrapper(name = "Merges")
    @XmlElementRef
    private List<Merge> merges = new ArrayList<>();

    public boolean validate(){
        return isEmpty(pathOne) || isEmpty(pathTwo) || isEmpty(pathThree) || isEmpty(mergeExecutablePath) || isEmpty(queryRegex);
    }

    public ConcurrentMap<String,Merge> refreshMergeMap(){
        mergeMap.clear();
        if(nonEmpty(merges)) merges.parallelStream().forEach(merge->mergeMap.put(merge.getPath(),merge));
        return mergeMap;
    }

    public String getPathOne(){
        return pathOne;
    }

    public void setPathOne(String pathOne){
        this.pathOne = pathOne;
    }

    public String getPathTwo(){
        return pathTwo;
    }

    public void setPathTwo(String pathTwo){
        this.pathTwo = pathTwo;
    }

    public String getPathThree(){
        return pathThree;
    }

    public void setPathThree(String pathThree){
        this.pathThree = pathThree;
    }

    public String getMergeExecutablePath(){
        return mergeExecutablePath;
    }

    public void setMergeExecutablePath(String mergeExecutablePath){
        this.mergeExecutablePath = mergeExecutablePath;
    }

    public List<Merge> getMerges(){
        return merges;
    }

    public void setMerges(List<Merge> merges){
        this.merges = merges;
    }
}
