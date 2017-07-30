package com.cpxiao.hexagon.mode;

import android.content.Context;
import android.graphics.Color;

import com.cpxiao.R;

import java.util.Random;

/**
 * 组合六边形块
 *
 * @author cpxiao on 2016/2/1.
 */
public class HexBase {
    public int mColor;
    public int mBlockX;
    public int mBlockY;
    public int mShowHexNumber;
    public HexSingle[][] mHexagonBlocks;

    public HexBase(Context c) {
        int[][] mData = HexBaseExtra.getData();
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

    private int getColor(Context c) {
        String[] colors = c.getResources().getStringArray(R.array.app);
        Random random = new Random();
        int i = random.nextInt(colors.length);
        return Color.parseColor(colors[i]);
    }


}
