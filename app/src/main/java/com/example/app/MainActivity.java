package com.example.app;

import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.hyeoksin.admanager.AdManager;
import com.hyeoksin.admanager.OnInterstitialAdLoadListener;
import com.hyeoksin.admanager.data.Ad;
import com.hyeoksin.admanager.data.AdName;
import com.hyeoksin.admanager.data.AdType;
import com.example.app.databinding.MainBinder;

public class MainActivity extends AppCompatActivity {

    private AdManager adManager;
    private Context context;
    private MainBinder binding;
    private MainClickListener buttonLIstener;

    final String INTERSTITIAL = "전면광고";
    final String STRIP_BANNER = "배너광고(띠)";
    final String REC_BANNER = "배너광고(직사각형)";
    final String NATIVE = "네이티브광고";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.edtUrl.setText(getText(R.string.default_base_url));
        setUpListener();
    }

    private void setUpListener() {
        buttonLIstener = new MainClickListener(this, binding, new MainClickListener.MainButtonListenerInterface() {
            @Override
            public void CheckUrl(Boolean valid) {
                if (!valid) {
                    binding.tvLog.append("***URL이 유효하지않습니다.***" + "\n");
                    binding.cbxUrl.setChecked(false);
                }
            }

            @Override
            public void Interstitial() {
                binding.edtUrl.setText(getText(R.string.default_base_url));
                binding.edtUrl.append("interstitial.json");
                binding.tvLog.append("[전면광고]를 선택했습니다.\n");
            }

            @Override
            public void StripBanner() {
                binding.edtUrl.setText(getText(R.string.default_base_url));
                binding.edtUrl.append("banner.json");
                binding.tvLog.append("[배너광고(띠)]를 선택했습니다.\n");
            }

            @Override
            public void RecBanner() {
                binding.edtUrl.setText(getText(R.string.default_base_url));
                binding.edtUrl.append("half_banner.json");
                binding.tvLog.append("[배너광고(직사각형)]를 선택했습니다.\n");
            }

            @Override
            public void Native() {
                binding.edtUrl.setText(getText(R.string.default_base_url));
                binding.edtUrl.append("native.json");
                binding.tvLog.append("[네이티브광고]를 선택했습니다.\n");
            }

            @Override
            public void Test() {
                RadioGroup rg = binding.rgpOptions;
                RadioButton rbt = (RadioButton)findViewById(rg.getCheckedRadioButtonId());
                if(rbt==null){
                    Toast.makeText(context,"테스트 타입을 선택해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                String type = rbt.getText().toString();
                AdManager.Builder builder = new AdManager.Builder(context);
                if (binding.cbxUrl.isChecked()) {
                    String baseUrl = binding.edtUrl.getText().toString();
                    switch (type){
                        case INTERSTITIAL:
                            binding.tvLog.append("[전면광고] 테스트를 시작합니다.\n");
                            adManager = new AdManager.Builder(context)
                                    .setAdmangerTest(true)
                                    //.setBaseUrl("http://trot.mobilelink.xyz/Trot/coupon2/ondisk/interstitial.json")
                                    .setAd(new Ad(AdName.FACEBOOK,AdType.INTERSTITIAL,"901009453626141_902463743480712"))
                                    .setAd(new Ad(AdName.FACEBOOK,AdType.INTERSTITIAL,getText(R.string.default_facebook_interstitial).toString()))
                                    .setAd(new Ad(AdName.ADMOB,AdType.INTERSTITIAL,getText(R.string.default_admob_interstitial).toString()))
                                    .setAd(new Ad(AdName.CAULY,AdType.INTERSTITIAL,getText(R.string.default_cauly_interstitial).toString()))
                                    .setOnInterstitialAdLoadListener(new OnInterstitialAdLoadListener() {
                                        @Override
                                        public void onAdLoaded() {
                                            binding.tvLog.append("[전면광고] 테스트 성공.\n");
                                            adManager.showInterstitial();
                                        }

                                        @Override
                                        public void onAdFailedToLoad() {
                                            binding.tvLog.append("[전면광고] 테스트 실패.\n");
                                        }
                                    })
                                    .build();
                            adManager.load();
                            break;
                        case STRIP_BANNER:
                            binding.tvLog.append("[배너광고(띠)] 테스트를 시작합니다.\n");
                            adManager = new AdManager.Builder(context)
                                    .setAdmangerTest(true)
                                    .setBaseUrl("http://trot.mobilelink.xyz/Trot/coupon2/ondisk/banner.json")
                                    .setContainer(binding.lytBanner)
                                    .setAd(new Ad(AdName.ADMOB,AdType.BANNER,getText(R.string.default_admob_banner).toString()))
                                    .setAd(new Ad(AdName.CAULY,AdType.BANNER,getText(R.string.default_cauly_banner).toString()))
                                    .setAd(new Ad(AdName.FACEBOOK,AdType.BANNER,getText(R.string.default_facebook_banner).toString()))
                                    .build();
                            adManager.load();
                            break;
                        case REC_BANNER: // Admob, Adfit, Facebook만 사용가능 함.
                            binding.tvLog.append("[배너광고(직사각형)] 테스트를 시작합니다.\n");
                            adManager = new AdManager.Builder(context)
                                    .setAdmangerTest(true)
                                    .setBaseUrl(baseUrl)
                                    .setContainer(binding.lytBanner)
                                    .setAd(new Ad(AdName.ADMOB,AdType.HALF_BANNER,getText(R.string.default_admob_banner).toString()))
                                    .setAd(new Ad(AdName.FACEBOOK,AdType.HALF_BANNER,getText(R.string.default_facebook_banner).toString()))
                                    .build();
                            adManager.load();
                            break;
                        case NATIVE:
                            binding.tvLog.append("[네이티브광고] 테스트를 시작합니다.\n");
                            adManager = new AdManager.Builder(context)
                                    .setBaseUrl(baseUrl)
                                    //.setContainer(binding.lytBanner)
                                    .setAd(new Ad(AdName.FACEBOOK,AdType.NATIVE,getText(R.string.default_facebook_native).toString()))
                                    .setAd(new Ad(AdName.ADMOB,AdType.NATIVE,getText(R.string.default_admob_native).toString()))
                                    .build();
                            /* 네이티브 광고 페이스북 키 필요 함.*/
                            adManager.load();
                            break;
                    }
                } else{
                    switch (type){
                        case INTERSTITIAL:
                            binding.tvLog.append("[전면광고] 테스트를 시작합니다.\n");
                            adManager = new AdManager.Builder(context)
//                                    .setAd(new Ad(AdName.CAULY,AdType.INTERSTITIAL,getText(R.string.default_cauly_interstitial).toString()))
                                    .setAd(new Ad(AdName.FACEBOOK,AdType.INTERSTITIAL,"901009453626141_902463743480712"))
                                    .setAd(new Ad(AdName.CAULY,AdType.INTERSTITIAL,"QAvzGHj5"))
                                    .setAd(new Ad(AdName.ADMOB,AdType.INTERSTITIAL,"ca-app-pub-2462990331209005/8186472396"))
                                    .setAd(new Ad(AdName.MANPLUS,AdType.INTERSTITIAL,"1409,31898,804144"))
//                                    .setAd(new Ad(AdName.FACEBOOK,AdType.INTERSTITIAL,getText(R.string.default_facebook_interstitial).toString()))
                                    .setOnInterstitialAdLoadListener(new OnInterstitialAdLoadListener() {
                                        @Override
                                        public void onAdLoaded() {
                                            binding.tvLog.append("[전면광고] 테스트 성공.\n");
                                            adManager.showInterstitial();
                                        }

                                        @Override
                                        public void onAdFailedToLoad() {
                                            binding.tvLog.append("[전면광고] 테스트 실패.\n");
                                        }
                                    })
                                    .build();
                            adManager.load();
                            break;
                        case STRIP_BANNER:
                            binding.tvLog.append("[배너광고(띠)] 테스트를 시작합니다.\n");
                            adManager = new AdManager.Builder(context)
                                    .setContainer(findViewById(R.id.lyt_banner))
//                                    .setAd(new Ad(AdName.ADMOB,AdType.BANNER,getText(R.string.default_admob_banner).toString()))
//                                    .setAd(new Ad(AdName.CAULY,AdType.BANNER,getText(R.string.default_cauly_banner).toString()))
                                    .setAd(new Ad(AdName.FACEBOOK,AdType.BANNER,getText(R.string.default_facebook_banner).toString()))
                                    .build();
                            adManager.load();
                            break;
                        case REC_BANNER: //AdMob, AdFit, Facebook 만 사용가능 함.
                            binding.tvLog.append("[배너광고(직사각형)] 테스트를 시작합니다.\n");
                            adManager = new AdManager.Builder(context)
                                    .setContainer(binding.lytBanner)
//                                    .setAd(new Ad(AdName.ADMOB,AdType.HALF_BANNER,getText(R.string.default_admob_banner).toString()))
                                    .setAd(new Ad(AdName.FACEBOOK,AdType.HALF_BANNER,getText(R.string.default_facebook_banner).toString()))
                                    .build();
                            adManager.load();
                            break;
                        case NATIVE:
                            binding.tvLog.append("[네이티브광고] 테스트를 시작합니다.\n");
                            adManager = new AdManager.Builder(context)
                                    .setContainer(binding.lytBanner)
                                    .setAd(new Ad(AdName.ADMOB,AdType.NATIVE,getText(R.string.default_admob_native).toString()))
                                    .setAd(new Ad(AdName.FACEBOOK,AdType.NATIVE,getText(R.string.default_facebook_native).toString()))
                                    .build();
                            /* 네이티브 광고 페이스북 키 필요 함.*/
                            adManager.load();
                            break;
                    }
                }
            }

            @Override
            public void Reset() {
                adManager=null;
                binding.lytBanner.removeAllViews();
                binding.tvLog.setText("");
                binding.tvLog.append("[팝업광고] 테스트를 시작합니다.\n");
                adManager = new AdManager.Builder(context)
                        .setContainer(binding.lytBanner)
                        .setBaseUrl("http://trot.mobilelink.xyz/Trot/coupon2/ondisk/popup_banner.json")
                        .build();
                adManager.load();
            }
        });

        binding.cbxUrl.setOnClickListener(buttonLIstener);
        binding.rbtInterstitial.setOnClickListener(buttonLIstener);
        binding.rbtStripBanner.setOnClickListener(buttonLIstener);
        binding.rbtRecBanner.setOnClickListener(buttonLIstener);
        binding.rbtNative.setOnClickListener(buttonLIstener);
        binding.btnTest.setOnClickListener(buttonLIstener);
        binding.btnReset.setOnClickListener(buttonLIstener);

    }

}
