package com.cpxiao.hexagon.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cpxiao.R;
import com.cpxiao.gamelib.activity.BaseZAdsActivity;
import com.cpxiao.hexagon.mode.extra.GameMode;
import com.cpxiao.hexagon.views.dialog.BestScoreDialog;
import com.cpxiao.zads.ZAdManager;
import com.cpxiao.zads.core.ZAdPosition;

/**
 * HomeActivity
 *
 * @author cpxiao on 2016/5/5.
 */
public class HomeActivity extends BaseZAdsActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initWidget();
//        initFbAds50("299750750363934_299751287030547");
//        initAdMobAds50("ca-app-pub-4157365005379790/4701236068");

        ZAdManager.getInstance().init(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_ads);
        ZAdManager.getInstance().loadAd(this, ZAdPosition.POSITION_HOME, layout);
    }

    @Override
    protected void onDestroy() {
        ZAdManager.getInstance().destroyAllPosition(this);
        super.onDestroy();
    }

    protected void initWidget() {
        Button newGameR5Button = (Button) findViewById(R.id.btn_r5);
        Button newGameR6Button = (Button) findViewById(R.id.btn_r6);
        Button newGameR7Button = (Button) findViewById(R.id.btn_r7);
        Button newGameR8Button = (Button) findViewById(R.id.btn_r8);
        Button bestScoreButton = (Button) findViewById(R.id.btn_best_score);
        Button settingsButton = (Button) findViewById(R.id.btn_settings);
        Button quitButton = (Button) findViewById(R.id.btn_quit);

        newGameR5Button.setOnClickListener(this);
        newGameR6Button.setOnClickListener(this);
        newGameR7Button.setOnClickListener(this);
        newGameR8Button.setOnClickListener(this);
        bestScoreButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_r5) {
            startActivity(GameActivity.makeIntent(HomeActivity.this, GameMode.MODE_R_5));
        } else if (id == R.id.btn_r6) {
            startActivity(GameActivity.makeIntent(HomeActivity.this, GameMode.MODE_R_6));
        } else if (id == R.id.btn_r7) {
            startActivity(GameActivity.makeIntent(HomeActivity.this, GameMode.MODE_R_7));
        } else if (id == R.id.btn_r8) {
            startActivity(GameActivity.makeIntent(HomeActivity.this, GameMode.MODE_R_8));
        } else if (id == R.id.btn_best_score) {
            final BestScoreDialog dialog = new BestScoreDialog(HomeActivity.this);
            dialog.setButtonOK(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (id == R.id.btn_settings) {
            Intent intent = SettingsActivity.makeIntent(HomeActivity.this, null);
            startActivity(intent);
        } else if (id == R.id.btn_quit) {
            showQuitConfirmDialog();
        }
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        showQuitConfirmDialog();
    }

    private void showQuitConfirmDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                //                .setTitle(R.string.quit_msg)
                .setMessage(R.string.quit_msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        //            dialog.setCancelable(true);
        //            dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
