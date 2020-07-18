package com.hyeoksin.admanager.third.cauly;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hyeoksin.admanager.AdManager;
import com.hyeoksin.admanager.BuildConfig;
import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.third.BaseInterstitialThirdParty;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;

public class CaulyInterstitial extends BaseInterstitialThirdParty {
    private Context context;
    private CaulyInterstitialAd caulyInterstitialAd;
    private static final String TAG = CaulyInterstitial.class.getSimpleName();
    private long startTime,endTime;

    public CaulyInterstitial(@NonNull Context context,
                             @NonNull final Ad ad) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "/CaulyInterstitial");
        }

        this.context = context;
        caulyInterstitialAd = new CaulyInterstitialAd();
        caulyInterstitialAd.setAdInfo(new CaulyAdInfoBuilder(ad.key).build());
        caulyInterstitialAd.setInterstialAdListener(new CaulyInterstitialAdListener() {
            @Override
            public void onReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd,
                                                boolean isChargeableAd) {
                endTime = System.currentTimeMillis();

                if(AdManager.TEST) {
                    Log.d(AdManager.TAG, "#1 onAdLoaded() - type: [ CAULY ], kind: [ INTERSTITIAL ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                }

                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdLoaded(ad, null);
                }
            }

            @Override
            public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd,
                                                        int errorCode,
                                                        String errorMsg) {
                endTime = System.currentTimeMillis();

                if(AdManager.TEST) {
                    Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ CAULY ], kind: [ INTERSTITIAL ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                    Log.d(AdManager.TAG,"#1 onAdFailed() - type: [ CAULY ], kind: [ INTERSTITIAL ], errorCode: "+errorCode+", "+"errorMsg: "+errorMsg);
                }

                switch (errorCode) {
                    case -200:
                    case 0:
                    case 100:
                        break;
                    default:
                        caulyInterstitialAd.cancel();
                        if (onAdLoadListener != null) {
                            onAdLoadListener.onAdFailedToLoad(ad);
                        }
                        break;
                }
            }

            @Override
            public void onClosedInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {
            }

            @Override
            public void onLeaveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {
            }
        });
    }

    @Override
    public void loadPreparedAd() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "/loadPreparedAd");
        }
        startTime = System.currentTimeMillis();
        caulyInterstitialAd.requestInterstitialAd((Activity) context);
    }

    @Override
    public void showPreparedAd() {
        caulyInterstitialAd.show();
    }
}
