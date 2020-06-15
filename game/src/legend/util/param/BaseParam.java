package legend.util.param;

public abstract class BaseParam{
    protected long condition;

    public boolean meetCondition(long condition){
        return condition == (condition & this.condition);
    }
}
