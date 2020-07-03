package com.hyeoksin.admanager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.data.AdDataRepository;
import com.hyeoksin.admanager.data.AdDataSource;
import com.hyeoksin.admanager.data.AdType;
import com.hyeoksin.admanager.third.BaseInterstitialThirdParty;
import com.hyeoksin.admanager.third.OnAdLoadListener;
import com.hyeoksin.admanager.third.adfit.AdFitBanner;
import com.hyeoksin.admanager.third.admob.AdMobBanner;
import com.hyeoksin.admanager.third.admob.AdMobInterstitial;
import com.hyeoksin.admanager.third.admob.AdMobNative;
import com.hyeoksin.admanager.third.cauly.CaulyBanner;
import com.hyeoksin.admanager.third.cauly.CaulyInterstitial;
import com.hyeoksin.admanager.third.cauly.CaulyNative;
import com.hyeoksin.admanager.third.facebook.FacebookBanner;
import com.hyeoksin.admanager.third.facebook.FacebookInterstitial;
import com.hyeoksin.admanager.third.facebook.FacebookNative;
import com.hyeoksin.admanager.third.house.HouseBanner;
import com.hyeoksin.admanager.third.house.HouseNative;
import com.hyeoksin.admanager.third.manplus.ManPlusBanner;
import com.hyeoksin.admanager.third.manplus.ManPlusEnding;
import com.hyeoksin.admanager.third.manplus.ManPlusInterstitial;
import com.bumptech.glide.Glide;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.kakao.adfit.ads.ba.BannerAdView;
import com.mapps.android.view.EndingAdView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AdManager implements OnAdLoadListener {
    private Context context;
    private final AdDataRepository adDataRepository;
    private FrameLayout container;
    private Timer mTimer;
    private int interstitialCount;
    private OnInterstitialAdLoadListener onInterstitialAdLoadListener;
    private LoadTimerTask mTimerTask;
    private AdFitBanner adFitBanner;
    private AdMobNative adMobNative;
    private AdMobBanner adMobBanner;
    private AdMobInterstitial adMobInterstitial;
    private CaulyBanner caulyBanner;
    private CaulyNative caulyNative;
    private CaulyInterstitial caulyInterstitial;
    private FacebookBanner facebookBanner;
    private FacebookNative facebookNative;
    private FacebookInterstitial facebookInterstitial;
    private HouseBanner houseBanner;
    private HouseNative houseNative;
    private ManPlusBanner manPlusBanner;
    private ManPlusEnding manPlusEnding;
    private ManPlusInterstitial manPlusInterstitial;
    private LoadHandler mHandler;
    private Runnable mRunnable;
    private BaseInterstitialThirdParty currentBaseInterstitialThirdParty;
    private static final int FACEBOOK = 2;
    private static final int ADMOB = 3;
    private static final int MANPLUS = 5;
    private static final int CAULY = 7;
    private static final int ADFIT = 11;
    private static final int HOUSE = 13;
    private static final int BANNER = 101;
    private static final int HALF_BANNER = 103;
    private static final int INTERSTITIAL = 107;
    private static final int NATIVE = 109;
    private static final int ENDING = 113;
    private static final int DEFAULT_DURATION_TIME = 3;
    public static final String TAG = "AdManagerTest";
    public static Boolean TEST = false;

    private AdManager(Builder builder) {
        context = builder.context;
        adDataRepository = builder.adDataRepository;
        container = builder.container;
        mTimer = new Timer();
        interstitialCount = builder.interstitialCount;
        onInterstitialAdLoadListener = builder.onInterstitialAdLoadListener;
        TEST = builder.test_option;

        adDataRepository.getAll(new AdDataSource.OnGetAllSuccessListener() {
            @Override
            public void onSuccess(@NonNull List<Ad> adList) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "//onSuccess");
                }
                mHandler = new LoadHandler(Looper.getMainLooper());
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mHandler.handleMessage(new Message());
                    }
                };
            }
        }, new AdDataSource.OnGetAllFailureListener() {
            @Override
            public void onFailure() {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "//onFailure");
                }
                mHandler = new LoadHandler(Looper.getMainLooper());
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mHandler.handleMessage(new Message());
                    }
                };
            }
        });
    }

    @Override
    public void onAdLoaded(@NonNull Ad ad,
                           @Nullable Object data) {
        switch (ad.name.getCode() * ad.type.getCode()) {
            case CAULY * BANNER:
            case HOUSE * INTERSTITIAL:
                break;
            case ADMOB * INTERSTITIAL:
                currentBaseInterstitialThirdParty = adMobInterstitial;

                if (onInterstitialAdLoadListener != null) {
                    onInterstitialAdLoadListener.onAdLoaded();
                }
                break;
            case CAULY * INTERSTITIAL:
                currentBaseInterstitialThirdParty = caulyInterstitial;

                if (onInterstitialAdLoadListener != null) {
                    onInterstitialAdLoadListener.onAdLoaded();
                }
                break;
            case FACEBOOK * INTERSTITIAL:
                currentBaseInterstitialThirdParty = facebookInterstitial;

                if (onInterstitialAdLoadListener != null) {
                    onInterstitialAdLoadListener.onAdLoaded();
                }
                break;
            case MANPLUS * INTERSTITIAL:
                currentBaseInterstitialThirdParty = manPlusInterstitial;

                if (onInterstitialAdLoadListener != null) {
                    onInterstitialAdLoadListener.onAdLoaded();
                }
                break;
            case ADFIT * BANNER:
            case ADFIT * HALF_BANNER: {
                BannerAdView bannerAdView = (BannerAdView) data;
                container.removeAllViews();
                container.addView(bannerAdView);
                break;
            }
            case ADMOB * BANNER:
            case ADMOB * HALF_BANNER: {
                com.google.android.gms.ads.AdView adView =
                        (com.google.android.gms.ads.AdView) data;

                container.removeAllViews();
                container.addView(adView);
                break;
            }
            case ADMOB * NATIVE: {
                UnifiedNativeAd unifiedNativeAd = (UnifiedNativeAd) data;
                View view = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.view_admob_native, container, false);
                ImageView image = view.findViewById(R.id.view_admob_image);
                ImageView icon = view.findViewById(R.id.view_admob_icon);
                TextView headLine = view.findViewById(R.id.view_admob_headLine);
                TextView body = view.findViewById(R.id.view_admob_body);

                image.setImageDrawable(unifiedNativeAd.getImages().get(0).getDrawable());
                if (unifiedNativeAd.getIcon() != null) {
                    icon.setVisibility(View.VISIBLE);
                    icon.setImageDrawable(unifiedNativeAd.getIcon().getDrawable());
                } else {
                    icon.setVisibility(View.GONE);
                }
                headLine.setText(unifiedNativeAd.getHeadline());
                body.setText(unifiedNativeAd.getBody());

                container.removeAllViews();
                container.addView(view);
                break;
            }
            case CAULY * NATIVE: {
                try {
                    JSONObject target = new JSONObject((String) data)
                            .getJSONArray("ads").getJSONObject(0);
                    View view = LayoutInflater.from(container.getContext())
                            .inflate(R.layout.view_cauly_native, container, false);
                    ImageView image = view.findViewById(R.id.view_cauly_image);
                    ImageView icon = view.findViewById(R.id.view_cauly_icon);
                    TextView title = view.findViewById(R.id.view_cauly_headLine);
                    TextView description = view.findViewById(R.id.view_cauly_body);

                    Glide.with(context)
                            .load(target.getString("image"))
                            .into(image);
                    icon.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(target.get("icon"))
                            .into(icon);
                    title.setText(target.getString("title"));
                    description.setText(target.getString("subtitle"));

                    container.removeAllViews();
                    container.addView(view);
                } catch (Exception e) {
                    onAdFailedToLoad(ad);
                }
                break;
            }
            case FACEBOOK * BANNER:
            case FACEBOOK * HALF_BANNER: {
                com.facebook.ads.AdView adView = (com.facebook.ads.AdView) data;

                container.removeAllViews();
                container.addView(adView);
                break;
            }
            case FACEBOOK * NATIVE: {
                NativeAd nativeAd = (NativeAd) data;
                View view = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.view_facebook_native, container, false);
                MediaView media = view.findViewById(R.id.view_facebook_media);
                AdIconView icon = view.findViewById(R.id.view_facebook_icon);
                TextView headLine = view.findViewById(R.id.view_facebook_headLine);
                TextView body = view.findViewById(R.id.view_facebook_body);
                List<View> clickableViews = new ArrayList<>();

                container.removeAllViews();
                container.addView(view);

                nativeAd.downloadMedia();
                headLine.setText(nativeAd.getAdvertiserName());
                body.setText(nativeAd.getAdBodyText());
                clickableViews.add(icon);
                clickableViews.add(headLine);
                clickableViews.add(body);

                nativeAd.registerViewForInteraction(view, media, icon, clickableViews);
                break;
            }
            case HOUSE * HALF_BANNER: {
                View view = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.view_house_banner, container, false);
                ImageView image = view.findViewById(R.id.view_house_image);

                Glide.with(context)
                        .load(ad.image)
                        .into(image);

                container.removeAllViews();
                container.addView(view);
                break;
            }
            case HOUSE * NATIVE: {
                View view = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.view_house_native, container, false);
                ImageView image = view.findViewById(R.id.view_house_image);
                ImageView icon = view.findViewById(R.id.view_house_icon);
                TextView title = view.findViewById(R.id.view_house_headLine);
                TextView description = view.findViewById(R.id.view_house_body);

                Glide.with(context)
                        .load(ad.icon)
                        .into(icon);
                title.setText(ad.title);
                description.setText(ad.subTitle);
                icon.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(ad.image)
                        .into(image);

                container.removeAllViews();
                container.addView(view);
                break;
            }
            case MANPLUS * BANNER: {
                com.mapps.android.view.AdView adView = (com.mapps.android.view.AdView) data;

                container.removeAllViews();
                container.addView(adView);
                break;
            }
            case MANPLUS * ENDING: {
                EndingAdView endingAdView = (EndingAdView) data;
                ViewGroup.LayoutParams containerLayoutParams = container.getLayoutParams();

                endingAdView.measure(View.MeasureSpec.UNSPECIFIED,
                        View.MeasureSpec.UNSPECIFIED);

                endingAdView.setScaleX(0.75f);
                containerLayoutParams.height = (int) (endingAdView.getMeasuredHeight() * 0.75);

                container.setLayoutParams(containerLayoutParams);
                container.removeAllViews();
                container.addView(endingAdView);
                break;
            }
            default:
                onAdFailedToLoad(ad);
                break;
        }
    }

    @Override
    public void onAdFailedToLoad(Ad ad) {
        if (ad.type == AdType.INTERSTITIAL &&
                --interstitialCount <= 0) {
            if (onInterstitialAdLoadListener != null) {
                onInterstitialAdLoadListener.onAdFailedToLoad();
            }
        } else {
            adDataRepository.next(new AdDataSource.OnNextSuccessListener() {
                @Override
                public void onSuccess(@NonNull Ad ad) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "/onAdFailedToLoad//onSuccess" +
                                "\nad type : " + ad.type +
                                "\nad name : " + ad.name);
                    }

                    load(ad);
                }
            }, new AdDataSource.OnNextFailureListener() {
                @Override
                public void onFailure() {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "/onAdFailedToLoad//onFailure");
                    }
                }
            });
        }
    }

    public void load() {
        if (mTimerTask == null) {
            mTimerTask = new LoadTimerTask() {
                @Override
                public void run() {
                    super.run();
                    if (mHandler != null &&
                            mRunnable != null) {
                        mHandler.post(mRunnable);
                        mTimerTask.cancel();
                    }
                }
            };
        }

        if (mTimerTask.isRun()) {
            mTimerTask.cancel();
            mTimerTask = new LoadTimerTask() {
                @Override
                public void run() {
                    super.run();
                    if (mHandler != null &&
                            mRunnable != null) {

                        mHandler.post(mRunnable);
                        mTimerTask.cancel();
                    }
                }
            };
        }

        mTimer.schedule(mTimerTask, 0, TimeUnit.SECONDS.toMillis(DEFAULT_DURATION_TIME));
    }

    private void load(Ad ad) {
        switch (ad.name.getCode() * ad.type.getCode()) {
            case ADFIT * BANNER:
            case ADFIT * HALF_BANNER:
                if (adFitBanner == null) {
                    adFitBanner = new AdFitBanner(context, ad);

                    adFitBanner.setOnAdLoadListener(this);
                }

                adFitBanner.loadPreparedAd();
                break;
            case ADMOB * BANNER:
            case ADMOB * HALF_BANNER:
                if (adMobBanner == null) {
                    adMobBanner = new AdMobBanner(context, ad);

                    adMobBanner.setOnAdLoadListener(this);
                }

                adMobBanner.loadPreparedAd();
                break;
            case ADMOB * NATIVE:
                if (adMobNative == null) {
                    adMobNative = new AdMobNative(context, ad);

                    adMobNative.setOnAdLoadListener(this);
                }

                adMobNative.loadPreparedAd();
                break;
            case ADMOB * INTERSTITIAL:
                if (adMobInterstitial == null) {
                    adMobInterstitial = new AdMobInterstitial(context, ad);

                    adMobInterstitial.setOnAdLoadListener(this);
                }

                adMobInterstitial.loadPreparedAd();
                break;
            case CAULY * BANNER:
                if (caulyBanner == null) {
                    caulyBanner = new CaulyBanner(context, ad, container);

                    caulyBanner.setOnAdLoadListener(this);
                }

                caulyBanner.loadPreparedAd();
                break;
            case CAULY * NATIVE:
                if (caulyNative == null) {
                    caulyNative = new CaulyNative(context, ad);

                    caulyNative.setOnAdLoadListener(this);
                }

                caulyNative.loadPreparedAd();
                break;
            case CAULY * INTERSTITIAL:
                if (caulyInterstitial == null) {
                    caulyInterstitial = new CaulyInterstitial(context, ad);

                    caulyInterstitial.setOnAdLoadListener(this);
                }

                caulyInterstitial.loadPreparedAd();
                break;
            case FACEBOOK * BANNER:
            case FACEBOOK * HALF_BANNER:
                if (facebookBanner == null) {
                    facebookBanner = new FacebookBanner(context, ad);

                    facebookBanner.setOnAdLoadListener(this);
                }

                facebookBanner.loadPreparedAd();
                break;
            case FACEBOOK * NATIVE:
                if (facebookNative == null) {
                    facebookNative = new FacebookNative(context, ad);

                    facebookNative.setOnAdLoadListener(this);
                }

                facebookNative.loadPreparedAd();
                break;
            case FACEBOOK * INTERSTITIAL:
                if (facebookInterstitial == null) {
                    facebookInterstitial = new FacebookInterstitial(context, ad);

                    facebookInterstitial.setOnAdLoadListener(this);
                }

                facebookInterstitial.loadPreparedAd();
                break;
            case HOUSE * HALF_BANNER:
                if (houseBanner == null) {
                    houseBanner = new HouseBanner(ad);

                    houseBanner.setOnAdLoadListener(this);
                }

                houseBanner.loadPreparedAd();
                break;
            case HOUSE * NATIVE:
                if (houseNative == null) {
                    houseNative = new HouseNative(ad);

                    houseNative.setOnAdLoadListener(this);
                }

                houseNative.loadPreparedAd();
                break;
            case HOUSE * INTERSTITIAL:
                break;
            case MANPLUS * BANNER:
                if (manPlusBanner == null) {
                    manPlusBanner = new ManPlusBanner(context, ad);

                    manPlusBanner.setOnAdLoadListener(this);
                }

                manPlusBanner.loadPreparedAd();
                break;
            case MANPLUS * ENDING:
                if (manPlusEnding == null) {
                    manPlusEnding = new ManPlusEnding(context, ad);

                    manPlusEnding.setOnAdLoadListener(this);
                }

                manPlusEnding.loadPreparedAd();
                break;
            case MANPLUS * INTERSTITIAL:
                if (manPlusInterstitial == null) {
                    manPlusInterstitial = new ManPlusInterstitial(context, ad);

                    manPlusInterstitial.setOnAdLoadListener(this);
                }

                manPlusInterstitial.loadPreparedAd();
                break;
        }
    }

    public void showInterstitial() {
        if (currentBaseInterstitialThirdParty != null) {
            currentBaseInterstitialThirdParty.showPreparedAd();
        }
    }

    public void close() {
        if (mTimerTask != null
                && mTimerTask.isRun()) {
            mTimerTask.cancel();
        }

        mTimerTask = null;
    }

    public static class Builder {
        private Context context;
        private List<Ad> adList;
        private FrameLayout container;
        private AdDataRepository adDataRepository;
        private int interstitialCount;
        private OnInterstitialAdLoadListener onInterstitialAdLoadListener;
        private Boolean test_option=false;

        public Builder(@NonNull Context context) {
            this.context = context;
            adList = new ArrayList<>();
            interstitialCount = 0;
        }

        public Builder setBaseUrl(@Nullable String baseUrl) {
            adDataRepository = new AdDataRepository(baseUrl);
            return this;
        }

        public Builder setAd(@NonNull Ad ad) {
            adList.add(ad);

            return this;
        }

        public Builder setContainer(@NonNull FrameLayout container) {
            this.container = container;

            return this;
        }

        public Builder setOnInterstitialAdLoadListener(OnInterstitialAdLoadListener onInterstitialAdLoadListener) {
            this.onInterstitialAdLoadListener = onInterstitialAdLoadListener;

            return this;
        }

        public Builder setAdmangerTest(Boolean flag){
            this.test_option = flag;
            return this;
        }

        public AdManager build() {
            boolean valid = true;
            for (Ad ad : adList) {
                if ((ad.type == AdType.BANNER ||
                        ad.type == AdType.NATIVE) &&
                        container == null) {
                    valid = false;
                    break;
                }

                if (ad.type == AdType.INTERSTITIAL &&
                        container != null) {
                    valid = false;
                    break;
                } else {
                    interstitialCount++;
                }

                if (adDataRepository == null) {
                    adDataRepository = new AdDataRepository(null);
                }

                adDataRepository.add(ad);
            }
            if (adDataRepository == null) {
                valid = false;
            }
            return valid ? new AdManager(this) : null;
        }
    }

    class LoadTimerTask extends TimerTask {
        private boolean run = false;

        @Override
        public void run() {
            run = true;
        }

        @Override
        public boolean cancel() {
            run = false;

            return super.cancel();
        }

        boolean isRun() {
            return run;
        }
    }

    class LoadHandler extends Handler {
        private final WeakReference<Looper> looper;

        LoadHandler(Looper looper) {
            this.looper = new WeakReference<>(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            adDataRepository.next(new AdDataSource.OnNextSuccessListener() {
                @Override
                public void onSuccess(@NonNull Ad ad) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "//handleMessage//onSuccess" +
                                "\nad type : " + ad.type +
                                "\nad name : " + ad.name);
                    }
                    load(ad);
                }
            }, new AdDataSource.OnNextFailureListener() {
                @Override
                public void onFailure() {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "//handleMessage//onFailure");
                    }
                    onAdFailedToLoad(null);
                }
            });
        }
    }
}
