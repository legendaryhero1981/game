package legend.util.entity;

import static java.util.Collections.addAll;
import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.nonEmpty;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import legend.util.intf.IFileUtil;

@XmlRootElement(name = "ILCode")
@XmlType(propOrder = {"processingMode","lineNumber","codeDesc","queryRegex","codeFragment"})
public class ILCode implements IFileUtil{
    @XmlElement
    private String processingMode = MODE_IL_NATIVE;
    @XmlElement
    private String lineNumber = "";
    @XmlElement
    private String codeDesc = "";
    @XmlElement
    private String queryRegex = "";
    @XmlElement
    private String codeFragment = "";
    @XmlTransient
    private int startLine;
    @XmlTransient
    private int endLine;
    @XmlTransient
    private List<String> regexCache = new CopyOnWriteArrayList<>();
    @XmlTransient
    private List<String> fragmentCache = new CopyOnWriteArrayList<>();

    protected boolean validate(){
        if(MODE_IL_NATIVE != processingMode && MODE_IL_MOD != processingMode) processingMode = MODE_IL_NATIVE;
        Matcher matcher = compile(REG_LINE_NUMBER).matcher(lineNumber);
        if(!matcher.matches()) return false;
        startLine = Integer.parseInt(matcher.group(1));
        endLine = matcher.groupCount() > 1 ? Integer.parseInt(matcher.group(3)) : startLine;
        return startLine <= endLine && (MODE_IL_NATIVE == processingMode || nonEmpty(queryRegex));
    }

    protected ILCode trim(){
        processingMode = processingMode.trim();
        lineNumber = lineNumber.trim();
        codeDesc = codeDesc.trim();
        queryRegex = queryRegex.trim();
        codeFragment = codeFragment.trim();
        return this;
    }

    public List<String> refreshRegexCache(){
        regexCache.clear();
        if(nonEmpty(queryRegex)) addAll(regexCache,queryRegex.split(SPRT_LINE));
        return regexCache;
    }

    public List<String> refreshFragmentCache(){
        fragmentCache.clear();
        if(nonEmpty(codeFragment)) addAll(fragmentCache,codeFragment.split(SPRT_LINE));
        return fragmentCache;
    }

    public String getProcessingMode(){
        return processingMode;
    }

    public void setProcessingMode(String processingMode){
        this.processingMode = processingMode;
    }

    public int getStartLine(){
        return startLine;
    }

    public void setStartLine(int startLine){
        this.startLine = startLine;
    }

    public int getEndLine(){
        return endLine;
    }

    public void setEndLine(int endLine){
        this.endLine = endLine;
    }

    public String getQueryRegex(){
        return queryRegex;
    }

    public String getCodeFragment(){
        return codeFragment;
    }
}
