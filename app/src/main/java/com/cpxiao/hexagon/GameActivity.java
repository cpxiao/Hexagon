package com.cpxiao.hexagon;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);


        mScoreView = (TextView) findViewById(R.id.score);
        mBestView = (TextView) findViewById(R.id.best);

        mScoreView.setText(String.valueOf(0));

        mBestView.setText(String.valueOf(0));


        LinearLayout layout = (LinearLayout) findViewById(R.id.game_view);
        mGameView = new GameView(GameActivity.this, 6);
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
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Try Again ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
