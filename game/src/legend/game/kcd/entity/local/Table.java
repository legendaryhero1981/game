package legend.game.kcd.entity.local;

import static legend.util.ValueUtil.nonEmpty;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementRefs;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import legend.game.kcd.entity.Value;

@XmlRootElement(name = "Table")
public class Table{
    @XmlTransient
    private ConcurrentMap<String,Row> rowMap = new ConcurrentHashMap<>();
    @XmlElementRefs({@XmlElementRef(name = "Row", type = Row.class),@XmlElementRef(name = "Value", type = Value.class)})
    private List<Object> rows;

    public ConcurrentMap<String,Row> getRowMap(){
        if(rowMap.isEmpty()) refreshRowMap();
        return rowMap;
    }

    public ConcurrentMap<String,Row> refreshRowMap(){
        if(nonEmpty(rows)) rows.parallelStream().forEach(o->{
            if(o instanceof Row){
                Row row = (Row)o;
                List<Value> cells = row.getCells();
                if(nonEmpty(cells)) rowMap.put(cells.get(0).getText().trim(),row);
            }
        });
        return rowMap;
    }

    public List<Object> getRows(){
        return rows;
    }
}
