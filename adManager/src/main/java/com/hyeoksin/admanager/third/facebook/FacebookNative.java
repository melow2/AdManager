package com.hyeoksin.admanager.third.facebook;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.hyeoksin.admanager.third.BaseThirdParty;

public class FacebookNative extends BaseThirdParty {
    private com.hyeoksin.admanager.data.Ad advertisement;
    private NativeAd nativeAd;
    private static final String TAG = FacebookNative.class.getSimpleName();

    public FacebookNative(@NonNull Context context,
                          @NonNull final com.hyeoksin.admanager.data.Ad advertisement) {
        this.advertisement = advertisement;
        nativeAd = new NativeAd(context, advertisement.key);

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("onAdFailed","type: [ FACEBOOK ], kind: [ NATIVE ], errorCode: "+adError.getErrorCode()+", "+"errorMsg: "+adError.getErrorMessage());
                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdFailedToLoad(advertisement);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdLoaded(advertisement, nativeAd);
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
        try {
            nativeAd.loadAd();
        } catch (Exception e) {
            if (onAdLoadListener != null) {
                onAdLoadListener.onAdFailedToLoad(advertisement);
            }
        }
    }
}
