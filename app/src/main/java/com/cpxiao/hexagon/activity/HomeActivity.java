package com.cpxiao.hexagon.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cpxiao.hexagon.R;
import com.cpxiao.hexagon.mode.GameMode;
import com.cpxiao.hexagon.views.BestScoreDialog;
import com.cpxiao.lib.activity.BaseActivity;

/**
 * HomeActivity
 *
 * @author cpxiao on 2016/5/5.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    protected Button mNewGameR5Button;
    protected Button mNewGameR6Button;
    protected Button mNewGameR7Button;
    protected Button mBestScoreButton;
    protected Button mQuitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initWidget();
        initFbAds50("299750750363934_299751287030547");

    }

    protected void initWidget() {

        mNewGameR5Button = (Button) findViewById(R.id.btn_r5);
        mNewGameR6Button = (Button) findViewById(R.id.btn_r6);
        mNewGameR7Button = (Button) findViewById(R.id.btn_r7);
        mBestScoreButton = (Button) findViewById(R.id.btn_best_score);
        mQuitButton = (Button) findViewById(R.id.btn_quit);

        mNewGameR5Button.setOnClickListener(this);
        mNewGameR6Button.setOnClickListener(this);
        mNewGameR7Button.setOnClickListener(this);
        mBestScoreButton.setOnClickListener(this);
        mQuitButton.setOnClickListener(this);
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
        } else if (id == R.id.btn_quit) {
            finish();
        }
    }
}
