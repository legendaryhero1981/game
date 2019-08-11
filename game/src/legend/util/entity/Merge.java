package legend.util.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Merge")
@XmlType(propOrder = {"path","md5"})
public class Merge{
    @XmlElement
    @XmlID
    private String path = "";
    @XmlElement
    private String md5 = "";

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getMd5(){
        return md5;
    }

    public void setMd5(String md5){
        this.md5 = md5;
    }
}
