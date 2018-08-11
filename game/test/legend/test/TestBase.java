package legend.test;

import legend.intf.ICommonVar;

public abstract class TestBase implements ICommonVar{
    protected String[] args;

    protected void monkParam(String s){
        args = s.split("  +");
    }

    public abstract void monkParam();
}
