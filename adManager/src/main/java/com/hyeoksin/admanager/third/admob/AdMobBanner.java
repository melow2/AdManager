package com.hyeoksin.admanager.third.admob;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hyeoksin.admanager.AdManager;
import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.data.AdType;
import com.hyeoksin.admanager.third.BaseThirdParty;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdMobBanner extends BaseThirdParty {
    private AdView adView;
    private static final String TAG = AdMobBanner.class.getSimpleName();
    private long startTime,endTime;

    public AdMobBanner(@NonNull Context context,
                       @NonNull final Ad ad) {
        adView = new AdView(context);
        adView.setAdSize(ad.type == AdType.BANNER ? AdSize.BANNER : ad.type == AdType.HALF_BANNER ? AdSize.MEDIUM_RECTANGLE : null);
        adView.setAdUnitId(ad.key);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                endTime = System.currentTimeMillis();
                if (onAdLoadListener != null) {
                    if(AdManager.TEST) {
                        if(ad.type == AdType.HALF_BANNER){
                            Log.d(AdManager.TAG, "#1 onAdLoaded() - type: [ ADMOB ], kind: [ HALF_BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                        }else {
                            Log.d(AdManager.TAG, "#1 onAdLoaded() - type: [ ADMOB ], kind: [ BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime - startTime) / 1000) + "초");
                        }
                    }
                    onAdLoadListener.onAdLoaded(ad, adView);
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                endTime = System.currentTimeMillis();
                if(AdManager.TEST) {
                    if(ad.type == AdType.HALF_BANNER) {
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ ADMOB ], kind: [ HALF_BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime - startTime) / 1000) + "초");
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ ADMOB ], kind: [ HALF_BANNER ], errorCode: " + errorCode);
                    }else{
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ ADMOB ], kind: [ BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime - startTime) / 1000) + "초");
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ ADMOB ], kind: [ BANNER ], errorCode: " + errorCode);
                    }
                }
                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdFailedToLoad(ad);
                }
            }
        });
    }

    @Override
    public void loadPreparedAd() {
        startTime = System.currentTimeMillis();
        adView.loadAd(new AdRequest.Builder().build());
    }
}
