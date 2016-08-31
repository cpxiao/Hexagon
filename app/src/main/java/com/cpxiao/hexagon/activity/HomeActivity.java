package com.cpxiao.hexagon.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.cpxiao.commonlibrary.utils.PreferencesUtils;
import com.cpxiao.hexagon.R;
import com.cpxiao.minigamelib.ExtraKey;
import com.cpxiao.minigamelib.activity.CommonHomeActivity;

/**
 * HomeActivity
 */
public class HomeActivity extends CommonHomeActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWidget();
        initSmallAds("299750750363934_299751287030547");

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mLayout.setBackgroundResource(R.drawable.hexagon_bg);

        mContinueButton.setVisibility(View.GONE);

        mNewGameButton.setOnClickListener(this);

        mBestScoreButton.setOnClickListener(this);

        mSettingsButton.setVisibility(View.GONE);
        mHelpButton.setVisibility(View.GONE);
        mMoreGamesButton.setVisibility(View.GONE);
        mAboutMeButton.setVisibility(View.GONE);

        mQuitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_new_game) {
            GameActivity.come2me(HomeActivity.this, 5);
        } else if (id == R.id.btn_best_score) {
            AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                    .setTitle(R.string.btn_best_score)
                    .setMessage(String.valueOf(PreferencesUtils.getInt(this, ExtraKey
                            .KEY_BEST_SCORE, 0)))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create();
            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
            dialog.show();
        } else if (id == R.id.btn_quit) {
            finish();
        }
    }
}
