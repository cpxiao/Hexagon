package com.cpxiao.hexagon.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpxiao.commonlibrary.utils.MediaPlayerUtils;
import com.cpxiao.commonlibrary.utils.PreferencesUtils;
import com.cpxiao.hexagon.ExtraKey;
import com.cpxiao.hexagon.GameView;
import com.cpxiao.hexagon.OnGameListener;
import com.cpxiao.hexagon.R;

/**
 * Created by cpxiao on 4/9/16.
 * GameActivity
 */

public class GameActivity extends BaseActivity implements OnGameListener {
    private static final String TAG = GameActivity.class.getSimpleName();
    /**
     * 当前分数
     */
    private TextView mScoreView;
    /**
     * 最高分
     */
    private TextView mBestView;
    /**
     * 游戏View
     */
    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mScoreView = (TextView) findViewById(R.id.score);
        mScoreView.setText(String.valueOf(0));

        mBestView = (TextView) findViewById(R.id.best);
        mBestView.setText(String.valueOf(PreferencesUtils.getInt(this, ExtraKey.KEY_BEST_SCORE, 0)));


        LinearLayout layout = (LinearLayout) findViewById(R.id.game_view);
        int gameType = getIntent().getIntExtra(ExtraKey.INTENT_EXTRA_GAME_TYPE, 5);
        mGameView = new GameView(GameActivity.this, gameType);
        mGameView.setGameListener(this);

        layout.addView(mGameView);

        MediaPlayerUtils.getInstance().init(this, R.raw.hexagon_bgm);

        initAds("299750750363934_299751660363843");
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

    private void saveBestScore(int score) {
        int bestScore = PreferencesUtils.getInt(this, ExtraKey.KEY_BEST_SCORE, 0);
        bestScore = Math.max(score, bestScore);
        PreferencesUtils.putInt(this, ExtraKey.KEY_BEST_SCORE, bestScore);
    }

    public static void come2me(Context context, int gameType) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(ExtraKey.INTENT_EXTRA_GAME_TYPE, gameType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onScoreChange(int score, int bestScore) {
        mScoreView.setText(String.valueOf(score));
        mBestView.setText(String.valueOf(bestScore));

        if (score >= bestScore) {
            saveBestScore(score);
        }
    }

    @Override
    public void onGameOver() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.game_over))
                .setMessage(getString(R.string.try_again))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity.come2me(GameActivity.this, 5);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
}
