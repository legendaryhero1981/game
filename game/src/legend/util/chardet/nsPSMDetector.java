package legend.util.chardet;

public abstract class nsPSMDetector{
    public static final int ALL = 0;
    public static final int JAPANESE = 1;
    public static final int CHINESE = 2;
    public static final int SIMPLIFIED_CHINESE = 3;
    public static final int TRADITIONAL_CHINESE = 4;
    public static final int KOREAN = 5;
    public static final int NO_OF_LANGUAGES = 6;
    public static final int MAX_VERIFIERS = 16;
    nsVerifier[] mVerifier;
    nsEUCStatistics[] mStatisticsData;
    nsEUCSampler mSampler = new nsEUCSampler();
    byte[] mState = new byte[MAX_VERIFIERS];
    int[] mItemIdx = new int[MAX_VERIFIERS];
    int mItems;
    int mClassItems;
    boolean mDone;
    boolean mRunSampler;
    boolean mClassRunSampler;

    public nsPSMDetector(){
        initVerifiers(nsPSMDetector.ALL);
        reset();
    }

    public nsPSMDetector(int langFlag){
        initVerifiers(langFlag);
        reset();
    }

    public nsPSMDetector(int aItems, nsVerifier[] aVerifierSet, nsEUCStatistics[] aStatisticsSet){
        mClassRunSampler = (aStatisticsSet != null);
        mStatisticsData = aStatisticsSet;
        mVerifier = aVerifierSet;
        mClassItems = aItems;
        reset();
    }

    public void reset(){
        mRunSampler = mClassRunSampler;
        mDone = false;
        mItems = mClassItems;
        for(int i = 0;i < mItems;i++){
            mState[i] = 0;
            mItemIdx[i] = i;
        }
        mSampler.Reset();
    }

    protected void initVerifiers(int currVerSet){
        int currVerifierSet;
        if(currVerSet >= 0 && currVerSet < NO_OF_LANGUAGES){
            currVerifierSet = currVerSet;
        }else{
            currVerifierSet = nsPSMDetector.ALL;
        }
        mVerifier = null;
        mStatisticsData = null;
        if(currVerifierSet == nsPSMDetector.TRADITIONAL_CHINESE){
            mVerifier = new nsVerifier[]{new nsUTF8Verifier(),new nsBIG5Verifier(),new nsISO2022CNVerifier(),new nsEUCTWVerifier(),new nsCP1252Verifier(),new nsUCS2BEVerifier(),new nsUCS2LEVerifier()};
            mStatisticsData = new nsEUCStatistics[]{null,new Big5Statistics(),null,new EUCTWStatistics(),null,null,null};
        }else if(currVerifierSet == nsPSMDetector.KOREAN){
            mVerifier = new nsVerifier[]{new nsUTF8Verifier(),new nsEUCKRVerifier(),new nsISO2022KRVerifier(),new nsCP1252Verifier(),new nsUCS2BEVerifier(),new nsUCS2LEVerifier()};
        }else if(currVerifierSet == nsPSMDetector.SIMPLIFIED_CHINESE){
            mVerifier = new nsVerifier[]{new nsUTF8Verifier(),new nsGB2312Verifier(),new nsGB18030Verifier(),new nsISO2022CNVerifier(),new nsHZVerifier(),new nsCP1252Verifier(),new nsUCS2BEVerifier(),new nsUCS2LEVerifier()};
        }else if(currVerifierSet == nsPSMDetector.JAPANESE){
            mVerifier = new nsVerifier[]{new nsUTF8Verifier(),new nsSJISVerifier(),new nsEUCJPVerifier(),new nsISO2022JPVerifier(),new nsCP1252Verifier(),new nsUCS2BEVerifier(),new nsUCS2LEVerifier()};
        }else if(currVerifierSet == nsPSMDetector.CHINESE){
            mVerifier = new nsVerifier[]{new nsUTF8Verifier(),new nsGB2312Verifier(),new nsGB18030Verifier(),new nsBIG5Verifier(),new nsISO2022CNVerifier(),new nsHZVerifier(),new nsEUCTWVerifier(),new nsCP1252Verifier(),new nsUCS2BEVerifier(),new nsUCS2LEVerifier()};
            mStatisticsData = new nsEUCStatistics[]{null,new GB2312Statistics(),null,new Big5Statistics(),null,null,new EUCTWStatistics(),null,null,null};
        }else if(currVerifierSet == nsPSMDetector.ALL){
            mVerifier = new nsVerifier[]{new nsUTF8Verifier(),new nsSJISVerifier(),new nsEUCJPVerifier(),new nsISO2022JPVerifier(),new nsEUCKRVerifier(),new nsISO2022KRVerifier(),new nsBIG5Verifier(),new nsEUCTWVerifier(),new nsGB2312Verifier(),new nsGB18030Verifier(),new nsISO2022CNVerifier(),new nsHZVerifier(),new nsCP1252Verifier(),new nsUCS2BEVerifier(),new nsUCS2LEVerifier()};
            mStatisticsData = new nsEUCStatistics[]{null,null,new EUCJPStatistics(),null,new EUCKRStatistics(),null,new Big5Statistics(),new EUCTWStatistics(),new GB2312Statistics(),null,null,null,null,null,null};
        }
        mClassRunSampler = (mStatisticsData != null);
        mClassItems = mVerifier.length;
    }

