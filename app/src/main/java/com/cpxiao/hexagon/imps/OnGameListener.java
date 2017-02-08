package com.cpxiao.hexagon.imps;

/**
 * OnGameListener
 *
 * @author cpxiao on 2016/8/31
 */
public interface OnGameListener {
    /**
     * 统计分数
     */
    void onScoreChange(int score);

    /**
     * 游戏结束
     */
    void onGameOver();

    /**
     * 游戏成功
     */
    void onSuccess();
}
