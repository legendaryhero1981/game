package legend.test;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import legend.intf.ICommon;

@TestInstance(value = Lifecycle.PER_CLASS)
public abstract class TestBase implements ICommon{
    protected String[] args;

    protected void monkParam(String s){
        args = s.split("  +");
    }

    public abstract void monkParam();
}
