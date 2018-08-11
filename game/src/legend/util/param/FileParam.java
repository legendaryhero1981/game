package legend.util.param;

import static legend.intf.ICommonVar.gsph;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.ZipOutputStream;

import legend.intf.ICommonVar;
import legend.intf.IValue;

public class FileParam implements ICommonVar,IValue<FileParam>,AutoCloseable{
    private Path srcPath;
    private Path destPath;
    private Path backupPath;
    private Pattern pattern;
    private Pattern replacePattern;
    private Pattern askPattern;
    private String replacement;
    private String cmds;
    private String cmd;
    private String opt;
    private long minSize;
    private long maxSize;
    private int level;
    private int deflaterLevel;
    private ZipOutputStream zipOutputStream;
    private Stream<Path> pathStream;
    private ConcurrentMap<BasicFileAttributes,Path> pathMap;
    private ConcurrentMap<Path,Path> rePathMap;
    private ConcurrentMap<Path,List<Path>> pathsMap;
    private BlockingDeque<Path> pathDeque;
    private AtomicLong filesSize;
    private AtomicInteger filesCount;
    private AtomicInteger dirsCount;
    private boolean cache;
    private long cacheFileSize;
    private int cacheFilesCount;
    private int cacheDirsCount;
    private Optional<String> detailOptional;
    private Optional<String> cmdOptional;
    private Optional<Boolean> progressOptional;

    public FileParam(){
        cmd = "";
        opt = OPT_NONE;
        maxSize = Long.MAX_VALUE;
        level = RECURSION_LEVEL;
        deflaterLevel = Deflater.DEFAULT_COMPRESSION;
        pathMap = new ConcurrentHashMap<>();
        rePathMap = new ConcurrentHashMap<>();
        pathsMap = new ConcurrentHashMap<>();
        pathDeque = new LinkedBlockingDeque<>();
        filesSize = new AtomicLong();
        filesCount = new AtomicInteger();
        dirsCount = new AtomicInteger();
    }

    @Override
    public FileParam cloneValue(){
        FileParam fileParam = new FileParam();
        fileParam.srcPath = srcPath;
        fileParam.destPath = destPath;
        fileParam.backupPath = backupPath;
        fileParam.pattern = pattern;
        fileParam.replacePattern = replacePattern;
        fileParam.askPattern = askPattern;
        fileParam.replacement = replacement;
        fileParam.cmds = cmds;
        fileParam.cmd = cmd;
        fileParam.opt = opt;
        fileParam.minSize = minSize;
        fileParam.maxSize = maxSize;
        fileParam.level = level;
        fileParam.deflaterLevel = deflaterLevel;
        return fileParam;
    }

    @Override
    public void close() throws Exception{
        close(zipOutputStream,pathStream);
    }

    public void createOptional(){
        detailOptional = Optional.of(opt).filter(s->s.contains(OPT_DETAIL) || s.contains(OPT_SIMULATE));
        cmdOptional = Optional.of(opt).filter(s->!s.contains(OPT_SIMULATE));
        progressOptional = Optional.of(!opt.matches(REG_NON_PROG));
    }

