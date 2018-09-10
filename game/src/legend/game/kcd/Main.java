package legend.game.kcd;

import static java.nio.file.Paths.get;
import static java.util.regex.Pattern.compile;
import static legend.intf.ICommon.gs;
import static legend.intf.ICommon.gsph;
import static legend.util.ConsoleUtil.CS;
import static legend.util.FileUtil.copyFile;
import static legend.util.FileUtil.dealFiles;
import static legend.util.FileUtil.deleteFile;
import static legend.util.FileUtil.existsPath;
import static legend.util.FileUtil.makeDirs;
import static legend.util.FileUtil.moveFile;
import static legend.util.FileUtil.nonEmptyDir;
import static legend.util.FileUtil.writeFile;
import static legend.util.JaxbUtil.convertToJavaBean;
import static legend.util.JaxbUtil.convertToXml;
import static legend.util.MD5Util.getMD5L16;
import static legend.util.TimeUtil.getDateTime;
import static legend.util.TimeUtil.runWithConsole;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import legend.game.kcd.entity.Value;
import legend.game.kcd.entity.local.Row;
import legend.game.kcd.entity.local.Table;
import legend.game.kcd.entity.mod.Config;
import legend.game.kcd.entity.mod.Conflict;
import legend.game.kcd.entity.mod.Kcd;
import legend.game.kcd.entity.mod.Kcd.MergeSet;
import legend.game.kcd.entity.mod.Mapping;
import legend.game.kcd.entity.mod.Merge;
import legend.game.kcd.entity.mod.Mod;
import legend.game.kcd.intf.IMain;
import legend.util.ProgressUtil;
import legend.util.intf.IFileUtil;
import legend.util.intf.IProgress;
import legend.util.param.FileParam;
import legend.util.param.SingleValue;

public final class Main implements IMain,IFileUtil{
    private static final IProgress progress;
    private static final Path kcdPath;
    private static final FileParam srcParam;
    private static FileParam destParam;
    private static Kcd kcd;
    private static Config config;
    private static Path gamePath;
    private static Path modPath;
    private static Path mergePath;
    private static List<Mod> mods;
    private static List<Mapping> uniques;
    private static List<Merge> merges;
    private static List<Conflict> conflicts;
    private static MergeSet mergeSet;
    private static ConcurrentMap<String,List<Path>> pathsMap;
    private static ConcurrentMap<String,Mod> modMap;
    private static ConcurrentMap<String,Mapping> uniqueMap;
    private static ConcurrentMap<String,Merge> mergeMap;
    private static ConcurrentMap<String,Conflict> conflictMap;
    static{
        progress = ProgressUtil.ConsoleProgress();
        kcdPath = get(KCD_FILE_CONFIG);
        srcParam = new FileParam();
        srcParam.setOpt(OPT_INSIDE);
    }

    public static void main(String[] args){
        runWithConsole(t->runCmd(args),args,HELP_KCD);
    }

    public static void runCmd(String[] args){
        dealParam(args);
        switch(args[0]){
            case KCD_LOC_MRG:
            case KCD_LOC_MRG_A:
            case KCD_LOC_MRG_U:
            progress.runUntillFinish(Main::mergeLocalization);
            break;
            case KCD_LOC_CMP:
            case KCD_LOC_CMP_A:
            case KCD_LOC_CMP_U:
            progress.runUntillFinish(Main::compareLocalization);
            break;
            case KCD_LOC_DBG:
            progress.runUntillFinish(Main::debugLocalization);
            break;
            case KCD_LOC_RLS:
            progress.runUntillFinish(Main::releaseLocalization);
            break;
            case KCD_MOD_CRT:
            progress.runUntillFinish(Main::createMod);
            break;
            case KCD_MOD_PAK:
            progress.runUntillFinish(Main::pakMod);
            break;
            case KCD_MOD_UNPAK:
            progress.runUntillFinish(Main::unpakMod);
            break;
            case KCD_MOD_MRG_A:
            progress.runUntillFinish(Main::mergeAddition);
            break;
            case KCD_MOD_MRG_C:
            progress.runUntillFinish(Main::mergeConflict);
            break;
            case KCD_MOD_MRG_F:
            progress.runUntillFinish(Main::mergeFull);
            break;
            case KCD_MOD_MRG_O:
            progress.runUntillFinish(Main::mergeOrder);
            break;
            case KCD_MOD_MRG_U:
            progress.runUntillFinish(Main::mergeUnique);
        }
    }

