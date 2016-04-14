package com.cpxiao.hexagon;

/**
 * Created by cpxiao on 4/10/16.
 */
public interface onGameListener {
    /**
     * 统计分数
     */
    void onScoreChange(int score, int bestScore);


    /**
     * 游戏结束
     */
    void onGameOver();


}
