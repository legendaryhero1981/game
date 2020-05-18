package legend.game.kcd.entity.mod;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Conflict")
@XmlType(propOrder = {"mod","mappings"})
public class Conflict{
    @XmlElement
    @XmlID
    private String mod = "";
    @XmlElementRef
    private List<Mapping> mappings = new CopyOnWriteArrayList<>();

    public String getMod(){
        return mod;
    }

    public void setMod(String mod){
        this.mod = mod;
    }

    public List<Mapping> getMappings(){
        return mappings;
    }
}
