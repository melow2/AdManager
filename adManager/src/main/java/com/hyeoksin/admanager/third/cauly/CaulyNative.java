package com.hyeoksin.admanager.third.cauly;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyCustomAd;
import com.fsn.cauly.CaulyCustomAdListener;
import com.fsn.cauly.CaulyNativeAdInfoBuilder;
import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.third.BaseThirdParty;

public class CaulyNative extends BaseThirdParty {
    private CaulyCustomAd caulyCustomAd;
    private static final String TAG = CaulyNative.class.getSimpleName();

    public CaulyNative(@NonNull Context context,
                       @NonNull final Ad ad) {
        CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(ad.key).build();

        caulyCustomAd = new CaulyCustomAd(context);
        caulyCustomAd.setAdInfo(adInfo);
        caulyCustomAd.setCustomAdListener(new CaulyCustomAdListener() {
            @Override
            public void onShowedAd() {
            }

            //광고 호출이 성공할 경우, 호출된다.
            @Override
            public void onLoadedAd(boolean isChargeableAd) {
                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdLoaded(ad, caulyCustomAd.getJsonString());
                }
            }

            // 광고 호출이 실패할 경우, 호출된다.
            @Override
            public void onFailedAd(int errCode, String errMsg) {
                Log.d("onAdFailed","type: [ CAULY ], kind: [ NATIVE ], errorCode: "+errCode+", "+"errorMsg: "+errMsg);
                if (onAdLoadListener != null) {
                    onAdLoadListener.onAdFailedToLoad(ad);
                }
            }

            // 광고가 클릭된 경우, 호출된다.
            @Override
            public void onClikedAd() {
            }
        });
    }

    @Override
    public void loadPreparedAd() {
        caulyCustomAd.requestAdData(CaulyCustomAd.NATIVE_LANDSCAPE, 1);
    }
}
