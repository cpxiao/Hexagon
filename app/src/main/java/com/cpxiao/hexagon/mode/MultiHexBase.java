package com.cpxiao.hexagon.mode;

import android.content.Context;
import android.util.Log;

import com.cpxiao.R;
import com.cpxiao.hexagon.mode.extra.GameColor;
import com.cpxiao.hexagon.mode.extra.MultiHexBaseExtra;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * 待选的组合六边形块
 *
 * @author cpxiao on 2016/2/1.
 */
public class MultiHexBase {
    public int mColor;
    public int mCountX;
    public int mCountY;
    public int mVisibleHexCount;
    public SingleHex[][] mHexArray;

    public MultiHexBase(Context c, int gameMode) {
        int[][] mData = MultiHexBaseExtra.getData(gameMode);
        mColor = GameColor.getColor(c, R.array.app);
        mCountY = mData.length;
        mCountX = mData[0].length;
        mVisibleHexCount = 0;
        mHexArray = new SingleHex[mCountY][mCountX];

        //根据第一行判断那个状态是gone
        int tmpY = 0;
        for (int i = 0; i < mCountY; i++) {
            if (mData[i][0] != 0) {
                tmpY = i % 2;
                break;
            }
        }
        for (int x = 0; x < mCountX; x++) {
            for (int y = 0; y < mCountY; y++) {
                SingleHex singleHex = new SingleHex(c);
                if (mData[y][x] != 0) {
                    mVisibleHexCount++;
                    //                    singleHex.setState(SingleHex.STATE_HAS_COLOR);
                    singleHex.setHexState(HexState.STATE_HAS_COLOR);
                    singleHex.setColor(mColor);
                } else {
                    //此处特殊处理，注意区别invisible和gone
                    if ((x + y) % 2 == tmpY) {
                        singleHex.setHexState(HexState.INVISIBLE);
                    } else {
                        singleHex.setHexState(HexState.GONE);
                    }
                }
                mHexArray[y][x] = singleHex;
            }
        }

        for (int y = 0; y < mCountY; y++) {
            String msg = "";
            for (int x = 0; x < mCountX; x++) {
                msg = msg + mHexArray[y][x].getHexState() + ", ";
            }
            Log.d(TAG, "MultiHexBase: " + msg);
        }
        Log.d(TAG, "MultiHexBase:------------------------ ");
    }


}
