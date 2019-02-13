package legend.util.param;

import static java.nio.file.Paths.get;
import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ConsoleUtil.FS;
import static legend.util.StringUtil.SU;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.nonEmpty;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.ZipOutputStream;

import legend.intf.IValue;
import legend.util.intf.IConsoleUtil.UNIT_TYPE;
import legend.util.intf.IFileUtil;

public class FileParam implements IFileUtil,IValue<FileParam>,AutoCloseable{
    private Path srcPath;
    private Path destPath;
    private Path backupPath;
    private Path rootPath;
    private Path outPath;
    private Pattern pattern;
    private String sizeExpr;
    private String split;
    private String replacement;
    private String zipName;
    private String cmd;
    private String opt;
    private long condition;
    private long minSize;
    private long maxSize;
    private int limit;
    private int level;
    private int zipLevel;
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
    private long cacheFileSize;
    private int cacheFilesCount;
    private int cacheDirsCount;
    private Optional<Long> detailOptional;
    private Optional<Long> cmdOptional;
    private Optional<Long> progressOptional;

    public FileParam(){
        zipName = replacement = sizeExpr = cmd = opt = OPT_NONE;
        split = REG_SPRT_COL;
        maxSize = Long.MAX_VALUE;
        limit = level = Integer.MAX_VALUE;
        zipLevel = Deflater.DEFAULT_COMPRESSION;
        pathMap = new ConcurrentHashMap<>();
        rePathMap = new ConcurrentHashMap<>();
        pathsMap = new ConcurrentHashMap<>();
        sizeMap = new ConcurrentHashMap<>();
        pathDeque = new LinkedBlockingDeque<>();
        pathList = new CopyOnWriteArrayList<>();
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
        fileParam.split = split;
        fileParam.replacement = replacement;
        fileParam.cmd = cmd;
        fileParam.opt = opt;
        fileParam.minSize = minSize;
        fileParam.maxSize = maxSize;
        fileParam.level = level;
        fileParam.zipLevel = zipLevel;
        fileParam.limit = limit;
        return fileParam;
    }

    @Override
    public void close() throws Exception{
        if(nonEmpty(zipOutputStream)) zipOutputStream.close();
    }

    public void cacheZipOutputStream(ZipOutputStream zipOutputStream){
        try{
            close();
        }catch(Exception e){
            CS.sl(gsph(ERR_RES_CLS,e.toString()));
        }
        this.zipOutputStream = zipOutputStream;
    }

