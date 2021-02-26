package legend.util;

import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.find;
import static java.nio.file.Files.move;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.write;
import static java.nio.file.Files.writeString;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;
import static legend.util.CharsetDetectorUtil.detectorFileCharset;
import static legend.util.ConsoleUtil.IN;
import static legend.util.JsonUtil.formatJson;
import static legend.util.JsonUtil.trimJson;
import static legend.util.MD5Util.getMD5L32;
import static legend.util.StringUtil.getFileNameWithoutSuffix;
import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.glph;
import static legend.util.StringUtil.gs;
import static legend.util.StringUtil.gsph;
import static legend.util.TimeUtil.countDuration;
import static legend.util.TimeUtil.decTotalDuration;
import static legend.util.TimeUtil.getDurationString;
import static legend.util.TimeUtil.getTotalDurationString;
import static legend.util.TimeUtil.incTotalDuration;
import static legend.util.TimeUtil.resetTime;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;
import static legend.util.param.FileParam.analyzeParam;
import static legend.util.rule.ReplaceRuleEngine.ProvideRuleEngine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import legend.intf.IValue;
import legend.util.intf.ICharsetDetectorUtil.Language;
import legend.util.intf.IConsoleUtil;
import legend.util.intf.IFileUtil;
import legend.util.intf.IFileVersion;
import legend.util.intf.IProgress;
import legend.util.logic.FileHandleDSRPLogic;
import legend.util.logic.FileHandleZip7Logic;
import legend.util.logic.FileMergeLogic;
import legend.util.logic.FileReplaceILCodeLogic;
import legend.util.logic.FileReplaceSPKCodeLogic;
import legend.util.logic.intf.ILogic;
import legend.util.param.FileParam;
import legend.util.param.FileVersion;
import legend.util.param.SingleValue;
import legend.util.rule.intf.IReplaceRuleEngine;

public final class FileUtil implements IFileUtil,IConsoleUtil{
    private static final FileParam CACHE;
    private static FileParam FP;
    private static PrintStream PS;
    static{
        CACHE = new FileParam();
    }

    private FileUtil(){}

    public static void main(String[] args){
        dealFiles(args);
    }

    public static void dealFiles(Collection<FileParam> fileParams){
        fileParams.stream().forEach(param->{
            param.refreshConditions(CACHE);
            FP = param;
            final String opt = FP.getOpt();
            final boolean progress = !opt.matches(REG_NON_PROG);
            final String command = FP.toString();
            if(opt.contains(OPT_SIMULATE)) CS.s(V_SMLT + gsph(ST_CMD_START,command)).l(2);
            else CS.s(gsph(ST_CMD_START,command)).l(2);
            if(FP.getOpt().contains(OPT_ASK)){
                CS.s(ST_ASK_CONT);
                String line = decTotalDuration(()->IN.nextLine());
                CS.sl(false,line).l(1);
                if(!PTRN_ASK_NO.matcher(line).find()){
                    if(progress) countDuration(t->PG.runUntillFinish(FileUtil::dealFiles));
                    else countDuration(t->dealFiles(FP));
                }else resetTime();
            }else if(progress) countDuration(t->PG.runUntillFinish(FileUtil::dealFiles));
            else countDuration(t->dealFiles(FP));
            if(opt.contains(OPT_SIMULATE)) CS.s(V_SMLT + gsph(ST_CMD_DONE,command) + N_DEAL + FP.getDirsCount().get() + N_AN + N_DIR + S_COMMA + FP.getFilesCount().get() + N_AN + N_FILE + S_BRACKET_L + N_SIZE + S_COLON).formatSize(param.getFilesSize().get(),UnitType.TB).s(S_BRACKET_R + S_SEMICOLON + N_TIME + S_COLON + getDurationString() + S_PERIOD).l(2);
            else CS.s(gsph(ST_CMD_DONE,command) + N_DEAL + FP.getDirsCount().get() + N_AN + N_DIR + S_COMMA + FP.getFilesCount().get() + N_AN + N_FILE + S_BRACKET_L + N_SIZE + S_COLON).formatSize(FP.getFilesSize().get(),UnitType.TB).s(S_BRACKET_R + S_SEMICOLON + N_TIME + S_COLON + getDurationString() + S_PERIOD).l(2);
        });
    }

