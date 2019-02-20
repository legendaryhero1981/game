package legend.util.entity;

import static java.util.Collections.addAll;
import static java.util.regex.Pattern.compile;
import static legend.util.StringUtil.gsph;
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
@XmlType(propOrder = {"processingMode","lineNumber","codeDesc","queryRegex","codeRegex","codeFragment"})
public class ILCode implements IILCode{
    @XmlElement
    protected String processingMode = MODE_NATIVE;
    @XmlElement
    protected String lineNumber = "1-1";
    @XmlElement
    protected String codeDesc = "";
    @XmlJavaTypeAdapter(CDataAdapter.class)
    @XmlElement
    protected String queryRegex = "";
    @XmlElement
    protected String codeRegex = "";
    @XmlJavaTypeAdapter(CDataAdapter.class)
    @XmlElement
    protected String codeFragment = "";
    @XmlTransient
    protected String errorInfo = "";
    @XmlTransient
    protected int startLine = 1;
    @XmlTransient
    protected int endLine = 1;
    @XmlTransient
    private Pattern pattern = compile(REG_SPRT_CODE);
    @XmlTransient
    private List<Pattern> queryRegexCache = new CopyOnWriteArrayList<>();
    @XmlTransient
    private List<Pattern> codeRegexCache = new CopyOnWriteArrayList<>();
    @XmlTransient
    private List<String> codeFragmentCache = new CopyOnWriteArrayList<>();

    protected boolean validate(){
        Matcher matcher = compile(REG_LINE_NUMBER).matcher(lineNumber);
        if(!matcher.matches()){
            errorInfo = ERR_LINE_NUM_FORMAT;
            return false;
        }
        startLine = Integer.parseInt(matcher.group(1));
        endLine = matcher.groupCount() > 1 ? Integer.parseInt(matcher.group(3)) : startLine;
        switch(processingMode){
            case MODE_NATIVE:
            case MODE_REPL:
            if(startLine > endLine){
                errorInfo = gsph(ERR_LINE_NUM_VAL_LESS,lineNumber);
                return false;
            }
            break;
            case MODE_ADD:
            if(startLine != endLine){
                errorInfo = gsph(ERR_LINE_NUM_VAL_EQUAL,lineNumber);
                return false;
            }
            break;
            default:
            processingMode = MODE_NATIVE;
            if(startLine > endLine){
                errorInfo = gsph(ERR_LINE_NUM_VAL_LESS,lineNumber);
                return false;
            }
        }
        return true;
    }

    protected ILCode trim(){
        processingMode = processingMode.trim();
        lineNumber = lineNumber.trim();
        queryRegex = queryRegex.trim();
        codeRegex = codeRegex.trim();
        return this;
    }

    public List<Pattern> refreshQueryRegexCache(boolean force){
        if(force || queryRegexCache.isEmpty()){
            queryRegexCache.clear();
            if(nonEmpty(queryRegex)){
                List<String> cache = new CopyOnWriteArrayList<>();
                addAll(cache,pattern.split(queryRegex));
                cache.stream().forEach(s->queryRegexCache.add(compile(s.trim())));
            }
        }
        return queryRegexCache;
    }

    public List<Pattern> refreshCodeRegexCache(boolean force){
        if(force || codeRegexCache.isEmpty()){
            codeRegexCache.clear();
            if(nonEmpty(codeRegex)){
                List<String> cache = new CopyOnWriteArrayList<>();
                addAll(cache,pattern.split(codeRegex));
                cache.stream().forEach(s->codeRegexCache.add(compile(s.trim())));
            }
        }
        return codeRegexCache;
    }

    public List<String> refreshCodeFragmentCache(boolean force){
        if(force || codeFragmentCache.isEmpty()){
            codeFragmentCache.clear();
            if(nonEmpty(codeFragment)) addAll(codeFragmentCache,pattern.split(codeFragment));
        }
        return codeFragmentCache;
    }

    public String getProcessingMode(){
        return processingMode;
    }

    public void setProcessingMode(String processingMode){
        this.processingMode = processingMode;
    }

    public void setLineNumer(int start, int end){
        startLine = start;
        endLine = end;
        lineNumber = startLine + SPRT_LINE_NUMBER + endLine;
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
