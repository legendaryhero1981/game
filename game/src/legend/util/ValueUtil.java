package legend.util;

import java.util.Collection;
import java.util.Map;

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
            return 0 == ((Object[])o).length;
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

    public static boolean matchRange(int n, int min, int max){
        return min <= n && max >= n;
    }

    public static int limitValue(int n, int min, int max){
        return n < min ? min : n > max ? max : n;
    }
}
