package com.cpxiao.hexagon;

import android.content.Context;
import android.graphics.Color;

import java.util.Random;

/**
 * Created by cpxiao on 2/1/16.
 * 组合六边形块
 */
public class HexBase {
    public int mColor;
    public int mBlockX;
    public int mBlockY;
    public int mShowHexNumber;
    public HexSingle[][] mHexagonBlocks;

    public HexBase(Context c) {
        int[][] mData = getData();
        mColor = getColor(c);
        mBlockY = mData.length;
        mBlockX = mData[0].length;
        mShowHexNumber = 0;
        mHexagonBlocks = new HexSingle[mBlockY][mBlockX];
        for (int x = 0; x < mBlockX; x++) {
            for (int y = 0; y < mBlockY; y++) {
                mHexagonBlocks[y][x] = new HexSingle(c);
                if (mData[y][x] != 0) {
                    mShowHexNumber++;
                    mHexagonBlocks[y][x].setState(HexSingle.STATE_HAS_COLOR);
                    mHexagonBlocks[y][x].setColor(mColor);
                }
            }
        }
    }

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

    public static int[][] getData() {
        Random random = new Random();
        return data[random.nextInt(data.length)];
    }

    private int getColor(Context c) {
        String[] colors = c.getResources().getStringArray(R.array.app);
        Random random = new Random();
        int i = random.nextInt(colors.length);
        return Color.parseColor(colors[i]);
    }


}
