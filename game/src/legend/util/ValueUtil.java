package legend.util;

import  java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ValueUtil{
    private ValueUtil(){}

    public static boolean nonEmpty(Object o){
        return !isEmpty(o);
    }

    public static boolean isEmpty(Object o){
        if(isNull(o)){
            return true;
        }else if(o instanceof String){
            return ((String)o).isEmpty();
        }else if(o instanceof Object[]){
            return 0 == ((Object[])o).length || 1 == ((Object[])o).length && isEmpty(((Object[])o)[0]);
        }else if(o instanceof Collection){
            return ((Collection<?>)o).isEmpty();
        }else if(o instanceof Map) return ((Map<?,?>)o).isEmpty();
        return false;
    }

    public static boolean nonNull(Object o){
        return !isNull(o);
    }

    public static boolean isNull(Object o){
        return null == o;
    }

    public static <T> Set<T> arrayToSet(T[] array){
        return new HashSet<>(Arrays.asList(array));
    }

    public static boolean matchRange(long n, long min, long max){
        return min <= n && max >= n;
    }

    public static boolean matchRange(int n, int min, int max){
        return min <= n && max >= n;
    }

    public static int limitValue(int n, int min, int max){
        return n < min ? min : n > max ? max : n;
    }

    public static int takeValueIfBeyond(int n, int min, int max, int value){
        return n < min || n > max ? value : n;
    }

    public static int takeMinValueIfBeyond(int n, int min, int max){
        return takeValueIfBeyond(n,min,max,min);
    }

    public static int takeMaxValueIfBeyond(int n, int min, int max){
        return takeValueIfBeyond(n,min,max,max);
    }
}
