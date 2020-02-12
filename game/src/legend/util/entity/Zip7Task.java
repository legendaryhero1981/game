package legend.util.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IZip7;

@XmlRootElement(name = "Zip7Task")
@XmlType(propOrder = {"queryRegex","mode","input","output","password","listFile","compression","volumeSize","sfxModule","moreArgs"})
public class Zip7Task extends BaseEntity<Zip7Task> implements IZip7{
    @XmlElement
    private String queryRegex = REG_ANY;
    @XmlElement
    private String mode = S_EMPTY;
    @XmlElement
    private String input = S_EMPTY;
    @XmlElement
    private String output = S_EMPTY;
    @XmlElement
    private String password = S_EMPTY;
    @XmlElement
    private String listFile = S_EMPTY;
    @XmlElement
    private String compression = S_EMPTY;
    @XmlElement
    private String volumeSize = S_EMPTY;
    @XmlElement
    private String sfxModule = S_EMPTY;
    @XmlElement
    private String moreArgs = S_EMPTY;

    @Override
    public Zip7Task trim(){
        return super.trim();
    }

    @Override
    public boolean validate(){
        return super.validate();
    }

    public String getQueryRegex(){
        return queryRegex;
    }

    public String getMode(){
        return mode;
    }

    public String getInput(){
        return input;
    }

    public String getOutput(){
        return output;
    }

    public String getPassword(){
        return password;
    }

    public String getListFile(){
        return listFile;
    }

    public String getCompression(){
        return compression;
    }

    public String getVolumeSize(){
        return volumeSize;
    }

    public String getSfxModule(){
        return sfxModule;
    }

    public String getMoreArgs(){
        return moreArgs;
    }
}
