package com.cpxiao.hexagon.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpxiao.hexagon.ExtraKey;
import com.cpxiao.hexagon.GameView;
import com.cpxiao.hexagon.R;
import com.cpxiao.hexagon.onGameListener;
import com.cpxiao.utils.MediaPlayerUtils;
import com.cpxiao.utils.PreferencesUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by cpxiao on 4/9/16.
 */

public class GameActivity extends Activity implements onGameListener {
    private static final String TAG = "CPXIAO";
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


        //隐藏状态栏部分（电池电量、时间等部分）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);


        mScoreView = (TextView) findViewById(R.id.score);
        mScoreView.setText(String.valueOf(0));

        mBestView = (TextView) findViewById(R.id.best);
        mBestView.setText(String.valueOf(PreferencesUtils.getInt(this, ExtraKey.KEY_BEST_SCORE, 0)));


        LinearLayout layout = (LinearLayout) findViewById(R.id.game_view);
        int gameType = getIntent().getIntExtra(ExtraKey.GAME_TYPE, 5);
        mGameView = new GameView(GameActivity.this, gameType);
        mGameView.setGameListener(this);

//        mGameView.setOnGameListener(this);
        layout.addView(mGameView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
        intent.putExtra(ExtraKey.GAME_TYPE, gameType);
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
        dialog.show();
    }
}
