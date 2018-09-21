package legend.game.kcd.entity.mod;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
