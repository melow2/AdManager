package com.hyeoksin.admanager.third.house;

import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.third.BaseThirdParty;

public class HouseBanner extends BaseThirdParty {
    private Ad ad;
    private static final String TAG = HouseBanner.class.getSimpleName();

    public HouseBanner(Ad ad) {
        this.ad = ad;
    }

    @Override
    public void loadPreparedAd() {
        if (onAdLoadListener != null) {
            onAdLoadListener.onAdLoaded(ad, "");
        }
    }
}
