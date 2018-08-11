package legend.game.kcd.entity.local;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import legend.game.kcd.entity.Cell;

@XmlRootElement(name = "Row")
public class Row{
    @XmlElement(name = "Cell")
    private List<Cell> cells;

    public List<Cell> getCells(){
        return cells;
    }

    public void setCells(CopyOnWriteArrayList<Cell> cells){
        this.cells = cells;
    }
}
