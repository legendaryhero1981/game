package legend.util;

import static java.nio.ByteBuffer.allocate;
import static java.nio.file.Files.newByteChannel;
import static legend.util.ConsoleUtil.CS;
import static legend.util.StringUtil.gsph;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.security.MessageDigest;

import legend.util.intf.IMD5Util;

public final class MD5Util implements IMD5Util{
    private MD5Util(){}

    public static String getGuidU32(String s){
        s = getMD5U32(s);
        return s.substring(0,8) + "-" + s.substring(8,12) + "-" + s.substring(12,16) + "-" + s.substring(16,20) + "-" + s.substring(20,32);
    }

    public static String getGuidL32(String s){
        s = getMD5L32(s);
        return s.substring(0,8) + "-" + s.substring(8,12) + "-" + s.substring(12,16) + "-" + s.substring(16,20) + "-" + s.substring(20,32);
    }

    public static String getMD5U16(String s){
        return getMD5(s,0xF).toUpperCase();
    }

    public static String getMD5L16(String s){
        return getMD5(s,0xF).toLowerCase();
    }

    public static String getMD5U32(String s){
        return getMD5(s,0xFF).toUpperCase();
    }

    public static String getMD5L32(String s){
        return getMD5(s,0xFF).toLowerCase();
    }

    public static String getGuidU32(Path path){
        String s = getMD5U32(path);
        return s.substring(0,8) + "-" + s.substring(8,12) + "-" + s.substring(12,16) + "-" + s.substring(16,20) + "-" + s.substring(20,32);
    }

    public static String getGuidL32(Path path){
        String s = getMD5L32(path);
        return s.substring(0,8) + "-" + s.substring(8,12) + "-" + s.substring(12,16) + "-" + s.substring(16,20) + "-" + s.substring(20,32);
    }

    public static String getMD5U16(Path path){
        return getMD5(path,0xF).toUpperCase();
    }

    public static String getMD5L16(Path path){
        return getMD5(path,0xF).toLowerCase();
    }

    public static String getMD5U32(Path path){
        return getMD5(path,0xFF).toUpperCase();
    }

    public static String getMD5L32(Path path){
        return getMD5(path,0xFF).toLowerCase();
    }

    public static String cookHashByMD5(String path){
        File file = new File(path);
        int pos = path.length() + 1;
        StringBuilder sb = new StringBuilder();
        fillFilePaths(file,pos,sb);
        return sb.toString();
    }

    private static void fillFilePaths(File file, int pos, StringBuilder sb){
        if(file.isFile()){
            String line = file.getPath().substring(pos);
            line = "\"" + line + "\" " + getMD5U16(line) + SPRT_LINE;
            sb.append(line);
        }else{
            File[] files = file.listFiles();
            for(int i = 0;i < files.length;i++){
                fillFilePaths(files[i],pos,sb);
            }
        }
    }

    private static String getMD5(String s, int mode){
        StringBuffer sb = new StringBuffer(S_EMPTY);
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(s.getBytes("UTF-8"));
            for(int i = 0;i < bytes.length;i++){
                String hex = Integer.toHexString(bytes[i] & mode);
                if(mode == 0xFF && hex.length() < 2) sb.append("0");
                sb.append(hex);
            }
        }catch(Exception e){
            CS.sl(gsph(ERR_MD5_CRT,e.toString()));
        }
        return sb.toString();
    }

    private static String getMD5(Path path, int mode){
        StringBuffer sb = new StringBuffer(S_EMPTY);
        try(SeekableByteChannel byteChannel = newByteChannel(path)){
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            ByteBuffer byteBuffer = allocate(BUFFER_SIZE);
            for(int size = byteChannel.read(byteBuffer);0 < size;size = byteChannel.read(byteBuffer)){
                messageDigest.update(byteBuffer.array(),0,size);
                byteBuffer.clear();
            }
            byte[] bytes = messageDigest.digest();
            for(int i = 0;i < bytes.length;i++){
                String hex = Integer.toHexString(bytes[i] & mode);
                if(mode == 0xFF && hex.length() < 2) sb.append("0");
                sb.append(hex);
            }
        }catch(Exception e){
            CS.sl(gsph(ERR_MD5_CRT,e.toString()));
        }
        return sb.toString();
    }
}
