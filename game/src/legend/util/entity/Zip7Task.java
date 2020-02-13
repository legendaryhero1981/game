package legend.util.entity;

import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.FS;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IZip7;

@XmlRootElement(name = "Zip7Task")
@XmlType(propOrder = {"queryRegex","queryPath","mode","filePath","listFilePath","password","compression","volumeSize","sfxModule","moreArgs"})
public class Zip7Task extends BaseEntity<Zip7Task> implements IZip7{
    @XmlElement
    private String queryRegex = REG_ANY;
    @XmlElement
    private String queryPath = S_EMPTY;
    @XmlElement
    private String mode = MODE_ZIP;
    @XmlElement
    private String filePath = S_EMPTY;
    @XmlElement
    private String listFilePath = S_EMPTY;
    @XmlElement
    private String password = S_EMPTY;
    @XmlElement
    private String compression = ZIP7_VAL_COMP_DEF;
    @XmlElement
    private String volumeSize = S_EMPTY;
    @XmlElement
    private String sfxModule = S_EMPTY;
    @XmlElement
    private String moreArgs = S_EMPTY;
    @XmlTransient
    protected Deque<String> cmd = new ArrayDeque<>();
    @XmlTransient
    protected static final Pattern modePattern;
    @XmlTransient
    protected static final Pattern compPattern;
    @XmlTransient
    protected static final Pattern volumePattern;
    static{
        modePattern = compile(REG_ZIP7_MODE);
        compPattern = compile(REG_ZIP7_COMP);
        volumePattern = compile(REG_ZIP7_VOL);
    }

    @Override
    public Zip7Task trim(){
        queryRegex = queryRegex.trim();
        queryPath = queryPath.trim();
        mode = mode.trim();
        filePath = filePath.trim();
        listFilePath = listFilePath.trim();
        password = password.trim();
        compression = compression.trim();
        volumeSize = volumeSize.trim();
        sfxModule = sfxModule.trim();
        moreArgs = moreArgs.trim();
        return this;
    }

    @Override
    public boolean validate(){
        if(isEmpty(queryRegex) || isEmpty(queryPath) || isEmpty(mode)){
            errorInfo = ERR_ZIP7_TASK_NON;
            return false;
        }
        Matcher matcher = modePattern.matcher(mode);
        if(!matcher.matches() || MODE_ZIP.equals(mode)) mode = ZIP7_ARG_ZIP;
        else mode = ZIP7_ARG_UNZIP;
        if(nonEmpty(sfxModule)) if(!matcher.reset(sfxModule).matches() || MODE_ZIP.equals(sfxModule)) sfxModule = ZIP7_ARG_SFX_GUI;
        else sfxModule = ZIP7_ARG_SFX_CON;
        matcher = compPattern.matcher(compression);
        if(!matcher.matches()) compression = ZIP7_ARG_COMP_DEF;
        else compression = ZIP7_ARG_COMP + compression;
        matcher = volumePattern.matcher(volumeSize);
        if(nonEmpty(volumeSize)) if(matcher.matches()){
            long size = FS.matchSize(Long.parseLong(matcher.group(1)),FS.matchType(matcher.group(2)));
            if(ZIP7_VOL_SIZE_DEF > size) volumeSize = ZIP7_ARG_VOL_DEF;
            else volumeSize = ZIP7_ARG_VOL + volumeSize;
        }else volumeSize = ZIP7_ARG_VOL_DEF;
        if(nonEmpty(password)) password = ZIP7_ARG_PW + password;
        cmd.clear();
        if(ZIP7_ARG_ZIP.equals(mode)) cmd.addAll(asList(mode,password,ZIP7_ARG_SPF,compression,volumeSize,sfxModule,moreArgs,filePath,ZIP7_ARG_LIST_FILE + listFilePath));
        else cmd.addAll(asList(mode,password,ZIP7_ARG_SPF,ZIP7_ARG_YES_ALL,moreArgs,ZIP7_ARG_OUT + filePath));
        return true;
    }

    public String getQueryRegex(){
        return queryRegex;
    }

    public String getQueryPath(){
        return queryPath;
    }

    public String getMode(){
        return mode;
    }

    public String getFilePath(){
        return filePath;
    }

    public String getListFilePath(){
        return listFilePath;
    }

    public String getPassword(){
        return password;
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
