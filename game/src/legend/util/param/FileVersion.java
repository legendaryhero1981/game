package legend.util.param;

import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import legend.intf.IValue;
import legend.util.intf.IFileVersion;

public class FileVersion implements IFileVersion<FileParam,FileVersion>,IValue<FileVersion>{
    private Path path;
    private boolean isFile;
    private boolean isNewest;
    private String name;
    private List<Long> versions;
    private long size;
    private int level;

    @Override
    public FileVersion cloneValue(){
        FileVersion fileVersion = new FileVersion();
        fileVersion.path = path;
        fileVersion.isFile = isFile;
        fileVersion.isNewest = isNewest;
        fileVersion.name = name;
        fileVersion.versions = new ArrayList<>(versions);
        fileVersion.size = size;
        fileVersion.level = level;
        return fileVersion;
    }

    @Override
    public List<List<FileVersion>> getSortedFileVersions(FileParam param){
        Queue<FileVersion> fileVersionQueue = new ConcurrentLinkedQueue<>();
        Pattern pattern = compile(nonEmpty(param.getSplit()) ? gsph(REG_FILE_VER,param.getSplit()) : REG_FILE_VER_DEF);
        param.getPathMap().entrySet().parallelStream().forEach(e1->{
            if(param.getPathMap().entrySet().parallelStream().filter(e->e.getKey().isDirectory()).allMatch(e2->e1.getValue().equals(e2.getValue()) || !e1.getValue().startsWith(e2.getValue()))){
                FileVersion fileVersion = generateFileVersion(e1,pattern);
                if(nonEmpty(fileVersion)) fileVersionQueue.add(fileVersion);
            }
        });
        Map<String,Queue<FileVersion>> fileVersionsMap = new ConcurrentHashMap<>();
        fileVersionQueue.parallelStream().forEach(fv->fileVersionsMap.computeIfAbsent(fv.name,s->new ConcurrentLinkedQueue<>()).add(fv));
        fileVersionsMap.entrySet().stream().filter(e->1 == e.getValue().size()).flatMap(e->of(e.getKey())).forEach(k->fileVersionsMap.remove(k));
        param.getFilesCount().set(0);
        param.getDirsCount().set(0);
        List<List<FileVersion>> fileVersionLists = new ArrayList<>();
        fileVersionsMap.entrySet().stream().sorted(new StringQueueComparator(false)).forEach(e->{
            List<FileVersion> fileVersions = e.getValue().parallelStream().sorted(new FileVersionComparator(false)).collect(toList());
            fileVersionLists.add(fileVersions);
            fileVersions.get(0).isNewest = true;
            fileVersions.parallelStream().forEach(fileVersion->{
                if(fileVersion.isFile){
                    param.meetFilesSize(fileVersion.size);
                    param.getFilesCount().incrementAndGet();
                }else param.getDirsCount().incrementAndGet();
            });
        });
        return fileVersionLists;
    }

    private FileVersion generateFileVersion(Entry<BasicFileAttributes,Path> entry, Pattern pattern){
        Matcher matcher = pattern.matcher(entry.getValue().getFileName().toString());
        if(!matcher.matches() || isEmpty(matcher.group(2))) return null;
        FileVersion fileVersion = new FileVersion();
        fileVersion.isFile = entry.getKey().isRegularFile();
        fileVersion.size = entry.getKey().size();
        fileVersion.path = entry.getValue();
        fileVersion.level = fileVersion.path.getNameCount();
        fileVersion.name = fileVersion.path.getParent().resolve(matcher.group(1)).toString();
        fileVersion.versions = new ArrayList<>();
        for(matcher = PTRN_NUM.matcher(matcher.group(2));matcher.find();fileVersion.versions.add(Long.parseLong(matcher.group())));
        return fileVersion;
    }

    @Override
    public String toString(){
        return path.toString();
    }

    public Path getPath(){
        return path;
    }

    public boolean isFile(){
        return isFile;
    }

    public boolean isNewest(){
        return isNewest;
    }

    public static class StringQueueComparator implements Comparator<Entry<String,Queue<FileVersion>>>{
        private boolean order;

        public StringQueueComparator(boolean ascending){
            order = ascending;
        }

        @Override
        public int compare(Entry<String,Queue<FileVersion>> e1, Entry<String,Queue<FileVersion>> e2){
            int n1 = e1.getValue().peek().level, n2 = e2.getValue().peek().level;
            String s1 = e1.getKey(), s2 = e2.getKey();
            if(n1 == n2){
                int l1 = s1.length(), l2 = s2.length();
                return l1 == l2 ? s1.compareTo(s2) : sort(l1,l2);
            }
            return sort(n1,n2);
        }

        private int sort(int n1, int n2){
            return order ? (n1 > n2 ? 1 : -1) : (n1 < n2 ? 1 : -1);
        }
    }

    public static class FileVersionComparator implements Comparator<FileVersion>{
        private boolean order;

        public FileVersionComparator(boolean ascending){
            order = ascending;
        }

        @Override
        public int compare(FileVersion fv1, FileVersion fv2){
            List<Long> vs1 = fv1.versions, vs2 = fv2.versions;
            int i = 0;
            for(;i < vs1.size() && i < vs2.size() && vs1.get(i) == vs2.get(i);i++);
            return i == vs1.size() || i == vs2.size() ? 0 : order ? (vs1.get(i) > vs2.get(i) ? 1 : -1) : (vs1.get(i) < vs2.get(i) ? 1 : -1);
        }
    }
}
