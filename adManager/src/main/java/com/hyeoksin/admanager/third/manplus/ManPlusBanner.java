package com.hyeoksin.admanager.third.manplus;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.hyeoksin.admanager.AdManager;
import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.data.AdType;
import com.hyeoksin.admanager.third.BaseThirdParty;
import com.mapps.android.share.AdInfoKey;
import com.mapps.android.view.AdView;
import com.mz.common.listener.AdListener;

public class ManPlusBanner extends BaseThirdParty {
    private AdView adView;
    private static final String TAG = ManPlusBanner.class.getSimpleName();
    private long startTime,endTime;

    public ManPlusBanner(@NonNull Context context,
                         @NonNull final Ad ad) {
        adView = new AdView(context, 0, 0, AdInfoKey.TYPE_HTML);
        String[] keySet = ad.key.split(",");

        adView.setAdViewCode(keySet[0], keySet[1], keySet[2]);
        adView.setAdListener(new AdListener() {
            @Override
            public void onChargeableBannerType(View view,
                                               boolean bcharge) {
            }

            @Override
            public void onFailedToReceive(View view,
                                          int errorCode) {
                endTime = System.currentTimeMillis();

                if(AdManager.TEST) {
                    if(ad.type== AdType.HALF_BANNER){
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ MANPLUS ], kind: [ HALF_BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime-startTime)/1000)+"초");
                        Log.d(AdManager.TAG,"#1 onAdFailed() - type: [ MANPLUS ], kind: [ HALF_BANNER ], errorCode: "+errorCode);
                    }else {
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ MANPLUS ], kind: [ BANNER ], CURRENT_TIME_MILLIS: " + String.valueOf((endTime - startTime) / 1000) + "초");
                        Log.d(AdManager.TAG, "#1 onAdFailed() - type: [ MANPLUS ], kind: [ BANNER ], errorCode: " + errorCode);
                    }
                }

                if (errorCode >= 0) {
                    switch (errorCode) {
                        case AdInfoKey.AD_SUCCESS:
                            if (onAdLoadListener != null) {
                                onAdLoadListener.onAdLoaded(ad, adView);
                            }
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
        adView.StartService();
    }
}