    public void refreshConditions(FileParam cache){
        condition = 0;
        switch(cmd){
            case CMD_FND_SAM:
            condition |= COMPARE_SAME;
            case CMD_FND_DIF:
            case CMD_FIND:
            case CMD_FND_PTH_ABS:
            case CMD_FND_PTH_RLT:
            case CMD_FND_PTH_SRC:
            condition |= IS_QUERY_COMMAND | MATCH_FILE_ONLY;
            break;
            case CMD_FND_DIR_OLY_SIZ_ASC:
            condition |= MATCH_DIR_ONLY;
            case CMD_FND_SIZ_ASC:
            case CMD_FND_DIR_SIZ_ASC:
            case CMD_FND_DIR_DIR_SIZ_ASC:
            condition |= IS_QUERY_COMMAND | ORDER_ASC;
            break;
            case CMD_FND_DIR_OLY_SAM:
            condition |= COMPARE_SAME;
            case CMD_FND_DIR_OLY_DIF:
            case CMD_FND_PTH_DIR_OLY_ABS:
            case CMD_FND_PTH_DIR_OLY_RLT:
            case CMD_FND_PTH_DIR_OLY_SRC:
            case CMD_FND_DIR_OLY:
            case CMD_FND_DIR_OLY_SIZ_DSC:
            condition |= MATCH_DIR_ONLY;
            case CMD_FND_DIR_DIF:
            case CMD_FND_DIR:
            case CMD_FND_PTH_DIR_ABS:
            case CMD_FND_PTH_DIR_RLT:
            case CMD_FND_PTH_DIR_SRC:
            case CMD_FND_SIZ_DSC:
            case CMD_FND_DIR_SIZ_DSC:
            case CMD_FND_DIR_DIR_SIZ_DSC:
            condition |= IS_QUERY_COMMAND;
            break;
            case CMD_FND_DIR_SAM:
            condition |= IS_QUERY_COMMAND | COMPARE_SAME;
            break;
            case CMD_REN_DIR_OLY:
            case CMD_REN_DIR_OLY_LOW:
            case CMD_REN_DIR_OLY_UP:
            case CMD_REN_DIR_OLY_UP_FST:
            case CMD_DEL_DIR_OLY:
            case CMD_DEL_DIR_OLY_NUL:
            condition |= MATCH_DIR_ONLY;
            case CMD_REN_DIR:
            case CMD_REN_DIR_LOW:
            case CMD_REN_DIR_UP:
            case CMD_REN_DIR_UP_FST:
            case CMD_DEL_DIR:
            case CMD_DEL_DIR_NUL:
            condition |= NEED_CLEAR_CACHE;
            break;
            case CMD_MOV_DIR_OLY:
            condition |= MATCH_DIR_ONLY;
            case CMD_MOV_DIR:
            condition |= NEED_CLEAR_CACHE | NEED_REPATH;
            break;
            case CMD_CPY_DIR_OLY:
            condition |= MATCH_DIR_ONLY;
            case CMD_CPY_DIR:
            case CMD_UGD_DIR:
            condition |= NEED_REPATH;
            break;
            case CMD_RENAME:
            case CMD_REN_LOW:
            case CMD_REN_UP:
            case CMD_REN_UP_FST:
            case CMD_DELETE:
            case CMD_MOVE:
            case CMD_BAK_UGD:
            case CMD_BAK_RST:
            case CMD_JSON_ENC:
            case CMD_JSON_DEC:
            condition |= NEED_CLEAR_CACHE;
            case CMD_COPY:
            case CMD_REP_FILE:
            case CMD_UPGRADE:
            case CMD_ZIP_DEF:
            case CMD_ZIP_INF:
            case CMD_PAK_DEF:
            case CMD_PAK_INF:
            case CMD_GUID_L32:
            case CMD_GUID_U32:
            case CMD_MD5_L16:
            case CMD_MD5_U16:
            case CMD_MD5_L32:
            case CMD_MD5_U32:
            condition |= MATCH_FILE_ONLY;
        }
        if(opt.contains(OPT_CACHE)){
            condition |= ENABLE_CACHE;
            if(!matchConditions(NEED_CLEAR_CACHE)) condition |= CAN_SAVE_CACHE;
        }else if(nonEmpty(cache) && !cache.getPathMap().isEmpty() && !matchConditions(IS_QUERY_COMMAND)){
            condition |= CAN_USE_CACHE;
            pattern = cache.pattern;
            srcPath = cache.srcPath;
        }
        if(!opt.contains(OPT_SIMULATE)) condition |= EXEC_CMD;
        if(!opt.matches(REG_NON_PROG)) condition |= SHOW_PROGRESS;
        if(opt.contains(OPT_DETAIL) || opt.contains(OPT_SIMULATE)) condition |= SHOW_DETAIL;
        if(opt.contains(OPT_EXCLUDE_ROOT)){
            condition |= EXCLUDE_ROOT;
            rootPath = srcPath;
        }else rootPath = nonEmpty(srcPath.getParent()) ? srcPath.getParent() : srcPath;
        if(nonEmpty(backupPath)) outPath = backupPath;
        else if(nonEmpty(destPath)) outPath = destPath;
        else outPath = rootPath;
        cmdOptional = Optional.of(condition).filter(c->EXEC_CMD == (EXEC_CMD & c));
        progressOptional = Optional.of(condition).filter(c->SHOW_PROGRESS == (SHOW_PROGRESS & c));
        detailOptional = Optional.of(condition).filter(c->SHOW_DETAIL == (SHOW_DETAIL & c));
    }

