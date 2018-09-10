package legend.game.kcd.entity;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "Value")
public class Value{
    @XmlValue
    private String text;

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }
}
