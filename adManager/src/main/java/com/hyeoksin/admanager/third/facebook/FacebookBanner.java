package com.hyeoksin.admanager.third.facebook;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hyeoksin.admanager.AdManager;
import com.hyeoksin.admanager.data.AdType;
import com.hyeoksin.admanager.third.BaseThirdParty;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

public class FacebookBanner extends BaseThirdParty {
    private AdView adView;
    private static final String TAG = FacebookBanner.class.getSimpleName();
    private long startTime,endTime;

    public FacebookBanner(@NonNull Context context,
                          @NonNull final com.hyeoksin.admanager.data.Ad advertisement) {
        adView = new AdView(context, advertisement.key, advertisement.type == AdType.BANNER ?
                AdSize.BANNER_HEIGHT_50 : advertisement.type == AdType.HALF_BANNER ?
                AdSize.RECTANGLE_HEIGHT_250 : null);

        adView.setAdListener(new AbstractAdListener() {
            @Override
            public void onError(Ad ad,
                                AdError adError) {
                super.onError(ad, adError);
                endTime = System.currentTimeMillis();
                if(AdManager.TEST) {
                    if(advertisement.type ==AdType.HALF_BANNER){
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ FACEBOOK ], kind: [ HALF_BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                        Log.d(AdManager.TAG,"#1 onAdFailed() - type: [ FACEBOOK ], kind: [ HALF_BANNER ], errorCode: "+adError.getErrorCode()+", "+"errorMsg: "+adError.getErrorMessage());
                    }else{
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ FACEBOOK ], kind: [ BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                        Log.d(AdManager.TAG,"#1 onAdFailed() - type: [ FACEBOOK ], kind: [ BANNER ], errorCode: "+adError.getErrorCode()+", "+"errorMsg: "+adError.getErrorMessage());
                    }

                }

                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdFailedToLoad(advertisement);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
                endTime = System.currentTimeMillis();
                if(AdManager.TEST) {
                    if(advertisement.type == AdType.HALF_BANNER){
                        Log.d(AdManager.TAG, "#1 onAdLoaded() - type: [ FACEBOOK ], kind: [ HALF_BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                    }else{
                        Log.d(AdManager.TAG, "#1 onAdLoaded() - type: [ FACEBOOK ], kind: [ BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                    }
                }
                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdLoaded(advertisement, adView);
                }
            }
        });
    }

    @Override
    public void loadPreparedAd() {
        startTime = System.currentTimeMillis();
        adView.loadAd();
    }
}
