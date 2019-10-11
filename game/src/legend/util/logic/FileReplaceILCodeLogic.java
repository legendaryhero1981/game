package legend.util.logic;

import static java.util.stream.Collectors.toList;
import static legend.util.FileUtil.readFile;
import static legend.util.FileUtil.writeFile;
import static legend.util.JaxbUtil.convertToObject;
import static legend.util.JaxbUtil.convertToXml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

import legend.util.entity.ILCode;
import legend.util.entity.ILCodes;
import legend.util.intf.IFileUtil;
import legend.util.logic.intf.ILogic;
import legend.util.param.FileParam;

public class FileReplaceILCodeLogic implements IFileUtil,ILogic<Path>{
    private FileParam param;
    private ILCodes ilCodes;

    public FileReplaceILCodeLogic(FileParam param){
        this.param = param;
        this.ilCodes = convertToObject(param.getDestPath(),ILCodes.class);
    }

    @Override
    public void execute(Path path){
        List<String> datas = readFile(path,CHARSET_GBK);
        int dataSize = datas.size();
        ILCodes ilCodes = this.ilCodes.cloneValue();
        if(!ilCodes.trim().validate(dataSize)) CS.showError(ERR_FLE_REPL,new String[]{path.toString(),ilCodes.errorInfo()});
        if(MODE_NATIVE.equals(ilCodes.getMode())){
            int r = dataSize % SIZE_IL_PARTITION;
            List<Integer> partitions = new ArrayList<>();
            for(int i = SIZE_IL_PARTITION + r - 1;i < dataSize;i += SIZE_IL_PARTITION)
                partitions.add(i);
            if(SIZE_IL_HEADER < r) partitions.add(r - 1);
            List<ILCode> codes = new ArrayList<>();
            Collection<ILCode> caches = new ConcurrentLinkedQueue<>(ilCodes.getCodes().stream().filter(c->!MODE_NATIVE.equals(c.getProcessingMode())).collect(toList()));
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
                    j += l;
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
        if(!ilCodes.trim().validate(dataSize)) CS.showError(ERR_FLE_REPL,new String[]{path.toString(),ilCodes.errorInfo()});
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
    }
}
