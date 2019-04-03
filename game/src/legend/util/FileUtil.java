package legend.util;

import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.find;
import static java.nio.file.Files.move;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.write;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static legend.util.ConsoleUtil.IN;
import static legend.util.JaxbUtil.convertToJavaBean;
import static legend.util.JaxbUtil.convertToXml;
import static legend.util.JsonUtil.formatJson;
import static legend.util.JsonUtil.trimJson;
import static legend.util.MD5Util.getGuidL32;
import static legend.util.MD5Util.getGuidU32;
import static legend.util.MD5Util.getMD5L16;
import static legend.util.MD5Util.getMD5L32;
import static legend.util.MD5Util.getMD5U16;
import static legend.util.MD5Util.getMD5U32;
import static legend.util.StringUtil.gs;
import static legend.util.StringUtil.gsph;
import static legend.util.TimeUtil.countDuration;
import static legend.util.TimeUtil.decTotalDuration;
import static legend.util.TimeUtil.getDurationString;
import static legend.util.TimeUtil.getTotalDurationString;
import static legend.util.TimeUtil.incTotalDuration;
import static legend.util.TimeUtil.resetTime;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.matchRange;
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
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import legend.util.entity.ILCode;
import legend.util.entity.ILCodes;
import legend.util.intf.IConsoleUtil;
import legend.util.intf.IFileUtil;
import legend.util.intf.IProgress;
import legend.util.param.FileParam;
import legend.util.param.SingleValue;
import legend.util.rule.intf.IReplaceRuleEngine;

public final class FileUtil implements IFileUtil,IConsoleUtil{
    private static final FileParam CACHE;
    private static final ConsoleUtil CS;
    private static final IProgress PG;
    private static final Pattern RPTN;
    private static final Pattern APTN;
    private static FileParam FP;
    private static PrintStream PS;
    static{
        CACHE = new FileParam();
        CS = new ConsoleUtil();
        PG = ProgressUtil.ConsoleProgress();
        RPTN = compile(REG_REN_UP_FST);
        APTN = compile(REG_ASK_NO);
    }

    private FileUtil(){}

    public static void main(String[] args){
        dealFiles(args);
    }