    private static void loadKcd(){
        if(nonEmpty(kcd)) return;
        CS.showError(ERR_KCD_NON,null,()->!kcdPath.toFile().isFile());
        kcd = convertToJavaBean(kcdPath,Kcd.class);
        config = kcd.getConfig();
        gamePath = get(config.getGamePath());
        modPath = get(config.getModPath());
        mergePath = get(config.getMergePath());
        mods = kcd.getMods();
        uniques = kcd.getUniques();
        merges = kcd.getMerges();
        conflicts = kcd.getConflicts();
        mergeSet = kcd.getMergeSet();
        pathsMap = kcd.getPathsMap();
        modMap = kcd.getModMap();
        uniqueMap = kcd.getUniqueMap();
        mergeMap = kcd.getMergeMap();
        conflictMap = kcd.getConflictMap();
        CS.showError(ERR_KCD_NUL_CFG,null,()->config.trim().validate());
        CS.showError(ERR_KCD_MOD_PATH,null,()->!nonEmptyDir(modPath));
        CS.showError(ERR_KCD_NUL_MOD,null,()->mods.parallelStream().anyMatch(mod->!mod.trim().validate()));
    }

    private static void saveKcd(){
        convertToXml(kcdPath,kcd);
    }

    private static void createMod(IProgress progress){
        progress.reset(1,1,1);
        saveKcd();
    }

    private static void pakMod(IProgress progress){
        progress.reset(100,40,100);
        loadKcd();
        Path gameModPath = gamePath.resolve(MOD_MODS);
        Path gameMergePath = gameModPath.resolve(MOD_MERGE);
        Path conflictPath = mergePath.resolve(MOD_CONFLICT);
        srcParam.setCmd(CMD_DEL_DIR);
        srcParam.setPattern(compile(REG_ANY));
        srcParam.setSrcPath(gameModPath);
        dealFile(srcParam);
        if(mods.isEmpty()){
            saveKcd();
            return;
        }
        merges.parallelStream().forEach(merge->copyFile(getRepairPath(merge),getMergePath(merge)));
        srcParam.setCmd(CMD_PAK_DEF);
        srcParam.setSrcPath(mergePath.resolve(MOD_DATA));
        srcParam.setDestPath(gameMergePath.resolve(MOD_DATA));
        srcParam.setZipName(PAK_MERGE);
        srcParam.setZipLevel(1);
        dealFile(srcParam);
        progress.update(40);
        srcParam.setSrcPath(mergePath.resolve(MOD_LOCAL).resolve(MOD_CHS));
        srcParam.setDestPath(gameMergePath.resolve(MOD_LOCAL));
        srcParam.setZipName(PAK_CHINESES);
        dealFile(srcParam);
        progress.update(20);
        srcParam.setSrcPath(srcParam.getSrcPath().resolveSibling(MOD_ENG));
        srcParam.setZipName(PAK_ENGLISH);
        dealFile(srcParam);
        progress.update(20);
        conflicts.parallelStream().forEach(conflict->{
            FileParam param = srcParam.cloneValue();
            Path path = conflictPath.resolve(conflict.getMod()).resolve(MOD_DATA);
            param.setSrcPath(path);
            param.setDestPath(gameModPath.resolve(conflict.getMod()).resolve(MOD_DATA));
            param.setZipName(conflict.getMod() + EXT_PAK);
            dealFile(param);
            param.setSrcPath(path.resolveSibling(MOD_LOCAL).resolve(MOD_CHS));
            param.setDestPath(gameModPath.resolve(conflict.getMod()).resolve(MOD_LOCAL));
            param.setZipName(PAK_CHINESES);
            dealFile(param);
            param.setSrcPath(path.resolveSibling(MOD_LOCAL).resolve(MOD_ENG));
            param.setZipName(PAK_ENGLISH);
            dealFile(param);
        });
        progress.update(20);
        mergeOrder(progress);
    }

