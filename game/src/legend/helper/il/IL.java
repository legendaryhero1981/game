package legend.helper.il;

import static legend.util.ValueUtil.nonEmpty;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import legend.helper.entity.Helper;

@XmlRootElement(name = "IL")
@XmlType(propOrder = {"instructions","statements"})
public class IL{
    @XmlTransient
    private ConcurrentMap<String,Helper> instructionMap = new ConcurrentHashMap<>();
    @XmlTransient
    private ConcurrentMap<String,Helper> statementMap = new ConcurrentHashMap<>();
    @XmlElementWrapper(name = "Instructions")
    @XmlElementRef
    private List<Helper> instructions = new CopyOnWriteArrayList<>();
    @XmlElementWrapper(name = "Statements")
    @XmlElementRef
    private List<Helper> statements = new CopyOnWriteArrayList<>();

    public ConcurrentMap<String,Helper> refreshInstructionMap(){
        instructionMap.clear();
        if(nonEmpty(instructions)) instructions.parallelStream().forEach(helper->instructionMap.put(helper.getName(),helper));
        return instructionMap;
    }

    public ConcurrentMap<String,Helper> refreshStatementMap(){
        statementMap.clear();
        if(nonEmpty(statements)) statements.parallelStream().forEach(helper->statementMap.put(helper.getName(),helper));
        return statementMap;
    }

    public List<Helper> getInstructions(){
        return instructions;
    }

    public List<Helper> getStatements(){
        return statements;
    }
}
