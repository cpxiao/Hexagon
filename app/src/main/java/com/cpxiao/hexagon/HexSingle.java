package com.cpxiao.hexagon;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by cpxiao on 1/27/16.
 * 单个六边形块
 */
public class HexSingle {
    /**
     *
     */
    public static final int STATE_HIDE = 0;

    /**
     *
     */
    public static final int STATE_EMPTY = 1;

    /**
     *
     */
    public static final int STATE_HAS_COLOR = 2;

    /**
     * 待消除
     */
    public static final int STATE_NEED_ELIMINATE = 3;
    private int mState;
    private int mColor;
    public boolean hasColor;

    public HexSingle(Context c) {
        init(c);
    }

    private void init(Context c) {
        mState = STATE_HIDE;
        mColor = ContextCompat.getColor(c, R.color.colorBlockDefault);
        hasColor = false;
    }

    public void setState(int state) {
        mState = state;
    }

    public int getState() {
        return mState;
    }

    public boolean isHide() {
        return mState == STATE_HIDE;
    }



    public void resetColor(Context c) {
        mState = STATE_EMPTY;
        hasColor = false;
        mColor = ContextCompat.getColor(c, R.color.colorBlockDefault);
    }

    public void setColor(int color) {
        hasColor = true;
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }
}