    private static void unpakMod(IProgress progress){
        progress.reset(100,10,100);
        loadKcd();
        // 获得MOD目录列表
        srcParam.setCmd(CMD_FND_DIR_OLY);
        srcParam.setPattern(compile(REG_ANY));
        srcParam.setSrcPath(modPath);
        srcParam.setLevel(1);
        dealFiles(srcParam);
        progress.update(10);
        Collection<Path> paths = srcParam.getPathMap().values();
        paths.remove(modPath);
        if(paths.isEmpty()){
            kcd.clearCache();
            srcParam.setLevel(Integer.MAX_VALUE);
            return;
        }
        CS.showError(ERR_EXISTS_MERGE,new String[]{modPath.toString()},()->paths.parallelStream().anyMatch(path->MOD_MERGE.equalsIgnoreCase(path.getFileName().toString())));
        srcParam.setCmd(CMD_DELETE);
        srcParam.setPattern(compile(REG_MOD_NOT_PAK));
        srcParam.setLevel(Integer.MAX_VALUE);
        paths.parallelStream().forEach(p->{
            // 删除MOD目录中所有非.PAK文件
            FileParam param = srcParam.cloneValue();
            param.setSrcPath(p);
            dealFile(param);
            // 自动将.PAK文件移动到规定的MOD目录中
            param.setCmd(CMD_FIND);
            param.setPattern(compile(REG_MOD_PAK));
            dealFiles(param);
            param.getPathMap().values().parallelStream().forEach(p1->{
                final String name = p1.getFileName().toString();
                if(name.matches(REG_MOD_LOCAL)){
                    if(PAK_CHINESES.equalsIgnoreCase(name)) moveFile(p1,p.resolve(MOD_LOCAL_CHS).resolve(name));
                    else if(PAK_ENGLISH.equalsIgnoreCase(name)) moveFile(p1,p.resolve(MOD_LOCAL_ENG).resolve(name));
                    else deleteFile(p1);
                }else if(!MOD_DATA.equalsIgnoreCase(p1.getParent().getFileName().toString())) moveFile(p1,p.resolve(MOD_DATA).resolve(name));
            });
            param.clearCache();
            // 删除MOD目录中所有空目录
            param.setCmd(CMD_DEL_DIR_NUL);
            param.setPattern(compile(REG_ANY));
            dealFiles(param);
            if(existsPath(p)){
                Mod mod = new Mod();
                mod.setMod(p.getFileName().toString());
                modMap.put(mod.getMod(),mod);
            }
        });
        progress.update(30);
        // 解包MOD目录中所有.PAK文件
        srcParam.clearCache();
        srcParam.setCmd(CMD_PAK_INF);
        srcParam.setPattern(compile(REG_MOD_PAK));
        dealFile(srcParam);
        progress.update(60);
    }

    private static void mergeAddition(IProgress progress){
        loadKcd();
        unpakMod(progress);
        mods.parallelStream().forEach(m->modMap.replace(m.getMod(),m));
        dealMerge(progress);
        pakMod(progress);
    }

    private static void mergeFull(IProgress progress){
        loadKcd();
        kcd.clearCache();
        unpakMod(progress);
        dealMerge(progress);
        pakMod(progress);
    }

