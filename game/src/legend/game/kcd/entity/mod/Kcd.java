package legend.game.kcd.entity.mod;

import static legend.util.ValueUtil.nonEmpty;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Kcd")
@XmlType(propOrder = {"config","mods","merges","conflicts","uniques"})
public class Kcd{
    @XmlTransient
    private MergeSet mergeSet = new MergeSet();
    @XmlTransient
    private ConcurrentMap<String,List<Path>> pathsMap = new ConcurrentHashMap<>();
    @XmlTransient
    private ConcurrentMap<String,Mod> modMap = new ConcurrentHashMap<>();
    @XmlTransient
    private ConcurrentMap<String,Mapping> uniqueMap = new ConcurrentHashMap<>();
    @XmlTransient
    private ConcurrentMap<String,Merge> mergeMap = new ConcurrentHashMap<>();
    @XmlTransient
    private ConcurrentMap<String,Conflict> conflictMap = new ConcurrentHashMap<>();
    @XmlElementRef
    private Config config = new Config();
    @XmlElementWrapper(name = "Mods")
    @XmlElementRef
    private List<Mod> mods = new ArrayList<>();
    @XmlElementWrapper(name = "Merges")
    @XmlElementRef
    private List<Merge> merges = new ArrayList<>();
    @XmlElementWrapper(name = "Conflicts")
    @XmlElementRef
    private List<Conflict> conflicts = new ArrayList<>();
    @XmlElementWrapper(name = "Uniques")
    @XmlElementRef
    private List<Mapping> uniques = new ArrayList<>();

    public static final class MergeSet{
        private Set<String> modSet = new ConcurrentSkipListSet<>();
        private Set<String> pathSet = new ConcurrentSkipListSet<>();
        private Set<String> md5Set = new ConcurrentSkipListSet<>();

        private void clear(){
            modSet.clear();
            pathSet.clear();
            md5Set.clear();
        }

        public boolean contains(Merge merge){
            return !modSet.isEmpty() && !pathSet.isEmpty() && !md5Set.isEmpty() && pathSet.contains(merge.getPath()) && !merge.getMappings().parallelStream().anyMatch(mapping->!modSet.contains(mapping.getMod()) || !md5Set.contains(mapping.getMd5()));
        }
    }

    public void clearCache(){
        mergeSet.clear();
        pathsMap.clear();
        modMap.clear();
        mergeMap.clear();
        conflictMap.clear();
        uniqueMap.clear();
        mods.clear();
        merges.clear();
        conflicts.clear();
        uniques.clear();
    }

    public MergeSet getMergeSet(){
        return mergeSet;
    }

    public MergeSet refreshMergeSet(Collection<Merge> merges){
        mergeSet.clear();
        if(nonEmpty(merges)) merges.parallelStream().forEach(merge->{
            mergeSet.pathSet.add(merge.getPath());
            merge.getMappings().parallelStream().forEach(mapping->{
                mergeSet.modSet.add(mapping.getMod());
                mergeSet.md5Set.add(mapping.getMd5());
            });
        });
        return mergeSet;
    }

    public ConcurrentMap<String,List<Path>> getPathsMap(){
        return pathsMap;
    }

    public ConcurrentMap<String,Mod> getModMap(){
        return modMap;
    }

    public ConcurrentMap<String,Mod> refreshModMap(){
        modMap.clear();
        if(nonEmpty(mods)) mods.parallelStream().forEach(mod->modMap.put(mod.getMod(),mod));
        return modMap;
    }

    public ConcurrentMap<String,Mapping> getUniqueMap(){
        return uniqueMap;
    }

    public ConcurrentMap<String,Mapping> refreshUniqueMap(){
        uniqueMap.clear();
        if(nonEmpty(uniques)) uniques.parallelStream().forEach(mapping->uniqueMap.put(mapping.getPath(),mapping));
        return uniqueMap;
    }

    public ConcurrentMap<String,Merge> getMergeMap(){
        return mergeMap;
    }

    public ConcurrentMap<String,Merge> refreshMergeMap(){
        mergeMap.clear();
        if(nonEmpty(merges)) merges.parallelStream().forEach(merge->mergeMap.put(merge.getPath(),merge));
        return mergeMap;
    }

    public ConcurrentMap<String,Conflict> getConflictMap(){
        return conflictMap;
    }

    public ConcurrentMap<String,Conflict> refreshConflictMap(){
        conflictMap.clear();
        if(nonEmpty(conflicts)) conflicts.parallelStream().forEach(conflict->conflictMap.put(conflict.getMod(),conflict));
        return conflictMap;
    }

    public Config getConfig(){
        return config;
    }

    public void setConfig(Config config){
        this.config = config;
    }

    public List<Mod> getMods(){
        return mods;
    }

    public List<Merge> getMerges(){
        return merges;
    }

    public List<Conflict> getConflicts(){
        return conflicts;
    }

    public List<Mapping> getUniques(){
        return uniques;
    }
}
