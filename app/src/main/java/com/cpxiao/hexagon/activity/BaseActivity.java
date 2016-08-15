package com.cpxiao.hexagon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cpxiao.hexagon.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by cpxiao on 6/13/16.
 * BaseActivity
 */
public class BaseActivity extends Activity {
    protected final String TAG = getClass().getSimpleName() + "CPXIAO-";

    protected AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //隐藏状态栏部分（电池电量、时间等部分）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);

    }

    protected void initAds(String placementId) {
        // Instantiate an AdView view
        adView = new AdView(this, placementId, AdSize.BANNER_HEIGHT_50);

        // Find the main layout of your activity
        LinearLayout layout = (LinearLayout) findViewById(R.id.ads_layout);

        // Add the ad view to your activity layout
        layout.addView(adView);

        adView.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                Log.d(TAG, "onError: " + error.getErrorCode() + "," + error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "onAdLoaded: ");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "onAdClicked: ");
            }

        });
        // Request to load an ad
        adView.loadAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
