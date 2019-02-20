package legend.util.entity;

import static java.util.stream.Collectors.toList;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.matchRange;
import static legend.util.ValueUtil.nonEmpty;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import legend.intf.IValue;
import legend.util.intf.IILCode;

@XmlRootElement(name = "ILCodes")
@XmlType(propOrder = {"comment","mode","codes"})
public class ILCodes implements IILCode,IValue<ILCodes>{
    @XmlElement
    private String comment = ILCODES_COMMENT;
    @XmlElement
    private String mode = MODE_NATIVE;
    @XmlElementRef
    private List<ILCode> codes = new CopyOnWriteArrayList<>();
    @XmlTransient
    private String errorInfo = "";
    @XmlTransient
    private int maxLine;

    @Override
    public ILCodes cloneValue(){
        ILCodes ilCodes = new ILCodes();
        ilCodes.comment = comment;
        ilCodes.mode = mode;
        ilCodes.codes.addAll(codes);
        return ilCodes;
    }

    public boolean validate(int maxLine){
        this.maxLine = 0 < maxLine ? maxLine : 0;
        if(MODE_NATIVE != mode && MODE_REPL != mode) mode = MODE_NATIVE;
        if(isEmpty(codes)){
            errorInfo = ERR_ILCODE_NON;
            return false;
        }
        if(!codes.parallelStream().anyMatch(code->{
            if(!code.validate()){
                errorInfo = code.errorInfo;
                return true;
            }
            if(MODE_NATIVE == mode){
                if(MODE_NATIVE != code.processingMode && nonEmpty(code.queryRegex)){
                    errorInfo = ERR_QUERY_REGEX;
                    return true;
                }
                if(MODE_REPL == code.processingMode){
                    if(matchRange(code.refreshCodeRegexCache(false).size(),1,2)){
                        errorInfo = ERR_QUERY_REGEX;
                        return true;
                    }
                }
            }
            return false;
        })){
            if(0 < this.maxLine){
                sortCodes(codes);
                ILCode code = codes.get(0);
                if(1 != code.startLine){
                    errorInfo = gsph(ERR_LINE_NUM_VAL_MIN,code.lineNumber);
                    return false;
                }
                code = codes.get(codes.size() - 1);
                if(this.maxLine != code.endLine){
                    errorInfo = gsph(ERR_LINE_NUM_VAL_MAX,code.lineNumber);
                    return false;
                }
                for(int i = 0;i < codes.size() - 1;){
                    code = codes.get(i);
                    ILCode nextCode = codes.get(++i);
                    if(MODE_ADD == nextCode.processingMode && nextCode.startLine != code.endLine){
                        errorInfo = gsph(ERR_LINE_NUM_ADJION_ADD,nextCode.lineNumber,code.lineNumber);
                        return false;
                    }
                    if(MODE_ADD != nextCode.processingMode && nextCode.startLine != code.endLine){
                        errorInfo = gsph(ERR_LINE_NUM_ADJION_OTHER,nextCode.lineNumber,code.lineNumber);
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public ILCodes trim(){
        mode = mode.trim();
        codes.parallelStream().forEach(code->code.trim());
        return this;
    }

    public void regenSortedCodes(List<ILCode> ilCodes){
        if(isEmpty(ilCodes)) return;
        codes.clear();
        sortCodes(ilCodes);
        ILCode nativeCode, code = ilCodes.get(0);
        int start = 1, end = code.startLine;
        if(MODE_ADD == code.processingMode){
            nativeCode = new ILCode();
            nativeCode.setLineNumer(start,end);
            codes.add(nativeCode);
            start = end + 1;
        }else if(1 < end){
            nativeCode = new ILCode();
            nativeCode.setLineNumer(start,end - 1);
            codes.add(nativeCode);
            start = code.endLine + 1;
        }
        for(int i = 1;i < ilCodes.size();i++){
            code = ilCodes.get(i);
            end = MODE_ADD == code.processingMode ? code.startLine : code.startLine - 1;
            if(start <= end){
                nativeCode = new ILCode();
                nativeCode.setLineNumer(start,end);
                codes.add(nativeCode);
            }
            start = code.endLine + 1;
        }
    }

    private void sortCodes(List<ILCode> codes){
        List<ILCode> caches = codes.stream().sorted((ILCode code1, ILCode code2)->{
            int start1 = code1.startLine, start2 = code2.startLine;
            if(start1 == start2) return 0;
            return start1 > start2 ? 1 : -1;
        }).collect(toList());
        codes.clear();
        codes.addAll(caches);
    }

    public String errorInfo(){
        return errorInfo;
    }

    public String getMode(){
        return mode;
    }

    public void setMode(String mode){
        this.mode = mode;
    }

    public List<ILCode> getCodes(){
        return codes;
    }

    public void setCodes(List<ILCode> codes){
        this.codes = codes;
    }
}
