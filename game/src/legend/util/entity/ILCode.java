package legend.util.entity;

import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.nonEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import legend.intf.IValue;
import legend.util.intf.IILCode;

@XmlRootElement(name = "ILCode")
@XmlType(propOrder = {"processingMode","quoteMode","lineNumber","queryRegex","codeRegex","codeDesc","codeFragment"})
public class ILCode implements IILCode,IValue<ILCode>{
    @XmlElement
    private String processingMode = MODE_NATIVE;
    @XmlElement
    private String quoteMode = MODE_NATIVE;
    @XmlElement
    private String lineNumber = "1-1";
    @XmlElement
    private String queryRegex = "";
    @XmlElement
    private String codeRegex = "";
    @XmlElement
    private String codeDesc = "";
    @XmlElement
    private String codeFragment = "";
    @XmlTransient
    protected String errorInfo = "";
    @XmlTransient
    protected int startLine = 1;
    @XmlTransient
    protected int endLine = 1;
    @XmlTransient
    private Pattern pattern = compile(REG_SPRT_CODE);
    @XmlTransient
    private List<Pattern> queryRegexCache = new ArrayList<>();
    @XmlTransient
    private List<Pattern> codeRegexCache = new ArrayList<>();
    @XmlTransient
    private List<String> codeFragmentCache = new ArrayList<>();

    @Override
    public ILCode cloneValue(){
        ILCode ilCode = new ILCode();
        ilCode.processingMode = processingMode;
        ilCode.quoteMode = quoteMode;
        ilCode.lineNumber = lineNumber;
        ilCode.queryRegex = queryRegex;
        ilCode.codeRegex = codeRegex;
        ilCode.codeDesc = codeDesc;
        ilCode.codeFragment = codeFragment;
        ilCode.startLine = startLine;
        ilCode.endLine = endLine;
        return ilCode;
    }

    protected boolean validate(){
        if(!MODE_NATIVE.equals(processingMode) && !MODE_REPL.equals(processingMode) && !MODE_ADD.equals(processingMode)) processingMode = MODE_NATIVE;
        if(!MODE_NATIVE.equals(quoteMode) && !MODE_REPL.equals(quoteMode)) quoteMode = MODE_NATIVE;
        Matcher matcher = compile(REG_LINE_NUMBER).matcher(lineNumber);
        if(!matcher.matches()){
            errorInfo = ERR_LINE_NUM_FORMAT;
            return false;
        }
        startLine = Integer.parseInt(matcher.group(1));
        endLine = nonEmpty(matcher.group(3)) ? Integer.parseInt(matcher.group(3)) : startLine;
        if(!MODE_ADD.equals(processingMode) && startLine > endLine){
            errorInfo = gsph(ERR_LINE_NUM_VAL_LESS,lineNumber);
            return false;
        }
        if(MODE_ADD.equals(processingMode) && startLine != endLine){
            errorInfo = gsph(ERR_LINE_NUM_VAL_EQUAL,lineNumber);
            return false;
        }
        return true;
    }

    protected ILCode trim(){
        processingMode = processingMode.trim();
        quoteMode = quoteMode.trim();
        lineNumber = lineNumber.trim();
        codeDesc = codeDesc.trim();
        return this;
    }

    protected String getLineNumber(){
        return lineNumber;
    }

    protected String getQueryRegex(){
        return queryRegex;
    }

    public List<Pattern> refreshQueryRegexCache(boolean force){
        if(force || queryRegexCache.isEmpty()){
            queryRegexCache.clear();
            if(nonEmpty(queryRegex)) generateRegexCaches(queryRegex).stream().forEach(s->queryRegexCache.add(compile(s)));
        }
        return queryRegexCache;
    }

    public List<Pattern> refreshCodeRegexCache(boolean force){
        if(force || codeRegexCache.isEmpty()){
            codeRegexCache.clear();
            if(nonEmpty(codeRegex)) generateRegexCaches(codeRegex).stream().forEach(s->codeRegexCache.add(compile(s)));
        }
        return codeRegexCache;
    }

    public List<String> refreshCodeFragmentCache(boolean force){
        if(force || codeFragmentCache.isEmpty()){
            codeFragmentCache.clear();
            if(nonEmpty(codeFragment)){
                String[] codes = pattern.split(codeFragment);
                for(int i = 0;i < codes.length;i++)
                    if(nonEmpty(codes[i].trim())) codeFragmentCache.add(codes[i]);
            }
        }
        return codeFragmentCache;
    }

    private List<String> generateRegexCaches(String regex){
        List<String> caches = new ArrayList<>();
        String[] regexes = pattern.split(regex);
        for(int i = 0;i < regexes.length;i++){
            regex = regexes[i].trim();
            if(nonEmpty(regex)){
                if(MODE_NATIVE.equals(quoteMode)) caches.add(regex);
                else caches.add(quote(regex));
            }
        }
        return caches;
    }

    public String getProcessingMode(){
        return processingMode;
    }

    public int getStartLine(){
        return startLine;
    }

    public int getEndLine(){
        return endLine;
    }

    public void setLineNumer(int start, int end){
        startLine = start;
        endLine = end;
        lineNumber = startLine + SPRT_LINE_NUMBER + endLine;
    }
}