    public static void dealFiles(FileParam param){
        try(param){
            if(FP != param) param.refreshConditions(CACHE);
            if(!param.useCache(CACHE)){
                long count = cachePaths(param);
                param.getProgressOptional().ifPresent(c->PG.reset(count,PROGRESS_POSITION));
            }else param.getProgressOptional().ifPresent(c->PG.reset(CACHE.getCacheDirsCount() + CACHE.getCacheFilesCount(),PROGRESS_POSITION));
            cacheRepaths(param);
            param.getProgressOptional().ifPresent(c->PG.resume());
            switch(param.getCmd()){
                case CMD_FIND:
                case CMD_FND_DIR:
                case CMD_FND_DIR_OLY:
                findSortedFiles(param);
                break;
                case CMD_FND_PTH_ABS:
                case CMD_FND_PTH_RLT:
                case CMD_FND_PTH_DIR_ABS:
                case CMD_FND_PTH_DIR_RLT:
                case CMD_FND_PTH_DIR_OLY_ABS:
                case CMD_FND_PTH_DIR_OLY_RLT:
                findSortedFilePaths(param);
                break;
                case CMD_FND_SIZ_ASC:
                case CMD_FND_SIZ_DSC:
                case CMD_FND_DIR_SIZ_ASC:
                case CMD_FND_DIR_SIZ_DSC:
                findSortedFileSizes(param);
                break;
                case CMD_FND_DIR_DIR_SIZ_ASC:
                case CMD_FND_DIR_DIR_SIZ_DSC:
                case CMD_FND_DIR_OLY_SIZ_ASC:
                case CMD_FND_DIR_OLY_SIZ_DSC:
                findSortedDirSizes(param);
                break;
                case CMD_FND_SAM:
                case CMD_FND_DIF:
                case CMD_FND_DIR_SAM:
                case CMD_FND_DIR_DIF:
                case CMD_FND_DIR_OLY_SAM:
                case CMD_FND_DIR_OLY_DIF:
                findSortedFilesByCompared(param);
                break;
                case CMD_FND_SAM_MD5:
                case CMD_FND_DIF_MD5:
                findFilesByComparedUseMD5(param);
                break;
                case CMD_REP_FLE_SN:
                replaceFilesForSameName(param);
                break;
                case CMD_REP_FLE_MEG:
                replaceFilesForMergeContent(param);
                break;
                case CMD_REP_FLE_BT:
                replaceFilesWithBT(param);
                break;
                case CMD_REP_FLE_IL:
                replaceFilesWithIL(param);
                break;
                case CMD_REP_FLE_SPK:
                replaceFilesWithSPK(param);
                break;
                case CMD_7ZIP:
                handleFilesWith7Zip(param);
                break;
                case CMD_DSRP:
                handleFilesWithDSRP(param);
                break;
                case CMD_REG_FLE_GBK:
                regenFileWithGBK(param);
                break;
                case CMD_REG_FLE_BIG5:
                regenFileWithBIG5(param);
                break;
                case CMD_REG_FLE_CS:
                regenFileCharset(param);
                break;
                case CMD_RENAME:
                case CMD_REN_DIR:
                case CMD_REN_DIR_OLY:
                renameFiles(param);
                break;
                case CMD_REN_LOW:
                case CMD_REN_DIR_LOW:
                case CMD_REN_DIR_OLY_LOW:
                renLowFiles(param);
                break;
                case CMD_REN_UP:
                case CMD_REN_DIR_UP:
                case CMD_REN_DIR_OLY_UP:
                renUpFiles(param);
                break;
                case CMD_REN_UP_FST:
                case CMD_REN_DIR_UP_FST:
                case CMD_REN_DIR_OLY_UP_FST:
                renUpFstFiles(param);
                break;
                case CMD_COPY:
                case CMD_CPY_DIR:
                case CMD_CPY_DIR_OLY:
                copyFiles(param);
                break;
                case CMD_DELETE:
                case CMD_DEL_DIR:
                case CMD_DEL_DIR_OLY:
                deleteFiles(param);
                break;
                case CMD_DEL_NUL:
                case CMD_DEL_DIR_NUL:
                case CMD_DEL_DIR_OLY_NUL:
                delNulFiles(param);
                break;
                case CMD_DEL_OLD_VER:
                case CMD_DEL_DIR_OLD_VER:
                case CMD_DEL_DIR_OLY_OLD_VER:
                delOldVersions(param);
                break;
                case CMD_MOVE:
                case CMD_MOV_DIR:
                case CMD_MOV_DIR_OLY:
                moveFiles(param);
                break;
                case CMD_ITCHG_UGD:
                case CMD_ITCHG_RST:
                interchangeFiles(param);
                break;
                case CMD_UPGRADE:
                case CMD_UGD_DIR:
                upgradeFiles(param);
                break;
                case CMD_ZIP_DEF:
                case CMD_ZIP_DIR_DEF:
                case CMD_PAK_DEF:
                case CMD_PAK_DIR_DEF:
                zipFiles(param);
                break;
                case CMD_ZIP_INF:
                case CMD_ZIP_INF_DIR:
                case CMD_ZIP_INF_MD5:
                case CMD_PAK_INF:
                case CMD_PAK_INF_DIR:
                case CMD_PAK_INF_MD5:
                unzipFiles(param);
                break;
                case CMD_GUID_L32:
                showMD5(param,MD5Util::getGuidL32);
                break;
                case CMD_GUID_U32:
                showMD5(param,MD5Util::getGuidU32);
                break;
                case CMD_MD5_L8:
                showMD5(param,MD5Util::getMD5L8);
                break;
                case CMD_MD5_U8:
                showMD5(param,MD5Util::getMD5U8);
                break;
                case CMD_MD5_L16:
                showMD5(param,MD5Util::getMD5L16);
                break;
                case CMD_MD5_U16:
                showMD5(param,MD5Util::getMD5U16);
                break;
                case CMD_MD5_L32:
                showMD5(param,MD5Util::getMD5L32);
                break;
                case CMD_MD5_U32:
                showMD5(param,MD5Util::getMD5U32);
                break;
                case CMD_JSON_ENC:
                encodeJson(param);
                break;
                case CMD_JSON_DEC:
                decodeJson(param);
            }
            param.saveCache(CACHE);
        }catch(Exception e){
            CS.sl(gsph(ERR_EXEC_CMD_SPEC,param.toString(),e.toString()));
        }finally{
            param.getDetailOptional().ifPresent(s->CS.l(1));
        }
    }

    public static Path makeDirs(Path path){
        path.getParent().toFile().mkdirs();
        path.toFile().setWritable(true,true);
        return path;
    }

    public static boolean nonEmptyDir(Path path){
        File file = path.toFile();
        return file.isDirectory() && 0 < file.listFiles().length;
    }

    public static boolean existsPath(Path path){
        return exists(path);
    }

