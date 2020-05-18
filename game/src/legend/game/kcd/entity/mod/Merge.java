package legend.game.kcd.entity.mod;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

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
}
