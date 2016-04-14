package com.cpxiao.hexagon;

import android.content.Context;

/**
 * Created by cpxiao on 2/1/16.
 * 组合六边形块
 */
public class HexStore {
    public int mBlockX;
    public int mBlockY;
    public HexSingle[][] mHexagonBlocks;

    private HexStore() {

    }

    public HexStore(Context context, int blockX, int blockY) {
        mBlockX = blockX;
        mBlockY = blockY;
        init(context);
    }

    private void init(Context context) {
        mHexagonBlocks = new HexSingle[mBlockY][mBlockX];
        for (int y = 0; y < mBlockY; y++) {
            for (int x = 0; x < mBlockX; x++) {
                mHexagonBlocks[y][x] = new HexSingle(context);
            }
        }
    }


}
