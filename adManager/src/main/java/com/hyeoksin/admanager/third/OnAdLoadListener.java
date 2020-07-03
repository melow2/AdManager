package com.hyeoksin.admanager.third;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyeoksin.admanager.data.Ad;

public interface OnAdLoadListener {
    void onAdLoaded(@NonNull Ad ad,
                    @Nullable Object data);

    void onAdFailedToLoad(Ad ad);
}
