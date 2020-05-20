package legend.util.param;

import static java.nio.file.Paths.get;
import static java.util.Optional.of;
import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;
import static legend.util.ConsoleUtil.FS;
import static legend.util.StringUtil.brph;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.matchRange;
import static legend.util.ValueUtil.nonEmpty;
import static legend.util.ValueUtil.takeMaxValueIfBeyond;
import static legend.util.ValueUtil.takeMinValueIfBeyond;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.ZipOutputStream;

import legend.intf.IValue;
import legend.util.intf.IConsoleUtil.UnitType;
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
    private AtomicLong filesSize;
    private AtomicInteger filesCount;
    private AtomicInteger dirsCount;
    private ConcurrentMap<BasicFileAttributes,Path> pathMap;
    private ConcurrentMap<Path,Path> rePathMap;
    private ConcurrentMap<Path,List<Path>> pathsMap;
    private ConcurrentMap<Path,Long> sizeMap;
    private List<Path> dirsCache;
    private List<Path> pathsCache;
    private Optional<Long> detailOptional;
    private Optional<Long> cmdOptional;
    private Optional<Long> progressOptional;
    private long cacheFileSize;
    private int cacheFilesCount;
    private int cacheDirsCount;

    public FileParam(){
        zipName = replacement = sizeExpr = cmd = opt = S_EMPTY;
        split = REG_SPRT_COLS;
        minSize = 0l;
        maxSize = Long.MAX_VALUE;
        limit = level = Integer.MAX_VALUE;
        zipLevel = Deflater.DEFAULT_COMPRESSION;
        pathMap = new ConcurrentHashMap<>();
        rePathMap = new ConcurrentHashMap<>();
        pathsMap = new ConcurrentHashMap<>();
        sizeMap = new ConcurrentHashMap<>();
        dirsCache = new ArrayList<>();
        pathsCache = new ArrayList<>();
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
        fileParam.rootPath = rootPath;
        fileParam.outPath = outPath;
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

    @Override
    public String toString(){
        return getWholeCommand();
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
            case CMD_FND_SAM_MD5:
            condition |= COMPARE_SAME | IS_QUERY_COMMAND | MATCH_FILE_ONLY;
            break;
            case CMD_FND_DIR_OLY_SAM:
            condition |= COMPARE_SAME | IS_QUERY_COMMAND | MATCH_DIR_ONLY;
            break;
            case CMD_FND_DIR_SAM:
            condition |= COMPARE_SAME | IS_QUERY_COMMAND;
            break;
            case CMD_FND_PTH_RLT:
            condition |= PATH_RELATIVE;
            case CMD_FIND:
            case CMD_FND_DIF:
            case CMD_FND_DIF_MD5:
            case CMD_FND_PTH_ABS:
            condition |= IS_QUERY_COMMAND | MATCH_FILE_ONLY;
            break;
            case CMD_FND_PTH_DIR_OLY_RLT:
            condition |= MATCH_DIR_ONLY;
            case CMD_FND_PTH_DIR_RLT:
            condition |= PATH_RELATIVE | IS_QUERY_COMMAND;
            break;
            case CMD_FND_DIR_OLY_SIZ_ASC:
            condition |= MATCH_DIR_ONLY;
            case CMD_FND_SIZ_ASC:
            case CMD_FND_DIR_SIZ_ASC:
            case CMD_FND_DIR_DIR_SIZ_ASC:
            condition |= IS_QUERY_COMMAND | ORDER_ASC;
            break;
            case CMD_FND_DIR_OLY_DIF:
            case CMD_FND_DIR_OLY:
            case CMD_FND_DIR_OLY_SIZ_DSC:
            case CMD_FND_PTH_DIR_OLY_ABS:
            condition |= MATCH_DIR_ONLY;
            case CMD_FND_DIR:
            case CMD_FND_DIR_DIF:
            case CMD_FND_PTH_DIR_ABS:
            case CMD_FND_SIZ_DSC:
            case CMD_FND_DIR_SIZ_DSC:
            case CMD_FND_DIR_DIR_SIZ_DSC:
            condition |= IS_QUERY_COMMAND;
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
            case CMD_ZIP_INF_MD5:
            condition |= ZIP_UNZIP_MD5;
            case CMD_ZIP_INF_DIR:
            condition |= ZIP_UNZIP_DIR;
            case CMD_ZIP_INF:
            condition |= ZIP_UNZIP;
            case CMD_ZIP_DEF:
            case CMD_PAK_DEF:
            case CMD_PAK_INF:
            case CMD_7ZIP:
            condition |= MATCH_FILE_ONLY;
            break;
            case CMD_PAK_INF_MD5:
            condition |= ZIP_UNZIP_MD5;
            case CMD_PAK_INF_DIR:
            condition |= ZIP_UNZIP_DIR | MATCH_FILE_ONLY;
            break;
            case CMD_ITCHG_UGD:
            condition |= INTERCHANGE_UPGRADE;
            case CMD_ITCHG_RST:
            case CMD_RENAME:
            case CMD_REN_LOW:
            case CMD_REN_UP:
            case CMD_REN_UP_FST:
            case CMD_DELETE:
            case CMD_MOVE:
            case CMD_JSON_ENC:
            case CMD_JSON_DEC:
            condition |= NEED_CLEAR_CACHE;
            case CMD_COPY:
            case CMD_UPGRADE:
            case CMD_GUID_L32:
            case CMD_GUID_U32:
            case CMD_MD5_L8:
            case CMD_MD5_U8:
            case CMD_MD5_L16:
            case CMD_MD5_U16:
            case CMD_MD5_L32:
            case CMD_MD5_U32:
            case CMD_REP_FLE_BT:
            case CMD_REP_FLE_IL:
            case CMD_REG_FLE_GBK:
            case CMD_REG_FLE_BIG5:
            case CMD_REG_FLE_CS:
            case CMD_REP_FLE_SN:
            case CMD_REP_FLE_MEG:
            case CMD_REP_FLE_SPK:
            condition |= MATCH_FILE_ONLY;
        }
        if(opt.contains(OPT_CACHE)){
            condition |= ENABLE_CACHE;
            if(!meetCondition(NEED_CLEAR_CACHE)) condition |= CAN_SAVE_CACHE;
        }else if(nonEmpty(cache) && !cache.getPathMap().isEmpty() && !meetCondition(IS_QUERY_COMMAND)){
            condition |= CAN_USE_CACHE;
            pattern = cache.pattern;
            srcPath = cache.srcPath;
        }
        if(REG_ANY.equals(pattern.pattern())) condition |= IGNORE_REGEX;
        if(!opt.matches(REG_NON_PROG)) condition |= SHOW_PROGRESS;
        if(!opt.contains(OPT_SIMULATE)) condition |= EXEC_CMD;
        if(opt.contains(OPT_DETAIL) || opt.contains(OPT_SIMULATE)) condition |= SHOW_DETAIL;
        if(opt.contains(OPT_EXCLUDE_ROOT)){
            condition |= EXCLUDE_ROOT;
            rootPath = srcPath;
        }else rootPath = nonEmpty(srcPath.getParent()) ? srcPath.getParent() : srcPath;
        if(nonEmpty(backupPath)) outPath = backupPath;
        else if(nonEmpty(destPath)) outPath = destPath;
        else outPath = rootPath;
        cmdOptional = of(EXEC_CMD).filter(this::meetCondition);
        progressOptional = of(SHOW_PROGRESS).filter(this::meetCondition);
        detailOptional = of(SHOW_DETAIL).filter(this::meetCondition);
    }

    public boolean meetCondition(long condition){
        return condition == (condition & this.condition);
    }

    public boolean meetFilesSize(long size){
        if(!matchRange(size,minSize,maxSize)) return false;
        filesSize.addAndGet(size);
        return true;
    }

    public void saveCache(FileParam cache){
        if(meetCondition(CAN_USE_CACHE)){
            cache.getFilesSize().addAndGet(filesSize.addAndGet(cache.getCacheFileSize()));
            cache.getFilesCount().addAndGet(filesCount.addAndGet(cache.getCacheFilesCount()));
            cache.getDirsCount().addAndGet(dirsCount.addAndGet(cache.getCacheDirsCount()));
        }else{
            cache.getFilesSize().addAndGet(filesSize.get());
            cache.getFilesCount().addAndGet(filesCount.get());
            cache.getDirsCount().addAndGet(dirsCount.get());
        }
        if(meetCondition(CAN_SAVE_CACHE)){
            cache.clearCache();
            cache.setPathMap(pathMap);
            cache.setRePathMap(rePathMap);
            cache.setPathsMap(pathsMap);
            cache.setSizeMap(sizeMap);
            cache.setDirsCache(dirsCache);
            cache.setPattern(pattern);
            cache.setSrcPath(srcPath);
            cache.setCacheFileSize(filesSize.get());
            cache.setCacheFilesCount(filesCount.get());
            cache.setCacheDirsCount(dirsCount.get());
        }else if(meetCondition(NEED_CLEAR_CACHE)) cache.clearCache();
    }

    public boolean useCache(FileParam cache){
        if(meetCondition(CAN_USE_CACHE)){
            clearCache();
            pathMap = cache.getPathMap();
            rePathMap = cache.getRePathMap();
            pathsMap = cache.getPathsMap();
            sizeMap = cache.getSizeMap();
            dirsCache = cache.getDirsCache();
            return true;
        }else return false;
    }

    public void clearCache(){
        pathMap.clear();
        rePathMap.clear();
        pathsMap.clear();
        sizeMap.clear();
        dirsCache.clear();
    }

    public static List<FileParam> analyzeParam(String[] args){
        // 验证参数格式
        CS.checkError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT},()->args.length < 3);
        String[][] aa1 = new String[args.length][], aa2 = new String[args.length][];
        aa1[0] = aa2[0] = args[0].split(REG_SPRT_CMDS);
        Matcher mrpt = PTRN_RPT_ARG.matcher(aa1[0][0]);
        CS.checkError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT},()->mrpt.matches());
        Matcher mph = PTRN_OPT_ASK.matcher(S_EMPTY);
        for(int i = 0;i < aa1[0].length;i++){
            mph.reset(aa1[0][i]);
            CS.checkError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT},()->mph.matches());
        }
        // 将参数格式字符串解析成参数数组
        Deque<String> quotes1 = new ArrayDeque<>(), quotes2 = new ArrayDeque<>();
        Matcher mbq = PTRN_QUOTE_BQ.matcher(S_EMPTY);
        for(int i = 1;i < args.length;i++){
            mbq.reset(args[i]);
            while(mbq.find()){
                if(1 == i){
                    quotes1.add(quote(mbq.group(1)));
                    quotes2.add(quote(mbq.group()));
                }else{
                    quotes1.add(mbq.group(1));
                    quotes2.add(mbq.group());
                }
            }
            String s = mbq.replaceAll(SPC_NUL);
            aa1[i] = brph(s,SPH_MAP).split(REG_SPRT_CMDS);
            aa2[i] = s.split(REG_SPRT_CMDS);
            mrpt.reset(aa1[i][0]);
            if(aa1[0].length != aa1[i].length || mrpt.matches()) CS.checkError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT});
        }
        Matcher mnul = PTRN_SPC_NUL.matcher(S_EMPTY);
        StringBuilder sb1 = new StringBuilder(), sb2 = new StringBuilder();
        for(int i = 0;i < aa1.length;i++) for(int j = 0;j < aa1[i].length;j++){
            if(0 < i){
                sb1.delete(0,sb1.length());
                mnul.reset(aa1[i][j]);
                while(mnul.find() && !quotes1.isEmpty()) mnul.appendReplacement(sb1,quoteReplacement(quotes1.remove()));
                aa1[i][j] = mnul.appendTail(sb1).toString();
                sb2.delete(0,sb2.length());
                mnul.reset(aa2[i][j]);
                while(mnul.find() && !quotes2.isEmpty()) mnul.appendReplacement(sb2,quoteReplacement(quotes2.remove()));
                aa2[i][j] = mnul.appendTail(sb2).toString();
            }
            if(0 < j && mrpt.reset(aa1[i][j]).find()) aa1[i][j] = aa1[i][j - 1].replaceAll(OPT_CACHE,S_EMPTY) + mrpt.group(1);
            if(0 < j && mrpt.reset(aa2[i][j]).find()) aa2[i][j] = aa2[i][j - 1].replaceAll(OPT_CACHE,S_EMPTY) + mrpt.group(1);
        }
        // 去除所有占位符参数，重新生成每条子命令的有效参数数组。
        String[][] ss1 = new String[aa1[0].length][], ss2 = new String[aa2[0].length][];
        for(int i = 0,j,k;i < aa1[0].length;i++){
            int[] n = new int[aa1.length];
            for(j = k = 0;k < aa1.length;k++){
                mph.reset(aa1[k][i]);
                if(!mph.matches()) n[j++] = k;
            }
            for(ss1[i] = new String[j],ss2[i] = new String[j],k = 0;k < j;k++){
                ss1[i][k] = aa1[n[k]][i];
                ss2[i][k] = aa2[n[k]][i];
            }
        }
        // 生成所有子命令的参数对象集合，子命令与参数对象一一对应。
        List<FileParam> fileParams = new ArrayList<>(ss1.length);
        for(int i = 0;i < ss1.length;i++) try{
            FileParam param = new FileParam();
            String[] s1 = ss1[i], s2 = ss2[i];
            Optional<String[]> optional = of(s1);
            param.setCmd(s1[0]);
            if(s1[0].length() > 2){
                Matcher matcher = PTRN_OPT.matcher(s1[0]);
                if(matcher.find()){
                    param.setCmd(matcher.group(1));
                    param.setOpt(matcher.group(2));
                    while(matcher.find()) param.setOpt(param.getOpt() + matcher.group(2));
                }
            }
            param.setPattern(compile(s1[1]));
            param.setSrcPath(get(s1[2]));
            switch(param.getCmd()){
                case CMD_FIND:
                case CMD_FND_DIR:
                case CMD_FND_DIR_OLY:
                optional.filter(s->s.length > 3).ifPresent(s->param.setLimit(Integer.parseInt(s[3])));
                optional.filter(s->s.length > 4).ifPresent(s->param.setLevel(Integer.parseInt(s[4])));
                break;
                case CMD_FND_PTH_ABS:
                case CMD_FND_PTH_RLT:
                case CMD_FND_PTH_DIR_ABS:
                case CMD_FND_PTH_DIR_RLT:
                case CMD_FND_PTH_DIR_OLY_ABS:
                case CMD_FND_PTH_DIR_OLY_RLT:
                optional.filter(s->s.length > 3 && s[3].matches(REG_NUM)).ifPresent(s->{
                    param.setLimit(Integer.parseInt(s[3]));
                    if(s.length > 4) param.setLevel(Integer.parseInt(s[4]));
                });
                optional.filter(s->s.length > 3 && !s[3].matches(REG_NUM)).ifPresent(s->{
                    param.setDestPath(get(s[3]));
                    if(s.length > 4) param.setLimit(Integer.parseInt(s[4]));
                    if(s.length > 5) param.setLevel(Integer.parseInt(s[5]));
                });
                break;
                case CMD_FND_SIZ_ASC:
                case CMD_FND_SIZ_DSC:
                case CMD_FND_DIR_SIZ_ASC:
                case CMD_FND_DIR_SIZ_DSC:
                optional.filter(s->s.length > 3).ifPresent(s->{
                    param.setSizeExpr(s[3]);
                    matchSizes(param,s[3]);
                });
                optional.filter(s->s.length > 4).ifPresent(s->param.setLimit(Integer.parseInt(s[4])));
                optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
                break;
                case CMD_FND_DIR_DIR_SIZ_ASC:
                case CMD_FND_DIR_DIR_SIZ_DSC:
                case CMD_FND_DIR_OLY_SIZ_ASC:
                case CMD_FND_DIR_OLY_SIZ_DSC:
                optional.filter(s->s.length > 3).ifPresent(s->{
                    param.setSizeExpr(s[3]);
                    matchSizes(param,s[3]);
                });
                optional.filter(s->s.length > 4).ifPresent(s->param.setLimit(Integer.parseInt(s[4])));
                param.setLevel(1);
                break;
                case CMD_FND_SAM:
                case CMD_FND_DIF:
                case CMD_FND_SAM_MD5:
                case CMD_FND_DIF_MD5:
                case CMD_FND_DIR_SAM:
                case CMD_FND_DIR_DIF:
                case CMD_FND_DIR_OLY_SAM:
                case CMD_FND_DIR_OLY_DIF:
                case CMD_REP_FLE_SN:
                param.setDestPath(get(s1[3]));
                optional.filter(s->s.length > 4).ifPresent(s->param.setLimit(Integer.parseInt(s[4])));
                optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
                break;
                case CMD_REP_FLE_BT:
                param.setReplacement(s2[3]);
                optional.filter(s->s.length > 4).ifPresent(s->param.setSplit(s[4]));
                optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
                break;
                case CMD_REP_FLE_IL:
                optional.filter(s->s.length == 3).ifPresent(s->param.setDestPath(get(CONF_FILE_IL)));
                optional.filter(s->s.length > 3).ifPresent(s->{
                    if(s[3].matches(REG_NUM)){
                        param.setDestPath(get(CONF_FILE_IL));
                        param.setLevel(Integer.parseInt(s[3]));
                    }else param.setDestPath(get(s[3]));
                });
                optional.filter(s->s.length > 4).ifPresent(s->param.setLevel(Integer.parseInt(s[4])));
                break;
                case CMD_RENAME:
                case CMD_REN_DIR:
                case CMD_REN_DIR_OLY:
                case CMD_REG_FLE_CS:
                sb1.delete(0,sb1.length());
                mbq.reset(s2[3]);
                while(mbq.find()) mbq.appendReplacement(sb1,quoteReplacement(quoteReplacement(mbq.group(1))));
                s2[3] = mbq.appendTail(sb1).toString();
                param.setReplacement(s2[3]);
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
                case CMD_PAK_INF_DIR:
                case CMD_PAK_INF_MD5:
                case CMD_7ZIP:
                case CMD_GUID_L32:
                case CMD_GUID_U32:
                case CMD_MD5_L8:
                case CMD_MD5_U8:
                case CMD_MD5_L16:
                case CMD_MD5_U16:
                case CMD_MD5_L32:
                case CMD_MD5_U32:
                case CMD_JSON_ENC:
                case CMD_JSON_DEC:
                case CMD_REP_FLE_MEG:
                case CMD_REP_FLE_SPK:
                optional.filter(s->s.length > 3).ifPresent(s->param.setLevel(Integer.parseInt(s[3])));
                break;
                case CMD_COPY:
                case CMD_CPY_DIR:
                case CMD_CPY_DIR_OLY:
                case CMD_MOVE:
                case CMD_MOV_DIR:
                case CMD_MOV_DIR_OLY:
                case CMD_ZIP_INF:
                case CMD_ZIP_INF_DIR:
                case CMD_ZIP_INF_MD5:
                case CMD_REG_FLE_GBK:
                case CMD_REG_FLE_BIG5:
                param.setDestPath(get(s1[3]));
                optional.filter(s->s.length > 4).ifPresent(s->param.setLevel(Integer.parseInt(s[4])));
                break;
                case CMD_ITCHG_UGD:
                case CMD_ITCHG_RST:
                param.setDestPath(get(s1[3]));
                param.setBackupPath(get(s1[4]));
                optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
                break;
                case CMD_UPGRADE:
                case CMD_UGD_DIR:
                param.setDestPath(get(s1[3]));
                optional.filter(s->s.length > 4).ifPresent(s->{
                    if(s[3].matches(REG_NUM)){
                        param.setLevel(Integer.parseInt(s[4]));
                    }else param.setBackupPath(get(s[4]));
                });
                optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
                break;
                case CMD_ZIP_DEF:
                case CMD_ZIP_DIR_DEF:
                param.setDestPath(get(s1[3]));
                param.setZipName(s1[4] + EXT_ZIP);
                optional.filter(s->s.length > 5).ifPresent(s->param.setZipLevel(Integer.parseInt(s[5])));
                optional.filter(s->s.length > 6).ifPresent(s->param.setLevel(Integer.parseInt(s[6])));
                break;
                case CMD_PAK_DEF:
                case CMD_PAK_DIR_DEF:
                param.setDestPath(get(s1[3]));
                param.setZipName(s1[4] + EXT_PAK);
                optional.filter(s->s.length > 5).ifPresent(s->param.setZipLevel(Integer.parseInt(s[5])));
                optional.filter(s->s.length > 6).ifPresent(s->param.setLevel(Integer.parseInt(s[6])));
                break;
                default:
                CS.checkError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT});
            }
            fileParams.add(param);
        }catch(Exception e){
            CS.checkError(ERR_ARG_ANLS,new String[]{e.toString()});
        }
        return fileParams;
    }

    public static String convertParam(String param, boolean regex){
        Deque<String> quotes = new ArrayDeque<>();
        Matcher matcher = PTRN_QUOTE_BQ.matcher(param);
        if(regex) while(matcher.find()) quotes.add(quoteReplacement(quote(matcher.group(1))));
        else while(matcher.find()) quotes.add(matcher.group(1));
        StringBuilder builder = new StringBuilder(brph(matcher.replaceAll(SPC_NUL),SPH_MAP));
        matcher = PTRN_SPC_NUL.matcher(builder.toString());
        builder.delete(0,builder.length());
        while(matcher.find() && !quotes.isEmpty()) matcher.appendReplacement(builder,quotes.remove());
        return matcher.appendTail(builder).toString();
    }

    private static void matchSizes(FileParam param, String size){
        long minSize = param.getMinSize();
        long maxSize = param.getMaxSize();
        UnitType minType = UnitType.NON;
        UnitType maxType = UnitType.NON;
        Matcher matcher = PTRN_FLE_SIZ.matcher(size);
        if(matcher.find()){
            minType = FS.matchType(matcher.group(2));
            minSize = Long.parseLong(matcher.group(1));
        }
        if(matcher.find()){
            maxType = FS.matchType(matcher.group(2));
            maxSize = FS.matchSize(Long.parseLong(matcher.group(1)),maxType);
        }
        minSize = FS.matchSize(minSize,UnitType.NON == minType ? maxType : minType);
        if(minSize <= maxSize){
            param.setMinSize(minSize);
            param.setMaxSize(maxSize);
        }else{
            param.setMinSize(maxSize);
            param.setMaxSize(minSize);
        }
    }

    private String getWholeCommand(){
        String regex = getWrapedParam(pattern);
        String sp = getWrapedParam(srcPath);
        String dp = getWrapedParam(destPath);
        String bp = getWrapedParam(backupPath);
        String se = getWrapedParam(sizeExpr);
        String spt = getWrapedParam(split);
        String rp = getWrapedParam(replacement);
        String zn = getWrapedParam(zipName);
        String s = CMD + cmd + opt + regex + sp;
        switch(cmd){
            case CMD_FND_SIZ_ASC:
            case CMD_FND_SIZ_DSC:
            case CMD_FND_DIR_SIZ_ASC:
            case CMD_FND_DIR_SIZ_DSC:
            case CMD_FND_DIR_DIR_SIZ_ASC:
            case CMD_FND_DIR_DIR_SIZ_DSC:
            case CMD_FND_DIR_OLY_SIZ_ASC:
            case CMD_FND_DIR_OLY_SIZ_DSC:
            s += se;
            case CMD_FIND:
            case CMD_FND_DIR:
            case CMD_FND_DIR_OLY:
            s += S_SPACE + limit + S_SPACE + level;
            break;
            case CMD_FND_PTH_ABS:
            case CMD_FND_PTH_RLT:
            case CMD_FND_PTH_DIR_ABS:
            case CMD_FND_PTH_DIR_RLT:
            case CMD_FND_PTH_DIR_OLY_ABS:
            case CMD_FND_PTH_DIR_OLY_RLT:
            case CMD_FND_SAM:
            case CMD_FND_DIF:
            case CMD_FND_SAM_MD5:
            case CMD_FND_DIF_MD5:
            case CMD_FND_DIR_SAM:
            case CMD_FND_DIR_DIF:
            case CMD_FND_DIR_OLY_SAM:
            case CMD_FND_DIR_OLY_DIF:
            case CMD_REP_FLE_SN:
            s += dp + S_SPACE + limit + S_SPACE + level;
            break;
            case CMD_REP_FLE_BT:
            s += rp + spt + S_SPACE + level;
            break;
            case CMD_RENAME:
            case CMD_REN_DIR:
            case CMD_REN_DIR_OLY:
            case CMD_REG_FLE_CS:
            s += rp;
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
            case CMD_PAK_INF_DIR:
            case CMD_PAK_INF_MD5:
            case CMD_7ZIP:
            case CMD_GUID_L32:
            case CMD_GUID_U32:
            case CMD_MD5_L8:
            case CMD_MD5_U8:
            case CMD_MD5_L16:
            case CMD_MD5_U16:
            case CMD_MD5_L32:
            case CMD_MD5_U32:
            case CMD_JSON_ENC:
            case CMD_JSON_DEC:
            case CMD_REP_FLE_MEG:
            case CMD_REP_FLE_SPK:
            s += S_SPACE + level;
            break;
            case CMD_COPY:
            case CMD_CPY_DIR:
            case CMD_CPY_DIR_OLY:
            case CMD_MOVE:
            case CMD_MOV_DIR:
            case CMD_MOV_DIR_OLY:
            case CMD_ZIP_INF:
            case CMD_ZIP_INF_DIR:
            case CMD_ZIP_INF_MD5:
            case CMD_REP_FLE_IL:
            case CMD_REG_FLE_GBK:
            case CMD_REG_FLE_BIG5:
            s += dp + S_SPACE + level;
            break;
            case CMD_ITCHG_UGD:
            case CMD_ITCHG_RST:
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

    private static String getWrapedParam(Object param){
        return nonEmpty(param) ? S_SPACE + S_DQM + param + S_DQM : S_EMPTY;
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
        this.limit = takeMaxValueIfBeyond(limit,1,Integer.MAX_VALUE);
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int level){
        this.level = takeMaxValueIfBeyond(level,1,Integer.MAX_VALUE);
    }

    public int getZipLevel(){
        return zipLevel;
    }

    public void setZipLevel(int zipLevel){
        this.zipLevel = takeMinValueIfBeyond(zipLevel,Deflater.DEFAULT_COMPRESSION,Deflater.BEST_COMPRESSION);
    }

    public ZipOutputStream getZipOutputStream(){
        return zipOutputStream;
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

    public List<Path> getDirsCache(){
        return dirsCache;
    }

    private void setDirsCache(List<Path> dirsCache){
        this.dirsCache = dirsCache;
    }

    public List<Path> getPathsCache(){
        return pathsCache;
    }

    public void setPathsCache(List<Path> pathsCache){
        this.pathsCache = pathsCache;
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
}
