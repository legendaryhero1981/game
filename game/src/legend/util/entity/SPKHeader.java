package legend.util.entity;

import static legend.util.StringUtil.glph;
import static legend.util.StringUtil.hexToBytes;
import static legend.util.ValueUtil.nonEmpty;
import static legend.util.param.FileParam.convertParam;

import java.nio.file.Path;
import java.util.regex.Matcher;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import legend.intf.IValue;
import legend.util.entity.intf.IFileSPK;
import legend.util.param.SingleValue;

@XmlRootElement(name = "SPKHeader")
@XmlType(propOrder = {"headerSize","headerFlag","recordSizeExpr","fileStartPosExpr","fileSizeExpr","pathSizeExpr"})
public class SPKHeader extends BaseEntity<SPKHeader> implements IFileSPK{
    @XmlElement
    private String headerSize = S_EMPTY;
    @XmlElement
    private String headerFlag = S_EMPTY;
    @XmlElement
    private String recordSizeExpr = S_EMPTY;
    @XmlElement
    private String fileStartPosExpr = S_EMPTY;
    @XmlElement
    private String fileSizeExpr = S_EMPTY;
    @XmlElement
    private String pathSizeExpr = S_EMPTY;
    @XmlTransient
    protected MetaData headerSizeData = new MetaData();
    @XmlTransient
    protected MetaData headerFlagData = new MetaData();
    @XmlTransient
    protected MetaData recordSizeData = new MetaData();
    @XmlTransient
    protected MetaData fileStartPosData = new MetaData();
    @XmlTransient
    protected MetaData fileSizeData = new MetaData();
    @XmlTransient
    protected MetaData pathSizeData = new MetaData();

    public static final class MetaData{
        protected int size;
        protected int offset;
        protected int position;
        protected int deviation;
        protected byte[] bytes;
        protected byte[] nulbytes;
        protected int pathSize;
        protected Path filePath;

        public int getSize(){
            return size;
        }

        public void setSize(int size){
            this.size = size;
        }

        public int getOffset(){
            return offset;
        }

        public void setOffset(int offset){
            this.offset = offset;
        }

        public int getPosition(){
            return position;
        }

        public void setPosition(int position){
            this.position = position;
        }

        public int getDeviation(){
            return deviation;
        }

        public void setDeviation(int deviation){
            this.deviation = deviation;
        }

        public byte[] getBytes(){
            return bytes;
        }

        public void setBytes(byte[] bytes){
            this.bytes = bytes;
        }

        public byte[] getNulbytes(){
            return nulbytes;
        }

        public void setNulbytes(byte[] nulbytes){
            this.nulbytes = nulbytes;
        }

        public int getPathSize(){
            return pathSize;
        }

        public void setPathSize(int pathSize){
            this.pathSize = pathSize;
        }

        public Path getFilePath(){
            return filePath;
        }

        public void setFilePath(Path filePath){
            this.filePath = filePath;
        }
    }

    @Override
    public SPKHeader trim(){
        headerFlag = headerFlag.strip();
        headerSize = headerSize.strip();
        recordSizeExpr = recordSizeExpr.strip();
        fileStartPosExpr = fileStartPosExpr.strip();
        fileSizeExpr = fileSizeExpr.strip();
        pathSizeExpr = pathSizeExpr.strip();
        return this;
    }

    @Override
    public boolean validate(){
        IValue<Boolean> value = new SingleValue<>(true);
        if(nonEmpty(headerSize)){
            Matcher matcher = PTRN_SPK_SIZE.matcher(headerSize);
            if(!matcher.matches()){
                errorInfo = glph(ERR_SPKH_EXPR_FMT,"headerSize");
                value.setValue(false);
            }else headerSizeData.size = Integer.parseInt(headerSize);
        }
        if(nonEmpty(headerFlag) && value.getValue()){
            Matcher matcher = PTRN_SPK_FLAG_HEX.matcher(headerFlag);
            if(matcher.matches()){
                if(0 == matcher.group(1).length() % 2) headerFlagData.bytes = hexToBytes(matcher.group(1));
                else errorInfo += glph(ERR_SPKH_HEX_FMT,"headerFlag");
            }else headerFlagData.bytes = convertParam(headerFlag,false).getBytes();
        }
        if(nonEmpty(recordSizeExpr) && value.getValue()) validateSizeExpr(value,recordSizeData,recordSizeExpr,"recordSizeExpr");
        if(nonEmpty(fileStartPosExpr) && value.getValue()) validateSizeExpr(value,fileStartPosData,fileStartPosExpr,"fileStartPosExpr");
        if(nonEmpty(fileSizeExpr) && value.getValue()) validateSizeExpr(value,fileSizeData,fileSizeExpr,"fileSizeExpr");
        if(nonEmpty(pathSizeExpr) && value.getValue()) validateSizeExpr(value,pathSizeData,pathSizeExpr,"pathSizeExpr");
        return value.getValue();
    }

    private void validateSizeExpr(IValue<Boolean> value, MetaData metaData, String expr, String field){
        Matcher matcher = PTRN_SPK_SIZE_EXPR.matcher(expr);
        if(matcher.matches()){
            metaData.offset = Integer.parseInt(matcher.group(1));
            String size = matcher.group(2);
            metaData.size = nonEmpty(size) ? Integer.parseInt(size) : 1;
        }else{
            errorInfo += glph(ERR_SPKH_EXPR_FMT,field);
            value.setValue(false);
        }
    }

    public MetaData getHeaderSizeData(){
        return headerSizeData;
    }

    public MetaData getHeaderFlagData(){
        return headerFlagData;
    }

    public MetaData getRecordSizeData(){
        return recordSizeData;
    }

    public MetaData getFileStartPosData(){
        return fileStartPosData;
    }

    public MetaData getFileSizeData(){
        return fileSizeData;
    }

    public MetaData getPathSizeData(){
        return pathSizeData;
    }

    protected String getHeaderSize(){
        return headerSize;
    }

    protected String getHeaderFlag(){
        return headerFlag;
    }

    protected String getRecordSizeExpr(){
        return recordSizeExpr;
    }

    protected String getFileStartPosExpr(){
        return fileStartPosExpr;
    }

    protected String getFileSizeExpr(){
        return fileSizeExpr;
    }

    protected String getPathSizeExpr(){
        return pathSizeExpr;
    }
}
