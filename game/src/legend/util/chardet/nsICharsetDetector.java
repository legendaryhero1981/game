package legend.util.chardet;

public interface nsICharsetDetector{
    void init(nsICharsetDetectionObserver observer);

    boolean doIt(byte[] aBuf, int aLen, boolean oDontFeedMe);

    void done();
}