    private static void mergeOrder(IProgress progress){
        loadKcd();
        if(mods.isEmpty()) return;
        List<String> lines = new CopyOnWriteArrayList<>();
        mods.stream().sorted(new ModComparator()).forEach(mod->{
            if(!MOD_ORDER_INGNORE.equals(mod.getOrder())) lines.add(mod.getMod());
        });
        writeFile(get(config.getGamePath()).resolve(MOD_MODS).resolve(KCD_FILE_ORDER),lines);
        progress.update(20);
        saveKcd();
        progress.update(80);
    }

    private static void mergeConflict(IProgress progress){
        loadKcd();
        Path conflictPath = mergePath.resolve(MOD_CONFLICT);
        srcParam.setCmd(CMD_DEL_DIR);
        srcParam.setPattern(compile(REG_ANY));
        srcParam.setSrcPath(conflictPath);
        dealFile(srcParam);
        progress.update(10);
        conflicts.stream().forEach(conflict->{
            conflict.getMappings().parallelStream().forEach(mapping->{
                Merge merge = mergeMap.get(mapping.getPath());
                if(isEmpty(merge)){
                    merge = new Merge();
                    merge.setPath(mapping.getPath());
                    mergeMap.put(mapping.getPath(),merge);
                }
                merge.getMappings().add(mapping);
            });
        });
        modMap = kcd.refreshModMap();
        dealConflict(progress,0.8f);
        saveKcd();
        progress.update(10);
    }

    private static void mergeUnique(IProgress progress){
        loadKcd();
        int size = uniques.size();
        uniques.parallelStream().forEach(mapping->{
            Path path = getModPath(mapping);
            mapping.setMd5(getMD5L16(path));
            copyFile(path,getMergePath(mapping));
            progress.update(progress.countUpdate(size,1),0.9f);
        });
        saveKcd();
        progress.update(10);
    }

    private static void mergeLocalization(IProgress progress){
        dealFiles(srcParam);
        dealFiles(destParam);
        ConcurrentMap<BasicFileAttributes,Path> srcs = srcParam.getPathMap();
        ConcurrentMap<BasicFileAttributes,Path> dests = destParam.getPathMap();
        CS.showError(ERR_NOT_FIND,null,()->isEmpty(srcs) || isEmpty(dests));
        progress.reset(srcs.size(),PROGRESS_POSITION);
        Optional<String> optional = Optional.of(srcParam.getOpt());
        SingleValue<Boolean> find = new SingleValue<>(false);
        srcs.entrySet().parallelStream().forEach(srcEntry->{
            Path src = srcEntry.getValue();
            dests.entrySet().parallelStream().forEach(destEntry->{
                Path dest = destEntry.getValue();
                if(src.getFileName().equals(dest.getFileName())){
                    find.set(true);
                    Table srcTable = convertToJavaBean(src,Table.class);
                    Table destTable = convertToJavaBean(dest,Table.class);
                    if(isEmpty(srcTable) || isEmpty(destTable)) return;
                    List<Object> destRows = destTable.getRows();
                    ConcurrentMap<String,Row> rowMap = destTable.getRowMap();
                    List<Object> mods = new CopyOnWriteArrayList<>();
                    List<Object> adds = new CopyOnWriteArrayList<>();
                    final String modNote = V_UPD + gs(2) + getDateTime();
                    final String addNote = V_ADD + gs(2) + getDateTime();
                    Value mod = new Value();
                    Value add = new Value();
                    mod.setText(modNote);
                    add.setText(addNote);
                    mods.add(mod);
                    adds.add(add);
                    srcTable.getRowMap().entrySet().stream().forEach(entry->{
                        String key = entry.getKey();
                        Row value = entry.getValue();
                        if(rowMap.containsKey(key)){
                            mods.add(value);
                            destRows.remove(rowMap.get(key));
                        }else adds.add(value);
                    });
                    if(1 < mods.size()){
                        mod = new Value();
                        mod.setText(modNote);
                        mods.add(mod);
                        optional.filter(s->!KCD_LOC_MRG_A.equals(s)).ifPresent(s->destRows.addAll(mods));
                    }
                    if(1 < adds.size()){
                        add = new Value();
                        add.setText(addNote);
                        adds.add(add);
                        optional.filter(s->!KCD_LOC_MRG_U.equals(s)).ifPresent(s->destRows.addAll(adds));
                    }
                    convertToXml(makeDirs(srcParam.getDestPath().resolve(dest.getFileName())),destTable,true);
                }
            });
            progress.update(1,PROGRESS_SCALE);
        });
        CS.showError(ERR_NOT_FIND,null,()->!find.get());
    }

