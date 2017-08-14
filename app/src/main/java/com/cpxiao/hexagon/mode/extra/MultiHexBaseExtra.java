package com.cpxiao.hexagon.mode.extra;

import java.util.Random;

/**
 * 组合六边形块
 *
 * @author cpxiao on 2017/7/30.
 */
public final class MultiHexBaseExtra {


    private static final int[][] _1A = {
            {1}
    };

    private static final int[][] _2A = {
            {1, 0, 1}
    };
    private static final int[][] _2B1 = {
            {1, 0},
            {0, 1}
    };
    private static final int[][] _2B2 = {
            {0, 1},
            {1, 0}
    };

    private static final int[][] _3A = {
            {1, 0, 1, 0, 1}
    };
    private static final int[][] _3B1 = {
            {1, 0, 1},
            {0, 1, 0}
    };
    private static final int[][] _3B2 = {
            {0, 1, 0},
            {1, 0, 1}
    };
    private static final int[][] _3C1 = {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
    };
    private static final int[][] _3C2 = {
            {0, 0, 1},
            {0, 1, 0},
            {1, 0, 0}
    };
    private static final int[][] _3D1 = {
            {1, 0},
            {0, 1},
            {1, 0}
    };
    private static final int[][] _3D2 = {
            {0, 1},
            {1, 0},
            {0, 1}
    };
    private static final int[][] _3E1 = {
            {1, 0, 0, 0},
            {0, 1, 0, 1}
    };
    private static final int[][] _3E2 = {
            {0, 0, 0, 1},
            {1, 0, 1, 0}
    };
    private static final int[][] _3E3 = {
            {0, 1, 0, 1},
            {1, 0, 0, 0}
    };
    private static final int[][] _3E4 = {
            {1, 0, 1, 0},
            {0, 0, 0, 1}
    };


    private static final int[][] _4A = {
            {1, 0, 1, 0, 1, 0, 1}
    };
    private static final int[][] _4B1 = {
            {1, 0, 0, 0, 1},
            {0, 1, 0, 1, 0}
    };
    private static final int[][] _4B2 = {
            {0, 1, 0, 1, 0},
            {1, 0, 0, 0, 1}
    };
    private static final int[][] _4C = {
            {0, 1, 0},
            {1, 0, 1},
            {0, 1, 0}
    };
    private static final int[][] _4D1 = {
            {0, 1, 0, 1},
            {1, 0, 1, 0}
    };
    private static final int[][] _4D2 = {
            {1, 0, 1, 0},
            {0, 1, 0, 1}
    };
    private static final int[][] _4E1 = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    };
    private static final int[][] _4E2 = {
            {0, 0, 0, 1},
            {0, 0, 1, 0},
            {0, 1, 0, 0},
            {1, 0, 0, 0}
    };
    private static final int[][] _4F1 = {
            {1, 0, 1},
            {0, 1, 0},
            {1, 0, 0}
    };
    private static final int[][] _4F2 = {
            {1, 0, 1},
            {0, 1, 0},
            {0, 0, 1}
    };
    private static final int[][] _4F3 = {
            {0, 0, 1},
            {0, 1, 0},
            {1, 0, 1}
    };
    private static final int[][] _4F4 = {
            {1, 0, 0},
            {0, 1, 0},
            {1, 0, 1}
    };
    private static final int[][] _4G1 = {
            {0, 0, 0, 0, 1},
            {0, 0, 0, 1, 0},
            {1, 0, 1, 0, 0}
    };
    private static final int[][] _4G2 = {
            {1, 0, 0, 0, 0},
            {0, 1, 0, 0, 0},
            {0, 0, 1, 0, 1}
    };
    private static final int[][] _4G3 = {
            {0, 0, 1, 0, 1},
            {0, 1, 0, 0, 0},
            {1, 0, 0, 0, 0}
    };
    private static final int[][] _4G4 = {
            {1, 0, 1, 0, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 0, 1}
    };


    private static final int[][] _5A1 = {
            {1, 0, 1, 0, 1},
            {0, 1, 0, 1, 0}
    };
    private static final int[][] _5A2 = {
            {0, 1, 0, 1, 0},
            {1, 0, 1, 0, 1}
    };
    private static final int[][] _5B1 = {
            {0, 0, 1, 0, 0},
            {0, 1, 0, 1, 0},
            {1, 0, 0, 0, 1}
    };
    private static final int[][] _5B2 = {
            {1, 0, 0, 0, 1},
            {0, 1, 0, 1, 0},
            {0, 0, 1, 0, 0}
    };


    private static final int[][] _6A1 = {
            {0, 0, 1, 0, 0},
            {0, 1, 0, 1, 0},
            {1, 0, 1, 0, 1}
    };
    private static final int[][] _6A2 = {
            {1, 0, 1, 0, 1},
            {0, 1, 0, 1, 0},
            {0, 0, 1, 0, 0}
    };


    private static final int[][][] DATA_R_5 = {
            _1A,
            _2A, _2B1, _2B2,
            _3A, _3B1, _3B2,
            _4A, _4B1, _4B2, _4C, _4D1, _4D2, _4F1, _4F2, _4F3, _4F4
    };
    private static final int[][][] DATA_R_6 = {
            _1A,
            _2A, _2B1, _2B2,
            _3A, _3B1, _3B2, _3C1, _3C2, _3D1, _3D2,
            _4A, _4B1, _4B2, _4C, _4D1, _4D2, _4F1, _4F2, _4F3, _4F4
    };
    private static final int[][][] DATA_R_7 = {
            _1A,
            _2A, _2B1, _2B2,
            _3A, _3B1, _3B2, _3C1, _3C2, _3D1, _3D2, _3E1, _3E2, _3E3, _3E4,
            _4A, _4B1, _4B2, _4C, _4D1, _4D2, _4E1, _4E2, _4F1, _4F2, _4F3, _4F4,
            _5B1, _5B2
    };
    private static final int[][][] DATA_R_8 = {
            _1A,
            _2A, _2B1, _2B2,
            _3A, _3B1, _3B2, _3C1, _3C2, _3D1, _3D2, _3E1, _3E2, _3E3, _3E4,
            _4A, _4B1, _4B2, _4C, _4D1, _4D2, _4E1, _4E2, _4F1, _4F2, _4F3, _4F4, _4G1, _4G2, _4G3, _4G4,
            _5A1, _5A2, _5B1, _5B2,
            _6A1, _6A2
    };

    private static Random mRandom = new Random();

    public static int[][] getData(int gameMode) {

//        return _2A;
        if (gameMode == GameMode.MODE_R_5) {
            return DATA_R_5[mRandom.nextInt(DATA_R_5.length)];
        } else if (gameMode == GameMode.MODE_R_6) {
            return DATA_R_6[mRandom.nextInt(DATA_R_6.length)];
        } else if (gameMode == GameMode.MODE_R_7) {
            return DATA_R_7[mRandom.nextInt(DATA_R_7.length)];
        } else if (gameMode == GameMode.MODE_R_8) {
            return DATA_R_8[mRandom.nextInt(DATA_R_8.length)];
        } else {
            return DATA_R_5[mRandom.nextInt(DATA_R_5.length)];
        }

    }


}
