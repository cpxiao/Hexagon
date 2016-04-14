package com.cpxiao.hexagon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by cpxiao on 4/9/16.
 */

public class GameActivity extends Activity implements onGameListener {
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
    private GameView gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏状态栏部分（电池电量、时间等部分）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);


        mScoreView = (TextView) findViewById(R.id.score);
        mBestView = (TextView) findViewById(R.id.best);

        mScoreView.setText(String.valueOf(0));

        mBestView.setText(String.valueOf(0));


        LinearLayout layout = (LinearLayout) findViewById(R.id.game_view);
        gameView = new GameView(GameActivity.this, 5);
        gameView.setGameListener(this);

//        gameView.setOnGameListener(this);
        layout.addView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void come2me(Context context) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onScoreChange(int score, int bestScore) {
        mScoreView.setText(String.valueOf(score));
        mBestView.setText(String.valueOf(bestScore));
    }

    @Override
    public void onGameOver() {

    }
}
