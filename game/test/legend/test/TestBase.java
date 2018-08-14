package legend.test;

import legend.intf.ICommon;

public abstract class TestBase implements ICommon{
    protected String[] args;

    protected void monkParam(String s){
        args = s.split("  +");
    }

    public abstract void monkParam();
}
