package legend.util;

import static java.time.LocalDateTime.now;
import static legend.util.ConsoleUtil.CS;
import static legend.util.ValueUtil.isEmpty;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import legend.util.intf.ITimeUtil;

public final class TimeUtil implements ITimeUtil{
    private static AtomicLong time;
    private static AtomicLong totalTime;
    static{
        time = new AtomicLong();
        totalTime = new AtomicLong();
    }

    private TimeUtil(){}

    public static String getDateTime(){
        return now().toString().replaceFirst("T"," ").replaceFirst("\\.\\d*","");
    }

    public static <T> void runWithConsole(Consumer<T> consumer, String[] args, String help){
        CS.showHelp(help,()->isEmpty(args) || isEmpty(args[0]));
        CS.s(ST_PRG_START).l(2).s(ST_PRG_DONE + N_TIME + S_COLON + getDurationString(consumer) + S_PERIOD).l(2);
    }

    public static <T> T countDuration(Supplier<T> supplier){
        LocalTime start = LocalTime.now();
        T t = supplier.get();
        LocalTime end = LocalTime.now();
        time.set(start.until(end,ChronoUnit.MILLIS));
        return t;
    }

    public static <T> void countDuration(Consumer<T> consumer){
        LocalTime start = LocalTime.now();
        consumer.accept(null);
        LocalTime end = LocalTime.now();
        time.set(start.until(end,ChronoUnit.MILLIS));
    }

    public static <T> String getDurationString(Consumer<T> consumer){
        LocalTime start = LocalTime.now();
        consumer.accept(null);
        LocalTime end = LocalTime.now();
        long duration = start.until(end,ChronoUnit.MILLIS);
        time.set(duration);
        return getDurationString(duration);
    }

    public static String getDurationString(){
        return getDurationString(time.get());
    }

    public static String getDurationString(long time){
        long hour = time / UNIT_MILLI / UNIT_SECOND / UNIT_MINUTE;
        long minute = time / UNIT_MILLI / UNIT_SECOND % UNIT_MINUTE;
        long second = time / UNIT_MILLI % UNIT_SECOND;
        long milli = time % UNIT_MILLI;
        return hour + "时" + minute + "分" + second + "秒" + milli + "毫秒";
    }

    public static void resetTime(){
        time.set(0);
    }

    public static <T> T incTotalDuration(Supplier<T> supplier){
        LocalTime start = LocalTime.now();
        T t = supplier.get();
        LocalTime end = LocalTime.now();
        totalTime.addAndGet(start.until(end,ChronoUnit.MILLIS));
        return t;
    }

    public static <T> T decTotalDuration(Supplier<T> supplier){
        LocalTime start = LocalTime.now();
        T t = supplier.get();
        LocalTime end = LocalTime.now();
        totalTime.addAndGet(start.until(end,ChronoUnit.MILLIS) * -1);
        return t;
    }

    public static <T> void incTotalDuration(Consumer<T> consumer){
        LocalTime start = LocalTime.now();
        consumer.accept(null);
        LocalTime end = LocalTime.now();
        totalTime.addAndGet(start.until(end,ChronoUnit.MILLIS));
    }

    public static <T> void decTotalDuration(Consumer<T> consumer){
        LocalTime start = LocalTime.now();
        consumer.accept(null);
        LocalTime end = LocalTime.now();
        totalTime.addAndGet(start.until(end,ChronoUnit.MILLIS) * -1);
    }

    public static <T> String getTotalDurationString(Consumer<T> consumer){
        LocalTime start = LocalTime.now();
        consumer.accept(null);
        LocalTime end = LocalTime.now();
        long duration = start.until(end,ChronoUnit.MILLIS);
        totalTime.set(duration);
        return getTotalDurationString(duration);
    }

    public static String getTotalDurationString(){
        return getTotalDurationString(totalTime.get());
    }

    public static String getTotalDurationString(long totalTime){
        long hour = totalTime / UNIT_MILLI / UNIT_SECOND / UNIT_MINUTE;
        long minute = totalTime / UNIT_MILLI / UNIT_SECOND % UNIT_MINUTE;
        long second = totalTime / UNIT_MILLI % UNIT_SECOND;
        long milli = totalTime % UNIT_MILLI;
        return hour + "时" + minute + "分" + second + "秒" + milli + "毫秒";
    }

    public static void resetTotalTime(){
        totalTime.set(0);
    }
}
