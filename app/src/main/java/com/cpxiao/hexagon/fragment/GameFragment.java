package com.cpxiao.hexagon.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.MediaPlayerUtils;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.hexagon.imps.OnGameListener;
import com.cpxiao.hexagon.mode.extra.Extra;
import com.cpxiao.hexagon.mode.extra.GameMode;
import com.cpxiao.hexagon.views.GameView;
import com.cpxiao.hexagon.views.dialog.SettingsDialog;
import com.cpxiao.zads.core.ZAdPosition;

/**
 * @author cpxiao on 2017/09/06.
 */

public class GameFragment extends BaseZAdsFragment implements OnGameListener {
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

    private LinearLayout gameViewLayout;

    public static GameFragment newInstance(Bundle bundle) {
        GameFragment fragment = new GameFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        loadZAds(ZAdPosition.POSITION_GAME);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mGameMode = bundle.getInt(Extra.Name.GAME_MODE, GameMode.MODE_DEFAULT);
        }

        final Context context = getHoldingActivity();
        MediaPlayerUtils.getInstance().init(context, R.raw.hexagon_bgm);

        mScoreView = (TextView) view.findViewById(R.id.score);
        mBestScoreView = (TextView) view.findViewById(R.id.best_score);

        //Settings Btn
        ImageView settingsView = (ImageView) view.findViewById(R.id.btn_settings);
        settingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SettingsDialog dialog = new SettingsDialog(context);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });

        gameViewLayout = (LinearLayout) view.findViewById(R.id.game_view_layout);

        setScoreView(0);

        mBestScore = getBestScore();
        setBestScoreView(mBestScore);


        initGameView();
    }

    /**
     * 创建游戏View
     */
    private void initGameView() {
        loadZAds(ZAdPosition.POSITION_GAME);
        Context context = getHoldingActivity();
        GameView gameView = new GameView(context, mGameMode);
        gameView.setGameListener(this);
        gameViewLayout.removeAllViews();
        gameViewLayout.addView(gameView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game;
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getHoldingActivity();
        boolean isMusicOn = PreferencesUtils.getBoolean(context, Extra.Key.SETTING_MUSIC, Extra.Key.SETTING_MUSIC_DEFAULT);
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
    public void onDestroy() {
        super.onDestroy();
        MediaPlayerUtils.getInstance().stop();
    }

    public static Bundle makeBundle(Context context, int gameType) {
        Bundle bundle = new Bundle();
        bundle.putInt(Extra.Name.GAME_MODE, gameType);
        return bundle;
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
        final Context context = getHoldingActivity();
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(getString(R.string.game_over))
                .setMessage(getString(R.string.play_again))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setScoreView(0);
                        initGameView();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        removeFragment();
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    protected void setScoreView(int score) {
        if (mScoreView != null) {
            mScoreView.setText(String.valueOf(score));
        }
    }

    protected void setBestScoreView(int bestScore) {
        if (mBestScoreView != null) {
            String bestScoreText = getResources().getText(R.string.best_score) + ": " + bestScore;
            mBestScoreView.setText(bestScoreText);
        }
    }

    private int getBestScore() {
        Context context = getHoldingActivity();
        int bestScore = 0;
        if (mGameMode == GameMode.MODE_R_5) {
            bestScore = PreferencesUtils.getInt(context, Extra.Key.BEST_SCORE, 0);
        } else if (mGameMode == GameMode.MODE_R_6) {
            bestScore = PreferencesUtils.getInt(context, Extra.Key.BEST_SCORE_R6, 0);
        } else if (mGameMode == GameMode.MODE_R_7) {
            bestScore = PreferencesUtils.getInt(context, Extra.Key.BEST_SCORE_R7, 0);
        } else if (mGameMode == GameMode.MODE_R_8) {
            bestScore = PreferencesUtils.getInt(context, Extra.Key.BEST_SCORE_R8, 0);
        }
        return bestScore;
    }

    private void setBestScore(int bestScore) {
        Context context = getHoldingActivity();
        if (mGameMode == GameMode.MODE_R_5) {
            PreferencesUtils.putInt(context, Extra.Key.BEST_SCORE, bestScore);
        } else if (mGameMode == GameMode.MODE_R_6) {
            PreferencesUtils.putInt(context, Extra.Key.BEST_SCORE_R6, bestScore);
        } else if (mGameMode == GameMode.MODE_R_7) {
            PreferencesUtils.putInt(context, Extra.Key.BEST_SCORE_R7, bestScore);
        } else if (mGameMode == GameMode.MODE_R_8) {
            PreferencesUtils.putInt(context, Extra.Key.BEST_SCORE_R8, bestScore);
        }
    }

}