    public boolean matchConditions(long condition){
        return condition == (condition & this.condition);
    }

    public boolean matchFilesSize(long size){
        if(minSize <= size && maxSize >= size){
            filesSize.addAndGet(size);
            return true;
        }
        return false;
    }

    public void saveCache(FileParam cache){
        if(matchConditions(CAN_USE_CACHE)){
            cache.getFilesSize().addAndGet(filesSize.addAndGet(cache.getCacheFileSize()));
            cache.getFilesCount().addAndGet(filesCount.addAndGet(cache.getCacheFilesCount()));
            cache.getDirsCount().addAndGet(dirsCount.addAndGet(cache.getCacheDirsCount()));
        }else{
            cache.getFilesSize().addAndGet(filesSize.get());
            cache.getFilesCount().addAndGet(filesCount.get());
            cache.getDirsCount().addAndGet(dirsCount.get());
        }
        if(matchConditions(CAN_SAVE_CACHE)){
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
        }else if(matchConditions(NEED_CLEAR_CACHE)) cache.clearCache();
    }

    public boolean useCache(FileParam cache){
        if(matchConditions(CAN_USE_CACHE)){
            clearCache();
            pathMap = cache.getPathMap();
            rePathMap = cache.getRePathMap();
            pathsMap = cache.getPathsMap();
            sizeMap = cache.getSizeMap();
            pathDeque = cache.getPathDeque();
            pathList = cache.getPathList();
            return true;
        }else return false;
    }

    public void clearCache(){
        pathMap.clear();
        rePathMap.clear();
        pathsMap.clear();
        sizeMap.clear();
        pathDeque.clear();
        pathList.clear();
    }

