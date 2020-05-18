package legend.helper.entity;

import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.gs;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Helper")
@XmlType(propOrder = {"name","desc","example"})
public class Helper{
    @XmlElement
    @XmlID
    private String name = "";
    @XmlElement
    private String desc = "";
    @XmlElement
    private String example = "";

    @Override
    public String toString(){
        return name + gs(4) + desc + gl(1) + example + gl(1);
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getExample(){
        return example;
    }

    public void setExample(String example){
        this.example = example;
    }
}
