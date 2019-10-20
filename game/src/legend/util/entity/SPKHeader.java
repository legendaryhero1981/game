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
@XmlType(propOrder = {"headerSize","headerFlag","filePathExpr","fileSizeExpr","fileStartPosExpr"})
public class SPKHeader extends BaseEntity<SPKHeader> implements IFileSPK{
    @XmlElement
    private String headerSize = S_EMPTY;
    @XmlElement
    private String headerFlag = S_EMPTY;
    @XmlElement
    private String filePathExpr = S_EMPTY;
    @XmlElement
    private String fileSizeExpr = S_EMPTY;
    @XmlElement
    private String fileStartPosExpr = S_EMPTY;
    @XmlTransient
    protected int size;
    @XmlTransient
    protected byte[] flags = new byte[0];
    @XmlTransient
    protected MetaData filePath = new MetaData();
    @XmlTransient
    protected MetaData fileSize = new MetaData();
    @XmlTransient
    protected MetaData fileStartPos = new MetaData();
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
        protected int length;

        public int getStart(){
            return start;
        }

        public int getLength(){
            return length;
        }
    }

    @Override
    public SPKHeader trim(){
        headerFlag = headerFlag.trim();
        headerSize = headerSize.trim();
        filePathExpr = filePathExpr.trim();
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
            }else size = Integer.parseInt(headerSize);
        }else if(nonEmpty(headerFlag)){
            Matcher matcher = flagHexPattern.matcher(headerFlag);
            if(matcher.matches()) flags = hexToBytes(matcher.group(1));
            else flags = convertParam(headerFlag,false).getBytes();
        }else if(nonEmpty(filePathExpr)) validateSizeExpr(value,filePath,filePathExpr,"filePathExpr");
        else if(nonEmpty(fileSizeExpr)) validateSizeExpr(value,fileSize,fileSizeExpr,"fileSizeExpr");
        else if(nonEmpty(fileStartPosExpr)) validateSizeExpr(value,fileStartPos,fileStartPosExpr,"fileStartPosExpr");
        return value.getValue();
    }

    private void validateSizeExpr(IValue<Boolean> value, MetaData metaData, String expr, String field){
        Matcher matcher = sizeExprPattern.matcher(expr);
        if(matcher.matches()){
            metaData.start = Integer.parseInt(matcher.group(1));
            String length = matcher.group(3);
            metaData.length = nonEmpty(length) ? Integer.parseInt(length) : 0;
        }else{
            errorInfo = gsph(ERR_SPKH_EXPR_DESC,field);
            value.setValue(false);
        }
    }

    public int getSize(){
        return size;
    }

    public byte[] getFlags(){
        return flags;
    }

    public MetaData getFilePath(){
        return filePath;
    }

    public MetaData getFileSize(){
        return fileSize;
    }

    public MetaData getFileStartPos(){
        return fileStartPos;
    }

    protected String getHeaderSize(){
        return headerSize;
    }

    protected String getHeaderFlag(){
        return headerFlag;
    }

    protected String getFilePathExpr(){
        return filePathExpr;
    }

    protected String getFileSizeExpr(){
        return fileSizeExpr;
    }

    protected String getFileStartPosExpr(){
        return fileStartPosExpr;
    }
}
