package com.hyeoksin.admanager.third.facebook;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hyeoksin.admanager.AdManager;
import com.hyeoksin.admanager.BuildConfig;
import com.hyeoksin.admanager.third.BaseInterstitialThirdParty;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class FacebookInterstitial extends BaseInterstitialThirdParty {
    private InterstitialAd interstitialAd;
    private static final String TAG = FacebookInterstitial.class.getSimpleName();
    private long startTime,endTime;

    public FacebookInterstitial(@NonNull Context context,
                                @NonNull final com.hyeoksin.admanager.data.Ad advertisement) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "/FacebookInterstitial");
        }

        interstitialAd = new InterstitialAd(context, advertisement.key);

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
            }

            @Override
            public void onError(Ad ad,
                                AdError adError) {
                endTime = System.currentTimeMillis();


                if(AdManager.TEST) {
                    Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ FACEBOOK ], kind: [ INTERSTITIAL ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                    Log.d(AdManager.TAG,"#1 onAdFailed() - type: [ FACEBOOK ], kind: [ INTERSTITIAL ], errorCode: "+adError.getErrorCode()+", "+"errorMsg: "+adError.getErrorMessage());
                }

                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdFailedToLoad(advertisement);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                endTime = System.currentTimeMillis();
                if(AdManager.TEST) {
                    Log.d(AdManager.TAG, "#1 onAdLoaded() - type: [ FACEBOOK ], kind: [ INTERSTITIAL ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                }

                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdLoaded(advertisement, null);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });
    }

    @Override
    public void loadPreparedAd() {
        startTime = System.currentTimeMillis();
        interstitialAd.loadAd();
    }

    @Override
    public void showPreparedAd() {
        interstitialAd.show();
    }
}
