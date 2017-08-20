package com.cpxiao.hexagon.mode;

import android.content.Context;

/**
 * 组合六边形块
 *
 * @author cpxiao on 2016/2/1.
 */
public class MultiHexStore {
    public int mCountX;
    public int mCountY;
    public SingleHex[][] mHexArray;

    private MultiHexStore() {

    }

    public MultiHexStore(Context context, int gameMode) {
        int countX = gameMode * 4 - 3;
        int countY = gameMode * 2 - 1;

        mCountX = countX;
        mCountY = countY;

        //        init(context);
        mHexArray = new SingleHex[mCountY][mCountX];
        for (int y = 0; y < mCountY; y++) {
            for (int x = 0; x < mCountX; x++) {
                mHexArray[y][x] = new SingleHex(context);
            }
        }


        for (int y = 0; y < countY; y++) {
            for (int x = 0; x < countX; x++) {
                SingleHex singleHex = mHexArray[y][x];
                if ((gameMode + y + x) % 2 == 0) {
                    //这种情况设置为Gone
                    singleHex.setHexState(HexState.GONE);
                    continue;
                }
                int tmp_y;
                if (y < gameMode) {
                    tmp_y = y;
                } else {
                    tmp_y = (gameMode - 1) * 2 - y;
                }
                if (x >= gameMode - 1 - tmp_y && x <= gameMode * 3 - 3 + tmp_y) {
                    //在此范围内的设置为visible
                    //                    singleHex.setState(SingleHex.STATE_EMPTY);
                    singleHex.setHexState(HexState.STATE_EMPTY);
                } else {
                    //设置为invisible
                    singleHex.setHexState(HexState.INVISIBLE);
                }
            }
        }
    }


}
