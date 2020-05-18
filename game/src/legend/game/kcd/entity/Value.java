package legend.game.kcd.entity;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;

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
