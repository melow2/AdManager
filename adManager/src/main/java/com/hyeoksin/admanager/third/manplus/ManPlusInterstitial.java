package com.hyeoksin.admanager.third.manplus;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.hyeoksin.admanager.AdManager;
import com.hyeoksin.admanager.BuildConfig;
import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.third.BaseInterstitialThirdParty;
import com.mapps.android.share.AdInfoKey;
import com.mapps.android.view.AdInterstitialView;
import com.mz.common.listener.AdListener;

public class ManPlusInterstitial extends BaseInterstitialThirdParty {
    private Ad ad;
    private AdInterstitialView adInterstitialView;
    private static final String TAG = ManPlusInterstitial.class.getSimpleName();
    private long startTime,endTime;

    public ManPlusInterstitial(@NonNull Context context,
                               @NonNull final Ad ad) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "/ManPlusInterstitial");
        }

        this.ad = ad;
        adInterstitialView = new AdInterstitialView(context);
        String[] keySet = ad.key.split(",");

        adInterstitialView.setAdViewCode(keySet[0], keySet[1], keySet[2]);
        adInterstitialView.setViewStyle(AdInfoKey.VIEWSTYLE.NONE);
        adInterstitialView.setAdListener(new AdListener() {
            @Override
            public void onChargeableBannerType(View view,
                                               boolean bcharge) {
            }

            @Override
            public void onFailedToReceive(View view,
                                          int errorCode) {
                endTime = System.currentTimeMillis();

                if(AdManager.TEST) {
                    Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ MANPLUS ], kind: [ INTERSTITIAL ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                    Log.d(AdManager.TAG,"#1 onAdFailed() - type: [ MANPLUS ], kind: [ INTERSTITIAL ], errorCode: "+errorCode);
                }

                if (errorCode >= 0) {
                    switch (errorCode) {
                        case AdInfoKey.AD_SUCCESS:
                            break;
                    }
                } else {
                    if (onAdLoadListener != null) {
                        onAdLoadListener.onAdFailedToLoad(ad);
                    }
                }
            }

            @Override
            public void onInterClose(View view) {
            }

            @Override
            public void onAdClick(View view) {
            }
        });
    }

    @Override
    public void loadPreparedAd() {
        startTime = System.currentTimeMillis();
        if (onAdLoadListener != null) {
            onAdLoadListener.onAdLoaded(ad, null);
        }
    }

    @Override
    public void showPreparedAd() {
        adInterstitialView.ShowInterstitialView();
    }
}