    private static void compareLocalization(IProgress progress){
        dealFiles(srcParam);
        dealFiles(destParam);
        ConcurrentMap<BasicFileAttributes,Path> srcs = srcParam.getPathMap();
        ConcurrentMap<BasicFileAttributes,Path> dests = destParam.getPathMap();
        CS.showError(ERR_NOT_FIND,null,()->isEmpty(srcs) || isEmpty(dests));
        progress.reset(srcs.size(),PROGRESS_POSITION);
        Optional<String> optional = Optional.of(srcParam.getOpt());
        SingleValue<Boolean> find = new SingleValue<>(false);
        srcs.entrySet().forEach(srcEntry->{
            Path src = srcEntry.getValue();
            dests.entrySet().parallelStream().forEach(destEntry->{
                Path dest = destEntry.getValue();
                if(src.getFileName().equals(dest.getFileName())){
                    find.set(true);
                    Table srcTable = convertToJavaBean(src,Table.class);
                    Table destTable = convertToJavaBean(dest,Table.class);
                    if(isEmpty(srcTable) || isEmpty(destTable)) return;
                    List<Object> destRows = destTable.getRows();
                    ConcurrentMap<String,Row> rowMap = destTable.getRowMap();
                    List<Object> mods = new CopyOnWriteArrayList<>();
                    List<Object> adds = new CopyOnWriteArrayList<>();
                    final String modNote = V_UPD + gs(2) + getDateTime();
                    final String addNote = V_ADD + gs(2) + getDateTime();
                    Value mod = new Value();
                    Value add = new Value();
                    mod.setText(modNote);
                    add.setText(addNote);
                    mods.add(mod);
                    adds.add(add);
                    srcTable.getRowMap().entrySet().stream().forEach(entry->{
                        String key = entry.getKey();
                        Row value = entry.getValue();
                        if(rowMap.containsKey(key)){
                            Row row = rowMap.get(key);
                            row.getCells().add(value.getCells().get(value.getCells().size() - 1));
                            mods.add(row);
                        }else adds.add(value);
                    });
                    destRows.clear();
                    if(1 < mods.size()){
                        mod = new Value();
                        mod.setText(modNote);
                        mods.add(mod);
                        optional.filter(s->!KCD_LOC_CMP_A.equals(s)).ifPresent(s->destRows.addAll(mods));
                    }
                    if(1 < adds.size()){
                        add = new Value();
                        add.setText(addNote);
                        adds.add(add);
                        optional.filter(s->!KCD_LOC_CMP_U.equals(s)).ifPresent(s->destRows.addAll(adds));
                    }
                    convertToXml(makeDirs(srcParam.getDestPath().resolve(dest.getFileName())),destTable,true);
                }
            });
            progress.update(1,PROGRESS_SCALE);
        });
        CS.showError(ERR_NOT_FIND,null,()->!find.get());
    }

    private static void debugLocalization(IProgress progress){
        dealFiles(srcParam);
        ConcurrentMap<BasicFileAttributes,Path> srcs = srcParam.getPathMap();
        CS.showError(ERR_NOT_FIND,null,()->isEmpty(srcs));
        progress.reset(srcs.size(),PROGRESS_POSITION);
        srcs.entrySet().parallelStream().forEach(srcEntry->{
            Path src = srcEntry.getValue();
            srcParam.getFilesCount().set(0);
            Table srcTable = convertToJavaBean(src,Table.class,true);
            srcTable.getRowMap().values().parallelStream().forEach(row->{
                Value cell = row.getCells().get(2);
                String value = FLAG_DEBUG + srcParam.getFilesCount().incrementAndGet() + FLAG_DEBUG + cell.getText();
                cell.setText(value);
            });
            convertToXml(makeDirs(srcParam.getDestPath().resolve(src.getFileName())),srcTable,true);
            progress.update(1,PROGRESS_SCALE);
        });
    }

