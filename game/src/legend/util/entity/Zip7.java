package legend.util.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IZip7;

@XmlRootElement(name = "Zip7")
@XmlType(propOrder = {"comment","zip7ExecutablePath","tasks"})
public class Zip7 extends BaseEntity<Zip7> implements IZip7{
    @XmlElement
    private String comment = FILE_7ZIP_COMMENT;
    @XmlElement
    private String zip7ExecutablePath = EXECUTABLE_PATH;
    @XmlElementRef
    private List<Zip7Task> tasks;

    public Zip7(){
        tasks = new ArrayList<>();
        tasks.add(new Zip7Task());
    }

    @Override
    public Zip7 trim(){
        return super.trim();
    }

    @Override
    public boolean validate(){
        return super.validate();
    }

    public String getZip7ExecutablePath(){
        return zip7ExecutablePath;
    }

    public List<Zip7Task> getTasks(){
        return tasks;
    }
}
