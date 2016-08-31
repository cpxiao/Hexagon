package com.cpxiao.hexagon.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cpxiao.commonlibrary.utils.MediaPlayerUtils;
import com.cpxiao.commonlibrary.utils.PreferencesUtils;
import com.cpxiao.hexagon.GameView;
import com.cpxiao.hexagon.R;
import com.cpxiao.minigamelib.ExtraKey;
import com.cpxiao.minigamelib.OnGameListener;
import com.cpxiao.minigamelib.activity.CommonGameActivity;
import com.cpxiao.minigamelib.views.DialogUtils;

/**
 * Created by cpxiao on 4/9/16.
 * GameActivity
 */

public class GameActivity extends CommonGameActivity implements OnGameListener {
    private int mGameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MediaPlayerUtils.getInstance().init(this, R.raw.hexagon_bgm);

        initWidget();
        initSmallAds("299750750363934_299751660363843");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mScoreView.setTextColor(ContextCompat.getColor(this, R.color.color_text_score));
        setScore(0);

        mBestScoreView.setTextColor(ContextCompat.getColor(this, R.color.color_text_best));
        mBestScore = PreferencesUtils.getInt(this, ExtraKey.KEY_BEST_SCORE, 0);
        setBestScore(mBestScore);

        mLifeBar.setVisibility(View.GONE);

        mSettingBtn.setVisibility(View.GONE);

        /**
         *创建游戏View
         */
        mGameType = getIntent().getIntExtra(ExtraKey.INTENT_EXTRA_GAME_TYPE, 5);
        GameView gameView = new GameView(GameActivity.this, mGameType);
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


    public static void come2me(Context context, int gameType) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(ExtraKey.INTENT_EXTRA_GAME_TYPE, gameType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onScoreChange(int score) {
        setScore(score);
        if (score > mBestScore) {
            mBestScore = score;
            PreferencesUtils.putInt(this, ExtraKey.KEY_BEST_SCORE, mBestScore);
        }
        setBestScore(mBestScore);
    }

    @Override
    public void onGameOver() {
        DialogUtils.createGameOverDialog(this,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity.come2me(GameActivity.this, mGameType);
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
}
