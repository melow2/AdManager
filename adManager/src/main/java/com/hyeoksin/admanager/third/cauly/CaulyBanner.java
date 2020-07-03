package com.hyeoksin.admanager.third.cauly;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.hyeoksin.admanager.AdManager;
import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.data.AdType;
import com.hyeoksin.admanager.third.BaseThirdParty;
import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyAdViewListener;

public class CaulyBanner extends BaseThirdParty {
    private CaulyAdView caulyAdView;
    private FrameLayout container;
    private static final String TAG = CaulyBanner.class.getSimpleName();
    private long startTime,endTime;

    public CaulyBanner(@NonNull Context context,
                       @NonNull final Ad ad,
                       @NonNull FrameLayout container) {
        CaulyAdInfo caulyAdInfo = new CaulyAdInfoBuilder(ad.key).effect("None")
                .bannerHeight("Fixed_50").build();
        caulyAdView = new CaulyAdView(context);
        this.container = container;

        caulyAdView.setAdInfo(caulyAdInfo);
        caulyAdView.setAdViewListener(new CaulyAdViewListener() {
            @Override
            public void onReceiveAd(CaulyAdView caulyAdView,
                                    boolean isChargeableAd) {
                endTime = System.currentTimeMillis();
                if(AdManager.TEST) {
                    if(ad.type== AdType.HALF_BANNER) {
                        Log.d(AdManager.TAG, "#1 onAdLoaded() - type: [ CAULY ], kind: [ HALF_BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime - startTime) / 1000) + "초");
                    }else{
                        Log.d(AdManager.TAG, "#1 onAdLoaded() - type: [ CAULY ], kind: [ BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime - startTime) / 1000) + "초");
                    }
                }
                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdLoaded(ad, caulyAdView);
                }
            }

            @Override
            public void onFailedToReceiveAd(CaulyAdView caulyAdView,
                                            int errorCode,
                                            String errorMsg) {
                endTime = System.currentTimeMillis();
                if(AdManager.TEST) {
                    if(ad.type==AdType.HALF_BANNER){
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ CAULY ], kind: [ HALF_BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ CAULY ], kind: [ HALF_BANNER ], errorCode: "+errorCode+", "+"errorMsg: "+errorMsg);
                    }else{
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ CAULY ], kind: [ BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ CAULY ], kind: [ BANNER ], errorCode: "+errorCode+", "+"errorMsg: "+errorMsg);
                    }
                }

                switch (errorCode) {
                    case -200:
                    case 0:
                    case 100:
                        break;
                    default:
                        caulyAdView.pause();
                        caulyAdView.destroy();

                        if (onAdLoadListener != null) {
                            onAdLoadListener.onAdFailedToLoad(ad);
                        }
                        break;
                }
            }

            @Override
            public void onShowLandingScreen(CaulyAdView caulyAdView) {
            }

            @Override
            public void onCloseLandingScreen(CaulyAdView caulyAdView) {
            }
        });
    }

    @Override
    public void loadPreparedAd() {
        startTime = System.currentTimeMillis();
        container.removeAllViews();
        container.addView(caulyAdView);
    }
}
