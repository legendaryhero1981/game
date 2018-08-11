package legend.game.kcd.entity.mod;

import static legend.util.ValueUtil.isEmpty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Config")
@XmlType(propOrder = {"gamePath","modPath","mergePath","mergeExecutablePath"})
public class Config{
    @XmlElement
    private String gamePath = "";
    @XmlElement
    private String modPath = "";
    @XmlElement
    private String mergePath = "";
    @XmlElement
    private String mergeExecutablePath = "";

    public boolean validate(){
        return isEmpty(gamePath) || isEmpty(modPath) || isEmpty(mergePath) || isEmpty(mergeExecutablePath);
    }

    public Config trim(){
        gamePath = gamePath.trim();
        modPath = modPath.trim();
        mergePath = mergePath.trim();
        mergeExecutablePath = mergeExecutablePath.trim();
        return this;
    }

    public String getGamePath(){
        return gamePath;
    }

    public void setGamePath(String gamePath){
        this.gamePath = gamePath;
    }

    public String getModPath(){
        return modPath;
    }

    public void setModPath(String modPath){
        this.modPath = modPath;
    }

    public String getMergePath(){
        return mergePath;
    }

    public void setMergePath(String mergePath){
        this.mergePath = mergePath;
    }

    public String getMergeExecutablePath(){
        return mergeExecutablePath;
    }

    public void setMergeExecutablePath(String mergeExecutablePath){
        this.mergeExecutablePath = mergeExecutablePath;
    }
}
