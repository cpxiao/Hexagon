package com.cpxiao.hexagon.mode;

import java.util.Random;

/**
 * 组合六边形块
 *
 * @author cpxiao on 2017/7/30.
 */
public final class HexBaseExtra {


    private static final int[][] A1 = {
            {1}
    };

    private static final int[][] B1 = {
            {1, 0, 1}
    };
    private static final int[][] B2 = {
            {1, 0},
            {0, 1}
    };
    private static final int[][] B3 = {
            {0, 1},
            {1, 0}
    };

    private static final int[][] C1 = {
            {1, 0, 1, 0, 1}
    };


    private static final int[][] D1 = {
            {1, 0, 1, 0, 1, 0, 1}
    };
    private static final int[][] D2 = {
            {1, 0, 0, 0, 1},
            {0, 1, 0, 1, 0}
    };
    private static final int[][] D3 = {
            {0, 1, 0, 1, 0},
            {1, 0, 0, 0, 1}
    };
    private static final int[][] D4 = {
            {0, 1, 0},
            {1, 0, 1},
            {0, 1, 0}
    };
    private static final int[][] D5 = {
            {0, 1, 0, 1},
            {1, 0, 1, 0}
    };
    private static final int[][] D6 = {
            {1, 0, 1, 0},
            {0, 1, 0, 1}
    };

    private static final int[][][] data = {A1, B1, B2, B3, C1, D1, D2, D3, D4, D5, D6};
    private static Random mRandom = new Random();

    public static int[][] getData() {
        return data[mRandom.nextInt(data.length)];
    }


}