    public boolean matchFilesSize(long size){
        if(minSize <= size && maxSize >= size){
            filesSize.addAndGet(size);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public Stream<Path> cachePathStream(Stream<Path> pathStream){
        try{
            close(this.pathStream);
        }catch(Exception e){
            CS.sl(gsph(ERR_RES_CLS,e.toString()));
        }
        return (this.pathStream = pathStream);
    }

    public void cacheZipOutputStream(ZipOutputStream zipOutputStream){
        try{
            close(this.zipOutputStream);
        }catch(Exception e){
            CS.sl(gsph(ERR_RES_CLS,e.toString()));
        }
        this.zipOutputStream = zipOutputStream;
    }

    public void saveCache(FileParam cache){
        if(!canSaveCache()) return;
        cache.clearCache();
        cache.setPathMap(pathMap);
        cache.setRePathMap(rePathMap);
        cache.setPathDeque(pathDeque);
        cache.setCacheFileSize(filesSize.get());
        cache.setCacheDirsCount(dirsCount.get());
        cache.setCacheFilesCount(filesCount.get());
        cache.setCache(true);
    }

    public boolean useCache(FileParam cache){
        if(canUseCache(cache)){
            clearCache();
            pathMap = cache.getPathMap();
            rePathMap = cache.getRePathMap();
            pathDeque = cache.getPathDeque();
            return true;
        }
        return false;
    }

    public boolean canUseCache(FileParam cache){
        if(!cache.isCache() || opt.contains(OPT_CACHE) || isQueryCmds()) return false;
        if(isEmpty(cache.getPathMap())){
            opt += OPT_CACHE;
            return false;
        }else return true;
    }

    public boolean isQueryCmds(){
        switch(cmd){
            case CMD_FIND:
            case CMD_FND_SIZ:
            case CMD_FND_SIZ_ASC:
            case CMD_FND_SIZ_DSC:
            case CMD_FND_DIR:
            case CMD_FND_DIR_SIZ:
            case CMD_FND_DIR_SIZ_ASC:
            case CMD_FND_DIR_SIZ_DSC:
            case CMD_FND_DIR_OLY:
            return true;
        }
        return false;
    }

    public boolean needRefreshCache(FileParam cache){
        return opt.contains(OPT_CACHE) || canUseCache(cache);
    }

    public void clearCache(){
        pathMap.clear();
        rePathMap.clear();
        pathDeque.clear();
    }

    @SuppressWarnings("unchecked")
    private <T extends AutoCloseable> void close(T... types) throws Exception{
        if(nonEmpty(types)) for(T type : types)
            if(nonEmpty(type)) type.close();
    }

    private boolean canSaveCache(){
        if(!opt.contains(OPT_CACHE)) return false;
        switch(cmd){
            case CMD_DELETE:
            case CMD_DEL_DIR:
            case CMD_DEL_DIR_NUL:
            case CMD_MOVE:
            case CMD_MOV_DIR:
            case CMD_BAK_UGD:
            case CMD_BAK_RST:
            return false;
            default:
            return true;
        }
    }

    public Path getSrcPath(){
        return srcPath;
    }

    public void setSrcPath(Path srcPath){
        this.srcPath = srcPath;
    }

    public Path getDestPath(){
        return destPath;
    }

    public void setDestPath(Path destPath){
        this.destPath = destPath;
    }

    public Path getBackupPath(){
        return backupPath;
    }

    public void setBackupPath(Path backupPath){
        this.backupPath = backupPath;
    }

    public Pattern getPattern(){
        return pattern;
    }

    public void setPattern(Pattern pattern){
        this.pattern = pattern;
    }

    public Pattern getReplacePattern(){
        return replacePattern;
    }

    public void setReplacePattern(Pattern replacePattern){
        this.replacePattern = replacePattern;
    }

    public Pattern getAskPattern(){
        return askPattern;
    }

    public void setAskPattern(Pattern askPattern){
        this.askPattern = askPattern;
    }

    public String getReplacement(){
        return replacement;
    }

    public void setReplacement(String replacement){
        this.replacement = replacement;
    }

    public String getCmds(){
        return cmds;
    }

    public void setCmds(String cmds){
        this.cmds = cmds;
    }

    public String getCmd(){
        return cmd;
    }

    public void setCmd(String cmd){
        this.cmd = cmd;
    }

    public String getOpt(){
        return opt;
    }

    public void setOpt(String opt){
        this.opt = opt;
    }

    public long getMinSize(){
        return minSize;
    }

    public void setMinSize(long minSize){
        this.minSize = minSize;
    }

    public long getMaxSize(){
        return maxSize;
    }

    public void setMaxSize(long maxSize){
        this.maxSize = maxSize;
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getDeflaterLevel(){
        return deflaterLevel;
    }

    public void setDeflaterLevel(int deflaterLevel){
        this.deflaterLevel = deflaterLevel;
    }

    public ZipOutputStream getZipOutputStream(){
        return zipOutputStream;
    }

    public Stream<Path> getPathStream(){
        return pathStream;
    }

    public ConcurrentMap<BasicFileAttributes,Path> getPathMap(){
        return pathMap;
    }

    public void setPathMap(ConcurrentMap<BasicFileAttributes,Path> pathMap){
        this.pathMap = pathMap;
    }

    public ConcurrentMap<Path,Path> getRePathMap(){
        return rePathMap;
    }

    public void setRePathMap(ConcurrentMap<Path,Path> rePathMap){
        this.rePathMap = rePathMap;
    }

    public ConcurrentMap<Path,List<Path>> getPathsMap(){
        return pathsMap;
    }

    public void setPathsMap(ConcurrentMap<Path,List<Path>> pathsMap){
        this.pathsMap = pathsMap;
    }

    public BlockingDeque<Path> getPathDeque(){
        return pathDeque;
    }

    public void setPathDeque(BlockingDeque<Path> pathDeque){
        this.pathDeque = pathDeque;
    }

    public AtomicLong getFilesSize(){
        return filesSize;
    }

    public AtomicInteger getFilesCount(){
        return filesCount;
    }

    public void setFilesCount(AtomicInteger filesCount){
        this.filesCount = filesCount;
    }

    public AtomicInteger getDirsCount(){
        return dirsCount;
    }

    public boolean isCache(){
        return cache;
    }

    public void setCache(boolean cache){
        this.cache = cache;
    }

    public long getCacheFileSize(){
        return cacheFileSize;
    }

    public void setCacheFileSize(long cacheFileSize){
        this.cacheFileSize = cacheFileSize;
    }

    public int getCacheFilesCount(){
        return cacheFilesCount;
    }

    public void setCacheFilesCount(int cacheFilesCount){
        this.cacheFilesCount = cacheFilesCount;
    }

    public int getCacheDirsCount(){
        return cacheDirsCount;
    }

    public void setCacheDirsCount(int cacheDirsCount){
        this.cacheDirsCount = cacheDirsCount;
    }

    public Optional<String> getDetailOptional(){
        return detailOptional;
    }

    public void setDetailOptional(Optional<String> detailOptional){
        this.detailOptional = detailOptional;
    }

    public Optional<String> getCmdOptional(){
        return cmdOptional;
    }

    public void setCmdOptional(Optional<String> cmdOptional){
        this.cmdOptional = cmdOptional;
    }

    public Optional<Boolean> getProgressOptional(){
        return progressOptional;
    }

    public void setProgressOptional(Optional<Boolean> progressOptional){
        this.progressOptional = progressOptional;
    }
}
