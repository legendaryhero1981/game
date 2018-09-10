package legend.game.kcd.entity.local;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import legend.game.kcd.entity.Value;

@XmlRootElement(name = "Row")
public class Row{
    @XmlElement(name = "Cell")
    private List<Value> cells;

    public List<Value> getCells(){
        return cells;
    }
}
