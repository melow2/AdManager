package com.example.app;

import android.content.Context;
import android.view.View;
import android.webkit.URLUtil;

import com.example.app.databinding.MainBinder;

/**
 * @auther hyeoksin
 * @since
 */
public class MainClickListener implements View.OnClickListener {

    private MainButtonListenerInterface listener;
    private Context context;
    private MainBinder binder;

    public MainClickListener(Context context, MainBinder binder, MainButtonListenerInterface listener) {
        this.context = context;
        this.listener = listener;
        this.binder = binder;
    }

    interface MainButtonListenerInterface {
        void CheckUrl(Boolean valid);        // URL 체크

        void Interstitial();                // 전면광고

        void StripBanner();                 // 띠배너

        void RecBanner();                   // 직사각형배너

        void Native();                      // 네이티브

        void Test();

        void Reset();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cbx_url:
                String url = binder.edtUrl.getText().toString();
                Boolean valid = URLUtil.isValidUrl(url);
                listener.CheckUrl(valid);
                break;
            case R.id.rbt_interstitial:
                listener.Interstitial();
                break;
            case R.id.rbt_strip_banner:
                listener.StripBanner();
                break;
            case R.id.rbt_rec_banner:
                listener.RecBanner();
                break;
            case R.id.rbt_native:
                listener.Native();
                break;
            case R.id.btn_test:
                listener.Test();
                break;
            case R.id.btn_reset:
                listener.Reset();
                break;
        }
    }
}
