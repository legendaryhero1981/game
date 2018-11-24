package legend.helper.entity;

import static legend.intf.ICommon.gl;
import static legend.intf.ICommon.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
