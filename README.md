## AdManager
ElderberryTart version

## Changes
1. Some library version is renewed

#### Support Library
1. AdMob : 19.1.0
2. Cauly : 3.5.06
3. Facebook : 5.9.0
4. MEBA A-plus (MAN) : 105
5. AdFit : 3.0.12

#### How to make a new Library?
1. 우측 Gradle 메뉴, Execute Gradle Task 아이콘 클릭합니다.
2. Command line에 assembleRelease 입력(디버그용 라이브러리는 assembleDebug 입력)합니다.
3. Run을 클릭합니다.
4. 정상적으로 완료되면 ./app/build/outputs/aar/ 디렉토리에 라이브러리가 생성됩니다.

#### Setting
1. 생성한 AdManager.aar 파일을 libs 디렉토리에 넣습니다.
2. jCenter나, Maven Central repository에서 배포한 파일이 아니기에 빌드 과정에서 파일을 탐색할 수 있도록 아래 구문을 build.gradle(project level)추가합니다.(없어도 무방)
```
repositories { 
    flatDir { 
        dirs 'libs'
     }
 }
```
3. 아래 코드를 build.gradle(app level)에 추가합니다.
```
dependencies {
   implementation fileTree(include: ['*.aar'], dir: 'libs')
}
```
4. 아래 코드를 AndroidManifest.xml에 추가합니다.
```
<manifest>
    <application>
        <!-- Essential code -->
        <activity
            android:name="com.facebook.ads.AudienceNetworkActivity"
            android:configChanges="keyboardHidden|orientation|screenSize"
            android:hardwareAccelerated="true" />
        <activity
            android:name="com.google.android.gms.ads.AdActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
            android:theme="@android:style/Theme.Translucent" />
        <activity
            android:name="com.fsn.cauly.blackdragoncore.LandingActivity"
            android:configChanges="keyboardHidden|orientation|keyboard" />
    </application>
</manifest>
```
5. ManPlus's ending을 사용한다면 아래 코드를 attrs.xml에 추가합니다.
```
<resources>
    <declare-styleable name="com.mapps.android.view.EndingAdView">
        <attr name="pCode" format="string" />
        <attr name="mCode" format="string" />
        <attr name="sCode" format="string" />
    </declare-styleable>
</resources>
```

#### Usage
전면 광고 사용법(AdMob, Cauly, Facebook, Manplus만 사용할 수 있음)
```
public class MainActivity extends AppCompatActivity {
    private AdManager adManager;
    ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        /* 먼저 셋팅된 광고들이 먼저 호출됩니다. */
        adManager = new AdManager.Builder(context)
                .setBaseUrl(/* 광고 순서를 가져올 URL, 선택 사항 */)
                .setAd(new Ad(AdName.ADMOB, AdType.INTERSTITIAL, /* String: AdMob interstitial id */))
                .setAd(new Ad(AdName.CAULY, AdType.INTERSTITIAL, /* String: Cauly app code */))
                .setAd(new Ad(AdName.FACEBOOK, AdType.INTERSTITIAL, /* String: Facebook interstitial id */))
                .setAd(new Ad(AdName.MANPLUS, AdType.INTERSTITIAL, /* String: ManPlus publisher, media, interstitial section code (ex. "100,200,300") */)
                .setOnInterstitialAdLoadListener(new OnInterstitialAdLoadListener() {
                     @Override
                     public void onAdLoaded() {
                        adManager.showInterstitial();
                     }
             
                     @Override
                     public void onAdFailedToLoad() {
                     }
                 })
                .build();

        adManager.load();
    }

    @Override
    protected void onDestroy() {
        if (adManager != null) {
            adManager.close();

            adManager = null;
        }

        super.onDestroy();
    }
    ...
}
```
띠 배너 사용법(AdMob, AdFit, Cauly, Facebook, Manplus 모두 사용할 수 있음)
```
public class MainActivity extends AppCompatActivity {
    private AdManager adManager;
    ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        /* 먼저 셋팅된 광고들이 먼저 호출됩니다. */
        adManager = new AdManager.Builder(MainBaseActivity.this)
                .setBaseUrl(/* 광고 순서를 가져올 URL, 선택 사항 */)
                .setContainer(/* int: FrameLayout (ex. findViewById(R.id.container)) */)
                .setAd(new Ad(AdName.ADFIT, AdType.BANNER, /* String: AdFit banner id */))
                .setAd(new Ad(AdName.ADMOB, AdType.BANNER, /* String: AdMob banner id */))
                .setAd(new Ad(AdName.CAULY, AdType.BANNER, /* String: Cauly app code */))
                .setAd(new Ad(AdName.FACEBOOK, AdType.BANNER, /* String: Facebook banner id */))
                .setAd(new Ad(AdName.MANPLUS, AdType.BANNER, /* String: ManPlus publisher, media, banner section code (ex. "100,200,300") */))
                .build();

        adManager.load();
    }
    
    @Override
    public void onDetachedFromWindow() {
        if (adManager != null) {
            adManager.close();
            
            adManager = null;
        }
    
        super.onDetachedFromWindow();
    }
    ...
}
```
직사각형 배너 사용법(AdMob, AdFit, Facebook만 사용할 수 있음)
```
public class MyDialog extends Dialog {
    ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        private AdManager adManager;
        ...
        /* 먼저 셋팅된 광고들이 먼저 호출됩니다. */
        adManager = new AdManager.Builder(/* Context */)
                .setBaseUrl(/* 광고 순서를 가져올 URL, 선택 사항 */)
                .setContainer(/* int: FrameLayout (ex. findViewById(R.id.container)) */)
                .setAd(new Ad(AdName.ADFIT, AdType.HALF_BANNER, /* String: AdFit banner id */))
                .setAd(new Ad(AdName.ADMOB, AdType.HALF_BANNER, /* String: AdMob banner id */))
                .setAd(new Ad(AdName.FACEBOOK, AdType.HALF_BANNER, /* String: Facebook banner id */))
                .build();
    
        adManager.load();
    }
    
    @Override
    public void onDetachedFromWindow() {
        if (adManager != null) {
            adManager.close();
            
            adManager = null;
        }
    
        super.onDetachedFromWindow();
    }
    ...
}

/* In something that calls dialog */
public class MyActivity extends Activity {
    ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        closeDialog = new CloseDialog(this);
    
        /* Pre-load code, that only supports upper API level 21 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            closeDialog.create();
        }
    }
    ...
}
```

#### Extra Setting
```
.setBaseUrl(...)
을 사용하려면, json file로 응답해야 합니다.
형식은 아래와 같습니다.
[
  {
    "name": "ADMOB",
    "type": "INTERSTITIAL",
    "key": "ca-app-pub-.../..."
  },
  {
    "name": "CAULY",
    "type": "INTERSTITIAL",
    "key": "..."
  },
  {
    "name": "MANPLUS",
    "type": "INTERSTITIAL",
    "key": "...,...,..."
  },
  {
    "name": "FACEBOOK",
    "type": "INTERSTITIAL",
    "key": "..."
  }
] 
```

## Issue
1) 테스트앱 실행 시 광고 아이디가 유효하지 않을 경우 무한 루프 발생.


