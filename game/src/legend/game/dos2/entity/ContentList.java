package legend.game.dos2.entity;

import static legend.util.ValueUtil.nonEmpty;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "contentList")
public class ContentList{
    @XmlTransient
    private ConcurrentMap<String,Content> contentMap = new ConcurrentHashMap<>();
    @XmlElement(name = "content")
    private List<Content> contents;

    public ConcurrentMap<String,Content> getContentMap(){
        if(contentMap.isEmpty()) refreshContentMap();
        return contentMap;
    }

    public ConcurrentMap<String,Content> refreshContentMap(){
        if(nonEmpty(contents)) contents.parallelStream().forEach((content)->contentMap.put(content.getId(),content));
        return contentMap;
    }

    public List<Content> getContents(){
        return contents;
    }
}