    public static void dealFiles(List<FileParam> fileParams){
        fileParams.stream().forEach(param->{
            param.refreshConditions(CACHE);
            FP = param;
            String opt = FP.getOpt();
            boolean progress = !opt.matches(REG_NON_PROG);
            String command = FP.getWholeCommand();
            if(opt.contains(OPT_SIMULATE)) CS.s(V_SMLT + gsph(ST_CMD_START,command)).l(2);
            else CS.s(gsph(ST_CMD_START,command)).l(2);
            if(FP.getOpt().contains(OPT_ASK)){
                CS.s(ST_ASK_CONT);
                String line = decTotalDuration(()->IN.nextLine());
                CS.sl(false,line).l(1);
                if(!APTN.matcher(line).find()){
                    if(progress) countDuration(t->PG.runUntillFinish(FileUtil::dealFiles));
                    else countDuration(t->dealFiles(FP));
                }else resetTime();
            }else if(progress) countDuration(t->PG.runUntillFinish(FileUtil::dealFiles));
            else countDuration(t->dealFiles(FP));
            if(opt.contains(OPT_SIMULATE)) CS.s(V_SMLT + gsph(ST_CMD_DONE,command) + V_DEAL + FP.getDirsCount().get() + N_AN + N_DIR + S_COMMA + FP.getFilesCount().get() + N_AN + N_FLE + S_BRACKET_L + N_SIZE + S_COLON).formatSize(param.getFilesSize().get(),UNIT_TYPE.TB).s(S_BRACKET_R + S_SEMICOLON + N_TIME + S_COLON + getDurationString() + S_PERIOD).l(2);
            else CS.s(gsph(ST_CMD_DONE,command) + V_DEAL + FP.getDirsCount().get() + N_AN + N_DIR + S_COMMA + FP.getFilesCount().get() + N_AN + N_FLE + S_BRACKET_L + N_SIZE + S_COLON).formatSize(FP.getFilesSize().get(),UNIT_TYPE.TB).s(S_BRACKET_R + S_SEMICOLON + N_TIME + S_COLON + getDurationString() + S_PERIOD).l(2);
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
                case CMD_REP_FILE_BT:
                replaceFilesWithBT(param);
                break;
                case CMD_REP_FILE_IL:
                replaceFilesWithIL(param);
                break;
                case CMD_REG_FILE_GBK:
                regenFileWithGBK(param);
                break;
                case CMD_REG_FILE_BIG5:
                regenFileWithBIG5(param);
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
                case CMD_PAK_INF:
                unzipFiles(param);
                break;
                case CMD_GUID_L32:
                showMd5(param,p->getGuidL32(p));
                break;
                case CMD_GUID_U32:
                showMd5(param,p->getGuidU32(p));
                break;
                case CMD_MD5_L16:
                showMd5(param,p->getMD5L16(p));
                break;
                case CMD_MD5_U16:
                showMd5(param,p->getMD5U16(p));
                break;
                case CMD_MD5_L32:
                showMd5(param,p->getMD5L32(p));
                break;
                case CMD_MD5_U32:
                showMd5(param,p->getMD5U32(p));
                break;
                case CMD_JSON_ENC:
                encodeJson(param);
                break;
                case CMD_JSON_DEC:
                decodeJson(param);
            }
            param.saveCache(CACHE);
        }catch(Exception e){
            CS.sl(gsph(ERR_CMD_EXEC,e.toString()));
            e.printStackTrace();
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
        try(OutputStream outputStream = newOutputStream(path);BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,CHARSET_UTF8))){
            outputStream.write(BOM_UTF8);
            bufferedWriter.write(data);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_WRT,path.toString(),e.toString()));
        }
    }

    public static void writeFileWithUTF16LEBom(Path path, String data){
        try(OutputStream outputStream = newOutputStream(path);BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,CHARSET_UTF16LE))){
            outputStream.write(BOM_UTF16LE);
            bufferedWriter.write(data);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_WRT,path.toString(),e.toString()));
        }
    }

    public static void writeFileWithUTF16BEBom(Path path, String data){
        try(OutputStream outputStream = newOutputStream(path);BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,CHARSET_UTF16BE))){
            outputStream.write(BOM_UTF16BE);
            bufferedWriter.write(data);
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_WRT,path.toString(),e.toString()));
        }
    }

    public static String analyzeCharsetEncode(Path path, String charset){
        try(InputStream inputStream = newInputStream(path)){
            byte[] bom = new byte[3];
            int i, r = inputStream.read(bom);
            if(-1 == r || BOM_UTF8[0] == bom[0] && BOM_UTF8[1] == bom[1] && BOM_UTF8[2] == bom[2]) return CHARSET_UTF8;
            else if(BOM_UTF16LE[0] == bom[0] && BOM_UTF16LE[1] == bom[1]) return CHARSET_UTF16LE;
            else if(BOM_UTF16BE[0] == bom[0] && BOM_UTF16BE[1] == bom[1]) return CHARSET_UTF16BE;
            boolean match = false;
            SingleValue<Integer> bytesCount = new SingleValue<>(0);
            byte[] bytes = new byte[BLOCK_SIZE_FILE + 3];
            for(i = 0;i < bom.length;i++)
                bytes[i] = bom[i];
            r = inputStream.read(bytes,3,BLOCK_SIZE_FILE) + 3;
            do{
                for(i = 0;matchCharsetWithUTF8(bytes,i,bytesCount) && i < r - 3;i += bytesCount.get())
                    if(1 < bytesCount.get()) match = true;
            }while(!match && -1 != (r = inputStream.read(bytes)));
            if(r - 3 > i) return charset;
        }catch(IOException e){
            CS.sl(gsph(ERR_FLE_READ,path.toString(),e.toString()));
        }
        return CHARSET_UTF8;
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
        CS.s(ST_PRG_DONE + V_DEAL + CACHE.getDirsCount().get() + N_AN + N_DIR + S_COMMA + CACHE.getFilesCount().get() + N_AN + N_FLE + S_BRACKET_L + N_SIZE + S_COLON).formatSize(CACHE.getFilesSize().get(),UNIT_TYPE.TB).s(S_BRACKET_R + S_SEMICOLON + N_TIME + S_COLON + getTotalDurationString() + S_PERIOD).l(2);
    }

    private static void dealFiles(IProgress progress){
        CS.clearStream();
        dealFiles(FP);
        CS.cacheStream(PS);
    }

    private static void findSortedFiles(FileParam param){
        param.getDetailOptional().ifPresent(c->param.getPathList().stream().sorted(new PathListComparator(true)).limit(param.getLimit()).forEach(p->showDir(new String[]{V_FIND},new FileSizeMatcher(p.toFile()),p)));
        int limit = param.getLimit() - param.getPathList().size();
        if(0 < limit) param.getDetailOptional().ifPresent(c->param.getPathMap().entrySet().stream().filter(e->e.getKey().isRegularFile()).flatMap(m->of(m.getValue())).sorted(new PathListComparator(true)).limit(limit).forEach(p->showFile(new String[]{V_FIND},new FileSizeMatcher(p.toFile()),p)));
    }

    private static void findSortedFilePaths(FileParam param){
        boolean relative = param.matchConditions(PATH_RELATIVE);
        param.getDetailOptional().ifPresent(c->param.getPathList().stream().sorted(new PathListComparator(true)).limit(param.getLimit()).forEach(p->{
            if(relative) CS.sl(param.getRootPath().relativize(p).toString());
            else CS.sl(p.toString());
        }));
        int limit = param.getLimit() - param.getPathList().size();
        if(0 < limit) param.getDetailOptional().ifPresent(c->param.getPathMap().entrySet().stream().filter(e->e.getKey().isRegularFile()).flatMap(m->of(m.getValue())).sorted(new PathListComparator(true)).limit(limit).forEach(p->{
            if(relative) CS.sl(param.getRootPath().relativize(p).toString());
            else CS.sl(p.toString());
        }));
    }

    private static void findSortedFileSizes(FileParam param){
        param.getDetailOptional().ifPresent(c->param.getPathMap().entrySet().stream().filter(e->e.getKey().isRegularFile()).sorted(new BasicFileAttributesPathComparator(param.matchConditions(ORDER_ASC))).limit(param.getLimit()).forEach(e->CS.formatSize(e.getKey().size(),UNIT_TYPE.GB).s(4).sl(e.getValue().toString())));
    }

    private static void findSortedFilesByCompared(FileParam param){
        boolean same = param.matchConditions(COMPARE_SAME);
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
                param.getPathList().add(p);
                pathMap.put(a,p);
            }else param.getDirsCount().addAndGet(-1);
        });
        param.getPathMap().clear();
        param.getPathMap().putAll(pathMap);
        findSortedFiles(param);
    }

    private static void findSortedDirSizes(FileParam param){
        param.getPathMap().entrySet().parallelStream().filter(e->e.getKey().isRegularFile()).forEach(e->param.getSizeMap().put(e.getValue(),e.getKey().size()));
        param.getPathList().parallelStream().forEach(p->{
            FileParam fp = new FileParam();
            fp.setCmd(CMD_FND_SIZ_ASC);
            fp.setPattern(compile(REG_ANY));
            fp.setSrcPath(p);
            cachePaths(fp);
            fp.clearCache();
            long size = fp.getFilesSize().get();
            if(param.matchFilesSize(size)) param.getSizeMap().put(p,size);
        });
        param.getDetailOptional().ifPresent(c->param.getSizeMap().entrySet().stream().sorted(new PathLongComparator(param.matchConditions(ORDER_ASC))).limit(param.getLimit()).forEach(e->{
            Path path = e.getKey();
            if(path.toFile().isFile()) CS.formatSize(e.getValue(),UNIT_TYPE.GB).sl(gs(4) + N_FLE + gs(4) + path.toString());
            else CS.formatSize(e.getValue(),UNIT_TYPE.GB).sl(gs(4) + N_DIR + gs(4) + path.toString());
        }));
    }

    private static void replaceFilesWithBT(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            Path path = e.getValue();
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_REPL},new FileSizeMatcher(e.getKey()),path));
            IReplaceRuleEngine ruleEngine = ProvideRuleEngine(param.getReplacement());
            List<String> results = ruleEngine.execute(readFile(path),param.getSplit());
            param.getDetailOptional().ifPresent(c->CS.sl(results,1));
            param.getCmdOptional().ifPresent(c->writeFile(path,results));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void replaceFilesWithIL(FileParam param){
        if(existsPath(param.getDestPath())){
            ILCodes ILCODES = convertToJavaBean(param.getDestPath(),ILCodes.class);
            param.getPathMap().entrySet().stream().forEach(entry->{
                Path path = entry.getValue();
                param.getDetailOptional().ifPresent(c->showFile(new String[]{V_REPL},new FileSizeMatcher(entry.getKey()),path));
                List<String> datas = readFile(path,CHARSET_GBK);
                int dataSize = datas.size();
                ILCodes ilCodes = ILCODES.cloneValue();
                if(!ilCodes.trim().validate(dataSize)) CS.showError(ERR_FLE_REPL,new String[]{path.toString(),ilCodes.errorInfo()});
                if(MODE_NATIVE.equals(ilCodes.getMode())){
                    int r = dataSize % SIZE_IL_PARTITION;
                    List<Integer> partitions = new ArrayList<>();
                    for(int i = SIZE_IL_PARTITION + r - 1;i < dataSize;i += SIZE_IL_PARTITION)
                        partitions.add(i);
                    if(SIZE_IL_HEADER < r) partitions.add(r - 1);
                    List<ILCode> codes = new ArrayList<>();
                    Collection<ILCode> caches = new ConcurrentLinkedQueue<ILCode>(ilCodes.getCodes().stream().filter(c->!MODE_NATIVE.equals(c.getProcessingMode())).collect(toList()));
                    partitions.parallelStream().forEach(p->{
                        caches.parallelStream().forEach(c->{
                            ILCode code = c.cloneValue();
                            List<Pattern> queryRegexCache = code.refreshQueryRegexCache(false);
                            int l = queryRegexCache.size() - 1, i = l, j = p;
                            for(;0 <= i && 0 <= j && SIZE_IL_PARTITION > p - j;j--){
                                if(queryRegexCache.get(i).matcher(datas.get(j)).find()) i--;
                                else if(i < l){
                                    j += l - i;
                                    i = l;
                                }
                            }
                            if(l == i) return;
                            for(;0 <= i && 0 <= j && queryRegexCache.get(i).matcher(datas.get(j)).find();i--,j--);
                            if(-1 != i) return;
                            if(MODE_REPL.equals(code.getProcessingMode())){
                                List<Pattern> codeRegexCache = code.refreshCodeRegexCache(false);
                                for(l = 0,i = codeRegexCache.size() - 1;0 <= i && 0 <= j && SIZE_IL_PARTITION * 2 > p - j;j--)
                                    if(codeRegexCache.get(i).matcher(datas.get(j)).find()){
                                        if(0 == l) l = j + 1;
                                        i--;
                                    }
                                if(-1 != i) return;
                                code.setLineNumer(j + 2,l);
                            }else{
                                l += j + 2;
                                code.setLineNumer(l,l);
                            }
                            codes.add(code);
                            caches.remove(c);
                        });
                    });
                    CS.showError(ERR_FLE_REPL,new String[]{path.toString(),ST_FILE_IL_MISMATCH},()->!caches.isEmpty());
                    ilCodes.regenSortedCodes(codes);
                }
                if(!ilCodes.validate(dataSize)) CS.showError(ERR_FLE_REPL,new String[]{path.toString(),ilCodes.errorInfo()});
                List<String> results = new ArrayList<>();
                ilCodes.getCodes().stream().forEach(code->{
                    int start = code.getStartLine(), end = code.getEndLine();
                    if(MODE_NATIVE.equals(code.getProcessingMode())) for(int i = start - 1;i < end;i++)
                        results.add(datas.get(i));
                    else results.addAll(code.refreshCodeFragmentCache(false));
                });
                param.getCmdOptional().ifPresent(c->{
                    ilCodes.setMode(MODE_REPL);
                    convertToXml(path.resolveSibling(path.getFileName() + EXT_XML),ilCodes);
                    writeFile(path,results,CHARSET_GBK);
                });
                param.getDetailOptional().ifPresent(c->CS.sl(gsph(ST_FILE_IL_CONF,path.toString(),path + EXT_XML)));
                param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
            });
        }else{
            param.getCmdOptional().ifPresent(c->{
                ILCodes ilCodes = new ILCodes();
                ilCodes.getCodes().add(new ILCode());
                convertToXml(param.getDestPath(),ilCodes);
            });
            param.getDetailOptional().ifPresent(c->CS.sl(gsph(ST_FILE_IL_CONF,EXT_IL,param.getDestPath().toString())));
        }
    }

    private static void regenFileWithGBK(FileParam param){
        Pattern pattern = compile(REG_UC_NON_CHS);
        StringBuffer buffer = new StringBuffer();
        param.getPathMap().entrySet().parallelStream().forEach(entry->{
            Path path = entry.getValue();
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_EXTR},new FileSizeMatcher(entry.getKey()),path));
            List<String> datas = readFile(path,analyzeCharsetEncode(path,CHARSET_GBK));
            datas.parallelStream().distinct().forEach(data->buffer.append(pattern.matcher(data).replaceAll(S_EMPTY)));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        if(0 == buffer.length()){
            fillDatas(buffer,CHARSET_GBK,0xa1,0xa9,0xa1,0xfe);
            fillDatas(buffer,CHARSET_GBK,0xb0,0xf7,0xa1,0xfe);
        }else{
            Set<String> caches = new HashSet<>();
            char[] ca = buffer.toString().toCharArray();
            for(int i = 0;i < ca.length;i++)
                caches.add(ca[i] + S_EMPTY);
            buffer.delete(0,buffer.length());
            caches.parallelStream().forEach(cache->buffer.append(cache));
        }
        String result = pattern.matcher(buffer.toString().replaceAll(REG_UC_MC_GBK,S_EMPTY)).replaceAll(S_EMPTY);
        param.getDetailOptional().ifPresent(c->CS.sl(result,1));
        param.getCmdOptional().ifPresent(c->writeFileWithUTF16LEBom(param.getDestPath(),result));
    }

    private static void regenFileWithBIG5(FileParam param){
        Pattern pattern = compile(REG_UC_NON_CHS);
        StringBuffer buffer = new StringBuffer();
        param.getPathMap().entrySet().parallelStream().forEach(entry->{
            Path path = entry.getValue();
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_EXTR},new FileSizeMatcher(entry.getKey()),path));
            List<String> datas = readFile(path,analyzeCharsetEncode(path,CHARSET_BIG5));
            datas.parallelStream().distinct().forEach(data->buffer.append(pattern.matcher(data).replaceAll(S_EMPTY)));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        if(0 == buffer.length()){
            fillDatas(buffer,CHARSET_BIG5,0xa1,0xa3,0x40,0xbf);
            fillDatas(buffer,CHARSET_BIG5,0xa4,0xc6,0x40,0x7e);
            fillDatas(buffer,CHARSET_BIG5,0xc9,0xf9,0x40,0xd5);
        }else{
            Set<String> caches = new HashSet<>();
            char[] ca = buffer.toString().toCharArray();
            for(int i = 0;i < ca.length;i++)
                caches.add(ca[i] + S_EMPTY);
            buffer.delete(0,buffer.length());
            caches.parallelStream().forEach(cache->buffer.append(cache));
        }
        String result = pattern.matcher(buffer.toString().replaceAll(REG_UC_MC_BIG5,S_EMPTY)).replaceAll(S_EMPTY);
        param.getDetailOptional().ifPresent(c->CS.sl(result,1));
        param.getCmdOptional().ifPresent(c->writeFileWithUTF16LEBom(param.getDestPath(),result));
    }

    private static void renameFiles(FileParam param){
        renameFile(param,name->param.getPattern().matcher(name).replaceAll(param.getReplacement()));
    }

    private static void renLowFiles(FileParam param){
        renameFile(param,name->{
            StringBuffer stringBuffer = new StringBuffer(name.toLowerCase());
            dealMismatch(param,stringBuffer,name);
            return stringBuffer.toString();
        });
    }

    private static void renUpFiles(FileParam param){
        renameFile(param,name->{
            StringBuffer stringBuffer = new StringBuffer(name.toUpperCase());
            dealMismatch(param,stringBuffer,name);
            return stringBuffer.toString();
        });
    }

    private static void renUpFstFiles(FileParam param){
        renameFile(param,name->{
            StringBuffer stringBuffer = new StringBuffer();
            Matcher matcher = RPTN.matcher(name);
            while(matcher.find()){
                char[] s = matcher.group().toCharArray();
                s[0] -= (s[0] >= 97 && s[0] <= 122 ? 32 : 0);
                matcher.appendReplacement(stringBuffer,String.valueOf(s));
            }
            matcher.appendTail(stringBuffer);
            dealMismatch(param,stringBuffer,name);
            return stringBuffer.toString();
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

    private static void copyFiles(FileParam param){
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

    private static void deleteFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().filter(e->e.getKey().isRegularFile()).forEach(e->{
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_DEL},new FileSizeMatcher(e.getKey()),e.getValue()));
            param.getCmdOptional().ifPresent(c->deleteFile(e.getValue()));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        delNulDirs(param);
    }

    private static void delNulFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().filter(e->e.getKey().isRegularFile()).flatMap(m->of(m.getValue())).forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_DEL + N_FLE_NUL + gs(2) + p));
            param.getCmdOptional().ifPresent(c->deleteFile(p));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        delNulDirs(param);
    }

    private static void delNulDirs(FileParam param){
        param.getPathList().stream().sorted(new PathListComparator(false)).forEach(p->{
            if(0 == p.toFile().list().length){
                param.getDetailOptional().ifPresent(c->CS.sl(V_DEL + N_DIR_NUL + gs(2) + p));
                param.getCmdOptional().ifPresent(c->deleteFile(p));
                param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
            }else param.getDirsCount().addAndGet(-1);
        });
    }

    private static void moveFiles(FileParam param){
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
        param.getRePathMap().keySet().stream().sorted(new PathListComparator(false)).forEach(p->param.getCmdOptional().ifPresent(c->deleteFile(p)));
    }

    private static void interchangeFiles(FileParam param){
        boolean upgrade = param.matchConditions(INTERCHANGE_UPGRADE);
        param.getPathMap().values().stream().sorted(new PathListComparator(true)).forEach(src->{
            File file = src.toFile();
            Path dest = param.getDestPath().resolve(param.getSrcPath().relativize(src));
            if(dest.getParent().toFile().isDirectory()) try{
                if(isEmpty(param.getPathsMap().get(dest.getParent()))){
                    List<Path> paths = new ArrayList<>();
                    param.getPathsMap().put(dest.getParent(),paths);
                    find(dest.getParent(),1,(p, b)->b.isRegularFile()).parallel().forEach(p->paths.add(p));
                }
                param.getPathList().clear();
                List<Path> paths = param.getPathsMap().get(dest.getParent());
                paths.parallelStream().filter(p->upgrade && p.getFileName().toString().toLowerCase().startsWith(src.getFileName().toString().toLowerCase()) || !upgrade && src.getFileName().toString().toLowerCase().startsWith(p.getFileName().toString().toLowerCase())).forEach(p->{
                    param.getFilesSize().addAndGet(p.toFile().length());
                    param.getPathList().add(p);
                });
                param.getFilesCount().addAndGet(param.getPathList().size());
                param.getPathList().parallelStream().forEach(p->{
                    Path backup = param.getBackupPath().resolve(param.getDestPath().relativize(p));
                    param.getDetailOptional().ifPresent(t->showFile(new String[]{V_BAK + V_MOV,V_TO},new FileSizeMatcher(file),p,backup));
                    param.getCmdOptional().ifPresent(c->moveFile(p,backup));
                });
                paths.removeAll(param.getPathList());
            }catch(IOException ioe){
                CS.sl(gsph(ERR_DIR_VST,dest.getParent().toString(),ioe.toString()));
            }
            if(param.getPathList().isEmpty()) param.getDetailOptional().ifPresent(t->showFile(new String[]{V_ADD + V_MOV,V_TO},new FileSizeMatcher(file),src,dest));
            else param.getDetailOptional().ifPresent(t->showFile(new String[]{V_UPD + V_MOV,V_TO},new FileSizeMatcher(file),src,dest));
            param.getCmdOptional().ifPresent(c->moveFile(src,dest));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void upgradeFiles(FileParam param){
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
                Path backup = param.getBackupPath().resolve(param.getDestPath().getParent().relativize(dest));
                param.getDetailOptional().ifPresent(t->showFile(new String[]{V_BAK,V_TO},new FileSizeMatcher(file),dest,backup));
                param.getCmdOptional().ifPresent(c->copyFile(dest,backup));
                param.getDetailOptional().ifPresent(t->showFile(new String[]{V_UPD},new FileSizeMatcher(e.getKey()),dest));
                param.getCmdOptional().ifPresent(c->copyFile(src,dest));
            }else{
                param.getDetailOptional().ifPresent(t->showFile(new String[]{V_ADD,V_TO},new FileSizeMatcher(e.getKey()),src,dest));
                param.getCmdOptional().ifPresent(c->copyFile(src,dest));
            }
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void zipFiles(FileParam param){
        Path path = param.getDestPath().resolve(param.getZipName());
        CS.showError(ERR_ZIP_FLE_CRT,new String[]{path.toString(),gsph(ERR_ZIP_FILE_SAME,path.toString())},()->param.getPathMap().containsValue(path));
        param.getCmdOptional().ifPresent(c->createZipFile(param));
        param.getPathList().stream().sorted(new PathListComparator(true)).forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_CPRS + N_DIR_NUL + gs(2) + p));
            param.getCmdOptional().ifPresent(c->zipDir(param.getZipOutputStream(),param.getRootPath(),p));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
        param.getPathMap().entrySet().stream().filter(e->e.getKey().isRegularFile()).flatMap(e->of(e.getValue())).sorted(new PathListComparator(true)).forEach(p->{
            param.getDetailOptional().ifPresent(c->showFile(new String[]{V_CPRS},new FileSizeMatcher(p.toFile()),p));
            param.getCmdOptional().ifPresent(c->zipFile(param.getZipOutputStream(),param.getRootPath(),p));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void unzipFiles(FileParam param){
        boolean unzip = param.matchConditions(ZIP_UNZIP);
        param.getPathMap().entrySet().parallelStream().flatMap(e->of(e.getValue())).forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.s(V_DCPRS + N_FLE + gs(2) + p + gs(1) + V_START + S_ELLIPSIS).l(2));
            SingleValue<ZipEntry> value = new SingleValue<>(null);
            File file = p.toFile();
            try(ZipFile zipFile = new ZipFile(file);ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)))){
                CS.showError(ERR_ZIP_FLE_DCPRS,new String[]{p.toString(),gsph(ERR_ZIP_FILE_SAME,p.toString())},()->zipFile.stream().parallel().anyMatch(entry->{
                    if(entry.isDirectory() || entry.getName().endsWith(SPRT_FILE)) return false;
                    if(unzip) return p.equals(param.getDestPath().resolve(entry.getName()));
                    else return p.equals(p.getParent().resolve(entry.getName()));
                }));
                int size = zipFile.size();
                for(value.set(zipInputStream.getNextEntry());nonEmpty(value.get());value.set(zipInputStream.getNextEntry())){
                    ZipEntry entry = value.get();
                    SingleValue<Path> dest = new SingleValue<>(null);
                    if(unzip) dest.set(param.getDestPath().resolve(entry.getName()));
                    else dest.set(p.getParent().resolve(entry.getName()));
                    if(entry.isDirectory() || entry.getName().endsWith(SPRT_FILE)){
                        param.getDirsCount().incrementAndGet();
                        param.getDetailOptional().ifPresent(c->CS.sl(V_EXTR + N_DIR_NUL + gs(2) + dest));
                        param.getCmdOptional().ifPresent(c->dest.get().toFile().mkdirs());
                    }else{
                        param.getFilesCount().incrementAndGet();
                        param.getCmdOptional().ifPresent(c->{
                            try(InputStream inputStream = zipFile.getInputStream(entry)){
                                dest.get().getParent().toFile().mkdirs();
                                dest.get().toFile().setWritable(true,true);
                                if(nonEmpty(inputStream)) copy(inputStream,dest.get(),StandardCopyOption.REPLACE_EXISTING);
                            }catch(Exception ex){
                                CS.sl(gsph(ERR_ZIP_FLE_EXTR,entry.getName(),ex.toString()));
                            }
                        });
                        zipInputStream.closeEntry();
                        if(0 == entry.getSize()) param.getDetailOptional().ifPresent(c->CS.sl(V_EXTR + N_FLE_NUL + gs(2) + dest));
                        else param.getDetailOptional().ifPresent(c->CS.sl(V_EXTR + N_FLE + gs(4) + dest));
                    }
                    param.getProgressOptional().ifPresent(c->PG.update(PG.countUpdate(size,1),PROGRESS_SCALE));
                }
            }catch(Exception ex){
                CS.sl(gsph(ERR_ZIP_FLE_DCPRS,p.toString(),ex.toString()));
            }
            param.getDetailOptional().ifPresent(c->CS.l(1).s(V_DCPRS + N_FLE + gs(2) + p + gs(1) + V_DONE + S_PERIOD).l(2));
        });
    }

    private static void showMd5(FileParam param, Function<Path,String> function){
        param.getPathMap().values().parallelStream().forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(p.toString() + gs(4) + function.apply(p)));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void encodeJson(FileParam param){
        param.getPathMap().values().parallelStream().forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_ENC + N_FLE + gs(2) + p.toString()));
            param.getCmdOptional().ifPresent(c->trimJson(p));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void decodeJson(FileParam param){
        param.getPathMap().values().parallelStream().forEach(p->{
            param.getDetailOptional().ifPresent(c->CS.sl(V_DEC + N_FLE + gs(2) + p.toString()));
            param.getCmdOptional().ifPresent(c->formatJson(p));
            param.getProgressOptional().ifPresent(c->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static boolean matchCharsetWithUTF8(byte[] bytes, int index, SingleValue<Integer> bytesCount){
        if(0 <= bytes[index]){
            bytesCount.set(1);
            return true;
        }else if(matchRange(bytes[index],-64,-33)){
            bytesCount.set(2);
            if(matchRange(bytes[index + 1],-128,-65)) return true;
            return false;
        }else if(matchRange(bytes[index],-32,-17)){
            bytesCount.set(3);
            if(matchRange(bytes[index + 1],-128,-65) && matchRange(bytes[index + 2],-128,-65)) return true;
            return false;
        }else if(matchRange(bytes[index],-16,-9)){
            bytesCount.set(4);
            if(matchRange(bytes[index + 1],-128,-65) && matchRange(bytes[index + 2],-128,-65) && matchRange(bytes[index + 3],-128,-65)) return true;
            return false;
        }else return false;
    }

    private static void dealMismatch(FileParam param, StringBuffer stringBuffer, String string){
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
            stringBuffer.replace(start,end,string.substring(start,end));
        }
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

    private static void fillDatas(StringBuffer buffer, String charset, int... values){
        byte[] bytes = new byte[2];
        for(int high = values[0];high <= values[1];high++){
            bytes[0] = (byte)high;
            for(int low = values[2];low <= values[3];low++){
                bytes[1] = (byte)low;
                try{
                    buffer.append(new String(bytes,charset));
                }catch(UnsupportedEncodingException e){}
            }
        }
    }

    private static void showFile(String[] s, BooleanSupplier b, Path... p){
        if(1 == s.length){
            if(b.getAsBoolean()) CS.sl(s[0] + N_FLE_NUL + gs(2) + p[0]);
            else CS.sl(s[0] + N_FLE + gs(4) + p[0]);
        }else if(2 == s.length){
            if(b.getAsBoolean()) CS.sl(s[0] + N_FLE_NUL + gs(2) + p[0] + s[1] + p[1]);
            else CS.sl(s[0] + N_FLE + gs(4) + p[0] + s[1] + p[1]);
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
            param.getPathDeque().clear();
        }catch(Exception e){
            CS.sl(gsph(ERR_DIR_VST,param.getSrcPath().toString(),e.toString()));
        }
        return count;
    }

    private static void cacheRepaths(FileParam param){
        if(!param.matchConditions(MATCH_FILE_ONLY) && param.matchConditions(NEED_REPATH)) param.getPathList().parallelStream().forEach(p->param.getRePathMap().put(p,param.getOutPath().resolve(param.getRootPath().relativize(p))));
    }

    private static class PathMatcher implements BiPredicate<Path,BasicFileAttributes>{
        private FileParam param;
        boolean matchFileOnly, matchDirOnly, excludeRoot, ignoreRegex;

        private PathMatcher(FileParam param){
            this.param = param;
            matchFileOnly = param.matchConditions(MATCH_FILE_ONLY);
            matchDirOnly = param.matchConditions(MATCH_DIR_ONLY);
            excludeRoot = param.matchConditions(EXCLUDE_ROOT);
            ignoreRegex = param.matchConditions(IGNORE_REGEX);
        }

        @Override
        public boolean test(Path p, BasicFileAttributes a){
            boolean find = false;
            if(a.isRegularFile()){
                if(matchDirOnly) return false;
                find = ignoreRegex ? ignoreRegex : param.getPattern().matcher(p.getFileName().toString()).find();
                switch(param.getCmd()){
                    case CMD_DEL_NUL:
                    case CMD_DEL_DIR_NUL:
                    if(find = find && 0 == a.size()) param.getPathMap().put(a,p);
                    break;
                    default:
                    find = find && param.matchFilesSize(a.size());
                    if(find || (find = matchPath(p) && param.matchFilesSize(a.size()))) param.getPathMap().put(a,p);
                }
            }else if(a.isDirectory()){
                if(matchFileOnly || excludeRoot && p.equals(param.getSrcPath())) return false;
                find = ignoreRegex ? ignoreRegex : param.getPattern().matcher(p.getFileName().toString()).find();
                switch(param.getCmd()){
                    case CMD_FND_DIR_SAM:
                    case CMD_FND_DIR_DIF:
                    case CMD_FND_DIR_OLY_SAM:
                    case CMD_FND_DIR_OLY_DIF:
                    if(find = find || matchPath(p)){
                        param.getPathMap().put(a,p);
                        param.getPathDeque().push(p);
                    }
                    break;
                    case CMD_FND_DIR_OLY:
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
                        param.getPathList().add(p);
                    }
                    break;
                    case CMD_FND_DIR:
                    case CMD_FND_DIR_SIZ_ASC:
                    case CMD_FND_DIR_SIZ_DSC:
                    case CMD_FND_PTH_DIR_ABS:
                    case CMD_FND_PTH_DIR_RLT:
                    case CMD_FND_DIR_DIR_SIZ_ASC:
                    case CMD_FND_DIR_DIR_SIZ_DSC:
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
                    if(find = find || matchPath(p)){
                        param.getPathMap().put(a,p);
                        param.getPathList().add(p);
                        param.getPathDeque().push(p);
                    }
                }
            }
            if(find) if(a.isDirectory()) param.getDirsCount().incrementAndGet();
            else param.getFilesCount().incrementAndGet();
            return find;
        }

        private boolean matchPath(Path path){
            BlockingDeque<Path> deque = param.getPathDeque();
            for(Path p = deque.poll();nonEmpty(p);p = deque.poll())
                if(path.startsWith(p)){
                    deque.push(p);
                    return true;
                }
            return false;
        }
    }

    private static class FileSizeMatcher implements BooleanSupplier{
        private BasicFileAttributes attributes;
        private File file;

        private FileSizeMatcher(BasicFileAttributes attributes){
            this.attributes = attributes;
        }

        private FileSizeMatcher(File file){
            this.file = file;
        }

        @Override
        public boolean getAsBoolean(){
            if(nonEmpty(attributes)) return 0 == attributes.size();
            return 0 == (file.isDirectory() ? file.list().length : file.length());
        }
    }

    private static class BasicFileAttributesPathComparator implements Comparator<Entry<BasicFileAttributes,Path>>{
        private boolean order;

        private BasicFileAttributesPathComparator(boolean order){
            this.order = order;
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

    private static class PathPathComparator implements Comparator<Entry<Path,Path>>{
        private boolean order;

        private PathPathComparator(boolean order){
            this.order = order;
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

    private static class PathLongComparator implements Comparator<Entry<Path,Long>>{
        private boolean order;

        private PathLongComparator(boolean order){
            this.order = order;
        }

        @Override
        public int compare(Entry<Path,Long> e1, Entry<Path,Long> e2){
            long l1 = e1.getValue(), l2 = e2.getValue();
            return l1 == l2 ? e1.getKey().compareTo(e2.getKey()) : order ? (l1 > l2 ? 1 : -1) : (l1 < l2 ? 1 : -1);
        }
    }

    private static class PathListComparator implements Comparator<Path>{
        private boolean order;

        private PathListComparator(boolean order){
            this.order = order;
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
