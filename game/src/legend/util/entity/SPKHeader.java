package legend.util.entity;

import static java.util.regex.Pattern.compile;
import static legend.util.StringUtil.gsph;
import static legend.util.StringUtil.hexToBytes;
import static legend.util.ValueUtil.nonEmpty;
import static legend.util.param.FileParam.convertParam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import legend.intf.IValue;
import legend.util.entity.intf.IFileSPK;
import legend.util.param.SingleValue;

@XmlRootElement(name = "SPKHeader")
@XmlType(propOrder = {"headerSize","headerFlag","fileSizeExpr","fileStartPosExpr"})
public class SPKHeader extends BaseEntity<SPKHeader> implements IFileSPK{
    @XmlElement
    private String headerSize = S_EMPTY;
    @XmlElement
    private String headerFlag = S_EMPTY;
    @XmlElement
    private String fileSizeExpr = S_EMPTY;
    @XmlElement
    private String fileStartPosExpr = S_EMPTY;
    @XmlTransient
    protected MetaData headerSizeData = new MetaData();
    @XmlTransient
    protected MetaData headerFlagData = new MetaData();
    @XmlTransient
    protected MetaData fileSizeData = new MetaData();
    @XmlTransient
    protected MetaData fileStartPosData = new MetaData();
    @XmlTransient
    protected static final Pattern sizePattern;
    @XmlTransient
    protected static final Pattern sizeExprPattern;
    @XmlTransient
    protected static final Pattern flagHexPattern;
    static{
        sizePattern = compile(REG_SPK_SIZE);
        sizeExprPattern = compile(REG_SPK_SIZE_EXPR);
        flagHexPattern = compile(REG_SPK_FLAG_HEX);
    }

    public static final class MetaData{
        protected int start;
        protected int size;
        protected byte[] bytes;

        public int getStart(){
            return start;
        }

        public int getSize(){
            return size;
        }

        public byte[] getBytes(){
            return bytes;
        }
    }

    @Override
    public SPKHeader trim(){
        headerFlag = headerFlag.trim();
        headerSize = headerSize.trim();
        fileSizeExpr = fileSizeExpr.trim();
        fileStartPosExpr = fileStartPosExpr.trim();
        return this;
    }

    @Override
    public boolean validate(){
        IValue<Boolean> value = new SingleValue<>(true);
        if(nonEmpty(headerSize)){
            Matcher matcher = sizePattern.matcher(headerSize);
            if(!matcher.matches()){
                errorInfo = gsph(ERR_SPKH_EXPR_DESC,"headerSize");
                value.setValue(false);
            }else headerSizeData.size = Integer.parseInt(headerSize);
        }else if(nonEmpty(headerFlag)){
            Matcher matcher = flagHexPattern.matcher(headerFlag);
            if(matcher.matches()) headerFlagData.bytes = hexToBytes(matcher.group(1));
            else headerFlagData.bytes = convertParam(headerFlag,false).getBytes();
        }else if(nonEmpty(fileSizeExpr)) validateSizeExpr(value,fileSizeData,fileSizeExpr,"fileSizeExpr");
        else if(nonEmpty(fileStartPosExpr)) validateSizeExpr(value,fileStartPosData,fileStartPosExpr,"fileStartPosExpr");
        return value.getValue();
    }

    private void validateSizeExpr(IValue<Boolean> value, MetaData metaData, String expr, String field){
        Matcher matcher = sizeExprPattern.matcher(expr);
        if(matcher.matches()){
            metaData.start = Integer.parseInt(matcher.group(1));
            String size = matcher.group(3);
            metaData.size = nonEmpty(size) ? Integer.parseInt(size) : 4;
        }else{
            errorInfo = gsph(ERR_SPKH_EXPR_DESC,field);
            value.setValue(false);
        }
    }

    protected MetaData getSizeData(){
        return headerSizeData;
    }

    protected MetaData getFlagData(){
        return headerFlagData;
    }

    protected MetaData getFileSizeData(){
        return fileSizeData;
    }

    protected MetaData getFileStartPosData(){
        return fileStartPosData;
    }

    protected String getHeaderSize(){
        return headerSize;
    }

    protected String getHeaderFlag(){
        return headerFlag;
    }

    protected String getFileSizeExpr(){
        return fileSizeExpr;
    }

    protected String getFileStartPosExpr(){
        return fileStartPosExpr;
    }
}
