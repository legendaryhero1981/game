package legend.util.logic;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.exec;
import static legend.util.FileUtil.dealFiles;
import static legend.util.FileUtil.existsPath;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.JaxbUtil.convertToXml;
import static legend.util.MD5Util.getMD5L16;
import static legend.util.StringUtil.gsph;
import static legend.util.param.FileParam.convertParam;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import legend.util.entity.FileMerge;
import legend.util.entity.Merge;
import legend.util.entity.intf.IFileMerge;
import legend.util.logic.intf.ILogic;
import legend.util.param.FileParam;

public class FileMergeLogic implements IFileMerge,ILogic<Path>{
    private FileParam param;

    public FileMergeLogic(FileParam param){
        this.param = param;
        Path path = get(CONF_FILE_MERGE);
        if(!existsPath(path)){
            convertToXml(path,new FileMerge());
            param.getDetailOptional().ifPresent(c->CS.sl(ST_FILE_MERGE_CONF));
        }
    }

    @Override
    public void execute(Path path){
        CS.showError(ERR_CONFIG_NON,new String[]{path.toString()},()->!path.toFile().isFile());
        FileMerge fileMerge = convertToObject(path,FileMerge.class);
        CS.showError(ERR_CONFIG_INVALIDATE,new String[]{path.toString()},()->!fileMerge.trim().validate());
        String pathMd5 = getMD5L16(fileMerge.getPath() + fileMerge.getPath2() + fileMerge.getPath3());
        if(!pathMd5.equals(fileMerge.getPathMd5())) fileMerge.refreshMerges();
        fileMerge.setPathMd5(pathMd5);
        FileParam fp = new FileParam();
        fp.setCmd(CMD_FND_PTH_RLT);
        fp.setOpt(OPT_INSIDE + OPT_EXCLUDE_ROOT);
        fp.setPattern(compile(convertParam(fileMerge.getQueryRegex(),true)));
        fp.setSrcPath(get(fileMerge.getPath()));
        FileParam fp2 = fp.cloneValue(), fp3 = fp.cloneValue();
        fp2.setSrcPath(get(fileMerge.getPath2()));
        fp3.setSrcPath(get(fileMerge.getPath3()));
        asList(fp,fp2,fp3).parallelStream().forEach(p->dealFiles(p));
        final Path root = fp.getRootPath(), root2 = fp2.getRootPath(), root3 = fp3.getRootPath();
        Set<Path> pathSet = new HashSet<>(fp.getPathMap().values());
        Set<Path> pathSet2 = new HashSet<>(fp2.getPathMap().values());
        Set<Path> pathSet3 = new HashSet<>(fp3.getPathMap().values());
        List<Merge> merges = new ArrayList<>();
        ConcurrentMap<String,Merge> mergeMap = fileMerge.refreshMergeMap();
        pathSet.parallelStream().forEach(p->{
            if(!pathSet2.contains(p) || !pathSet3.contains(p)) return;
            String md2 = getMD5L16(root2.resolve(p));
            String md3 = getMD5L16(root3.resolve(p));
            if(md2.equals(md3)) return;
            String md1 = getMD5L16(md2 + md3);
            String p1 = p.toString(), key = p1.toLowerCase();
            Merge merge1 = new Merge();
            merge1.setPath(p1);
            merge1.setMd5(md1);
            Merge merge = mergeMap.computeIfAbsent(key,k->{
                merges.add(merge1);
                return merge1;
            });
            if(!merge.equals(merge1)){
                merges.add(merge1);
                mergeMap.put(key,merge1);
            }
        });
        final String mergeExecutablePath = fileMerge.getMergeExecutablePath();
        final float amount = merges.size(), scale = 1 / param.getPathMap().size();
        merges.stream().forEach(m->{
            Path p = root.resolve(m.getPath());
            String ps = p.toString();
            String ps2 = root2.resolve(m.getPath()).toString();
            String ps3 = root3.resolve(m.getPath()).toString();
            String md5 = getMD5L16(p);
            exec(gsph(EXEC_KDIFF_F3,mergeExecutablePath,ps,ps2,ps3,ps),ERR_FILE_MERGE);
            if(md5.equals(getMD5L16(p))) mergeMap.remove(m.getPath().toLowerCase());
            param.getProgressOptional().ifPresent(c->PG.update(PG.countUpdate(amount,1,scale),PROGRESS_SCALE));
        });
        fileMerge.refreshMerges();
        convertToXml(path,fileMerge);
    }
}