    public static List<FileParam> analyzeParam(String[] args){
        CS.showError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT},()->args.length < 3);
        String[][] aa = new String[args.length][];
        aa[0] = args[0].split(REG_SPRT_CMD);
        Matcher mrpt = compile(REG_RPT_ARG).matcher(aa[0][0]);
        CS.showError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT},()->mrpt.matches());
        Matcher mph = compile(REG_OPT_ASK).matcher("");
        for(int i = 0;i < aa[0].length;i++){
            mph.reset(aa[0][i]);
            CS.showError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT},()->mph.matches());
        }
        for(int i = 1;i < args.length;i++){
            aa[i] = args[i].split(REG_SPRT_CMD);
            mrpt.reset(aa[i][0]);
            if(aa[0].length != aa[i].length || mrpt.matches()) CS.showError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT});
        }
        for(int i = 0;i < aa.length;i++){
            String s = aa[i][0];
            for(int j = 1;j < aa[0].length;j++){
                mrpt.reset(aa[i][j]);
                if(mrpt.find()) aa[i][j] = s.replaceAll(OPT_CACHE,"") + mrpt.group(1);
                else s = aa[i][j];
            }
        }
        String[][] ss = new String[aa[0].length][];
        for(int i = 0,j,k;i < aa[0].length;i++){
            int[] n = new int[aa.length];
            for(j = k = 0;k < aa.length;k++){
                mph.reset(aa[k][i]);
                if(!mph.matches()) n[j++] = k;
            }
            for(ss[i] = new String[j],k = 0;k < j;k++)
                ss[i][k] = aa[n[k]][i];
        }
        List<FileParam> fileParams = new ArrayList<>(ss.length);
        for(String[] as : ss){
            FileParam param = new FileParam();
            try{
                Optional<String[]> optional = Optional.of(as);
                param.setCmd(as[0]);
                if(as[0].length() > 2){
                    Matcher matcher = compile(REG_OPT).matcher(as[0]);
                    if(matcher.find()){
                        param.setCmd(matcher.group(1));
                        param.setOpt(matcher.group(2));
                        while(matcher.find())
                            param.setOpt(param.getOpt() + matcher.group(2));
                    }
                }
                param.setPattern(compile(replacePlaceHolders(as[1])));
                param.setSrcPath(get(replacePlaceHolders(as[2])));
                switch(param.getCmd()){
                    case CMD_FND_SIZ_ASC:
                    case CMD_FND_SIZ_DSC:
                    case CMD_FND_DIR_SIZ_ASC:
                    case CMD_FND_DIR_SIZ_DSC:
                    optional.filter(s->s.length > 3).ifPresent(s->{
                        param.setSizeExpr(s[3]);
                        matchSizes(param,as[3].toUpperCase());
                    });
                    optional.filter(s->s.length > 4).ifPresent(s->{
                        int filesCountLimit = Integer.parseInt(s[4]);
                        param.setLimit(0 < filesCountLimit ? filesCountLimit : Integer.MAX_VALUE);
                    });
                    optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
                    break;
                    case CMD_FIND:
                    case CMD_FND_DIR:
                    case CMD_FND_DIR_OLY:
                    case CMD_FND_PTH_ABS:
                    case CMD_FND_PTH_RLT:
                    case CMD_FND_PTH_SRC:
                    case CMD_FND_PTH_DIR_ABS:
                    case CMD_FND_PTH_DIR_RLT:
                    case CMD_FND_PTH_DIR_SRC:
                    case CMD_FND_PTH_DIR_OLY_ABS:
                    case CMD_FND_PTH_DIR_OLY_RLT:
                    case CMD_FND_PTH_DIR_OLY_SRC:
                    optional.filter(s->s.length > 3).ifPresent(s->{
                        int filesCountLimit = Integer.parseInt(s[3]);
                        param.setLimit(0 < filesCountLimit ? filesCountLimit : Integer.MAX_VALUE);
                    });
                    optional.filter(s->s.length > 4).ifPresent(s->param.setLevel(Integer.parseInt(s[4])));
                    break;
                    case CMD_FND_DIR_DIR_SIZ_ASC:
                    case CMD_FND_DIR_DIR_SIZ_DSC:
                    case CMD_FND_DIR_OLY_SIZ_ASC:
                    case CMD_FND_DIR_OLY_SIZ_DSC:
                    optional.filter(s->s.length > 3).ifPresent(s->{
                        param.setSizeExpr(s[3]);
                        matchSizes(param,as[3].toUpperCase());
                    });
                    optional.filter(s->s.length > 4).ifPresent(s->{
                        int filesCountLimit = Integer.parseInt(s[4]);
                        param.setLimit(0 < filesCountLimit ? filesCountLimit : Integer.MAX_VALUE);
                    });
                    param.setLevel(1);
                    break;
                    case CMD_FND_SAM:
                    case CMD_FND_DIF:
                    case CMD_FND_DIR_SAM:
                    case CMD_FND_DIR_DIF:
                    case CMD_FND_DIR_OLY_SAM:
                    case CMD_FND_DIR_OLY_DIF:
                    param.setDestPath(get(replacePlaceHolders(as[3])));
                    optional.filter(s->s.length > 4).ifPresent(s->{
                        int filesCountLimit = Integer.parseInt(s[4]);
                        param.setLimit(0 < filesCountLimit ? filesCountLimit : Integer.MAX_VALUE);
                    });
                    optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
                    break;
                    case CMD_REP_FILE:
                    param.setReplacement(replacePlaceHolders(as[3]));
                    optional.filter(s->s.length > 4).ifPresent(s->param.setSplit(replacePlaceHolders(s[4])));
                    optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
                    break;
                    case CMD_RENAME:
                    case CMD_REN_DIR:
                    case CMD_REN_DIR_OLY:
                    param.setReplacement(replacePlaceHolders(as[3]));
                    optional.filter(s->s.length > 4).ifPresent(s->param.setLevel(Integer.parseInt(s[4])));
                    break;
                    case CMD_REN_LOW:
                    case CMD_REN_UP:
                    case CMD_REN_UP_FST:
                    case CMD_REN_DIR_LOW:
                    case CMD_REN_DIR_UP:
                    case CMD_REN_DIR_UP_FST:
                    case CMD_REN_DIR_OLY_LOW:
                    case CMD_REN_DIR_OLY_UP:
                    case CMD_REN_DIR_OLY_UP_FST:
                    case CMD_DELETE:
                    case CMD_DEL_DIR:
                    case CMD_DEL_DIR_OLY:
                    case CMD_DEL_NUL:
                    case CMD_DEL_DIR_NUL:
                    case CMD_DEL_DIR_OLY_NUL:
                    case CMD_PAK_INF:
                    case CMD_GUID_L32:
                    case CMD_GUID_U32:
                    case CMD_MD5_L16:
                    case CMD_MD5_U16:
                    case CMD_MD5_L32:
                    case CMD_MD5_U32:
                    case CMD_JSON_ENC:
                    case CMD_JSON_DEC:
                    optional.filter(s->s.length > 3).ifPresent(s->param.setLevel(Integer.parseInt(s[3])));
                    break;
                    case CMD_COPY:
                    case CMD_CPY_DIR:
                    case CMD_CPY_DIR_OLY:
                    case CMD_MOVE:
                    case CMD_MOV_DIR:
                    case CMD_MOV_DIR_OLY:
                    case CMD_ZIP_INF:
                    param.setDestPath(get(replacePlaceHolders(as[3])));
                    optional.filter(s->s.length > 4).ifPresent(s->param.setLevel(Integer.parseInt(s[4])));
                    break;
                    case CMD_BAK_UGD:
                    case CMD_BAK_RST:
                    case CMD_UPGRADE:
                    case CMD_UGD_DIR:
                    param.setDestPath(get(replacePlaceHolders(as[3])));
                    param.setBackupPath(get(replacePlaceHolders(as[4])));
                    optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
                    break;
                    case CMD_ZIP_DEF:
                    case CMD_ZIP_DIR_DEF:
                    param.setDestPath(get(replacePlaceHolders(as[3])));
                    param.setZipName(replacePlaceHolders(as[4]) + EXT_ZIP);
                    optional.filter(s->s.length > 5).ifPresent(s2->param.setZipLevel(Integer.parseInt(s2[5])));
                    optional.filter(s->s.length > 6).ifPresent(s->param.setLevel(Integer.parseInt(s[6])));
                    break;
                    case CMD_PAK_DEF:
                    case CMD_PAK_DIR_DEF:
                    param.setDestPath(get(replacePlaceHolders(as[3])));
                    param.setZipName(replacePlaceHolders(as[4]) + EXT_PAK);
                    optional.filter(s->s.length > 5).ifPresent(s2->param.setZipLevel(Integer.parseInt(s2[5])));
                    optional.filter(s->s.length > 6).ifPresent(s->param.setLevel(Integer.parseInt(s[6])));
                    break;
                    default:
                    CS.showError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT});
                }
            }catch(Exception e){
                CS.showError(ERR_ARG_ANLS,new String[]{e.toString()});
            }
            fileParams.add(param);
        }
        return fileParams;
    }

    public static String replacePlaceHolders(String s){
        SingleValue<String> value = new SingleValue<>(s);
        SU.rph(value,REG_SPC_SQM,S_SQM).rph(value,REG_SPC_DQM,S_DQM);
        return value.get();
    }

    private static void matchSizes(FileParam param, String size){
        long minSize = param.getMinSize();
        long maxSize = param.getMaxSize();
        UNIT_TYPE minType = UNIT_TYPE.NON;
        UNIT_TYPE maxType = UNIT_TYPE.NON;
        Matcher matcher = compile(REG_FLE_SIZ).matcher(size);
        if(matcher.find()){
            minType = FS.matchType(matcher.group(2));
            minSize = Long.parseLong(matcher.group(1));
        }
        if(matcher.find()){
            maxType = FS.matchType(matcher.group(2));
            maxSize = FS.matchSize(Long.parseLong(matcher.group(1)),maxType);
        }
        minSize = FS.matchSize(minSize,UNIT_TYPE.NON == minType ? maxType : minType);
        if(minSize <= maxSize){
            param.setMinSize(minSize);
            param.setMaxSize(maxSize);
        }else{
            param.setMinSize(maxSize);
            param.setMaxSize(minSize);
        }
    }

    public String getWholeCommand(){
        String regex = getWrapedParam(pattern);
        String sp = getWrapedParam(srcPath);
        String dp = getWrapedParam(destPath);
        String bp = getWrapedParam(backupPath);
        String se = getWrapedParam(sizeExpr);
        String spt = getWrapedParam(split);
        String rm = getWrapedParam(replacement);
        String zn = getWrapedParam(zipName);
        String s = CMD + cmd + opt + regex + sp;
        switch(cmd){
            case CMD_FND_SIZ_ASC:
            case CMD_FND_SIZ_DSC:
            case CMD_FND_DIR_SIZ_ASC:
            case CMD_FND_DIR_SIZ_DSC:
            s += se;
            case CMD_FIND:
            case CMD_FND_DIR:
            case CMD_FND_DIR_OLY:
            case CMD_FND_PTH_ABS:
            case CMD_FND_PTH_RLT:
            case CMD_FND_PTH_SRC:
            case CMD_FND_PTH_DIR_ABS:
            case CMD_FND_PTH_DIR_RLT:
            case CMD_FND_PTH_DIR_SRC:
            case CMD_FND_PTH_DIR_OLY_ABS:
            case CMD_FND_PTH_DIR_OLY_RLT:
            case CMD_FND_PTH_DIR_OLY_SRC:
            case CMD_FND_DIR_DIR_SIZ_ASC:
            case CMD_FND_DIR_DIR_SIZ_DSC:
            case CMD_FND_DIR_OLY_SIZ_ASC:
            case CMD_FND_DIR_OLY_SIZ_DSC:
            s += S_SPACE + limit + S_SPACE + level;
            break;
            case CMD_FND_SAM:
            case CMD_FND_DIF:
            case CMD_FND_DIR_SAM:
            case CMD_FND_DIR_DIF:
            case CMD_FND_DIR_OLY_SAM:
            case CMD_FND_DIR_OLY_DIF:
            s += dp + S_SPACE + limit + S_SPACE + level;
            break;
            case CMD_REP_FILE:
            s += rm + spt + S_SPACE + level;
            break;
            case CMD_RENAME:
            case CMD_REN_DIR:
            case CMD_REN_DIR_OLY:
            s += rm;
            case CMD_REN_LOW:
            case CMD_REN_DIR_LOW:
            case CMD_REN_UP:
            case CMD_REN_DIR_UP:
            case CMD_REN_UP_FST:
            case CMD_REN_DIR_UP_FST:
            case CMD_REN_DIR_OLY_LOW:
            case CMD_REN_DIR_OLY_UP:
            case CMD_REN_DIR_OLY_UP_FST:
            case CMD_DELETE:
            case CMD_DEL_DIR:
            case CMD_DEL_DIR_OLY:
            case CMD_DEL_NUL:
            case CMD_DEL_DIR_NUL:
            case CMD_DEL_DIR_OLY_NUL:
            case CMD_PAK_INF:
            case CMD_GUID_L32:
            case CMD_GUID_U32:
            case CMD_MD5_L16:
            case CMD_MD5_U16:
            case CMD_MD5_L32:
            case CMD_MD5_U32:
            case CMD_JSON_ENC:
            case CMD_JSON_DEC:
            s += S_SPACE + level;
            break;
            case CMD_COPY:
            case CMD_CPY_DIR:
            case CMD_CPY_DIR_OLY:
            case CMD_MOVE:
            case CMD_MOV_DIR:
            case CMD_MOV_DIR_OLY:
            case CMD_ZIP_INF:
            s += dp + S_SPACE + level;
            break;
            case CMD_BAK_UGD:
            case CMD_BAK_RST:
            case CMD_UPGRADE:
            case CMD_UGD_DIR:
            s += dp + bp + S_SPACE + level;
            break;
            case CMD_ZIP_DEF:
            case CMD_ZIP_DIR_DEF:
            s += dp + zn + S_SPACE + zipLevel + S_SPACE + level;
            break;
            case CMD_PAK_DEF:
            case CMD_PAK_DIR_DEF:
            s += dp + zn + S_SPACE + level;
        }
        return s;
    }

    private String getWrapedParam(Object param){
        return nonEmpty(param) ? S_SPACE + S_DQM + param + S_DQM : OPT_NONE;
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

    public Path getRootPath(){
        return rootPath;
    }

    public Path getOutPath(){
        return outPath;
    }

    public Pattern getPattern(){
        return pattern;
    }

    public void setPattern(Pattern pattern){
        this.pattern = pattern;
    }

    public String getSizeExpr(){
        return sizeExpr;
    }

    public void setSizeExpr(String sizeExpr){
        this.sizeExpr = sizeExpr;
    }

    public String getSplit(){
        return split;
    }

    public void setSplit(String split){
        this.split = split;
    }

    public String getReplacement(){
        return replacement;
    }

    public void setReplacement(String replacement){
        this.replacement = replacement;
    }

    public String getZipName(){
        return zipName;
    }

    public void setZipName(String zipName){
        this.zipName = zipName;
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

    public long getCondition(){
        return condition;
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

    public int getZipLevel(){
        return zipLevel;
    }

    public void setZipLevel(int zipLevel){
        this.zipLevel = zipLevel;
    }

    public ZipOutputStream getZipOutputStream(){
        return zipOutputStream;
    }

    public ConcurrentMap<BasicFileAttributes,Path> getPathMap(){
        return pathMap;
    }

    private void setPathMap(ConcurrentMap<BasicFileAttributes,Path> pathMap){
        this.pathMap = pathMap;
    }

    public ConcurrentMap<Path,Path> getRePathMap(){
        return rePathMap;
    }

    private void setRePathMap(ConcurrentMap<Path,Path> rePathMap){
        this.rePathMap = rePathMap;
    }

    public ConcurrentMap<Path,List<Path>> getPathsMap(){
        return pathsMap;
    }

    private void setPathsMap(ConcurrentMap<Path,List<Path>> pathsMap){
        this.pathsMap = pathsMap;
    }

    public ConcurrentMap<Path,Long> getSizeMap(){
        return sizeMap;
    }

    private void setSizeMap(ConcurrentMap<Path,Long> sizeMap){
        this.sizeMap = sizeMap;
    }

    public BlockingDeque<Path> getPathDeque(){
        return pathDeque;
    }

    private void setPathDeque(BlockingDeque<Path> pathDeque){
        this.pathDeque = pathDeque;
    }

    public List<Path> getPathList(){
        return pathList;
    }

    private void setPathList(List<Path> pathList){
        this.pathList = pathList;
    }

    public AtomicLong getFilesSize(){
        return filesSize;
    }

    public AtomicInteger getFilesCount(){
        return filesCount;
    }

    public AtomicInteger getDirsCount(){
        return dirsCount;
    }

    public long getCacheFileSize(){
        return cacheFileSize;
    }

    private void setCacheFileSize(long cacheFileSize){
        this.cacheFileSize = cacheFileSize;
    }

    public int getCacheFilesCount(){
        return cacheFilesCount;
    }

    private void setCacheFilesCount(int cacheFilesCount){
        this.cacheFilesCount = cacheFilesCount;
    }

    public int getCacheDirsCount(){
        return cacheDirsCount;
    }

    private void setCacheDirsCount(int cacheDirsCount){
        this.cacheDirsCount = cacheDirsCount;
    }

    public Optional<Long> getDetailOptional(){
        return detailOptional;
    }

    public Optional<Long> getCmdOptional(){
        return cmdOptional;
    }

    public Optional<Long> getProgressOptional(){
        return progressOptional;
    }
}
