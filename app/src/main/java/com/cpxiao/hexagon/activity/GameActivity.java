package com.cpxiao.hexagon.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpxiao.androidutils.library.utils.MediaPlayerUtils;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.hexagon.Extra;
import com.cpxiao.hexagon.R;
import com.cpxiao.hexagon.imps.OnGameListener;
import com.cpxiao.hexagon.mode.GameMode;
import com.cpxiao.hexagon.views.DialogUtils;
import com.cpxiao.hexagon.views.GameView;
import com.cpxiao.hexagon.views.SettingsDialog;
import com.cpxiao.lib.activity.BaseActivity;

/**
 * GameActivity
 *
 * @author cpxiao on 2016/4/9.
 */

public class GameActivity extends BaseActivity implements OnGameListener {
    /**
     * 游戏类型
     */
    private int mGameMode;
    /**
     * 分数
     */
    private int mBestScore;
    /**
     * 当前分数view
     */
    private TextView mScoreView;
    /**
     * 最高分view
     */
    private TextView mBestScoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mGameMode = getIntent().getIntExtra(Extra.Name.GAME_MODE, GameMode.MODE_DEFAULT);
        MediaPlayerUtils.getInstance().init(this, R.raw.hexagon_bgm);

        initWidget();
        initFbAds50("299750750363934_299751660363843");
        initAdMobAds50("ca-app-pub-4157365005379790/1280015663");
    }

    protected void initWidget() {
        mScoreView = (TextView) findViewById(R.id.score);
        mBestScoreView = (TextView) findViewById(R.id.best_score);

        //Settings Btn
        ImageView settingsView = (ImageView) findViewById(R.id.btn_settings);
        settingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SettingsDialog dialog = new SettingsDialog(GameActivity.this);
                dialog.show();
            }
        });

        LinearLayout gameViewLayout = (LinearLayout) findViewById(R.id.game_view_layout);

        setScoreView(0);

        mBestScore = getBestScore();
        setBestScoreView(mBestScore);

        /**
         *创建游戏View
         */
        GameView gameView = new GameView(GameActivity.this, mGameMode);
        gameView.setGameListener(this);

        gameViewLayout.addView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isMusicOn = PreferencesUtils.getBoolean(getApplicationContext(), Extra.Key.SETTING_MUSIC, Extra.Key.SETTING_MUSIC_DEFAULT);
        if (isMusicOn) {
            MediaPlayerUtils.getInstance().start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayerUtils.getInstance().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerUtils.getInstance().stop();
    }


    public static Intent makeIntent(Context context, int gameType) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(Extra.Name.GAME_MODE, gameType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    public void onScoreChange(int score) {
        setScoreView(score);
        if (score > mBestScore) {
            mBestScore = score;
            setBestScore(mBestScore);
        }
        setBestScoreView(mBestScore);
    }

    @Override
    public void onGameOver() {
        DialogUtils.createGameOverDialog(this,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(makeIntent(GameActivity.this, mGameMode));
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    @Override
    public void onSuccess() {

    }

    protected void setScoreView(int score) {
        if (mScoreView != null) {
            mScoreView.setText(String.valueOf(score));
        }
    }

    protected void setBestScoreView(int bestScore) {
        if (mBestScoreView != null) {
            String bestScoreText = getResources().getText(R.string.btn_best_score) + ": " + String.valueOf(bestScore);
            mBestScoreView.setText(bestScoreText);
        }
    }

    private int getBestScore() {
        int bestScore = 0;
        if (mGameMode == GameMode.MODE_R_5) {
            bestScore = PreferencesUtils.getInt(this, Extra.Key.BEST_SCORE, 0);
        } else if (mGameMode == GameMode.MODE_R_6) {
            bestScore = PreferencesUtils.getInt(this, Extra.Key.BEST_SCORE_R6, 0);
        } else if (mGameMode == GameMode.MODE_R_7) {
            bestScore = PreferencesUtils.getInt(this, Extra.Key.BEST_SCORE_R7, 0);
        }
        return bestScore;
    }

    private void setBestScore(int bestScore) {
        if (mGameMode == GameMode.MODE_R_5) {
            PreferencesUtils.putInt(this, Extra.Key.BEST_SCORE, bestScore);
        } else if (mGameMode == GameMode.MODE_R_6) {
            PreferencesUtils.putInt(this, Extra.Key.BEST_SCORE_R6, bestScore);
        } else if (mGameMode == GameMode.MODE_R_7) {
            PreferencesUtils.putInt(this, Extra.Key.BEST_SCORE_R7, bestScore);
        }
    }

}
