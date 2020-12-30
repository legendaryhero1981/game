package legend.util.param;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import legend.intf.IValue;
import legend.util.intf.IFileVersion;

public class FileVersion implements IFileVersion,IValue<FileVersion>{
    private Path path;
    private boolean isFile;
    private boolean isNewest;
    private String name;
    private List<Long> versions;
    private int level;

    @Override
    public FileVersion cloneValue(){
        FileVersion fileVersion = new FileVersion();
        path = fileVersion.path;
        name = fileVersion.name;
        versions = new ArrayList<>(fileVersion.versions);
        isFile = fileVersion.isFile;
        level = fileVersion.level;
        return fileVersion;
    }

    @Override
    public Queue<Queue<FileVersion>> getSortedFileVersions(FileParam param){
        Queue<Queue<FileVersion>> fileVersionLists = new ConcurrentLinkedQueue<>();
        
        return fileVersionLists;
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
}