    private static void releaseLocalization(IProgress progress){
        dealFiles(srcParam);
        ConcurrentMap<BasicFileAttributes,Path> srcs = srcParam.getPathMap();
        CS.showError(ERR_NOT_FIND,null,()->isEmpty(srcs));
        progress.reset(srcs.size(),PROGRESS_POSITION);
        Pattern pattern = Pattern.compile(REG_RELEASE);
        srcs.entrySet().parallelStream().forEach(srcEntry->{
            Path src = srcEntry.getValue();
            Table srcTable = convertToJavaBean(src,Table.class,true);
            srcTable.getRowMap().values().parallelStream().forEach(row->{
                Value cell = row.getCells().get(2);
                String value = cell.getText();
                cell.setText(pattern.matcher(value).replaceFirst(""));
            });
            convertToXml(makeDirs(srcParam.getDestPath().resolve(src.getFileName())),srcTable,true);
            progress.update(1,PROGRESS_SCALE);
        });
    }

    private static void dealParam(String[] args){
        try{
            switch(args[0]){
                case KCD_LOC_MRG:
                case KCD_LOC_MRG_A:
                case KCD_LOC_MRG_U:
                case KCD_LOC_CMP:
                case KCD_LOC_CMP_A:
                case KCD_LOC_CMP_U:
                srcParam.setLevel(1);
                srcParam.setCmd(CMD_FIND);
                srcParam.setOpt(args[0]);
                srcParam.setPattern(compile(args[1]));
                srcParam.setDestPath(get(args[4]));
                destParam = srcParam.cloneValue();
                srcParam.setSrcPath(get(args[2]));
                destParam.setSrcPath(get(args[3]));
                break;
                case KCD_LOC_DBG:
                case KCD_LOC_RLS:
                srcParam.setLevel(1);
                srcParam.setPattern(compile(args[1]));
                srcParam.setSrcPath(get(args[2]));
                srcParam.setDestPath(get(args[3]));
                break;
                case KCD_MOD_CRT:
                kcd = new Kcd();
                config = kcd.getConfig();
                config.setGamePath(args[1]);
                config.setModPath(args[2]);
                config.setMergePath(args[3]);
                config.setMergeExecutablePath(args[4]);
                case KCD_MOD_PAK:
                case KCD_MOD_UNPAK:
                case KCD_MOD_MRG_A:
                case KCD_MOD_MRG_C:
                case KCD_MOD_MRG_F:
                case KCD_MOD_MRG_O:
                case KCD_MOD_MRG_U:
                break;
                default:
                CS.showError(ERR_ARG_ANLS,new String[]{ERR_ARG_FMT});
            }
        }catch(Exception e){
            CS.showError(ERR_CMD_EXEC,new String[]{e.toString()});
        }
    }

    private static void dealFile(FileParam param){
        if(nonEmptyDir(param.getSrcPath())){
            dealFiles(param);
            param.clearCache();
        }
    }

    private static void dealMerge(IProgress progress){
        progress.reset(100,10,100);
        dealUnique(progress,0.6f);
        dealConflict(progress,0.4f);
        mods.clear();
        if(!modMap.isEmpty()){
            Mod mod = new Mod();
            mod.setMod(MOD_MERGE);
            mod.setDesc(MOD_MERGE_DESC);
            mod.setOrder(MOD_ORDER_MERGE);
            mods.add(mod);
            mods.addAll(modMap.values());
        }
    }

