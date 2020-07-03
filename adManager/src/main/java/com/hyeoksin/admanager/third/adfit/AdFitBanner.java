package com.hyeoksin.admanager.third.adfit;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.third.BaseThirdParty;
import com.kakao.adfit.ads.AdListener;
import com.kakao.adfit.ads.ba.BannerAdView;

public class AdFitBanner extends BaseThirdParty {
    private BannerAdView adView;
    private static final String TAG = AdFitBanner.class.getSimpleName();

    public AdFitBanner(@NonNull Context context,
                       @NonNull final Ad ad) {
        adView = new BannerAdView(context);

        adView.setClientId(ad.key);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdLoaded(ad, adView);
                }
            }

            @Override
            public void onAdFailed(int errorCode) {
                Log.d("onAdFailed","type: [ ADFIT ], kind: [ BANNER ], errorCode: "+errorCode);
                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdFailedToLoad(ad);
                }
            }

            @Override
            public void onAdClicked() {
            }
        });
    }

    @Override
    public void loadPreparedAd() {
        adView.loadAd();
    }
}
