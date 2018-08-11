package legend.util.test.model;

public class GCDModel{
    private int n;
    private int m;
    private int min;
    private int max;

    public GCDModel(int n, int m){
        this.n = n;
        this.m = m;
    }

    public void gcd(){
        for(min = n * m;0 != (max = n % m);n = m,m = max);
        max = m;
        min /= m;
    }

    public int getMin(){
        return min;
    }

    public int getMax(){
        return max;
    }
}