    private static void dealUnique(IProgress progress, float scale){
        if(mods.isEmpty()){
            srcParam.setCmd(CMD_DEL_DIR);
            srcParam.setPattern(compile(REG_ANY));
            srcParam.setSrcPath(mergePath);
            dealFile(srcParam);
            if(modMap.isEmpty()) return;
        }
        progress.update(10,scale);
        srcParam.setCmd(CMD_FIND);
        srcParam.setPattern(compile(REG_MOD_NOT_PAK));
        modMap.keySet().stream().forEach(mod->{
            FileParam param = srcParam.cloneValue();
            param.setSrcPath(modPath.resolve(mod));
            dealFiles(param);
            param.getPathMap().values().parallelStream().forEach(p->{
                String key = param.getSrcPath().relativize(p).toString().toLowerCase();
                List<Path> value = pathsMap.get(key);
                if(isEmpty(value)){
                    value = new CopyOnWriteArrayList<>();
                    pathsMap.put(key,value);
                }
                value.add(modPath.relativize(p));
            });
            param.clearCache();
        });
        progress.update(20,scale);
        ConcurrentMap<Path,Path> pathMap = new ConcurrentHashMap<>();
        pathsMap.entrySet().parallelStream().forEach(entry->{
            String key = entry.getKey();
            List<Path> value = entry.getValue();
            Path path = value.get(0);
            if(1 == value.size()){
                pathMap.put(modPath.resolve(path),mergePath.resolve(key));
                Mapping mapping = getMapping(path);
                uniqueMap.put(mapping.getPath(),mapping);
            }else{
                Merge merge = new Merge();
                merge.setPath(key);
                value.parallelStream().forEach(p->merge.getMappings().add(getMapping(p)));
                mergeMap.put(key,merge);
            }
        });
        if(!merges.isEmpty()){
            List<Merge> oldMerges = new CopyOnWriteArrayList<>(merges);
            mergeSet = kcd.refreshMergeSet(mergeMap.values());
            merges.clear();
            oldMerges.parallelStream().forEach(merge->{
                if(mergeSet.contains(merge)) merges.add(merge);
                else deleteFile(getRepairPath(merge));
            });
        }
        progress.update(50,scale);
        pathMap.entrySet().parallelStream().forEach(entry->copyFile(entry.getKey(),entry.getValue()));
        pathMap.clear();
        uniques.clear();
        uniques.addAll(uniqueMap.values());
        progress.update(20,scale);
    }

