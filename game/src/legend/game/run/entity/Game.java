package legend.game.run.entity;

import static legend.util.StringUtil.gs;
import static legend.util.ValueUtil.isEmpty;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import legend.game.run.intf.IMain;
import legend.util.adapter.CDataAdapter;

@XmlRootElement(name = "Game")
@XmlType(propOrder = {"comment","name","id","path","exe","args","priority","icon","agentExecutablePath","agentArgs","before","after","beforeWait","afterWait","watchWait","watches"})
public class Game implements IMain{
    @XmlElement
    private String comment = "";
    @XmlElement
    private String name = "";
    @XmlElement
    private String id = "";
    @XmlElement
    private String path = "";
    @XmlElement
    private String exe = "";
    @XmlElement
    private String args = "";
    @XmlElement
    private String priority = PRIORITY_HIGH;
    @XmlElement
    private String icon = "";
    @XmlElement
    private String agentExecutablePath = "";
    @XmlElement
    private String agentArgs = "";
    @XmlJavaTypeAdapter(CDataAdapter.class)
    @XmlElement
    private String before = "";
    @XmlJavaTypeAdapter(CDataAdapter.class)
    @XmlElement
    private String after = "";
    @XmlElement
    private String beforeWait = WAIT_TIME;
    @XmlElement
    private String afterWait = WAIT_TIME;
    @XmlElement
    private String watchWait = WAIT_TIME;
    @XmlElement(name = "watch")
    private List<String> watches;

    public Game(){
        watches = new CopyOnWriteArrayList<>();
        watches.add("");
    }

    public boolean validate(){
        return isEmpty(id) || isEmpty(name) || isEmpty(path) || isEmpty(exe);
    }

    public Game trim(){
        comment = comment.trim();
        name = name.trim();
        id = id.trim();
        path = path.trim();
        exe = exe.trim();
        args = args.trim();
        priority = priority.trim();
        icon = icon.trim();
        agentExecutablePath = agentExecutablePath.trim();
        agentArgs = agentArgs.trim();
        before = before.trim();
        after = after.trim();
        beforeWait = beforeWait.trim();
        afterWait = afterWait.trim();
        watchWait = watchWait.trim();
        List<String> watchList = new CopyOnWriteArrayList<>();
        watches.parallelStream().forEach(watch->watchList.add(watch.trim()));
        watches = watchList;
        return this;
    }

    @Override
    public String toString(){
        return id + gs("\t",id.length() < 8 ? 2 : 1) + name;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getExe(){
        return exe;
    }

    public void setExe(String exe){
        this.exe = exe;
    }

    public String getArgs(){
        return args;
    }

    public void setArgs(String args){
        this.args = args;
    }

    public String getPriority(){
        return priority;
    }

    public void setPriority(String priority){
        this.priority = priority;
    }

    public String getIcon(){
        return icon;
    }

    public void setIcon(String icon){
        this.icon = icon;
    }

    public String getAgentExecutablePath(){
        return agentExecutablePath;
    }

    public void setAgentExecutablePath(String agentExecutablePath){
        this.agentExecutablePath = agentExecutablePath;
    }

    public String getAgentArgs(){
        return agentArgs;
    }

    public void setAgentArgs(String agentArgs){
        this.agentArgs = agentArgs;
    }

    public String getBefore(){
        return before;
    }

    public void setBefore(String before){
        this.before = before;
    }

    public String getAfter(){
        return after;
    }

    public void setAfter(String after){
        this.after = after;
    }

    public String getBeforeWait(){
        return beforeWait;
    }

    public void setBeforeWait(String beforeWait){
        this.beforeWait = beforeWait;
    }

    public String getAfterWait(){
        return afterWait;
    }

    public void setAfterWait(String afterWait){
        this.afterWait = afterWait;
    }

    public String getWatchWait(){
        return watchWait;
    }

    public void setWatchWait(String watchWait){
        this.watchWait = watchWait;
    }

    public List<String> getWatches(){
        return watches;
    }

    public void setWatches(List<String> watches){
        this.watches = watches;
    }
}
