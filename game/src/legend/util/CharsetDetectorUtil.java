package legend.util;

import static java.nio.file.Files.newInputStream;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.matchRange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import legend.util.chardet.nsDetector;
import legend.util.intf.ICharsetDetectorUtil;
import legend.util.param.SingleValue;

public final class CharsetDetectorUtil implements ICharsetDetectorUtil{
    private CharsetDetectorUtil(){}

    public static String detectorFileCharset(Path path, Language language){
        SingleValue<Integer> bytesCount = new SingleValue<>(0);
        try(InputStream inputStream = newInputStream(path)){
            byte[] bom = new byte[3];
            int i, r = inputStream.read(bom);
            if(-1 == r) return CHARSET_GBK;
            if(BOM_UTF8[0] == bom[0] && BOM_UTF8[1] == bom[1] && BOM_UTF8[2] == bom[2]) return CHARSET_UTF8_BOM;
            if(BOM_UTF16LE[0] == bom[0] && BOM_UTF16LE[1] == bom[1]) return CHARSET_UTF16LE;
            if(BOM_UTF16BE[0] == bom[0] && BOM_UTF16BE[1] == bom[1]) return CHARSET_UTF16BE;
            byte[] bytes = new byte[BLOCK_SIZE_FILE + 3];
            for(i = 0;i < bom.length;i++)
                bytes[i] = bom[i];
            r = inputStream.read(bytes,3,BLOCK_SIZE_FILE);
            do{
                for(i = 0;i < r && matchCharsetWithUTF8(bytes,i,bytesCount);i += bytesCount.getValue());
            }while(0 < bytesCount.getValue() && -1 != (r = inputStream.read(bytes,0,BLOCK_SIZE_FILE)));
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_READ,path.toString(),e.toString()));
        }
        if(0 < bytesCount.getValue()) return CHARSET_UTF8;
        return detectorCharset(path,language);
    }

    private static String detectorCharset(Path path, Language language){
        SingleValue<String> result = new SingleValue<>(CHARSET_UTF8);
        try(InputStream inputStream = newInputStream(path)){
            nsDetector detector = new nsDetector(language.ordinal());
            detector.init(cs->result.setValue(cs));
            byte[] bytes = new byte[BLOCK_SIZE_FILE];
            for(int r;-1 != (r = inputStream.read(bytes,0,BLOCK_SIZE_FILE));)
                if(!detector.isAscii(bytes,r) && detector.doIt(bytes,r,false)) break;
            detector.dataEnd();
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_READ,path.toString(),e.toString()));
        }
        return result.getValue();
    }

    private static boolean matchCharsetWithUTF8(byte[] bytes, int index, SingleValue<Integer> bytesCount){
        if(0 <= bytes[index]) bytesCount.setValue(1);
        else if(matchRange(bytes[index],-64,-33)){
            if(matchRange(bytes[index + 1],-128,-65)) bytesCount.setValue(2);
            else bytesCount.setValue(0);
        }else if(matchRange(bytes[index],-32,-17)){
            if(matchRange(bytes[index + 1],-128,-65) && matchRange(bytes[index + 2],-128,-65)) bytesCount.setValue(3);
            else bytesCount.setValue(0);
        }else if(matchRange(bytes[index],-16,-9)){
            if(matchRange(bytes[index + 1],-128,-65) && matchRange(bytes[index + 2],-128,-65) && matchRange(bytes[index + 3],-128,-65)) bytesCount.setValue(4);
            else bytesCount.setValue(0);
        }else bytesCount.setValue(0);
        return 0 < bytesCount.getValue();
    }
}
