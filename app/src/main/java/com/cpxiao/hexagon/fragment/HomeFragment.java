package com.cpxiao.hexagon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.RateAppUtils;
import com.cpxiao.androidutils.library.utils.ShareAppUtils;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.hexagon.mode.extra.GameMode;
import com.cpxiao.hexagon.views.dialog.BestScoreDialog;
import com.cpxiao.zads.core.ZAdPosition;


/**
 * @author cpxiao on 2017/09/06.
 */

public class HomeFragment extends BaseZAdsFragment implements View.OnClickListener {

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        loadZAds(ZAdPosition.POSITION_HOME);

        Button newGameR5Button = (Button) view.findViewById(R.id.btn_r5);
        Button newGameR6Button = (Button) view.findViewById(R.id.btn_r6);
        Button newGameR7Button = (Button) view.findViewById(R.id.btn_r7);
        Button newGameR8Button = (Button) view.findViewById(R.id.btn_r8);
        ImageButton bestRateApp = (ImageButton) view.findViewById(R.id.btn_rate_app);
        ImageButton bestShare = (ImageButton) view.findViewById(R.id.btn_share);
        ImageButton bestScoreButton = (ImageButton) view.findViewById(R.id.btn_best_score);
        ImageButton settingsButton = (ImageButton) view.findViewById(R.id.btn_settings);

        newGameR5Button.setOnClickListener(this);
        newGameR6Button.setOnClickListener(this);
        newGameR7Button.setOnClickListener(this);
        newGameR8Button.setOnClickListener(this);
        bestRateApp.setOnClickListener(this);
        bestShare.setOnClickListener(this);
        bestScoreButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void onClick(View v) {
        Context context = getHoldingActivity();
        int id = v.getId();
        if (id == R.id.btn_r5) {
            Bundle bundle = GameFragment.makeBundle(context, GameMode.MODE_R_5);
            addFragment(GameFragment.newInstance(bundle));
        } else if (id == R.id.btn_r6) {

            Bundle bundle = GameFragment.makeBundle(context, GameMode.MODE_R_6);
            addFragment(GameFragment.newInstance(bundle));
        } else if (id == R.id.btn_r7) {

            Bundle bundle = GameFragment.makeBundle(context, GameMode.MODE_R_7);
            addFragment(GameFragment.newInstance(bundle));
        } else if (id == R.id.btn_r8) {

            Bundle bundle = GameFragment.makeBundle(context, GameMode.MODE_R_8);
            addFragment(GameFragment.newInstance(bundle));
        } else if (id == R.id.btn_rate_app) {
            RateAppUtils.rate(context);
        } else if (id == R.id.btn_share) {
            String msg = getString(R.string.share_msg) + "\n" +
                    getString(R.string.app_name) + "\n" +
                    "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            ShareAppUtils.share(context, getString(R.string.share), msg);
        } else if (id == R.id.btn_best_score) {
            showBestScoreDialog(context);
        } else if (id == R.id.btn_settings) {
            addFragment(SettingsFragment.newInstance(null));
        }
    }

    private void showBestScoreDialog(Context context) {
        final BestScoreDialog dialog = new BestScoreDialog(context);
        dialog.setButtonOK(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}
