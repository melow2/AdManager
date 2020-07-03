package com.hyeoksin.admanager.third.manplus;

import android.content.Context;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;

import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.third.BaseThirdParty;
import com.mapps.android.share.AdInfoKey;
import com.mapps.android.view.EndingAdView;
import com.mz.common.listener.AdListener;

public class ManPlusEnding extends BaseThirdParty {
    private Ad ad;
    private EndingAdView endingAdView;
    private static final String TAG = ManPlusEnding.class.getSimpleName();

    public ManPlusEnding(@NonNull Context context,
                         @NonNull final Ad ad) {
        this.ad = ad;
        endingAdView = new EndingAdView(context, AdInfoKey.TYPE_IMAGE);
        String[] keySet = ad.key.split(",");

        endingAdView.setBannerSize(250, 300);
        endingAdView.setAdListener(new AdListener() {
            @Override
            public void onChargeableBannerType(View view,
                                               boolean bcharge) {
            }

            @Override
            public void onFailedToReceive(View view, int errorCode) {
                Log.d("onAdFailed","type: [ MANPLUS ], kind: [ ENDING ], errorCode: "+errorCode);
                if (errorCode >= 0) {
                    switch (errorCode) {
                        case AdInfoKey.AD_SUCCESS:
                            if (onAdLoadListener != null) {
                                onAdLoadListener.onAdLoaded(ad, endingAdView);
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

        if (keySet.length == 3) {
            endingAdView.setAdViewCode(keySet[0], keySet[1], keySet[2]);
        } else {
            endingAdView.setAdViewCode(String.valueOf(100), String.valueOf(200),
                    String.valueOf(302));
        }
    }

    @Override
    public void loadPreparedAd() {
        endingAdView.startEndingAdView();
    }
}
