package legend.util.intf;

import java.util.function.Consumer;

public interface IProgress{
    int MID = 50;
    int MIN = 0;
    int MAX = 100;
    int SLEEP = 10;
    String COMPLETE = ">";
    String REMAINER = ".";
    String RETURN = "\r";
    String STYLE = "%";

    enum State{
        RUN,STOP,RESET,FINISH
    }

    State state();

    void runUntillFinish(Consumer<IProgress> consumer);

    void run();

    void finish();

    void reset();
    
    void reset(float amount);

    void reset(float amount, int position);

    void reset(float amount, int begin, int end);

    void reset(float amount, int position, int begin, int end);

    void stop();

    void resume();

    void update(float size);

    void update(float size, float scale);

    float countUpdate(float amount, float size);

    float countUpdate(float amount, float size, float percent);
}
