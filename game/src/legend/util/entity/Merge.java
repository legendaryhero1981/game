package legend.util.entity;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Merge")
@XmlType(propOrder = {"path","md5"})
public class Merge{
    @XmlElement
    @XmlID
    private String path = "";
    @XmlElement
    private String md5 = "";

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Merge)) return false;
        Merge merge = (Merge)object;
        return this == merge || path.equalsIgnoreCase(merge.getPath()) && md5.equalsIgnoreCase(merge.getMd5());
    }

    public void trim(){
        path = path.trim();
        md5 = md5.trim();
    }

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