    public abstract void report(String charset);

    public boolean handleData(byte[] aBuf, int len){
        for(int i = 0,j;i < len;i++){
            byte b = aBuf[i];
            for(j = 0;j < mItems;){
                byte st = nsVerifier.getNextState(mVerifier[mItemIdx[j]],b,mState[j]);
                if(st == nsVerifier.eItsMe){
                    report(mVerifier[mItemIdx[j]].charset());
                    return mDone = true;
                }else if(st == nsVerifier.eError){
                    mItems--;
                    if(j < mItems){
                        mItemIdx[j] = mItemIdx[mItems];
                        mState[j] = mState[mItems];
                    }
                }else mState[j++] = st;
            }
            if(mItems < 2){
                if(1 == mItems) report(mVerifier[mItemIdx[0]].charset());
                return mDone = true;
            }else{
                int nonUCS2Num = 0;
                int nonUCS2Idx = 0;
                for(j = 0;j < mItems;j++)
                    if((!(mVerifier[mItemIdx[j]].isUCS2())) && (!(mVerifier[mItemIdx[j]].isUCS2()))){
                        nonUCS2Num++;
                        nonUCS2Idx = j;
                    }
                if(1 == nonUCS2Num){
                    report(mVerifier[mItemIdx[nonUCS2Idx]].charset());
                    return mDone = true;
                }
            }
        }
        if(mRunSampler) sample(aBuf,len);
        return mDone;
    }

    public void dataEnd(){
        if(mDone == true) return;
        if(mItems == 2){
            if((mVerifier[mItemIdx[0]].charset()).equals("GB18030")){
                report(mVerifier[mItemIdx[1]].charset());
                mDone = true;
            }else if((mVerifier[mItemIdx[1]].charset()).equals("GB18030")){
                report(mVerifier[mItemIdx[0]].charset());
                mDone = true;
            }
        }
        if(mRunSampler) sample(null,0,true);
    }

    public void sample(byte[] aBuf, int aLen){
        sample(aBuf,aLen,false);
    }

    public void sample(byte[] aBuf, int aLen, boolean aLastChance){
        int possibleCandidateNum = 0;
        int j;
        int eucNum = 0;
        for(j = 0;j < mItems;j++){
            if(null != mStatisticsData[mItemIdx[j]]) eucNum++;
            if((!mVerifier[mItemIdx[j]].isUCS2()) && (!(mVerifier[mItemIdx[j]].charset()).equals("GB18030"))) possibleCandidateNum++;
        }
        if(mRunSampler = (eucNum > 1)){
            mRunSampler = mSampler.Sample(aBuf,aLen);
            if(((aLastChance && mSampler.GetSomeData()) || mSampler.EnoughData()) && (eucNum == possibleCandidateNum)){
                mSampler.CalFreq();
                int bestIdx = -1;
                int eucCnt = 0;
                float bestScore = 0.0f;
                for(j = 0;j < mItems;j++){
                    if((null != mStatisticsData[mItemIdx[j]]) && (!(mVerifier[mItemIdx[j]].charset()).equals("Big5"))){
                        float score = mSampler.GetScore(mStatisticsData[mItemIdx[j]].mFirstByteFreq(),mStatisticsData[mItemIdx[j]].mFirstByteWeight(),mStatisticsData[mItemIdx[j]].mSecondByteFreq(),mStatisticsData[mItemIdx[j]].mSecondByteWeight());
                        if((0 == eucCnt++) || (bestScore > score)){
                            bestScore = score;
                            bestIdx = j;
                        }
                    }
                }
                if(bestIdx >= 0){
                    report(mVerifier[mItemIdx[bestIdx]].charset());
                    mDone = true;
                }
            }
        }
    }

    public String[] getProbableCharsets(){
        if(mItems <= 0){
            String[] nomatch = new String[1];
            nomatch[0] = "nomatch";
            return nomatch;
        }
        String ret[] = new String[mItems];
        for(int i = 0;i < mItems;i++)
            ret[i] = mVerifier[mItemIdx[i]].charset();
        return ret;
    }
}
