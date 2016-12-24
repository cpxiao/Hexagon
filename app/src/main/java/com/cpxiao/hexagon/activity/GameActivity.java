package com.cpxiao.hexagon.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
    protected int mBestScore;
    /**
     * 当前分数view
     */
    protected TextView mScoreView;
    /**
     * 最高分view
     */
    protected TextView mBestScoreView;
    /**
     * Game View Layout
     */
    protected LinearLayout mGameViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mGameMode = getIntent().getIntExtra(Extra.Name.GAME_MODE, GameMode.MODE_DEFAULT);
        MediaPlayerUtils.getInstance().init(this, R.raw.hexagon_bgm);

        initWidget();
        initFbAds50("299750750363934_299751660363843");
    }

    protected void initWidget() {
        mScoreView = (TextView) findViewById(R.id.score);
        mBestScoreView = (TextView) findViewById(R.id.best_score);
        mGameViewLayout = (LinearLayout) findViewById(R.id.game_view_layout);
        setScoreView(0);

        mBestScore = getBestScore();
        setBestScoreView(mBestScore);

        /**
         *创建游戏View
         */
        GameView gameView = new GameView(GameActivity.this, mGameMode);
        gameView.setGameListener(this);

        mGameViewLayout.addView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayerUtils.getInstance().start();
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

    public static void come2me(Context context, int gameType) {
        Intent intent = makeIntent(context, gameType);
        context.startActivity(intent);
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
