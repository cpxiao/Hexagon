package com.cpxiao.hexagon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cpxiao.gamelib.activity.BaseActivity;
import com.cpxiao.hexagon.R;
import com.cpxiao.hexagon.mode.GameMode;
import com.cpxiao.hexagon.views.BestScoreDialog;

/**
 * HomeActivity
 *
 * @author cpxiao on 2016/5/5.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initWidget();
        initFbAds50("299750750363934_299751287030547");
        initAdMobAds50("ca-app-pub-4157365005379790/4701236068");
    }

    protected void initWidget() {
        Button newGameR5Button = (Button) findViewById(R.id.btn_r5);
        Button newGameR6Button = (Button) findViewById(R.id.btn_r6);
        Button newGameR7Button = (Button) findViewById(R.id.btn_r7);
        Button bestScoreButton = (Button) findViewById(R.id.btn_best_score);
        Button settingsButton = (Button) findViewById(R.id.btn_settings);
        Button quitButton = (Button) findViewById(R.id.btn_quit);

        newGameR5Button.setOnClickListener(this);
        newGameR6Button.setOnClickListener(this);
        newGameR7Button.setOnClickListener(this);
        bestScoreButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
        quitButton.setVisibility(View.GONE);
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
            finish();
        }
    }
}
