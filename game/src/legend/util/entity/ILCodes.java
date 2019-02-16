package legend.util.entity;

import static java.util.Collections.addAll;
import static legend.util.ValueUtil.nonEmpty;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import legend.util.intf.IFileUtil;

@XmlRootElement(name = "ILCodes")
@XmlType(propOrder = {"comment","mode","codes"})
public class ILCodes implements IFileUtil{
    @XmlElement
    private String comment = ILCODES_COMMENT;
    @XmlElement
    private String mode = MODE_IL_NATIVE;
    @XmlElementRef
    private List<ILCode> codes = new CopyOnWriteArrayList<>();

    public boolean validate(){
        if(MODE_IL_NATIVE != mode && MODE_IL_MOD != mode) mode = MODE_IL_NATIVE;
        if(nonEmpty(codes) && !codes.parallelStream().anyMatch(code->!code.validate()) && 1 == sortedCodes().get(0).getStartLine()){
            for(int i = 0,start,end;i < codes.size() - 1;){
                end = codes.get(i).getEndLine();
                start = codes.get(++i).getStartLine();
                if(start != end + 1) return false;
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

    public List<ILCode> sortedCodes(){
        ILCode[] codeArray = (ILCode[])codes.stream().sorted(new ILCodeComparator()).toArray();
        codes.clear();
        addAll(codes,codeArray);
        return codes;
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

    private static class ILCodeComparator implements Comparator<ILCode>{
        @Override
        public int compare(ILCode code1, ILCode code2){
            int start1 = code1.getStartLine(), start2 = code2.getStartLine();
            if(start1 == start2) return 0;
            return start1 > start2 ? 1 : -1;
        }
    }
}
