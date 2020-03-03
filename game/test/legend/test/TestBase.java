package legend.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import legend.intf.ICommon;

@TestInstance(value = Lifecycle.PER_CLASS)
public abstract class TestBase implements ICommon{
    protected String[] args;

    @BeforeAll
    private void monkParams(){
        args = monk().split("  +");
    }

    /**
     * 构造命令行参数字符串，交给子类实现。
     * 
     * @return 以一个以上空格为分隔符的参数字符串。
     */
    public abstract String monk();
}
