package legend.util;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.find;
import static java.nio.file.Files.move;
import static java.nio.file.Files.write;
import static java.nio.file.Paths.get;
import static java.util.regex.Pattern.compile;
import static legend.intf.ICommon.gs;
import static legend.intf.ICommon.gsph;
import static legend.util.ConsoleUtil.FS;
import static legend.util.ConsoleUtil.IN;
import static legend.util.TimeUtil.countDuration;
import static legend.util.TimeUtil.decTotalDuration;
import static legend.util.TimeUtil.getDurationString;
import static legend.util.TimeUtil.getTotalDurationString;
import static legend.util.TimeUtil.incTotalDuration;
import static legend.util.TimeUtil.resetTime;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import legend.util.intf.IConsoleUtil;
import legend.util.intf.IFileUtil;
import legend.util.intf.IProgress;
import legend.util.param.FileParam;
import legend.util.param.SingleValue;

public class FileUtil implements IFileUtil,IConsoleUtil{
    private static final FileParam CACHE;
    private static final ConsoleUtil CS;
    private static final IProgress PG;
    private static FileParam FP;
    private static PrintStream PS;
    static{
        CACHE = new FileParam();
        CS = new ConsoleUtil();
        PG = ProgressUtil.ConsoleProgress();
    }

    private FileUtil(){}

    public static void main(String[] args){
        dealFiles(args);
    }