    public static byte[] readBinaryFormFile(Path path){
        try{
            return readAllBytes(path);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_READ,path.toString(),e.toString()));
        }
        return new byte[0];
    }

    public static void writeBinaryToFile(Path path, byte[] bytes){
        try{
            write(makeDirs(path),bytes);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_WRT,path.toString(),e.toString()));
        }
    }

    public static List<String> readFile(Path path, String charsetName){
        try{
            return readAllLines(path,forName(charsetName));
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_READ,path.toString(),e.toString()));
        }
        return new ArrayList<String>();
    }

    public static List<String> readFile(Path path){
        return readFile(path,CHARSET_UTF8);
    }

    public static void writeFile(Path path, Collection<String> lines, String charsetName){
        try{
            write(makeDirs(path),lines,forName(charsetName));
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_WRT,path.toString(),e.toString()));
        }
    }

    public static void writeFile(Path path, Collection<String> lines){
        writeFile(path,lines,CHARSET_UTF8);
    }

    public static void writeFileWithUTF8Bom(Path path, String data){
        try(OutputStream outputStream = newOutputStream(path);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,CHARSET_UTF8))){
            outputStream.write(BOM_UTF8);
            bufferedWriter.write(data);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_WRT,path.toString(),e.toString()));
        }
    }

    public static void writeFileWithUTF16LEBom(Path path, String data){
        try(OutputStream outputStream = newOutputStream(path);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,CHARSET_UTF16LE))){
            outputStream.write(BOM_UTF16LE);
            bufferedWriter.write(data);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_WRT,path.toString(),e.toString()));
        }
    }

    public static void writeFileWithUTF16BEBom(Path path, String data){
        try(OutputStream outputStream = newOutputStream(path);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,CHARSET_UTF16BE))){
            outputStream.write(BOM_UTF16BE);
            bufferedWriter.write(data);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_WRT,path.toString(),e.toString()));
        }
    }

    public static void writeFileForTranscoding(Path path, String charsetDest){
        try{
            byte[] bytes = readAllBytes(path);
            int offset = 0, length = bytes.length;
            if(1 > length) return;
            String charsetSrc = detectorFileCharset(path,Language.CHINESE);
            switch(charsetSrc){
                case CHARSET_UTF8_BOM:
                if(4 > length) return;
                offset = 3;
                length -= offset;
                break;
                case CHARSET_UTF16LE:
                case CHARSET_UTF16BE:
                if(3 > length) return;
                offset = 2;
                length -= offset;
            }
            String data = new String(bytes,offset,length,charsetSrc);
            if(CHARSET_UTF8_BOM.equalsIgnoreCase(charsetDest)) writeFileWithUTF8Bom(path,data);
            else if(CHARSET_UTF16LE.equalsIgnoreCase(charsetDest)) writeFileWithUTF16LEBom(path,data);
            else if(CHARSET_UTF16BE.equalsIgnoreCase(charsetDest)) writeFileWithUTF16BEBom(path,data);
            else writeString(path,data,forName(charsetDest));
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_WRT,path.toString(),e.toString()));
        }
    }

    public static void deleteFile(Path path){
        try{
            path.toFile().setWritable(true,true);
            deleteIfExists(path);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_DEL,path.toString(),e.toString()));
        }
    }

    public static void copyFile(Path src, Path dest){
        try{
            copy(src,makeDirs(dest),StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_CPY,src.toString(),dest.toString(),e.toString()));
        }
    }

    public static void moveFile(Path src, Path dest){
        try{
            move(src,makeDirs(dest),StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_MOV,src.toString(),dest.toString(),e.toString()));
        }
    }

    private static void dealFiles(String[] args){
        try{
            PS = new PrintStream(new File(FILE_LOG));
            CS.cacheStream(PS);
        }catch(FileNotFoundException e){
            CS.sl(gsph(ERR_LOG_FLE_CRT,FILE_LOG,e.toString()));
        }
        CS.showHelp(HELP_FILE,()->isEmpty(args));
        CS.s(ST_PRG_START).l(2);
        String cmds = incTotalDuration(()->gs(args));
        CS.s(gsph(ST_ARG_START,cmds)).l(2);
        List<FileParam> fileParams = incTotalDuration(()->analyzeParam(args));
        CS.s(gsph(ST_ARG_DONE,cmds) + N_TIME + S_COLON + getTotalDurationString() + S_PERIOD).l(2);
        incTotalDuration(t->dealFiles(fileParams));
        CS.s(ST_PRG_DONE + N_DEAL + CACHE.getDirsCount().get() + N_AN + N_DIR + S_COMMA + CACHE.getFilesCount().get() + N_AN + N_FILE + S_BRACKET_L + N_SIZE + S_COLON).formatSize(CACHE.getFilesSize().get(),UnitType.TB).s(S_BRACKET_R + S_SEMICOLON + N_TIME + S_COLON + getTotalDurationString() + S_PERIOD).l(2);
    }

    private static void dealFiles(IProgress progress){
        CS.clearStream();
        dealFiles(FP);
        CS.cacheStream(PS);
    }

    protected static void findSortedFiles(FileParam param){
        param.getDetailOptional().ifPresent(c->param.getDirsCache().stream().sorted(new PathComparator(true)).limit(param.getLimit()).forEach(p->showDir(new String[]{V_FIND},new FileSizeMatcher(p.toFile()),p)));
        int limit = param.getLimit() - param.getDirsCache().size();
        if(0 < limit) param.getDetailOptional().ifPresent(c->param.getPathMap().entrySet().stream().filter(e->e.getKey().isRegularFile()).flatMap(m->of(m.getValue())).sorted(new PathComparator(true)).limit(limit).forEach(p->showFile(new String[]{V_FIND},new FileSizeMatcher(p.toFile()),p)));
    }

    protected static void findSortedFilePaths(FileParam param){
        final boolean relative = param.meetCondition(PATH_RELATIVE);
        List<Path> pathsCache = param.getPathsCache();
        Stream<Path> dirs = param.getPathMap().entrySet().stream().filter(e->e.getKey().isDirectory()).flatMap(e->of(relative ? param.getRootPath().relativize(e.getValue()) : e.getValue())).sorted(new PathComparator(true)).limit(param.getLimit());
        int limit = param.getLimit() - param.getDirsCache().size();
        Stream<Path> files = null;
        if(0 < limit) files = param.getPathMap().entrySet().stream().filter(e->e.getKey().isRegularFile()).flatMap(e->of(relative ? param.getRootPath().relativize(e.getValue()) : e.getValue())).sorted(new PathComparator(true)).limit(limit);
        if(nonEmpty(files)) pathsCache.addAll(concat(dirs,files).collect(toList()));
        else pathsCache.addAll(dirs.collect(toList()));
        param.getDetailOptional().ifPresent(c->pathsCache.stream().forEach(p->CS.sl(p.toString())));
        if(nonEmpty(param.getDestPath())){
            param.getCmdOptional().ifPresent(c->writeFile(param.getDestPath(),pathsCache.stream().map(p->p.toString()).collect(toList())));
            param.getDetailOptional().ifPresent(c->CS.l(1).sl(V_WRITE + N_FILE + gs(4) + param.getDestPath()));
        }
    }

    protected static void findSortedFileSizes(FileParam param){
        param.getDetailOptional().ifPresent(c->param.getPathMap().entrySet().stream().filter(e->e.getKey().isRegularFile()).sorted(new BasicFileAttributesPathComparator(param.meetCondition(ORDER_ASC))).limit(param.getLimit()).forEach(e->CS.formatSize(e.getKey().size(),UnitType.GB).s(4).sl(e.getValue().toString())));
    }

    protected static void findSortedFilesByCompared(FileParam param){
        final boolean same = param.meetCondition(COMPARE_SAME);
        ConcurrentMap<BasicFileAttributes,Path> pathMap = new ConcurrentHashMap<>();
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            BasicFileAttributes a = e.getKey();
            Path p = e.getValue();
            File file = param.getDestPath().resolve(param.getSrcPath().relativize(p)).toFile();
            if(a.isRegularFile()){
                if(same && file.isFile() || !same && !file.isFile()) pathMap.put(a,p);
                else{
                    param.getFilesCount().addAndGet(-1);
                    param.getFilesSize().addAndGet(a.size() * -1);
                }
            }else if(same && file.isDirectory() || !same && !file.isDirectory()){
                param.getDirsCache().add(p);
                pathMap.put(a,p);
            }else param.getDirsCount().addAndGet(-1);
        });
        param.getPathMap().clear();
        param.getPathMap().putAll(pathMap);
        findSortedFiles(param);
    }

    protected static void findFilesByComparedUseMD5(FileParam param){
        final boolean same = param.meetCondition(COMPARE_SAME);
        ConcurrentMap<BasicFileAttributes,Path> pathMap = new ConcurrentHashMap<>();
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            BasicFileAttributes a = e.getKey();
            Path p1 = e.getValue();
            Path p2 = param.getDestPath().resolve(param.getSrcPath().relativize(p1));
            if(p2.toFile().isFile() && (same && getMD5L32(p1).equals(getMD5L32(p2)) || !same && !getMD5L32(p1).equals(getMD5L32(p2)))) pathMap.put(a,p1);
            else{
                param.getFilesCount().addAndGet(-1);
                param.getFilesSize().addAndGet(a.size() * -1);
            }
        });
        param.getPathMap().clear();
        param.getPathMap().putAll(pathMap);
        findSortedFiles(param);
    }

    protected static void findSortedDirSizes(FileParam param){
        param.getFilesCount().set(0);
        param.getDirsCount().set(0);
        param.getFilesSize().set(0l);
        param.getPathMap().entrySet().parallelStream().filter(e->e.getKey().isRegularFile()).forEach(e->param.getSizeMap().put(e.getValue(),e.getKey().size()));
        param.getDirsCache().parallelStream().forEach(p->{
            FileParam fp = new FileParam();
            fp.setCmd(CMD_FND_SIZ_ASC);
            fp.setPattern(PTRN_ANY);
            fp.setSrcPath(p);
            cachePaths(fp);
            fp.clearCache();
            final long size = fp.getFilesSize().get();
            if(param.meetFilesSize(size)) param.getSizeMap().put(p,size);
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        param.getSizeMap().entrySet().stream().sorted(new PathLongComparator(param.meetCondition(ORDER_ASC))).limit(param.getLimit()).forEach(e->{
            Path path = e.getKey();
            if(path.toFile().isFile()){
                param.getFilesCount().incrementAndGet();
                param.meetFilesSize(e.getValue());
                param.getDetailOptional().ifPresent(c->CS.formatSize(e.getValue(),UnitType.GB).sl(gs(4) + N_FILE + gs(4) + path.toString()));
            }else{
                param.getDirsCount().incrementAndGet();
                param.meetFilesSize(e.getValue());
                param.getDetailOptional().ifPresent(c->CS.formatSize(e.getValue(),UnitType.GB).sl(gs(4) + N_DIR + gs(4) + path.toString()));
            }
        });
    }

    protected static void replaceFilesForSameName(FileParam param){
        FileParam fp = param.cloneValue();
        fp.setCmd(CMD_FIND);
        fp.setSrcPath(fp.getDestPath());
        fp.setLevel(fp.getLimit());
        cachePaths(fp);
        Collection<Entry<BasicFileAttributes,Path>> caches = new ConcurrentLinkedQueue<>(fp.getPathMap().entrySet());
        param.getPathMap().values().parallelStream().forEach(p1->{
            caches.parallelStream().forEach(e->{
                Path p2 = e.getValue();
                if(p2.getFileName().toString().equalsIgnoreCase(p1.getFileName().toString())){
                    param.getDetailOptional().ifPresent(c->showFile(new String[]{V_REPL},new FileSizeMatcher(e.getKey()),p2));
                    param.getCmdOptional().ifPresent(c->copyFile(p1,p2));
                    caches.remove(e);
                }
            });
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    protected static void replaceFilesForMergeContent(FileParam param){
        executeFileLogic(param,new FileMergeLogic(param));
    }

    protected static void replaceFilesWithBT(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            Path path = e.getValue();
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_REPL},new FileSizeMatcher(e.getKey()),path));
            IReplaceRuleEngine ruleEngine = ProvideRuleEngine(param.getReplacement());
            Collection<String> results = ruleEngine.execute(readFile(path),param.getSplit());
            param.getDetailOptional().ifPresent(c->CS.sl(results,1));
            param.getCmdOptional().ifPresent(c->writeFile(path,results));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    protected static void replaceFilesWithIL(FileParam param){
        executeFileLogic(param,new FileReplaceILCodeLogic(param));
    }

    protected static void replaceFilesWithSPK(FileParam param){
        executeFileLogic(param,new FileReplaceSPKCodeLogic(param));
    }

    protected static void handleFilesWith7Zip(FileParam param){
        executeFileLogic(param,new FileHandleZip7Logic(param));
    }

    protected static void handleFilesWithDSRP(FileParam param){
        executeFileLogic(param,new FileHandleDSRPLogic(param));
    }

    protected static void regenFileWithGBK(FileParam param){
        StringBuffer buffer = new StringBuffer();
        param.getPathMap().entrySet().parallelStream().forEach(entry->{
            Path path = entry.getValue();
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_EXTR},new FileSizeMatcher(entry.getKey()),path));
            List<String> datas = readFile(path,detectorFileCharset(path,Language.SIMPLIFIED_CHINESE));
            datas.parallelStream().distinct().forEach(data->buffer.append(PTRN_UC_NON_CHS.matcher(data).replaceAll(S_EMPTY)));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        if(0 == buffer.length()){
            fillDatas(buffer,CHARSET_GBK,0xa1,0xa9,0xa1,0xfe);
            fillDatas(buffer,CHARSET_GBK,0xb0,0xf7,0xa1,0xfe);
        }else{
            Set<String> caches = new HashSet<>();
            char[] ca = buffer.toString().toCharArray();
            for(int i = 0;i < ca.length;i++) caches.add(ca[i] + S_EMPTY);
            buffer.delete(0,buffer.length());
            caches.parallelStream().forEach(cache->buffer.append(cache));
        }
        String result = PTRN_UC_NON_CHS.matcher(buffer.toString().replaceAll(REG_UC_MC_GBK,S_EMPTY)).replaceAll(S_EMPTY);
        param.getDetailOptional().ifPresent(c->CS.sl(result,1));
        param.getCmdOptional().ifPresent(c->writeFileWithUTF16LEBom(param.getDestPath(),result));
    }

    protected static void regenFileWithBIG5(FileParam param){
        StringBuffer buffer = new StringBuffer();
        param.getPathMap().entrySet().parallelStream().forEach(entry->{
            Path path = entry.getValue();
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_EXTR},new FileSizeMatcher(entry.getKey()),path));
            List<String> datas = readFile(path,detectorFileCharset(path,Language.TRADITIONAL_CHINESE));
            datas.parallelStream().distinct().forEach(data->buffer.append(PTRN_UC_NON_CHS.matcher(data).replaceAll(S_EMPTY)));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        if(0 == buffer.length()){
            fillDatas(buffer,CHARSET_BIG5,0xa1,0xa3,0x40,0xbf);
            fillDatas(buffer,CHARSET_BIG5,0xa4,0xc6,0x40,0x7e);
            fillDatas(buffer,CHARSET_BIG5,0xc9,0xf9,0x40,0xd5);
        }else{
            Set<String> caches = new HashSet<>();
            char[] ca = buffer.toString().toCharArray();
            for(int i = 0;i < ca.length;i++) caches.add(ca[i] + S_EMPTY);
            buffer.delete(0,buffer.length());
            caches.parallelStream().forEach(cache->buffer.append(cache));
        }
        String result = PTRN_UC_NON_CHS.matcher(buffer.toString().replaceAll(REG_UC_MC_BIG5,S_EMPTY)).replaceAll(S_EMPTY);
        param.getDetailOptional().ifPresent(c->CS.sl(result,1));
        param.getCmdOptional().ifPresent(c->writeFileWithUTF16LEBom(param.getDestPath(),result));
    }

    protected static void regenFileCharset(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(entry->{
            Path path = entry.getValue();
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_TSC},new FileSizeMatcher(entry.getKey()),path));
            param.getCmdOptional().ifPresent(c->writeFileForTranscoding(path,param.getReplacement()));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    protected static void renameFiles(FileParam param){
        renameFile(param,name->param.getPattern().matcher(name).replaceAll(param.getReplacement()));
    }

    protected static void renLowFiles(FileParam param){
        renameFile(param,name->{
            StringBuilder builder = new StringBuilder(name.toLowerCase());
            dealMismatch(param,builder,name);
            return builder.toString();
        });
    }

    protected static void renUpFiles(FileParam param){
        renameFile(param,name->{
            StringBuilder builder = new StringBuilder(name.toUpperCase());
            dealMismatch(param,builder,name);
            return builder.toString();
        });
    }

    protected static void renUpFstFiles(FileParam param){
        renameFile(param,name->{
            StringBuilder builder = new StringBuilder();
            Matcher matcher = PTRN_REN_UP_FST.matcher(name);
            while(matcher.find()){
                char[] s = matcher.group().toCharArray();
                s[0] -= (s[0] >= 97 && s[0] <= 122 ? 32 : 0);
                matcher.appendReplacement(builder,String.valueOf(s));
            }
            matcher.appendTail(builder);
            dealMismatch(param,builder,name);
            return builder.toString();
        });
    }

    protected static void copyFiles(FileParam param){
        param.getRePathMap().entrySet().stream().sorted(new PathPathComparator(true)).forEach(e->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_CPY + N_DIR_NUL + gs(2) + e.getKey() + V_TO + e.getValue()));
            param.getCmdOptional().ifPresent(c->e.getValue().toFile().mkdirs());
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        param.getPathMap().entrySet().parallelStream().filter(e->e.getKey().isRegularFile()).forEach(e->{
            Path src = e.getValue();
            Path dest = param.getDestPath().resolve(param.getRootPath().relativize(src));
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_CPY,V_TO},new FileSizeMatcher(e.getKey()),src,dest));
            param.getCmdOptional().ifPresent(c->copyFile(src,dest));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    protected static void deleteFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().filter(e->e.getKey().isRegularFile()).forEach(e->{
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_DEL},new FileSizeMatcher(e.getKey()),e.getValue()));
            param.getCmdOptional().ifPresent(c->deleteFile(e.getValue()));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        delNulDirs(param);
    }

    protected static void delNulFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().filter(e->e.getKey().isRegularFile()).flatMap(m->of(m.getValue())).forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_DEL + N_FILE_NUL + gs(2) + p));
            param.getCmdOptional().ifPresent(c->deleteFile(p));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        delNulDirs(param);
    }

    protected static void delOldVersions(FileParam param){
        IFileVersion<FileParam,FileVersion> iFileVersion = new FileVersion();
        List<List<FileVersion>> fileVersionLists = iFileVersion.getSortedFileVersions(param);
        param.getProgressOptional().ifPresent(c->PG.reset(param.getDirsCount().get() + param.getFilesCount().get(),PROGRESS_POSITION));
        fileVersionLists.stream().forEach(fileVersions->{
            fileVersions.stream().forEach(fileVersion->{
                Path path = fileVersion.getPath();
                if(fileVersion.isNewest()){
                    if(fileVersion.isFile()) param.getDetailOptional().ifPresent(c->showFile(new String[]{V_FIND + N_VER_NEW},new FileSizeMatcher(path.toFile()),path));
                    else param.getDetailOptional().ifPresent(c->showDir(new String[]{V_FIND + N_VER_NEW},new FileSizeMatcher(path.toFile()),path));
                }else if(fileVersion.isFile()){
                    param.getCmdOptional().ifPresent(c->deleteFile(path));
                    param.getDetailOptional().ifPresent(c->showFile(new String[]{V_DEL + N_VER_OLD},new FileSizeMatcher(path.toFile()),path));
                }else{
                    FileParam fp = new FileParam();
                    fp.setCmd(CMD_DEL_DIR);
                    if(param.getCmdOptional().isPresent()) fp.setOpt(OPT_INSIDE);
                    else fp.setOpt(OPT_INSIDE + OPT_SIMULATE);
                    fp.setPattern(PTRN_ANY);
                    fp.setSrcPath(path);
                    dealFiles(fp);
                    fp.clearCache();
                    CACHE.getDirsCount().addAndGet(-fp.getDirsCount().get());
                    CACHE.getFilesCount().addAndGet(-fp.getFilesCount().get());
                    param.meetFilesSize(fp.getFilesSize().get());
                    param.getDetailOptional().ifPresent(c->showDir(new String[]{V_DEL + N_VER_OLD},new FileSizeMatcher(path.toFile()),path));
                }
                param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
            });
            param.getDetailOptional().ifPresent(s->CS.l(1));
        });
    }

    protected static void moveFiles(FileParam param){
        param.getRePathMap().entrySet().stream().sorted(new PathPathComparator(true)).forEach(e->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_MOV + N_DIR_NUL + gs(2) + e.getKey() + V_TO + e.getValue()));
            param.getCmdOptional().ifPresent(c->e.getValue().toFile().mkdirs());
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        param.getPathMap().entrySet().parallelStream().filter(e->e.getKey().isRegularFile()).forEach(e->{
            Path src = e.getValue();
            Path dest = param.getDestPath().resolve(param.getRootPath().relativize(src));
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_MOV,V_TO},new FileSizeMatcher(e.getKey()),src,dest));
            param.getCmdOptional().ifPresent(c->moveFile(src,dest));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        param.getRePathMap().keySet().stream().sorted(new PathComparator(false)).forEach(p->param.getCmdOptional().ifPresent(c->deleteFile(p)));
    }

    protected static void interchangeFiles(FileParam param){
        final boolean upgrade = param.meetCondition(INTERCHANGE_UPGRADE);
        param.getPathMap().values().stream().sorted(new PathComparator(true)).forEach(src->{
            File file = src.toFile();
            Path dest = param.getDestPath().resolve(param.getSrcPath().relativize(src));
            if(dest.getParent().toFile().isDirectory()) try{
                if(isEmpty(param.getPathsMap().get(dest.getParent()))){
                    List<Path> paths = new ArrayList<>();
                    param.getPathsMap().put(dest.getParent(),paths);
                    find(dest.getParent(),1,(p, b)->b.isRegularFile()).parallel().forEach(p->paths.add(p));
                }
                param.getDirsCache().clear();
                List<Path> paths = param.getPathsMap().get(dest.getParent());
                paths.parallelStream().filter(p->upgrade && p.getFileName().toString().toLowerCase().startsWith(src.getFileName().toString().toLowerCase()) || !upgrade && src.getFileName().toString().toLowerCase().startsWith(p.getFileName().toString().toLowerCase())).forEach(p->{
                    param.getFilesSize().addAndGet(p.toFile().length());
                    param.getDirsCache().add(p);
                });
                param.getFilesCount().addAndGet(param.getDirsCache().size());
                param.getDirsCache().parallelStream().forEach(p->{
                    Path backup = param.getBackupPath().resolve(param.getDestPath().relativize(p));
                    param.getDetailOptional().ifPresent(t->showFile(new String[]{V_BAK + V_MOV,V_TO},new FileSizeMatcher(file),p,backup));
                    param.getCmdOptional().ifPresent(c->moveFile(p,backup));
                });
                paths.removeAll(param.getDirsCache());
            }catch(IOException ioe){
                CS.sl(gsph(ERR_DIR_VST,dest.getParent().toString(),ioe.toString()));
            }
            if(param.getDirsCache().isEmpty()) param.getDetailOptional().ifPresent(t->showFile(new String[]{V_ADD + V_MOV,V_TO},new FileSizeMatcher(file),src,dest));
            else param.getDetailOptional().ifPresent(t->showFile(new String[]{V_UPD + V_MOV,V_TO},new FileSizeMatcher(file),src,dest));
            param.getCmdOptional().ifPresent(c->moveFile(src,dest));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    protected static void upgradeFiles(FileParam param){
        param.getRePathMap().values().parallelStream().forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_ADD + N_DIR_NUL + gs(2) + p));
            param.getCmdOptional().ifPresent(c->p.toFile().mkdirs());
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        param.getPathMap().entrySet().parallelStream().filter(e->e.getKey().isRegularFile()).forEach(e->{
            Path src = e.getValue();
            Path dest = param.getDestPath().resolve(param.getSrcPath().relativize(src));
            File file = dest.toFile();
            if(file.isFile()){
                if(nonEmpty(param.getBackupPath())){
                    Path backup = param.getBackupPath().resolve(param.getDestPath().getParent().relativize(dest));
                    param.getDetailOptional().ifPresent(t->showFile(new String[]{V_BAK,V_TO},new FileSizeMatcher(file),dest,backup));
                    param.getCmdOptional().ifPresent(c->copyFile(dest,backup));
                }
                param.getDetailOptional().ifPresent(t->showFile(new String[]{V_UPD},new FileSizeMatcher(e.getKey()),dest));
                param.getCmdOptional().ifPresent(c->copyFile(src,dest));
            }else{
                param.getDetailOptional().ifPresent(t->showFile(new String[]{V_ADD,V_TO},new FileSizeMatcher(e.getKey()),src,dest));
                param.getCmdOptional().ifPresent(c->copyFile(src,dest));
            }
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    protected static void zipFiles(FileParam param){
        Path path = param.getDestPath().resolve(param.getZipName());
        CS.checkError(ERR_ZIP_FLE_CRT,new String[]{path.toString(),gsph(ERR_ZIP_FILE_SAME,path.toString())},()->param.getPathMap().containsValue(path));
        param.getCmdOptional().ifPresent(c->createZipFile(param));
        param.getDirsCache().stream().sorted(new PathComparator(true)).forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_CPRS + N_DIR_NUL + gs(2) + p));
            param.getCmdOptional().ifPresent(c->zipDir(param.getZipOutputStream(),param.getRootPath(),p));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        param.getPathMap().entrySet().stream().filter(e->e.getKey().isRegularFile()).flatMap(e->of(e.getValue())).sorted(new PathComparator(true)).forEach(p->{
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_CPRS},new FileSizeMatcher(p.toFile()),p));
            param.getCmdOptional().ifPresent(c->zipFile(param.getZipOutputStream(),param.getRootPath(),p));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    protected static void unzipFiles(FileParam param){
        final boolean unzip = param.meetCondition(ZIP_UNZIP);
        final boolean unzipDir = param.meetCondition(ZIP_UNZIP_DIR);
        final boolean unzipMd5 = param.meetCondition(ZIP_UNZIP_MD5);
        param.getPathMap().entrySet().parallelStream().flatMap(e->of(e.getValue())).forEach(p->{
            final StringBuilder builder = new StringBuilder(gl(V_DCPRS + N_FILE + gs(2) + p + gs(1) + V_START + S_ELLIPSIS,2));
            final IValue<Boolean> check = new SingleValue<>(false);
            final File file = p.toFile();
            final String duration = getDurationString(t->{
                try(ZipFile zipFile = new ZipFile(file);
                    ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)))){
                    if(!unzipDir) CS.checkException(ERR_ZIP_FLE_DCPRS,new String[]{p.toString(),gsph(ERR_ZIP_FILE_SAME,p.toString())},()->zipFile.stream().parallel().anyMatch(entry->{
                        if(entry.isDirectory() || entry.getName().endsWith(SPRT_FILE)) check.setValue(false);
                        if(unzip) check.setValue(p.equals(param.getDestPath().resolve(entry.getName())));
                        else check.setValue(p.equals(p.getParent().resolve(entry.getName())));
                        return check.getValue();
                    }));
                    if(check.getValue()) return;
                    if(unzipDir) param.getDirsCount().incrementAndGet();
                    final int size = zipFile.size();
                    zipFile.stream().parallel().forEach(entry->{
                        IValue<Path> dest = new SingleValue<>(null);
                        Path path = null;
                        if(unzip) path = param.getDestPath();
                        else path = p.getParent();
                        if(!unzipDir) path = path.resolve(entry.getName());
                        else if(unzipMd5) path = path.resolve(p.getFileName() + REG_ANY + getMD5L32(p) + SPRT_FILE + entry.getName());
                        else path = path.resolve(getFileNameWithoutSuffix(p.toString()) + SPRT_FILE + entry.getName());
                        dest.setValue(path);
                        if(entry.isDirectory() || entry.getName().endsWith(SPRT_FILE)){
                            param.getDirsCount().incrementAndGet();
                            builder.append(gl(V_EXTR + N_DIR_NUL + gs(2) + dest));
                            param.getCmdOptional().ifPresent(c->dest.getValue().toFile().mkdirs());
                        }else{
                            param.getFilesCount().incrementAndGet();
                            param.getCmdOptional().ifPresent(c->{
                                try(InputStream inputStream = zipFile.getInputStream(entry)){
                                    dest.getValue().getParent().toFile().mkdirs();
                                    dest.getValue().toFile().setWritable(true,true);
                                    if(nonEmpty(inputStream)) copy(inputStream,dest.getValue(),StandardCopyOption.REPLACE_EXISTING);
                                }catch(Exception ex){
                                    builder.append(glph(ERR_ZIP_FLE_EXTR,entry.getName(),ex.toString()));
                                }
                            });
                            if(0 == entry.getSize()) builder.append(gl(V_EXTR + N_FILE_NUL + gs(2) + dest));
                            else builder.append(gl(V_EXTR + N_FILE + gs(4) + dest));
                        }
                        param.getProgressOptional().ifPresent(c->PG.update(PG.countUpdate(size,1),PROGRESS_SCALE));
                    });
                }catch(Exception ex){
                    builder.append(glph(ERR_ZIP_FLE_DCPRS,p.toString(),ex.toString()));
                }
            });
            builder.append(gl(1) + gl(V_DCPRS + N_FILE + gs(2) + p + gs(1) + V_DONE + S_PERIOD + N_TIME + S_COLON + duration + S_PERIOD));
            param.getDetailOptional().ifPresent(c->CS.sl(builder.toString()));
        });
    }

    protected static void showMD5(FileParam param, Function<Path,String> function){
        param.getPathMap().values().parallelStream().forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(p.toString() + gs(4) + function.apply(p)));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    protected static void encodeJson(FileParam param){
        param.getPathMap().values().parallelStream().forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_ENC + N_FILE + gs(2) + p.toString()));
            param.getCmdOptional().ifPresent(c->trimJson(p));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    protected static void decodeJson(FileParam param){
        param.getPathMap().values().parallelStream().forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_DEC + N_FILE + gs(2) + p.toString()));
            param.getCmdOptional().ifPresent(c->formatJson(p));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void showFile(String[] s, BooleanSupplier b, Path... p){
        if(1 == s.length){
            if(b.getAsBoolean()) CS.sl(s[0] + N_FILE_NUL + gs(2) + p[0]);
            else CS.sl(s[0] + N_FILE + gs(4) + p[0]);
        }else if(2 == s.length){
            if(b.getAsBoolean()) CS.sl(s[0] + N_FILE_NUL + gs(2) + p[0] + s[1] + p[1]);
            else CS.sl(s[0] + N_FILE + gs(4) + p[0] + s[1] + p[1]);
        }
    }

    private static void showDir(String[] s, BooleanSupplier b, Path... p){
        if(1 == s.length){
            if(b.getAsBoolean()) CS.sl(s[0] + N_DIR_NUL + gs(2) + p[0]);
            else CS.sl(s[0] + N_DIR + gs(4) + p[0]);
        }else if(2 == s.length){
            if(b.getAsBoolean()) CS.sl(s[0] + N_DIR_NUL + gs(2) + p[0] + s[1] + p[1]);
            else CS.sl(s[0] + N_DIR + gs(4) + p[0] + s[1] + p[1]);
        }
    }

    private static long cachePaths(FileParam param){
        long count = 0;
        try{
            count = find(param.getSrcPath(),param.getLevel(),new PathMatcher(param)).parallel().count();
        }catch(Exception e){
            CS.sl(gsph(ERR_DIR_VST,param.getSrcPath().toString(),e.toString()));
        }
        return count;
    }

    private static void cacheRepaths(FileParam param){
        if(param.meetCondition(NEED_REPATH)) param.getDirsCache().parallelStream().forEach(p->param.getRePathMap().put(p,param.getOutPath().resolve(param.getRootPath().relativize(p))));
    }

    private static void createZipFile(FileParam param){
        try{
            param.getDestPath().toFile().mkdirs();
            File file = param.getDestPath().resolve(param.getZipName()).toFile();
            file.setWritable(true,true);
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            zipOutputStream.setLevel(param.getZipLevel());
            param.cacheZipOutputStream(zipOutputStream);
        }catch(Exception e){
            CS.sl(gsph(ERR_ZIP_FLE_CRT,param.getDestPath().resolve(param.getZipName()).toString(),e.toString()));
        }
    }

    private static void zipFile(ZipOutputStream stream, Path root, Path path){
        try{
            ZipEntry entry = new ZipEntry(root.relativize(path).toString());
            stream.putNextEntry(entry);
            copy(path,stream);
        }catch(IOException e){
            CS.sl(gsph(ERR_ZIP_FLE_CPY,path.toString(),e.toString()));
        }
    }

    private static void zipDir(ZipOutputStream stream, Path root, Path path){
        try{
            ZipEntry entry = new ZipEntry(root.relativize(path).toString() + SPRT_FILE_ZIP);
            stream.putNextEntry(entry);
        }catch(IOException e){
            CS.sl(gsph(ERR_ZIP_DIR_NUL_CPY,path.toString(),e.toString()));
        }
    }

    private static void executeFileLogic(FileParam param, ILogic<Path> fileLogic){
        param.getPathMap().entrySet().stream().forEach(e->{
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_DEAL},new FileSizeMatcher(e.getKey()),e.getValue()));
            param.getCmdOptional().ifPresent(c->fileLogic.execute(e.getValue()));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void renameFile(FileParam param, UnaryOperator<String> operator){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            Path src = e.getValue();
            BasicFileAttributes a = e.getKey();
            Path dest = src.resolveSibling(operator.apply(src.getFileName().toString()));
            if(a.isRegularFile()){
                param.getDetailOptional().ifPresent(c->showFile(new String[]{V_REN,V_BY},new FileSizeMatcher(a),src,dest));
                param.getCmdOptional().ifPresent(c->src.toFile().renameTo(dest.toFile()));
                param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
            }else param.getRePathMap().put(src,dest);
        });
        renameDir(param);
    }

    private static void renameDir(FileParam param){
        param.getRePathMap().entrySet().stream().sorted(new PathPathComparator(false)).forEach(e->{
            Path src = e.getKey(), dest = e.getValue();
            File file = src.toFile();
            param.getDetailOptional().ifPresent(c->showDir(new String[]{V_REN,V_BY},new FileSizeMatcher(file),src,dest));
            param.getCmdOptional().ifPresent(c->file.renameTo(dest.toFile()));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void delNulDirs(FileParam param){
        param.getDirsCache().stream().sorted(new PathComparator(false)).forEach(p->{
            if(0 == p.toFile().list().length){
                param.getDetailOptional().ifPresent(c->CS.sl(V_DEL + N_DIR_NUL + gs(2) + p));
                param.getCmdOptional().ifPresent(c->deleteFile(p));
                param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
            }else param.getDirsCount().addAndGet(-1);
        });
    }

    private static void dealMismatch(FileParam param, StringBuilder builder, String string){
        Matcher matcher = param.getPattern().matcher(string);
        final int count = matcher.groupCount();
        if(0 == count) return;
        Deque<Integer> starts = new ArrayDeque<>();
        Deque<Integer> ends = new ArrayDeque<>();
        while(matcher.find()){
            int start = matcher.start(1), end = matcher.end(1), i = 2;
            starts.add(0 == start ? end : 0);
            for(;i <= count;i++){
                starts.add(matcher.end(0 == start ? i : i - 1));
                ends.add(matcher.start(0 == start ? i : i - 1));
            }
            ends.add(0 == start ? matcher.end() : matcher.start(i - 1));
            if(0 < start){
                starts.add(matcher.end(count));
                ends.add(matcher.end());
            }
        }
        while(!starts.isEmpty()){
            int start = starts.remove(), end = ends.remove();
            builder.replace(start,end,string.substring(start,end));
        }
    }

    private static void fillDatas(StringBuffer buffer, String charset, int... values){
        byte[] bytes = new byte[2];
        for(int high = values[0];high <= values[1];high++){
            bytes[0] = (byte)high;
            for(int low = values[2];low <= values[3];low++){
                bytes[1] = (byte)low;
                try{
                    buffer.append(new String(bytes,charset));
                }catch(UnsupportedEncodingException e){
                    CS.sl(gsph(ERR_INFO,e.toString()));
                }
            }
        }
    }

    public static class PathMatcher implements BiPredicate<Path,BasicFileAttributes>{
        private FileParam param;
        private Queue<Path> dirsCache;
        private boolean matchFileOnly, matchDirOnly, excludeRoot, ignoreRegex;

        public PathMatcher(FileParam param){
            this.param = param;
            dirsCache = new ConcurrentLinkedQueue<>();
            matchFileOnly = param.meetCondition(MATCH_FILE_ONLY);
            matchDirOnly = param.meetCondition(MATCH_DIR_ONLY);
            excludeRoot = param.meetCondition(EXCLUDE_ROOT);
            ignoreRegex = param.meetCondition(IGNORE_REGEX);
        }

        @Override
        public boolean test(Path p, BasicFileAttributes a){
            boolean find = false;
            if(a.isRegularFile()){
                if(matchDirOnly && !(find = matchPath(p))) return false;
                find = find || ignoreRegex ? true : param.getPattern().matcher(p.getFileName().toString()).find();
                switch(param.getCmd()){
                    case CMD_DEL_NUL:
                    case CMD_DEL_DIR_NUL:
                    if(find = find && 0 == a.size()) param.getPathMap().put(a,p);
                    break;
                    case CMD_DEL_DIR_OLD_VER:
                    case CMD_DEL_DIR_OLY_OLD_VER:
                    if(find = find && !matchPath(p)) param.getPathMap().put(a,p);
                    break;
                    default:
                    find = find && param.meetFilesSize(a.size());
                    if(find || (find = matchPath(p) && param.meetFilesSize(a.size()))) param.getPathMap().put(a,p);
                }
            }else if(a.isDirectory()){
                if(matchFileOnly || excludeRoot && p.equals(param.getSrcPath())) return false;
                find = ignoreRegex ? ignoreRegex : param.getPattern().matcher(p.getFileName().toString()).find();
                switch(param.getCmd()){
                    case CMD_FND_DIR:
                    case CMD_FND_DIR_SIZ_ASC:
                    case CMD_FND_DIR_SIZ_DSC:
                    case CMD_FND_PTH_DIR_ABS:
                    case CMD_FND_PTH_DIR_RLT:
                    case CMD_FND_DIR_SAM:
                    case CMD_FND_DIR_DIF:
                    case CMD_FND_DIR_OLY_SAM:
                    case CMD_FND_DIR_OLY_DIF:
                    case CMD_CPY_DIR:
                    case CMD_CPY_DIR_OLY:
                    case CMD_DEL_DIR:
                    case CMD_DEL_DIR_OLY:
                    case CMD_DEL_DIR_NUL:
                    case CMD_DEL_DIR_OLY_NUL:
                    case CMD_MOV_DIR:
                    case CMD_MOV_DIR_OLY:
                    case CMD_UGD_DIR:
                    case CMD_ZIP_DIR_DEF:
                    case CMD_PAK_DIR_DEF:
                    if(!find) find = matchPath(p);
                    else if(!ignoreRegex) dirsCache.add(p);
                    case CMD_FND_DIR_OLY:
                    case CMD_FND_DIR_DIR_SIZ_ASC:
                    case CMD_FND_DIR_DIR_SIZ_DSC:
                    case CMD_FND_DIR_OLY_SIZ_ASC:
                    case CMD_FND_DIR_OLY_SIZ_DSC:
                    case CMD_FND_PTH_DIR_OLY_ABS:
                    case CMD_FND_PTH_DIR_OLY_RLT:
                    case CMD_REN_DIR:
                    case CMD_REN_DIR_LOW:
                    case CMD_REN_DIR_UP:
                    case CMD_REN_DIR_UP_FST:
                    case CMD_REN_DIR_OLY:
                    case CMD_REN_DIR_OLY_LOW:
                    case CMD_REN_DIR_OLY_UP:
                    case CMD_REN_DIR_OLY_UP_FST:
                    if(find){
                        param.getPathMap().put(a,p);
                        param.getDirsCache().add(p);
                    }
                    break;
                    case CMD_DEL_DIR_OLD_VER:
                    case CMD_DEL_DIR_OLY_OLD_VER:
                    if(find = find && !matchPath(p)){
                        dirsCache.add(p);
                        param.getPathMap().put(a,p);
                    }
                }
            }
            if(find) if(a.isDirectory()) param.getDirsCount().incrementAndGet();
            else param.getFilesCount().incrementAndGet();
            return find;
        }

        private boolean matchPath(Path path){
            return dirsCache.parallelStream().anyMatch(p->path.startsWith(p));
        }
    }

    public static class FileSizeMatcher implements BooleanSupplier{
        private BasicFileAttributes attributes;
        private File file;

        public FileSizeMatcher(BasicFileAttributes attributes){
            this.attributes = attributes;
        }

        public FileSizeMatcher(File file){
            this.file = file;
        }

        @Override
        public boolean getAsBoolean(){
            if(nonEmpty(attributes)) return 0 == attributes.size();
            return 0 == (file.isDirectory() ? file.list().length : file.length());
        }
    }

    public static class BasicFileAttributesPathComparator implements Comparator<Entry<BasicFileAttributes,Path>>{
        private boolean order;

        public BasicFileAttributesPathComparator(boolean ascending){
            order = ascending;
        }

        @Override
        public int compare(Entry<BasicFileAttributes,Path> e1, Entry<BasicFileAttributes,Path> e2){
            BasicFileAttributes a1 = e1.getKey(), a2 = e2.getKey();
            Path p1 = e1.getValue(), p2 = e2.getValue();
            if(a1.size() == a2.size()){
                int n1 = p1.getNameCount(), n2 = p2.getNameCount();
                if(n1 == n2){
                    String s1 = p1.toString(), s2 = p2.toString();
                    int l1 = s1.length(), l2 = s2.length();
                    return l1 == l2 ? s1.compareTo(s2) : l1 < l2 ? 1 : -1;
                }
                return n1 < n2 ? 1 : -1;
            }
            return order ? (a1.size() > a2.size() ? 1 : -1) : (a1.size() < a2.size() ? 1 : -1);
        }
    }

    public static class PathPathComparator implements Comparator<Entry<Path,Path>>{
        private boolean order;

        public PathPathComparator(boolean ascending){
            order = ascending;
        }

        @Override
        public int compare(Entry<Path,Path> e1, Entry<Path,Path> e2){
            Path p1 = e1.getKey(), p2 = e2.getKey();
            int n1 = p1.getNameCount(), n2 = p2.getNameCount();
            if(n1 == n2){
                String s1 = p1.toString(), s2 = p2.toString();
                int l1 = s1.length(), l2 = s2.length();
                return l1 == l2 ? s1.compareTo(s2) : sort(l1,l2);
            }
            return sort(n1,n2);
        }

        private int sort(int n1, int n2){
            return order ? (n1 > n2 ? 1 : -1) : (n1 < n2 ? 1 : -1);
        }
    }

    public static class PathLongComparator implements Comparator<Entry<Path,Long>>{
        private boolean order;

        public PathLongComparator(boolean ascending){
            order = ascending;
        }

        @Override
        public int compare(Entry<Path,Long> e1, Entry<Path,Long> e2){
            long l1 = e1.getValue(), l2 = e2.getValue();
            return l1 == l2 ? e1.getKey().compareTo(e2.getKey()) : order ? (l1 > l2 ? 1 : -1) : (l1 < l2 ? 1 : -1);
        }
    }

    public static class PathComparator implements Comparator<Path>{
        private boolean order;

        public PathComparator(boolean ascending){
            order = ascending;
        }

        @Override
        public int compare(Path path1, Path path2){
            int n1 = path1.getNameCount(), n2 = path2.getNameCount();
            if(n1 == n2){
                String s1 = path1.toString(), s2 = path2.toString();
                int l1 = s1.length(), l2 = s2.length();
                return l1 == l2 ? s1.compareTo(s2) : sort(l1,l2);
            }
            return sort(n1,n2);
        }

        private int sort(int n1, int n2){
            return order ? (n1 > n2 ? 1 : -1) : (n1 < n2 ? 1 : -1);
        }
    }
}