    private static void dealConflict(IProgress progress, float scale){
        if(mergeMap.isEmpty()) return;
        String mergeExecutablePath = config.getMergeExecutablePath();
        if(!merges.isEmpty()) mergeSet = kcd.refreshMergeSet(merges);
        int size = mergeMap.values().size();
        mergeMap.values().stream().forEach(merge->{
            if(mergeSet.contains(merge)) return;
            Path path = makeDirs(getRepairPath(merge));
            List<Mapping> mappings = merge.getMappings();
            ConcurrentMap<String,Mapping> mappingMap = new ConcurrentHashMap<>();
            merge.getMappings().stream().forEach(mapping->mappingMap.putIfAbsent(mapping.getMd5(),mapping));
            if(1 < mappingMap.size()){
                if(mappings.size() != mappingMap.size()){
                    mappings.clear();
                    mappings.addAll(mappingMap.values());
                }
                String[] md5 = new String[]{"",""};
                for(int i = 0,j = 0,l = mappings.size();i < l;j = i++){
                    if(existsPath(path)){
                        md5[0] = getMD5L16(path);
                        if(1 == (l - i) % 2) exec(gsph(EXEC_KDIFF_F2,mergeExecutablePath,getModPath(mappings.get(i)).toString(),path.toString(),path.toString()));
                        else exec(gsph(EXEC_KDIFF_F3,mergeExecutablePath,getModPath(mappings.get(i)).toString(),getModPath(mappings.get(++i)).toString(),path.toString(),path.toString()));
                        md5[1] = getMD5L16(path);
                        if(md5[0].equals(md5[1])){
                            i = j;
                            continue;
                        }
                    }else{
                        if(2 == l) exec(gsph(EXEC_KDIFF_F2,mergeExecutablePath,getModPath(mappings.get(i)).toString(),getModPath(mappings.get(++i)).toString(),path.toString()));
                        else if(2 < l) exec(gsph(EXEC_KDIFF_F3,mergeExecutablePath,getModPath(mappings.get(i)).toString(),getModPath(mappings.get(++i)).toString(),getModPath(mappings.get(++i)).toString(),path.toString()));
                        if(!existsPath(path)) break;
                    }
                }
            }else copyFile(getModPath(mappings.get(0)),path);
            if(existsPath(path)) merges.add(merge);
            else merge.getMappings().parallelStream().forEach(mapping->{
                String mod = mapping.getMod();
                Conflict conflict = conflictMap.get(mod);
                if(isEmpty(conflict)){
                    conflict = new Conflict();
                    conflict.setMod(mod);
                    conflictMap.put(mod,conflict);
                }
                conflict.getMappings().add(mapping);
            });
            progress.update(progress.countUpdate(size,1,80),scale);
        });
        modMap.entrySet().parallelStream().forEach(entry->{
            String key = entry.getKey();
            Mod mod = entry.getValue();
            if(conflictMap.containsKey(key)){
                if(MOD_ORDER_INGNORE.equals(mod.getOrder())){
                    mod.setOrder(MOD_ORDER_CONFLICT);
                    mod.setDesc("");
                }
            }else if(!MOD_MERGE.equalsIgnoreCase(key)){
                mod.setOrder(MOD_ORDER_INGNORE);
                mod.setDesc(MOD_INGNORE_DESC);
            }
        });
        srcParam.setCmd(CMD_DEL_DIR);
        srcParam.setPattern(compile(REG_ANY));
        srcParam.setSrcPath(mergePath.resolve(MOD_CONFLICT));
        dealFile(srcParam);
        conflicts.clear();
        conflicts.addAll(conflictMap.values());
        conflicts.parallelStream().forEach(conflict->conflict.getMappings().parallelStream().forEach(mapping->copyFile(getModPath(mapping),getConflictPath(mapping))));
        progress.update(20,scale);
    }

    private static void exec(String cmd){
        try{
            Runtime.getRuntime().exec(cmd).waitFor();
        }catch(InterruptedException | IOException e){
            CS.showError(ERR_KCD_MERGE,new String[]{e.toString()});
        }
    }

    private static Mapping getMapping(Path path){
        Mapping mapping = new Mapping();
        mapping.setMod(path.getName(0).toString());
        mapping.setPath(path.getName(0).relativize(path).toString().toLowerCase());
        mapping.setMd5(getMD5L16(modPath.resolve(path)));
        return mapping;
    }

    private static Path getConflictPath(Mapping mapping){
        return mergePath.resolve(MOD_CONFLICT).resolve(mapping.getMod()).resolve(mapping.getPath());
    }

    private static Path getMergePath(Merge merge){
        return mergePath.resolve(merge.getPath());
    }

    private static Path getMergePath(Mapping mapping){
        return mergePath.resolve(mapping.getPath());
    }

    private static Path getRepairPath(Merge merge){
        return mergePath.resolve(MOD_REPAIR).resolve(merge.getPath());
    }

    private static Path getModPath(Mapping mapping){
        return modPath.resolve(mapping.getMod()).resolve(mapping.getPath());
    }

    private static class ModComparator implements Comparator<Mod>{
        @Override
        public int compare(Mod mod1, Mod mod2){
            int o1 = Integer.parseInt(mod1.getOrder()), o2 = Integer.parseInt(mod2.getOrder());
            return o1 == o2 ? mod1.getMod().compareTo(mod2.getMod()) : o1 > o2 ? 1 : -1;
        }
    }
}
