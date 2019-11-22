package legend.util.entity;

import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.matchRange;
import static legend.util.ValueUtil.nonEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import legend.util.entity.intf.IILCode;

@XmlRootElement(name = "ILCodes")
@XmlType(propOrder = {"comment","mode","header","tail","partition","codes"})
public class ILCodes extends BaseEntity<ILCodes> implements IILCode{
    @XmlElement
    private String comment = ILCODES_COMMENT;
    @XmlElement
    private String mode = MODE_NATIVE;
    @XmlElement
    private String header = SIZE_IL_HEADER + S_EMPTY;
    @XmlElement
    private String tail = SIZE_IL_TAIL + S_EMPTY;
    @XmlElement
    private String partition = SIZE_IL_PARTITION + S_EMPTY;
    @XmlElementRef
    private List<ILCode> codes;
    @XmlTransient
    private int headerSize = SIZE_IL_HEADER;
    @XmlTransient
    private int tailSize = SIZE_IL_TAIL;
    @XmlTransient
    private int partitionSize = SIZE_IL_PARTITION;
    @XmlTransient
    private int maxLine;
    @XmlTransient
    protected static final Pattern headerPattern;
    @XmlTransient
    protected static final Pattern partitionPattern;
    static{
        headerPattern = compile(REG_NUM);
        partitionPattern = compile(REG_NUM_NATURAL);
    }

    public ILCodes(){
        codes = new ArrayList<>();
        codes.add(new ILCode());
    }

    @Override
    public ILCodes cloneValue(){
        ILCodes ilCodes = new ILCodes();
        ilCodes.mode = mode;
        ilCodes.header = header;
        ilCodes.tail = tail;
        ilCodes.partition = partition;
        ilCodes.codes.addAll(codes);
        return ilCodes;
    }

    @Override
    public <V> boolean validate(V v){
        if(!MODE_NATIVE.equals(mode) && !MODE_REPL.equals(mode)) mode = MODE_NATIVE;
        if(nonEmpty(header) && headerPattern.matcher(header).matches()) headerSize = Integer.parseInt(header);
        else headerSize = SIZE_IL_HEADER;
        header = headerSize + S_EMPTY;
        if(nonEmpty(tail) && headerPattern.matcher(tail).matches()) tailSize = Integer.parseInt(tail);
        else tailSize = SIZE_IL_TAIL;
        tail = tailSize + S_EMPTY;
        if(nonEmpty(partition) && partitionPattern.matcher(partition).matches()) partitionSize = Integer.parseInt(partition);
        else partitionSize = SIZE_IL_PARTITION;
        if(partitionSize < SIZE_IL_PARTITION_MIN) partitionSize = SIZE_IL_PARTITION_MIN;
        partition = partitionSize + S_EMPTY;
        if(isEmpty(codes)){
            errorInfo = ERR_ILCODE_NON;
            return false;
        }
        if(!codes.parallelStream().anyMatch(code->{
            if(!code.validate()){
                errorInfo = code.errorInfo;
                return true;
            }
            if(MODE_NATIVE.equals(mode)){
                if(!MODE_NATIVE.equals(code.getProcessingMode()) && isEmpty(code.getQueryRegex())){
                    errorInfo = ERR_QUERY_REGEX;
                    return true;
                }
                if(MODE_REPL.equals(code.getProcessingMode())){
                    if(!matchRange(code.refreshCodeRegexCache(false).size(),1,2)){
                        errorInfo = ERR_CODE_REGEX;
                        return true;
                    }
                }
            }
            return false;
        })){
            int maxLine = v instanceof Integer ? (Integer)v : this.maxLine;
            if(0 < this.maxLine){
                sortCodes(codes);
                ILCode code = codes.get(0);
                if(1 != code.startLine){
                    errorInfo = gsph(ERR_LINE_NUM_VAL_MIN,code.getLineNumber());
                    return false;
                }
                code = codes.get(codes.size() - 1);
                if(code.endLine != maxLine){
                    errorInfo = gsph(ERR_LINE_NUM_VAL_MAX,new String[]{code.getLineNumber(),maxLine + ""});
                    return false;
                }
                for(int i = 0;i < codes.size() - 1;){
                    code = codes.get(i);
                    ILCode nextCode = codes.get(++i);
                    if(MODE_ADD.equals(nextCode.getProcessingMode()) && nextCode.startLine != code.endLine){
                        errorInfo = gsph(ERR_LINE_NUM_ADJION_ADD,nextCode.getLineNumber(),code.getLineNumber());
                        return false;
                    }
                    if(!MODE_ADD.equals(nextCode.getProcessingMode()) && nextCode.startLine != code.endLine + 1){
                        errorInfo = gsph(ERR_LINE_NUM_ADJION_OTHER,nextCode.getLineNumber(),code.getLineNumber());
                        return false;
                    }
                }
            }
            this.maxLine = maxLine;
            return true;
        }
        return false;
    }

    @Override
    public ILCodes trim(){
        mode = mode.trim();
        header = header.trim();
        partition = partition.trim();
        codes.parallelStream().forEach(code->code.trim());
        return this;
    }

    public void regenSortedCodes(List<ILCode> ilCodes){
        if(isEmpty(ilCodes)) return;
        codes.clear();
        sortCodes(ilCodes);
        ILCode code;
        int start, end, size = ilCodes.size();
        for(int i = 0;i < size;i++){
            code = ilCodes.get(i);
            if(0 == i){
                start = 1;
                end = MODE_ADD.equals(code.getProcessingMode()) ? code.getStartLine() : code.getStartLine() - 1;
                if(0 < end) addNativeCode(start,end);
            }else{
                start = ilCodes.get(i - 1).getEndLine() + 1;
                end = MODE_ADD.equals(code.getProcessingMode()) ? code.getStartLine() : code.getStartLine() - 1;
                addNativeCode(start,end);
            }
            codes.add(code);
        }
        code = ilCodes.get(size - 1);
        start = code.getEndLine() + 1;
        if(start <= maxLine) addNativeCode(start,maxLine);
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

    private void addNativeCode(int start, int end){
        ILCode nativeCode = new ILCode();
        nativeCode.setLineNumer(start,end);
        codes.add(nativeCode);
    }

    public int getHeaderSize(){
        return headerSize;
    }

    public int getTailSize(){
        return tailSize;
    }

    public int getPartitionSize(){
        return partitionSize;
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
