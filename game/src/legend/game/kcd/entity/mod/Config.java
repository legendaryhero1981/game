package legend.game.kcd.entity.mod;

import static legend.util.ValueUtil.nonEmpty;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import legend.intf.IValue;

@XmlRootElement(name = "Config")
@XmlType(propOrder = {"gamePath","modPath","mergePath","mergeExecutablePath"})
public class Config implements IValue<Config>{
    @XmlElement
    private String gamePath = "";
    @XmlElement
    private String modPath = "";
    @XmlElement
    private String mergePath = "";
    @XmlElement
    private String mergeExecutablePath = "";

    @Override
    public boolean validate(){
        return nonEmpty(gamePath) && nonEmpty(modPath) && nonEmpty(mergePath) && nonEmpty(mergeExecutablePath);
    }

    @Override
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
