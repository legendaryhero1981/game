package legend.util.entity;

import static java.lang.String.valueOf;
import static java.util.Collections.addAll;
import static java.util.regex.Pattern.compile;
import static legend.util.ValueUtil.nonEmpty;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import legend.util.adapter.CDataAdapter;
import legend.util.intf.IILCode;

@XmlRootElement(name = "ILCode")
@XmlType(propOrder = {"processingMode","lineNumber","codeDesc","queryRegex","codeFragment"})
public class ILCode implements IILCode{
    @XmlElement
    private String processingMode = MODE_NATIVE;
    @XmlElement
    private String lineNumber = "1-1";
    @XmlElement
    private String codeDesc = "";
    @XmlJavaTypeAdapter(CDataAdapter.class)
    @XmlElement
    private String queryRegex = "";
    @XmlJavaTypeAdapter(CDataAdapter.class)
    @XmlElement
    private String codeFragment = "";
    @XmlTransient
    protected String errorInfo = "";
    @XmlTransient
    private Pattern pattern = compile(REG_SPRT_CODE);
    @XmlTransient
    private List<String> regexCache = new CopyOnWriteArrayList<>();
    @XmlTransient
    private List<String> fragmentCache = new CopyOnWriteArrayList<>();
    @XmlTransient
    private int startLine = 1;
    @XmlTransient
    private int endLine = 1;

    protected boolean validate(){
        if(MODE_NATIVE != processingMode && MODE_REPL != processingMode && MODE_ADD != processingMode) processingMode = MODE_NATIVE;
        Matcher matcher = compile(REG_LINE_NUMBER).matcher(lineNumber);
        if(!matcher.matches()){
            errorInfo = ERR_LINE_NUM_FORMAT;
            return false;
        }
        startLine = Integer.parseInt(matcher.group(1));
        endLine = matcher.groupCount() > 1 ? Integer.parseInt(matcher.group(3)) : startLine;
        if(startLine > endLine){
            errorInfo = ERR_LINE_NUM_VAL_RANGE;
            return false;
        }
        return true;
    }

    protected ILCode trim(){
        processingMode = processingMode.trim();
        lineNumber = lineNumber.trim();
        queryRegex = queryRegex.trim();
        codeFragment = codeFragment.trim();
        return this;
    }

    public List<String> refreshRegexCache(boolean force){
        if(force || regexCache.isEmpty()){
            regexCache.clear();
            if(nonEmpty(queryRegex)) addAll(regexCache,pattern.split(queryRegex));
        }
        return regexCache;
    }

    public List<String> refreshFragmentCache(boolean force){
        if(force || fragmentCache.isEmpty()){
            fragmentCache.clear();
            if(nonEmpty(codeFragment)) addAll(fragmentCache,pattern.split(codeFragment));
        }
        return fragmentCache;
    }

    public String getProcessingMode(){
        return processingMode;
    }

    public void setProcessingMode(String processingMode){
        this.processingMode = processingMode;
    }

    public void setLineNumer(int start, int end){
        lineNumber = valueOf(start) + SPRT_LINE_NUMBER + valueOf(end);
    }

    public int getStartLine(){
        return startLine;
    }

    public int getEndLine(){
        return endLine;
    }

    public String getQueryRegex(){
        return queryRegex;
    }

    public String getCodeFragment(){
        return codeFragment;
    }
}
