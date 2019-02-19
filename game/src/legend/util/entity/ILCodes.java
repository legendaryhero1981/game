package legend.util.entity;

import static java.util.Collections.addAll;
import static legend.util.ValueUtil.isEmpty;
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
    private int maxLineNumber;

    @Override
    public ILCodes cloneValue(){
        ILCodes ilCodes = new ILCodes();
        ilCodes.comment = comment;
        ilCodes.mode = mode;
        ilCodes.codes.addAll(codes);
        return ilCodes;
    }

    public boolean validate(int maxLineNumber){
        this.maxLineNumber = 0 < maxLineNumber ? maxLineNumber : 0;
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
            if(MODE_NATIVE == mode && MODE_NATIVE != code.getProcessingMode() && nonEmpty(code.getQueryRegex())){
                errorInfo = ERR_QUERY_REGEX;
                return true;
            }
            return false;
        })){
            if(0 < this.maxLineNumber){
                sortCodes(codes);
                if(1 != codes.get(0).getStartLine()){
                    errorInfo = ERR_LINE_NUM_VAL_MIN;
                    return false;
                }
                if(this.maxLineNumber != codes.get(codes.size() - 1).getEndLine()){
                    errorInfo = ERR_LINE_NUM_VAL_MAX;
                    return false;
                }
                for(int i = 0,start,end;i < codes.size() - 1;){
                    end = codes.get(i).getEndLine();
                    start = codes.get(++i).getStartLine();
                    if(start != end && start != end + 1){
                        errorInfo = ERR_LINE_NUM_VAL_ADJION;
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
        String processingMode = code.getProcessingMode();
        int start = 1, end = code.getStartLine();
        if(MODE_ADD == processingMode){
            nativeCode = new ILCode();
            nativeCode.setLineNumer(start,end);
            codes.add(nativeCode);
            start = end + 1;
        }else if(1 < end){
            nativeCode = new ILCode();
            nativeCode.setLineNumer(start,end - 1);
            codes.add(nativeCode);
            start = code.getEndLine() + 1;
        }
    }

    private void sortCodes(List<ILCode> codes){
        ILCode[] codeArray = (ILCode[])codes.stream().sorted((ILCode code1, ILCode code2)->{
            int start1 = code1.getStartLine(), start2 = code2.getStartLine();
            if(start1 == start2) return 0;
            return start1 > start2 ? 1 : -1;
        }).toArray();
        codes.clear();
        addAll(codes,codeArray);
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
