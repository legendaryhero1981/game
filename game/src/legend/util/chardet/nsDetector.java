package legend.util.chardet;

public class nsDetector extends nsPSMDetector implements nsICharsetDetector{
    nsICharsetDetectionObserver mObserver = null;

    public nsDetector(){
        super();
    }

    public nsDetector(int langFlag){
        super(langFlag);
    }

    public void init(nsICharsetDetectionObserver aObserver){
        mObserver = aObserver;
        return;
    }

    public boolean doIt(byte[] aBuf, int aLen, boolean oDontFeedMe){
        if(aBuf == null || oDontFeedMe) return false;
        this.handleData(aBuf,aLen);
        return mDone;
    }

    public void done(){
        this.dataEnd();
        return;
    }

    public void report(String charset){
        if(mObserver != null) mObserver.notify(charset);
    }

    public boolean isAscii(byte[] aBuf, int aLen){
        for(int i = 0;i < aLen;i++)
            if((0x0080 & aBuf[i]) != 0) return false;
        return true;
    }
}
