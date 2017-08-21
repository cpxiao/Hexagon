package com.cpxiao.hexagon.mode;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.cpxiao.R;

/**
 * 单个六边形块
 *
 * @author cpxiao on 2016/1/27.
 */
public class SingleHex {

    /**
     * Color
     */
    private int mColor;

    /**
     * 状态
     */
    private HexState mHexState;


    public SingleHex(Context context) {
        init(context);
    }

    private void init(Context context) {
        //        mHexState = HexState.VISIBLE;
        mHexState = HexState.STATE_EMPTY;
        mColor = ContextCompat.getColor(context, R.color.hexColorDefault);
    }


    public boolean isHide() {
        return mHexState == HexState.GONE || mHexState == HexState.INVISIBLE;
    }

    public boolean isGone() {
        return mHexState == HexState.GONE;
    }

    public boolean isInvisible() {
        return mHexState == HexState.INVISIBLE;
    }

    public boolean hasColor() {
        return mHexState == HexState.STATE_HAS_COLOR || mHexState == HexState.STATE_NEED_ELIMINATE;
    }

    public void resetColor(Context c) {
        mColor = ContextCompat.getColor(c, R.color.hexColorDefault);
        mHexState = HexState.STATE_EMPTY;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }

    public void setHexState(HexState hexState) {
        mHexState = hexState;
    }

    public HexState getHexState() {
        return mHexState;
    }
}
