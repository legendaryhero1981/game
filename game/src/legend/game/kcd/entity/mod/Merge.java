package legend.game.kcd.entity.mod;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Merge")
@XmlType(propOrder = {"path","mappings"})
public class Merge{
    @XmlElement
    private String path = "";
    @XmlElementRef
    private List<Mapping> mappings = new CopyOnWriteArrayList<>();

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public List<Mapping> getMappings(){
        return mappings;
    }

    public void setMappings(CopyOnWriteArrayList<Mapping> mappings){
        this.mappings = mappings;
    }
}
