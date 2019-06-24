package legend.util.chardet;

@FunctionalInterface
public interface nsICharsetDetectionObserver{
    void notify(String charset);
}
