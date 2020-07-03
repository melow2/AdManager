package com.hyeoksin.admanager.third;

public abstract class BaseThirdParty {
    public OnAdLoadListener onAdLoadListener;

    public void setOnAdLoadListener(OnAdLoadListener onAdLoadListener) {
        this.onAdLoadListener = onAdLoadListener;
    }

    public abstract void loadPreparedAd();
}
