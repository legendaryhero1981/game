package legend.game.kcd.entity;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType
public class Cell{
    @XmlValue
    private String text;

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }
}
