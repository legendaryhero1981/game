package legend.util.param;

import static legend.intf.ICommon.gs;
import static legend.intf.ICommon.gsph;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ValueUtil.nonEmpty;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.ZipOutputStream;

import legend.intf.IValue;
import legend.util.intf.IFileUtil;

public class FileParam implements IFileUtil,IValue<FileParam>,AutoCloseable{
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
    private int limit;
    private int level;
    private int deflaterLevel;
    private ZipOutputStream zipOutputStream;
    private ConcurrentMap<BasicFileAttributes,Path> pathMap;
    private ConcurrentMap<Path,Path> rePathMap;
    private ConcurrentMap<Path,List<Path>> pathsMap;
    private ConcurrentMap<Path,Long> sizeMap;
    private BlockingDeque<Path> pathDeque;
    private List<Path> pathList;
    private AtomicLong filesSize;
    private AtomicInteger filesCount;
    private AtomicInteger dirsCount;
    private boolean usingCaching;
    private long cacheFileSize;
    private int cacheFilesCount;
    private int cacheDirsCount;
    private Optional<String> detailOptional;
    private Optional<String> cmdOptional;
    private Optional<Boolean> progressOptional;

    public FileParam(){
        cmds = cmd = opt = CMD_OPT_NONE;
        maxSize = Long.MAX_VALUE;
        level = RECURSION_LEVEL;
        deflaterLevel = Deflater.DEFAULT_COMPRESSION;
        pathMap = new ConcurrentHashMap<>();
        rePathMap = new ConcurrentHashMap<>();
        pathsMap = new ConcurrentHashMap<>();
        sizeMap = new ConcurrentHashMap<>();
        pathDeque = new LinkedBlockingDeque<>();
        pathList = new CopyOnWriteArrayList<>();
        filesSize = new AtomicLong();
        filesCount = new AtomicInteger();
        dirsCount = new AtomicInteger();
        limit = Integer.MAX_VALUE;
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
        fileParam.limit = limit;
        return fileParam;
    }

    @Override
    public void close() throws Exception{
        close(zipOutputStream);
    }

    public void cacheZipOutputStream(ZipOutputStream zipOutputStream){
        try{
            close(this.zipOutputStream);
        }catch(Exception e){
            CS.sl(gsph(ERR_RES_CLS,e.toString()));
        }
        this.zipOutputStream = zipOutputStream;
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

    public void saveCache(FileParam cache){
        if(usingCaching){
            cache.getFilesSize().addAndGet(filesSize.addAndGet(cache.getCacheFileSize()));
            cache.getFilesCount().addAndGet(filesCount.addAndGet(cache.getCacheFilesCount()));
            cache.getDirsCount().addAndGet(dirsCount.addAndGet(cache.getCacheDirsCount()));
        }else{
            cache.getFilesSize().addAndGet(filesSize.get());
            cache.getFilesCount().addAndGet(filesCount.get());
            cache.getDirsCount().addAndGet(dirsCount.get());
        }
        if(canSaveCache(cache)){
            cache.clearCache();
            cache.setPathMap(pathMap);
            cache.setRePathMap(rePathMap);
            cache.setPathsMap(pathsMap);
            cache.setSizeMap(sizeMap);
            cache.setPathDeque(pathDeque);
            cache.setPathList(pathList);
            cache.setPattern(pattern);
            cache.setSrcPath(srcPath);
            cache.setCacheFileSize(filesSize.get());
            cache.setCacheFilesCount(filesCount.get());
            cache.setCacheDirsCount(dirsCount.get());
        }
    }

    public boolean useCache(FileParam cache){
        String[] s = cmds.split(SPRT_ARG);
        if(canUseCache(cache)){
            clearCache();
            pathMap = cache.getPathMap();
            rePathMap = cache.getRePathMap();
            pathsMap = cache.getPathsMap();
            sizeMap = cache.getSizeMap();
            pathDeque = cache.getPathDeque();
            pathList = cache.getPathList();
            pattern = cache.pattern;
            srcPath = cache.srcPath;
            if(s.length > 2){
                s[1] = pattern.toString();
                s[2] = srcPath.toString();
            }
            cmds = gs(s);
            usingCaching = true;
        }else usingCaching = false;
        cmds = gs(s);
        return usingCaching;
    }

    public boolean canUseCache(FileParam cache){
        if(cache.getPathMap().isEmpty() || opt.contains(OPT_CACHE) || isQueryCmds()) return false;
        return true;
    }

    public boolean isQueryCmds(){
        switch(cmd){
            case CMD_FIND:
            case CMD_FND_DIR:
            case CMD_FND_DIR_OLY:
            case CMD_FND_SIZ:
            case CMD_FND_SIZ_ASC:
            case CMD_FND_SIZ_DSC:
            case CMD_FND_DIR_SIZ:
            case CMD_FND_DIR_SIZ_ASC:
            case CMD_FND_DIR_SIZ_DSC:
            case CMD_FND_DIR_DIR_SIZ_ASC:
            case CMD_FND_DIR_DIR_SIZ_DSC:
            case CMD_FND_DIR_OLY_SIZ_ASC:
            case CMD_FND_DIR_OLY_SIZ_DSC:
            case CMD_FND_PTH_ABS:
            case CMD_FND_PTH_RLT:
            case CMD_FND_PTH_SRC:
            case CMD_FND_PTH_DIR_ABS:
            case CMD_FND_PTH_DIR_RLT:
            case CMD_FND_PTH_DIR_SRC:
            case CMD_FND_PTH_DIR_OLY_ABS:
            case CMD_FND_PTH_DIR_OLY_RLT:
            case CMD_FND_PTH_DIR_OLY_SRC:
            return true;
        }
        return false;
    }

    public boolean needCaching(){
        return opt.contains(OPT_CACHE);
    }

    public boolean usingCaching(){
        return usingCaching;
    }

    public boolean needRefreshCache(FileParam cache){
        return needCaching() || canUseCache(cache);
    }

    public void clearCache(){
        pathMap.clear();
        rePathMap.clear();
        pathsMap.clear();
        sizeMap.clear();
        pathDeque.clear();
        pathList.clear();
    }

    public float getFilesAndDirsCount(){
        return filesCount.get() + dirsCount.get();
    }

    @SuppressWarnings("unchecked")
    private <T extends AutoCloseable> void close(T... types) throws Exception{
        if(nonEmpty(types)) for(T type : types)
            if(nonEmpty(type)) type.close();
    }

    private boolean canSaveCache(FileParam cache){
        switch(cmd){
            case CMD_DELETE:
            case CMD_DEL_DIR:
            case CMD_DEL_DIR_NUL:
            case CMD_MOVE:
            case CMD_MOV_DIR:
            case CMD_BAK_UGD:
            case CMD_BAK_RST:
            cache.clearCache();
            return false;
            default:
            if(opt.contains(OPT_CACHE)) return true;
            return false;
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

    public int getLimit(){
        return limit;
    }

    public void setLimit(int limit){
        this.limit = limit;
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

    public ConcurrentMap<Path,Long> getSizeMap(){
        return sizeMap;
    }

    public void setSizeMap(ConcurrentMap<Path,Long> sizeMap){
        this.sizeMap = sizeMap;
    }

    public BlockingDeque<Path> getPathDeque(){
        return pathDeque;
    }

    public void setPathDeque(BlockingDeque<Path> pathDeque){
        this.pathDeque = pathDeque;
    }

    public List<Path> getPathList(){
        return pathList;
    }

    public void setPathList(List<Path> pathList){
        this.pathList = pathList;
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
