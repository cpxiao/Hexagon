package com.cpxiao.hexagon.activity;

import android.os.Bundle;

import com.cpxiao.gamelib.activity.BaseZAdsActivity;
import com.cpxiao.gamelib.fragment.BaseFragment;
import com.cpxiao.hexagon.fragment.HomeFragment;
import com.cpxiao.zads.ZAdManager;
import com.cpxiao.zads.core.ZAdPosition;

public class MainActivity extends BaseZAdsActivity {

    @Override
    protected BaseFragment getFirstFragment() {
        return HomeFragment.newInstance(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZAdManager.getInstance().init(getApplicationContext());
        loadAds();
    }

    private void loadAds() {
        initAdMobAds100("ca-app-pub-4157365005379790/9816792571");
        initFbAds90("299750750363934_512812365724437");
        loadZAds(ZAdPosition.POSITION_MAIN);
    }

    @Override
    protected void onDestroy() {
        ZAdManager.getInstance().destroyAllPosition(getApplicationContext());
        super.onDestroy();
    }
}
