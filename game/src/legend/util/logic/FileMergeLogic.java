package legend.util.logic;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static legend.util.FileUtil.dealFiles;
import static legend.util.JaxbUtil.convertToJavaBean;
import static legend.util.JaxbUtil.convertToXml;
import static legend.util.MD5Util.getMD5L16;
import static legend.util.ConsoleUtil.exec;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import legend.util.ConsoleUtil;
import legend.util.FileUtil;
import legend.util.entity.FileMerge;
import legend.util.entity.Merge;
import legend.util.intf.IProgress;
import legend.util.logic.intf.IFileMerge;
import legend.util.logic.intf.ILogic;
import legend.util.param.FileParam;

public class FileMergeLogic implements IFileMerge,ILogic<Path>{
    private FileParam param;

    public FileMergeLogic(FileParam param){
        this.param = param;
    }

    @Override
    public void execute(Path path){
        final ConsoleUtil CS = FileUtil.CS;
        final IProgress PG = FileUtil.PG;
        CS.showError(ERR_CONFIG_NON,null,()->!path.toFile().isFile());
        FileMerge fileMerge = convertToJavaBean(path,FileMerge.class);
        CS.showError(ERR_CONFIG_INVALIDATE,new String[]{FILE_CONFIG},()->!fileMerge.trim().validate());
        FileParam fp = new FileParam();
        fp.setCmd(CMD_FND_PTH_RLT);
        fp.setOpt(OPT_INSIDE + OPT_EXCLUDE_ROOT);
        fp.setSrcPath(get(fileMerge.getPath()));
        FileParam fp2 = fp.cloneValue(), fp3 = fp.cloneValue();
        fp2.setSrcPath(get(fileMerge.getPath2()));
        fp3.setSrcPath(get(fileMerge.getPath3()));
        asList(fp,fp2,fp3).parallelStream().forEach(p->dealFiles(p));
        Set<Path> pathSet = new HashSet<>(fp.getPathMap().values());
        Set<Path> pathSet2 = new HashSet<>(fp2.getPathMap().values());
        Set<Path> pathSet3 = new HashSet<>(fp3.getPathMap().values());
        List<Merge> merges = new ArrayList<>();
        ConcurrentMap<String,Merge> mergeMap = fileMerge.refreshMergeMap();
        pathSet.parallelStream().forEach(p->{
            if(!pathSet2.contains(p) || !pathSet3.contains(p)) return;
            String md2 = getMD5L16(fp2.getRootPath().resolve(p));
            String md3 = getMD5L16(fp3.getRootPath().resolve(p));
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
        final float amount = merges.size(), percent = 100 / param.getPathMap().size();
        merges.stream().forEach(m->{
            param.getProgressOptional().ifPresent(c->PG.update(PG.countUpdate(amount,1,percent),PROGRESS_SCALE));
        });
        fileMerge.refreshMerges();
        convertToXml(path,fileMerge);
    }
}
