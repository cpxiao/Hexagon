//package com.cpxiao.hexagon.activity;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.cpxiao.R;
//import com.cpxiao.androidutils.library.utils.MediaPlayerUtils;
//import com.cpxiao.androidutils.library.utils.PreferencesUtils;
//import com.cpxiao.gamelib.activity.BaseZAdsActivity;
//import com.cpxiao.hexagon.imps.OnGameListener;
//import com.cpxiao.hexagon.mode.extra.Extra;
//import com.cpxiao.hexagon.mode.extra.GameMode;
//import com.cpxiao.hexagon.views.GameView;
//import com.cpxiao.hexagon.views.dialog.SettingsDialog;
//import com.cpxiao.zads.ZAdManager;
//import com.cpxiao.zads.core.ZAdPosition;
//
///**
// * GameActivity
// *
// * @author cpxiao on 2016/4/9.
// */
//
//public class GameActivity extends BaseZAdsActivity implements OnGameListener {
//    /**
//     * 游戏类型
//     */
//    private int mGameMode;
//    /**
//     * 分数
//     */
//    private int mBestScore;
//    /**
//     * 当前分数view
//     */
//    private TextView mScoreView;
//    /**
//     * 最高分view
//     */
//    private TextView mBestScoreView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_game);
//        mGameMode = getIntent().getIntExtra(Extra.Name.GAME_MODE, GameMode.MODE_DEFAULT);
//        MediaPlayerUtils.getInstance().init(this, R.raw.hexagon_bgm);
//
//        initWidget();
////        initFbAds50("299750750363934_299751660363843");
////        initAdMobAds50("ca-app-pub-4157365005379790/1280015663");
//
//        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_ads);
//        ZAdManager.getInstance().loadAd(this, ZAdPosition.POSITION_GAME, layout);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        boolean isMusicOn = PreferencesUtils.getBoolean(getApplicationContext(), Extra.Key.SETTING_MUSIC, Extra.Key.SETTING_MUSIC_DEFAULT);
//        if (isMusicOn) {
//            MediaPlayerUtils.getInstance().start();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MediaPlayerUtils.getInstance().pause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        ZAdManager.getInstance().destroy(this, ZAdPosition.POSITION_GAME);
//        super.onDestroy();
//        MediaPlayerUtils.getInstance().stop();
//    }
//
//    protected void initWidget() {
//        mScoreView = (TextView) findViewById(R.id.score);
//        mBestScoreView = (TextView) findViewById(R.id.best_score);
//
//        //Settings Btn
//        ImageView settingsView = (ImageView) findViewById(R.id.btn_settings);
//        settingsView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final SettingsDialog dialog = new SettingsDialog(GameActivity.this);
//                dialog.show();
//            }
//        });
//
//        LinearLayout gameViewLayout = (LinearLayout) findViewById(R.id.game_view_layout);
//
//        setScoreView(0);
//
//        mBestScore = getBestScore();
//        setBestScoreView(mBestScore);
//
//        /*创建游戏View*/
//        GameView gameView = new GameView(GameActivity.this, mGameMode);
//        gameView.setGameListener(this);
//
//        gameViewLayout.addView(gameView);
//    }
//
//
//
//
//    public static Intent makeIntent(Context context, int gameType) {
//        Intent intent = new Intent(context, GameActivity.class);
//        intent.putExtra(Extra.Name.GAME_MODE, gameType);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        return intent;
//    }
//
//    @Override
//    public void onScoreChange(int score) {
//        setScoreView(score);
//        if (score > mBestScore) {
//            mBestScore = score;
//            setBestScore(mBestScore);
//        }
//        setBestScoreView(mBestScore);
//    }
//
//    @Override
//    public void onGameOver() {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle(getString(R.string.game_over))
//                .setMessage(getString(R.string.play_again))
//                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(makeIntent(GameActivity.this, mGameMode));
//                    }
//                })
//                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                })
//                .create();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.show();
//    }
//
//    protected void setScoreView(int score) {
//        if (mScoreView != null) {
//            mScoreView.setText(String.valueOf(score));
//        }
//    }
//
//    protected void setBestScoreView(int bestScore) {
//        if (mBestScoreView != null) {
//            String bestScoreText = getResources().getText(R.string.best_score) + ": " + bestScore;
//            mBestScoreView.setText(bestScoreText);
//        }
//    }
//
//    private int getBestScore() {
//        int bestScore = 0;
//        if (mGameMode == GameMode.MODE_R_5) {
//            bestScore = PreferencesUtils.getInt(this, Extra.Key.BEST_SCORE, 0);
//        } else if (mGameMode == GameMode.MODE_R_6) {
//            bestScore = PreferencesUtils.getInt(this, Extra.Key.BEST_SCORE_R6, 0);
//        } else if (mGameMode == GameMode.MODE_R_7) {
//            bestScore = PreferencesUtils.getInt(this, Extra.Key.BEST_SCORE_R7, 0);
//        } else if (mGameMode == GameMode.MODE_R_8) {
//            bestScore = PreferencesUtils.getInt(this, Extra.Key.BEST_SCORE_R8, 0);
//        }
//        return bestScore;
//    }
//
//    private void setBestScore(int bestScore) {
//        if (mGameMode == GameMode.MODE_R_5) {
//            PreferencesUtils.putInt(this, Extra.Key.BEST_SCORE, bestScore);
//        } else if (mGameMode == GameMode.MODE_R_6) {
//            PreferencesUtils.putInt(this, Extra.Key.BEST_SCORE_R6, bestScore);
//        } else if (mGameMode == GameMode.MODE_R_7) {
//            PreferencesUtils.putInt(this, Extra.Key.BEST_SCORE_R7, bestScore);
//        } else if (mGameMode == GameMode.MODE_R_8) {
//            PreferencesUtils.putInt(this, Extra.Key.BEST_SCORE_R8, bestScore);
//        }
//    }
//
//}
