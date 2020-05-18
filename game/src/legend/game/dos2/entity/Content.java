package legend.game.dos2.entity;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;

@XmlType(propOrder = {"id","value"})
public class Content{
    @XmlAttribute(name = "contentuid")
    private String id;
    @XmlValue
    private String value;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }
}