    public static void dealFiles(List<FileParam> fileParams){
        for(FileParam param : fileParams){
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
                if(!FP.getAskPattern().matcher(line).find()){
                    if(progress) countDuration(t->PG.runUntillFinish(FileUtil::dealFiles));
                    else countDuration(t->dealFiles(FP));
                }else resetTime();
            }else if(progress) countDuration(t->PG.runUntillFinish(FileUtil::dealFiles));
            else countDuration(t->dealFiles(FP));
            if(opt.contains(OPT_SIMULATE)) CS.s(V_SMLT + gsph(ST_CMD_DONE,command) + V_DEAL + FP.getDirsCount().get() + N_AN + N_DIR + S_COMMA + FP.getFilesCount().get() + N_AN + N_FLE + S_BRACKET_L + N_SIZE + S_COLON).formatSize(param.getFilesSize().get(),UNIT_TYPE.TB).s(S_BRACKET_R + S_SEMICOLON + N_TIME + S_COLON + getDurationString() + S_PERIOD).l(2);
            else CS.s(gsph(ST_CMD_DONE,command) + V_DEAL + FP.getDirsCount().get() + N_AN + N_DIR + S_COMMA + FP.getFilesCount().get() + N_AN + N_FLE + S_BRACKET_L + N_SIZE + S_COLON).formatSize(FP.getFilesSize().get(),UNIT_TYPE.TB).s(S_BRACKET_R + S_SEMICOLON + N_TIME + S_COLON + getDurationString() + S_PERIOD).l(2);
        }
    }

    public static void dealFiles(FileParam fileParam){
        try(FileParam param = fileParam){
            param.generatingConditions(CACHE);
            if(!param.useCache(CACHE)){
                param.getProgressOptional().ifPresent(t->PG.reset(1,PROGRESS_POSITION,100));
                cacheFiles(param);
                param.getProgressOptional().ifPresent(t->PG.reset(param.getFilesAndDirsCount(),PROGRESS_POSITION));
            }
            switch(param.getCmd()){
                case CMD_FIND:
                case CMD_FND_DIR:
                case CMD_FND_DIR_OLY:
                findSortedFiles(param);
                break;
                case CMD_FND_PTH_ABS:
                case CMD_FND_PTH_RLT:
                case CMD_FND_PTH_SRC:
                case CMD_FND_PTH_DIR_ABS:
                case CMD_FND_PTH_DIR_RLT:
                case CMD_FND_PTH_DIR_SRC:
                case CMD_FND_PTH_DIR_OLY_ABS:
                case CMD_FND_PTH_DIR_OLY_RLT:
                case CMD_FND_PTH_DIR_OLY_SRC:
                findSortedFilePaths(param);
                break;
                case CMD_FND_SIZ:
                case CMD_FND_DIR_SIZ:
                findFileSizes(param);
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
                case CMD_DELETE:
                case CMD_DEL_DIR:
                deleteFiles(param);
                break;
                case CMD_DEL_DIR_NUL:
                delNulFiles(param);
                break;
                case CMD_RENAME:
                case CMD_REN_DIR:
                renameFiles(param);
                break;
                case CMD_REN_LOW:
                case CMD_REN_DIR_LOW:
                renLowFiles(param);
                break;
                case CMD_REN_UP:
                case CMD_REN_DIR_UP:
                renUpFiles(param);
                break;
                case CMD_REN_UP_FST:
                case CMD_REN_DIR_UP_FST:
                renUpFstFiles(param);
                break;
                case CMD_COPY:
                case CMD_CPY_DIR:
                copyFiles(param);
                break;
                case CMD_MOVE:
                case CMD_MOV_DIR:
                moveFiles(param);
                break;
                case CMD_BACKUP:
                case CMD_BAK_DIR:
                backupFiles(param);
                break;
                case CMD_BAK_UGD:
                case CMD_BAK_RST:
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
            }
            param.saveCache(CACHE);
        }catch(Exception e){
            CS.sl(gsph(ERR_DIR_VST,fileParam.getSrcPath().toString(),e.toString()));
        }finally{
            fileParam.getDetailOptional().ifPresent(s->CS.l(1));
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

    public static void writeFile(Path path, Collection<String> lines){
        try{
            write(makeDirs(path),lines);
        }catch(IOException e){
            CS.sl(gsph(ERR_ZIP_FLE_WRT,path.toString(),e.toString()));
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
        List<FileParam> fileParams = incTotalDuration(()->dealParam(args));
        CS.s(gsph(ST_ARG_DONE,cmds) + N_TIME + S_COLON + getTotalDurationString() + S_PERIOD).l(2);
        incTotalDuration(t->dealFiles(fileParams));
        CS.s(ST_PRG_DONE + V_DEAL + CACHE.getDirsCount().get() + N_AN + N_DIR + S_COMMA + CACHE.getFilesCount().get() + N_AN + N_FLE + S_BRACKET_L + N_SIZE + S_COLON).formatSize(CACHE.getFilesSize().get(),UNIT_TYPE.TB).s(S_BRACKET_R + S_SEMICOLON + N_TIME + S_COLON + getTotalDurationString() + S_PERIOD).l(2);
    }

    private static void dealFiles(IProgress progress){
        CS.clearStream();
        dealFiles(FP);
        CS.cacheStream(PS);
    }

    private static List<FileParam> dealParam(String[] args){
        CS.showError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT},()->args.length < 3);
        String[][] aa = new String[args.length][];
        aa[0] = args[0].split(REG_SPRT_ARG);
        Matcher mrpt = compile(REG_RPT_ARG).matcher(aa[0][0]);
        CS.showError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT},()->mrpt.matches());
        Matcher mph = compile(REG_PH_ARG).matcher("");
        for(int i = 0;i < aa[0].length;i++){
            mph.reset(aa[0][i]);
            CS.showError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT},()->mph.matches());
        }
        for(int i = 1;i < args.length;i++){
            aa[i] = args[i].split(REG_SPRT_ARG);
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
                if(as[0].length() > 2) matchOpt(param,as[0]);
                param.setPattern(compile(as[1]));
                param.setReplacePattern(compile(REG_REN_UP_FST));
                param.setSrcPath(get(as[2]));
                switch(param.getCmd()){
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
                        if(0 != filesCountLimit) param.setLimit(filesCountLimit > 0 ? filesCountLimit : 1);
                    });
                    optional.filter(s->s.length > 4).ifPresent(s->param.setLevel(Integer.parseInt(s[4])));
                    break;
                    case CMD_FND_SIZ:
                    case CMD_FND_DIR_SIZ:
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
                        param.setLimit(0 < filesCountLimit && Integer.MAX_VALUE > filesCountLimit ? filesCountLimit : Integer.MAX_VALUE);
                    });
                    optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
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
                        param.setLimit(0 < filesCountLimit && Integer.MAX_VALUE > filesCountLimit ? filesCountLimit : Integer.MAX_VALUE);
                    });
                    param.setLevel(1);
                    break;
                    case CMD_REN_DIR_LOW:
                    case CMD_REN_DIR_UP:
                    case CMD_REN_DIR_UP_FST:
                    case CMD_DELETE:
                    case CMD_DEL_DIR:
                    case CMD_DEL_DIR_NUL:
                    case CMD_REN_LOW:
                    case CMD_REN_UP:
                    case CMD_REN_UP_FST:
                    case CMD_PAK_INF:
                    optional.filter(s->s.length > 3).ifPresent(s->param.setLevel(Integer.parseInt(s[3])));
                    break;
                    case CMD_COPY:
                    case CMD_CPY_DIR:
                    case CMD_MOVE:
                    case CMD_MOV_DIR:
                    case CMD_ZIP_INF:
                    param.setDestPath(get(as[3]));
                    optional.filter(s->s.length > 4).ifPresent(s->param.setLevel(Integer.parseInt(s[4])));
                    break;
                    case CMD_RENAME:
                    case CMD_REN_DIR:
                    param.setReplacement(as[3]);
                    optional.filter(s->s.length > 4).ifPresent(s->param.setLevel(Integer.parseInt(s[4])));
                    break;
                    case CMD_BACKUP:
                    case CMD_BAK_DIR:
                    case CMD_BAK_UGD:
                    case CMD_BAK_RST:
                    case CMD_UPGRADE:
                    case CMD_UGD_DIR:
                    param.setDestPath(get(as[3]));
                    param.setBackupPath(get(as[4]));
                    optional.filter(s->s.length > 5).ifPresent(s->param.setLevel(Integer.parseInt(s[5])));
                    break;
                    case CMD_ZIP_DEF:
                    case CMD_ZIP_DIR_DEF:
                    param.setDestPath(get(as[3]));
                    param.setZipName(as[4] + EXT_ZIP);
                    optional.filter(s->s.length > 5).ifPresent(s2->param.setZipLevel(Integer.parseInt(s2[5])));
                    optional.filter(s->s.length > 6).ifPresent(s->param.setLevel(Integer.parseInt(s[6])));
                    break;
                    case CMD_PAK_DEF:
                    case CMD_PAK_DIR_DEF:
                    param.setDestPath(get(as[3]));
                    param.setZipName(as[4] + EXT_PAK);
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

    private static void findSortedFiles(FileParam param){
        if(param.needCaching()) param.getPathMap().values().parallelStream().forEach(p->{
            if(p.toFile().isDirectory()) param.getPathList().add(p);
        });
        param.getDetailOptional().ifPresent(s->{
            param.getPathMap().values().stream().sorted(new PathListComparator(param)).limit(param.getLimit()).forEach(p->{
                File file = p.toFile();
                if(file.isFile()) showFile(new String[]{V_FIND},new FileSizeMatcher(file),p);
                else showDir(new String[]{V_FIND},new FileSizeMatcher(p.toFile()),p);
            });
        });
    }

    private static void findSortedFilePaths(FileParam param){
        if(param.needCaching()) param.getPathMap().values().parallelStream().forEach(p->{
            if(p.toFile().isDirectory()) param.getPathList().add(p);
        });
        param.getDetailOptional().ifPresent(s->param.getPathMap().values().stream().sorted(new PathListComparator(param)).limit(param.getLimit()).forEach(p->showFilePath(param,p)));
    }

    private static void findFileSizes(FileParam param){
        param.getDetailOptional().ifPresent(s->param.getPathMap().entrySet().stream().limit(param.getLimit()).forEach(e->CS.formatSize(e.getKey().size(),UNIT_TYPE.GB).s(4).sl(e.getValue().toString())));
    }

    private static void findSortedFileSizes(FileParam param){
        param.getDetailOptional().ifPresent(s->param.getPathMap().entrySet().stream().sorted(new BasicFileAttributesPathComparator(param)).limit(param.getLimit()).forEach(e->CS.formatSize(e.getKey().size(),UNIT_TYPE.GB).s(4).sl(e.getValue().toString())));
    }

    private static void findSortedDirSizes(FileParam param){
        if(param.needCaching()) param.getPathMap().values().parallelStream().forEach(p->{
            if(p.toFile().isDirectory()) param.getPathList().add(p);
        });
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            Path p = e.getValue();
            if(p.toFile().isFile()) param.getSizeMap().put(p,e.getKey().size());
            else if(!p.equals(param.getSrcPath())){
                FileParam fp = new FileParam();
                fp.setCmd(CMD_FND_SIZ);
                fp.setPattern(compile(REG_ANY));
                fp.setSrcPath(p);
                cacheFiles(fp);
                fp.clearCache();
                long size = fp.getFilesSize().get();
                if(param.matchFilesSize(size)) param.getSizeMap().put(p,size);
            }
        });
        param.getDetailOptional().ifPresent(s->{
            param.getSizeMap().entrySet().stream().sorted(new PathLongComparator(param)).limit(param.getLimit()).forEach(e->{
                Path path = e.getKey();
                if(path.toFile().isFile()) CS.formatSize(e.getValue(),UNIT_TYPE.GB).sl(gs(4) + N_FLE + gs(2) + path.toString());
                else CS.formatSize(e.getValue(),UNIT_TYPE.GB).sl(gs(4) + N_DIR + gs(2) + path.toString());
            });
        });
    }

    private static void deleteFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            Path p = e.getValue();
            BasicFileAttributes a = e.getKey();
            if(a.isRegularFile()){
                param.getDetailOptional().ifPresent(s->showFile(new String[]{V_DEL},new FileSizeMatcher(a),p));
                param.getCmdOptional().ifPresent(s->deleteFile(p));
                param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
            }else if(!param.usingCaching()) param.getPathList().add(p);
        });
        delNulDirs(param);
    }

    private static void delNulFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            Path p = e.getValue();
            BasicFileAttributes a = e.getKey();
            if(a.isRegularFile()){
                param.getDetailOptional().ifPresent(s->CS.sl(V_DEL + N_FLE_NUL + gs(1) + p));
                param.getCmdOptional().ifPresent(s->deleteFile(p));
                param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
            }else if(!param.usingCaching()) param.getPathList().add(p);
        });
        delNulDirs(param);
    }

    private static void delNulDirs(FileParam param){
        param.getPathList().stream().sorted(new PathListComparator(param)).forEach(p->{
            param.getDetailOptional().ifPresent(s->CS.sl(V_DEL + N_DIR_NUL + gs(1) + p));
            if(0 == p.toFile().list().length) param.getCmdOptional().ifPresent(s->deleteFile(p));
            param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void renameFiles(FileParam param){
        boolean refresh = param.needRefreshCache();
        param.getPathMap().entrySet().stream().forEach(e->{
            Path src = e.getValue();
            BasicFileAttributes a = e.getKey();
            String rename = param.getPattern().matcher(src.getFileName().toString()).replaceAll(param.getReplacement());
            Path dest = src.getParent().resolve(rename);
            if(refresh) param.getPathMap().replace(a,dest);
            if(a.isRegularFile()){
                param.getDetailOptional().ifPresent(s->showFile(new String[]{V_REN,V_BY},new FileSizeMatcher(a),src,dest));
                param.getCmdOptional().ifPresent(s->src.toFile().renameTo(dest.toFile()));
                param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
            }else if(!param.usingCaching()) param.getRePathMap().put(src,dest);
        });
        renameDirs(param);
    }

    private static void renLowFiles(FileParam param){
        boolean refresh = param.needRefreshCache();
        param.getPathMap().entrySet().stream().forEach(e->{
            Path src = e.getValue();
            BasicFileAttributes a = e.getKey();
            StringBuffer stringBuffer = new StringBuffer(src.getFileName().toString().toLowerCase());
            dealMismatch(param,stringBuffer,src.getFileName().toString());
            Path dest = src.resolveSibling(stringBuffer.toString());
            if(refresh) param.getPathMap().replace(a,dest);
            if(a.isRegularFile()){
                param.getDetailOptional().ifPresent(s->showFile(new String[]{V_REN,V_BY},new FileSizeMatcher(a),src,dest));
                param.getCmdOptional().ifPresent(s->src.toFile().renameTo(dest.toFile()));
                param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
            }else if(!param.usingCaching()) param.getRePathMap().put(src,dest);
        });
        renameDirs(param);
    }

    private static void renUpFiles(FileParam param){
        boolean refresh = param.needRefreshCache();
        param.getPathMap().entrySet().stream().forEach(e->{
            Path src = e.getValue();
            BasicFileAttributes a = e.getKey();
            StringBuffer stringBuffer = new StringBuffer(src.getFileName().toString().toUpperCase());
            dealMismatch(param,stringBuffer,src.getFileName().toString());
            Path dest = src.resolveSibling(stringBuffer.toString());
            if(refresh) param.getPathMap().replace(a,dest);
            if(a.isRegularFile()){
                param.getDetailOptional().ifPresent(s->showFile(new String[]{V_REN,V_BY},new FileSizeMatcher(a),src,dest));
                param.getCmdOptional().ifPresent(s->src.toFile().renameTo(dest.toFile()));
                param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
            }else if(!param.usingCaching()) param.getRePathMap().put(src,dest);
        });
        renameDirs(param);
    }

    private static void renUpFstFiles(FileParam param){
        boolean refresh = param.needRefreshCache();
        param.getPathMap().entrySet().stream().forEach(e->{
            Path src = e.getValue();
            BasicFileAttributes a = e.getKey();
            StringBuffer stringBuffer = new StringBuffer();
            Matcher matcher = param.getReplacePattern().matcher(src.getFileName().toString());
            while(matcher.find()){
                char[] s = matcher.group().toCharArray();
                s[0] -= (s[0] >= 97 && s[0] <= 122 ? 32 : 0);
                matcher.appendReplacement(stringBuffer,String.valueOf(s));
            }
            matcher.appendTail(stringBuffer);
            dealMismatch(param,stringBuffer,src.getFileName().toString());
            Path dest = src.resolveSibling(stringBuffer.toString());
            if(refresh) param.getPathMap().replace(a,dest);
            if(a.isRegularFile()){
                param.getDetailOptional().ifPresent(s->showFile(new String[]{V_REN,V_BY},new FileSizeMatcher(a),src,dest));
                param.getCmdOptional().ifPresent(s->src.toFile().renameTo(dest.toFile()));
                param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
            }else if(!param.usingCaching()) param.getRePathMap().put(src,dest);
        });
        renameDirs(param);
    }

    private static void renameDirs(FileParam param){
        param.getRePathMap().entrySet().stream().sorted(new PathPathComparator()).forEach(e->{
            Path src = e.getKey(), dest = e.getValue();
            File file = src.toFile();
            param.getDetailOptional().ifPresent(s->showDir(new String[]{V_REN,V_BY},new FileSizeMatcher(file),src,dest));
            param.getCmdOptional().ifPresent(s->file.renameTo(dest.toFile()));
            param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void copyFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            BasicFileAttributes a = e.getKey();
            Path src = e.getValue();
            Path dest = param.getDestPath().resolve(param.getSrcPath().getParent().relativize(src));
            if(a.isRegularFile()){
                param.getDetailOptional().ifPresent(s->showFile(new String[]{V_CPY,V_TO},new FileSizeMatcher(a),src,dest));
                param.getCmdOptional().ifPresent(s->copyFile(src,dest));
                param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
            }else if(param.usingCaching()) param.getRePathMap().replace(src,dest);
            else param.getRePathMap().put(src,dest);
        });
        param.getRePathMap().entrySet().parallelStream().forEach(e->{
            param.getDetailOptional().ifPresent(s->CS.sl(V_CPY + N_DIR_NUL + gs(1) + e.getKey() + V_TO + e.getValue()));
            param.getCmdOptional().ifPresent(s->e.getValue().toFile().mkdirs());
            param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void moveFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            BasicFileAttributes a = e.getKey();
            Path src = e.getValue();
            Path dest = param.getDestPath().resolve(param.getSrcPath().getParent().relativize(src));
            if(a.isRegularFile()){
                param.getDetailOptional().ifPresent(s->showFile(new String[]{V_MOV,V_TO},new FileSizeMatcher(a),src,dest));
                param.getCmdOptional().ifPresent(s->moveFile(src,dest));
                param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
            }else if(!param.usingCaching()) param.getRePathMap().put(src,dest);
        });
        if(param.usingCaching()){
            param.getRePathMap().clear();
            param.getPathList().parallelStream().forEach(src->{
                Path dest = param.getDestPath().resolve(param.getSrcPath().getParent().relativize(src));
                param.getRePathMap().put(src,dest);
            });
        }
        param.getRePathMap().entrySet().stream().sorted(new PathPathComparator()).forEach(e->{
            Path src = e.getKey(), dest = e.getValue();
            param.getDetailOptional().ifPresent(s->CS.sl(V_MOV + N_DIR_NUL + gs(1) + src + V_TO + dest));
            param.getCmdOptional().ifPresent(s->{
                deleteFile(src);
                dest.toFile().mkdirs();
            });
            param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void backupFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            BasicFileAttributes a = e.getKey();
            Path src = e.getValue();
            Path dest = param.getDestPath().resolve(param.getSrcPath().relativize(src));
            Path backup = param.getBackupPath().resolve(param.getSrcPath().getParent().relativize(src));
            File file = dest.toFile();
            if(a.isRegularFile()){
                if(file.isFile()){
                    param.getFilesCount().decrementAndGet();
                    param.getFilesSize().addAndGet(a.size() * -1);
                }else{
                    param.getDetailOptional().ifPresent(s->showFile(new String[]{V_BAK,V_TO},new FileSizeMatcher(a),src,backup));
                    param.getCmdOptional().ifPresent(s->copyFile(src,backup));
                }
                param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
            }else if(param.usingCaching()) param.getRePathMap().replace(src,backup);
            else param.getRePathMap().put(src,backup);
        });
        param.getRePathMap().entrySet().stream().forEach(e->{
            Path src = e.getKey(), dest = e.getValue();
            param.getDetailOptional().ifPresent(s->CS.sl(V_BAK + N_DIR_NUL + gs(1) + src + V_TO + dest));
            param.getCmdOptional().ifPresent(s->dest.toFile().mkdirs());
            param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void interchangeFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            BasicFileAttributes a = e.getKey();
            Path src = e.getValue();
            Path dest = param.getDestPath().resolve(param.getSrcPath().relativize(src));
            if(dest.getParent().toFile().isDirectory()) try{
                if(isEmpty(param.getPathsMap().get(dest.getParent()))){
                    List<Path> paths = new CopyOnWriteArrayList<>();
                    param.getPathsMap().put(dest.getParent(),paths);
                    find(dest.getParent(),1,(p, b)->b.isRegularFile()).parallel().forEach(p->paths.add(p));
                }
                List<Path> paths = param.getPathsMap().get(dest.getParent());
                paths.parallelStream().forEach(p->{
                    switch(param.getCmd()){
                        case CMD_BAK_UGD:
                        if(p.getFileName().toString().startsWith(src.getFileName().toString())) param.getPathList().add(p);
                        break;
                        case CMD_BAK_RST:
                        if(src.getFileName().toString().startsWith(p.getFileName().toString())) param.getPathList().add(p);
                    }
                });
                param.getFilesCount().addAndGet(param.getPathList().size());
                param.getPathList().parallelStream().forEach(p->{
                    Path backup = param.getBackupPath().resolve(param.getDestPath().relativize(p));
                    param.getDetailOptional().ifPresent(t->showFile(new String[]{V_BAK + V_MOV,V_TO},new FileSizeMatcher(a),p,backup));
                    param.getCmdOptional().ifPresent(s->moveFile(p,backup));
                });
                paths.removeAll(param.getPathList());
                param.getPathList().clear();
            }catch(IOException ioe){
                CS.sl(gsph(ERR_DIR_VST,dest.getParent().toString(),ioe.toString()));
            }
            param.getDetailOptional().ifPresent(t->showFile(new String[]{V_UPD + V_MOV,V_TO},new FileSizeMatcher(a),src,dest));
            param.getCmdOptional().ifPresent(s->moveFile(src,dest));
            param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void upgradeFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            BasicFileAttributes a = e.getKey();
            Path src = e.getValue();
            Path dest = param.getDestPath().resolve(param.getSrcPath().relativize(src));
            if(a.isRegularFile()){
                File file = dest.toFile();
                if(file.isFile()){
                    Path backup = param.getBackupPath().resolve(param.getDestPath().getParent().relativize(dest));
                    param.getDetailOptional().ifPresent(t->showFile(new String[]{V_BAK,V_TO},new FileSizeMatcher(file),dest,backup));
                    param.getCmdOptional().ifPresent(s->copyFile(dest,backup));
                    param.getDetailOptional().ifPresent(t->showFile(new String[]{V_UPD},new FileSizeMatcher(a),dest));
                    param.getCmdOptional().ifPresent(s->copyFile(src,dest));
                }else param.getDetailOptional().ifPresent(t->showFile(new String[]{V_ADD,V_TO},new FileSizeMatcher(a),src,dest));
                param.getCmdOptional().ifPresent(s->copyFile(src,dest));
                param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
            }else if(param.usingCaching()) param.getRePathMap().replace(src,dest);
            else param.getRePathMap().put(src,dest);
        });
        param.getRePathMap().entrySet().parallelStream().forEach(e->{
            Path dest = e.getValue();
            File file = dest.toFile();
            if(file.isDirectory()) param.getDetailOptional().ifPresent(s->CS.sl(V_UPD + N_DIR_NUL + gs(1) + dest));
            else{
                param.getDetailOptional().ifPresent(s->CS.sl(V_ADD + N_DIR_NUL + gs(1) + dest));
                param.getCmdOptional().ifPresent(s->file.mkdirs());
            }
            param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void zipFiles(FileParam param){
        Path path = param.getDestPath().resolve(param.getZipName());
        CS.showError(ERR_ZIP_FLE_CRT,new String[]{path.toString(),gsph(ERR_ZIP_FILE_SAME,path.toString())},()->param.getPathMap().containsValue(path));
        switch(param.getCmd()){
            case CMD_ZIP_DEF:
            case CMD_ZIP_DIR_DEF:
            param.setSrcPath(param.getSrcPath().getParent());
        }
        param.getCmdOptional().ifPresent(s->createZipFile(param));
        param.getPathMap().entrySet().stream().forEach(e->{
            Path p = e.getValue();
            BasicFileAttributes a = e.getKey();
            if(a.isRegularFile()){
                param.getDetailOptional().ifPresent(s->showFile(new String[]{V_CPRS},new FileSizeMatcher(a),p));
                param.getCmdOptional().ifPresent(s->zipFile(param,param.getSrcPath(),p));
            }else{
                param.getDetailOptional().ifPresent(s->CS.sl(V_CPRS + N_DIR_NUL + gs(1) + p));
                param.getCmdOptional().ifPresent(s->zipDir(param,param.getSrcPath(),p));
            }
            param.getProgressOptional().ifPresent(t->PG.update(1,PROGRESS_SCALE));
        });
    }

    private static void unzipFiles(FileParam param){
        param.getPathMap().entrySet().parallelStream().forEach(e->{
            SingleValue<ZipEntry> value = new SingleValue<>(null);
            Path p = e.getValue();
            File file = p.toFile();
            param.getDetailOptional().ifPresent(s->CS.s(V_DCPRS + N_FLE + gs(1) + p + gs(1) + V_START + S_ELLIPSIS).l(2));
            try(ZipFile zipFile = new ZipFile(file);ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)))){
                CS.showError(ERR_ZIP_FLE_DCPRS,new String[]{p.toString(),gsph(ERR_ZIP_FILE_SAME,p.toString())},()->zipFile.stream().parallel().anyMatch(entry->{
                    if(entry.isDirectory() || entry.getName().endsWith(SPRT_FILE)) return false;
                    switch(param.getCmd()){
                        case CMD_ZIP_INF:
                        return p.equals(param.getDestPath().resolve(entry.getName()));
                        default:
                        return p.equals(p.getParent().resolve(entry.getName()));
                    }
                }));
                int size = zipFile.size();
                for(value.set(zipInputStream.getNextEntry());nonEmpty(value.get());value.set(zipInputStream.getNextEntry())){
                    param.getFilesCount().incrementAndGet();
                    ZipEntry entry = value.get();
                    SingleValue<Path> dest = new SingleValue<>(null);
                    switch(param.getCmd()){
                        case CMD_ZIP_INF:
                        dest.set(param.getDestPath().resolve(entry.getName()));
                        break;
                        default:
                        dest.set(p.getParent().resolve(entry.getName()));
                    }
                    if(entry.isDirectory() || entry.getName().endsWith(SPRT_FILE)){
                        param.getDirsCount().incrementAndGet();
                        param.getDetailOptional().ifPresent(s->CS.sl(V_EXTR + N_DIR_NUL + gs(1) + dest));
                        param.getCmdOptional().ifPresent(s->dest.get().toFile().mkdirs());
                    }else{
                        param.getCmdOptional().ifPresent(s->{
                            try(InputStream inputStream = zipFile.getInputStream(entry)){
                                dest.get().getParent().toFile().mkdirs();
                                dest.get().toFile().setWritable(true,true);
                                if(nonEmpty(inputStream)) Files.copy(inputStream,dest.get(),StandardCopyOption.REPLACE_EXISTING);
                            }catch(Exception ex){
                                CS.sl(gsph(ERR_ZIP_FLE_EXTR,entry.getName(),ex.toString()));
                            }
                        });
                        zipInputStream.closeEntry();
                        if(0 == entry.getSize()) param.getDetailOptional().ifPresent(s->CS.sl(V_EXTR + N_FLE_NUL + gs(3) + dest));
                        else param.getDetailOptional().ifPresent(s->CS.sl(V_EXTR + N_FLE + gs(3) + dest));
                    }
                    param.getProgressOptional().ifPresent(t->PG.update(PG.countUpdate(size,1),PROGRESS_SCALE));
                }
            }catch(Exception ex){
                CS.sl(gsph(ERR_ZIP_FLE_DCPRS,p.toString(),ex.toString()));
            }
            param.getDetailOptional().ifPresent(s->CS.l(1).s(V_DCPRS + N_FLE + gs(1) + p + gs(1) + V_DONE + S_PERIOD).l(2));
        });
    }

    private static void matchOpt(FileParam param, String s){
        Matcher matcher = compile(REG_OPT).matcher(s);
        if(matcher.find()){
            param.setCmd(matcher.group(1));
            param.setOpt(matcher.group(2));
            while(matcher.find())
                param.setOpt(param.getOpt() + matcher.group(2));
        }
        if(param.getOpt().contains(OPT_ASK)) param.setAskPattern(compile(REG_ASK_NO));
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
        if(minSize > maxSize){
            minSize ^= maxSize;
            maxSize ^= minSize;
            minSize ^= maxSize;
        }
        param.setMinSize(minSize);
        param.setMaxSize(maxSize);
    }

    private static void dealMismatch(FileParam param, StringBuffer stringBuffer, String string){
        Matcher matcher = param.getPattern().matcher(string);
        final int count = matcher.groupCount();
        if(0 == count) return;
        ArrayDeque<Integer> starts = new ArrayDeque<>();
        ArrayDeque<Integer> ends = new ArrayDeque<>();
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

    private static void zipFile(FileParam param, Path root, Path path){
        try{
            ZipEntry entry = new ZipEntry(root.relativize(path).toString());
            param.getZipOutputStream().putNextEntry(entry);
            Files.copy(path,param.getZipOutputStream());
        }catch(IOException e){
            CS.sl(gsph(ERR_ZIP_FLE_CPY,path.toString(),e.toString()));
        }
    }

    private static void zipDir(FileParam param, Path root, Path path){
        try{
            ZipEntry entry = new ZipEntry(root.relativize(path).toString() + SPRT_FILE_ZIP);
            param.getZipOutputStream().putNextEntry(entry);
        }catch(IOException e){
            CS.sl(gsph(ERR_ZIP_DIR_NUL_CPY,path.toString(),e.toString()));
        }
    }

    private static void showFile(String[] s, BooleanSupplier b, Path... p){
        if(1 == s.length){
            if(b.getAsBoolean()) CS.sl(s[0] + N_FLE_NUL + gs(1) + p[0]);
            else CS.sl(s[0] + N_FLE + gs(3) + p[0]);
        }else if(2 == s.length){
            if(b.getAsBoolean()) CS.sl(s[0] + N_FLE_NUL + gs(1) + p[0] + s[1] + p[1]);
            else CS.sl(s[0] + N_FLE + gs(3) + p[0] + s[1] + p[1]);
        }
    }

    private static void showDir(String[] s, BooleanSupplier b, Path... p){
        if(1 == s.length){
            if(b.getAsBoolean()) CS.sl(s[0] + N_DIR_NUL + gs(1) + p[0]);
            else CS.sl(s[0] + N_DIR + gs(3) + p[0]);
        }else if(2 == s.length){
            if(b.getAsBoolean()) CS.sl(s[0] + N_DIR_NUL + gs(1) + p[0] + s[1] + p[1]);
            else CS.sl(s[0] + N_DIR + gs(3) + p[0] + s[1] + p[1]);
        }
    }

    private static void showFilePath(FileParam param, Path path){
        switch(param.getCmd()){
            case CMD_FND_PTH_RLT:
            case CMD_FND_PTH_DIR_RLT:
            case CMD_FND_PTH_DIR_OLY_RLT:
            CS.sl(param.getSrcPath().relativize(path).toString());
            break;
            case CMD_FND_PTH_SRC:
            case CMD_FND_PTH_DIR_SRC:
            case CMD_FND_PTH_DIR_OLY_SRC:
            CS.sl(param.getSrcPath().getFileName().resolve(param.getSrcPath().relativize(path)).toString());
            break;
            default:
            CS.sl(path.toString());
        }
    }

    private static void cacheFiles(FileParam param){
        try{
            find(param.getSrcPath(),param.getLevel(),new PathMatcher(param)).parallel().count();
            param.getPathDeque().clear();
        }catch(Exception e){
            CS.sl(gsph(ERR_DIR_VST,param.getSrcPath().toString(),e.toString()));
        }
    }

    private static class PathMatcher implements BiPredicate<Path,BasicFileAttributes>{
        private FileParam param;

        private PathMatcher(FileParam param){
            this.param = param;
        }

        @Override
        public boolean test(Path p, BasicFileAttributes a){
            boolean find = false;
            if(a.isRegularFile()){
                if(param.matchDirOnly()) return false;
                find = param.getPattern().matcher(p.getFileName().toString()).find();
                switch(param.getCmd()){
                    case CMD_DEL_DIR_NUL:
                    if(find = find && 0 == a.size()) param.getPathMap().put(a,p);
                    break;
                    default:
                    find = find && param.matchFilesSize(a.size());
                    if(find || (find = matchPath(p) && param.matchFilesSize(a.size()))) param.getPathMap().put(a,p);
                }
            }else if(a.isDirectory()){
                if(param.matchFileOnly()) return false;
                find = param.getPattern().matcher(p.getFileName().toString()).find();
                switch(param.getCmd()){
                    case CMD_FND_DIR_SIZ:
                    case CMD_FND_DIR_SIZ_ASC:
                    case CMD_FND_DIR_SIZ_DSC:
                    find = find || matchPath(p);
                    break;
                    case CMD_REN_DIR:
                    case CMD_REN_DIR_LOW:
                    case CMD_REN_DIR_UP:
                    case CMD_REN_DIR_UP_FST:
                    case CMD_FND_DIR_OLY:
                    case CMD_FND_DIR_DIR_SIZ_ASC:
                    case CMD_FND_DIR_DIR_SIZ_DSC:
                    case CMD_FND_DIR_OLY_SIZ_ASC:
                    case CMD_FND_DIR_OLY_SIZ_DSC:
                    case CMD_FND_PTH_DIR_OLY_ABS:
                    case CMD_FND_PTH_DIR_OLY_RLT:
                    case CMD_FND_PTH_DIR_OLY_SRC:
                    if(find) param.getPathMap().put(a,p);
                    break;
                    case CMD_DEL_DIR:
                    case CMD_MOV_DIR:
                    case CMD_FND_DIR:
                    case CMD_FND_PTH_DIR_ABS:
                    case CMD_FND_PTH_DIR_RLT:
                    case CMD_FND_PTH_DIR_SRC:
                    if(find = find || matchPath(p)){
                        param.getPathDeque().push(p);
                        param.getPathMap().put(a,p);
                    }
                    break;
                    case CMD_CPY_DIR:
                    case CMD_BAK_DIR:
                    case CMD_UGD_DIR:
                    case CMD_ZIP_DIR_DEF:
                    case CMD_PAK_DIR_DEF:
                    if(find = find || matchPath(p)){
                        param.getPathDeque().push(p);
                        if(param.needCaching()) param.getPathList().add(p);
                    }
                    if(0 == p.toFile().list().length) param.getPathMap().put(a,p);
                    break;
                    case CMD_DEL_DIR_NUL:
                    if(find = find || matchPath(p)){
                        param.getPathDeque().push(p);
                        param.getPathList().add(p);
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
        private FileParam param;

        private BasicFileAttributesPathComparator(FileParam param){
            this.param = param;
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
            switch(param.getCmd()){
                case CMD_FND_SIZ_ASC:
                case CMD_FND_DIR_SIZ_ASC:
                return a1.size() > a2.size() ? 1 : -1;
                default:
                return a1.size() < a2.size() ? 1 : -1;
            }
        }
    }

    private static class PathPathComparator implements Comparator<Entry<Path,Path>>{
        @Override
        public int compare(Entry<Path,Path> e1, Entry<Path,Path> e2){
            Path p1 = e1.getKey(), p2 = e2.getKey();
            int n1 = p1.getNameCount(), n2 = p2.getNameCount();
            if(n1 == n2){
                String s1 = p1.toString(), s2 = p2.toString();
                int l1 = s1.length(), l2 = s2.length();
                return l1 == l2 ? s1.compareTo(s2) : l1 < l2 ? 1 : -1;
            }
            return n1 < n2 ? 1 : -1;
        }
    }

    private static class PathLongComparator implements Comparator<Entry<Path,Long>>{
        private FileParam param;

        private PathLongComparator(FileParam param){
            this.param = param;
        }

        @Override
        public int compare(Entry<Path,Long> e1, Entry<Path,Long> e2){
            long l1 = e1.getValue(), l2 = e2.getValue();
            return l1 == l2 ? e1.getKey().compareTo(e2.getKey()) : sort(l1,l2);
        }

        private int sort(long l1, long l2){
            switch(param.getCmd()){
                case CMD_FND_DIR_DIR_SIZ_ASC:
                case CMD_FND_DIR_OLY_SIZ_ASC:
                return l1 > l2 ? 1 : -1;
                default:
                return l1 < l2 ? 1 : -1;
            }
        }
    }

    private static class PathListComparator implements Comparator<Path>{
        private FileParam param;

        private PathListComparator(FileParam param){
            this.param = param;
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
            return param.isQueryCommand() ? (n1 > n2 ? 1 : -1) : (n1 < n2 ? 1 : -1);
        }
    }
}
