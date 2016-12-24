package com.cpxiao.hexagon.mode;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.cpxiao.hexagon.R;

/**
 * 单个六边形块
 *
 * @author cpxiao on 2016/1/27.
 */
public class HexSingle {
    /**
     * 隐藏不可见
     */
    public static final int STATE_HIDE = 0;

    /**
     * 空白的，可填充
     */
    public static final int STATE_EMPTY = 1;

    /**
     * 已填充
     */
    public static final int STATE_HAS_COLOR = 2;

    /**
     * 待消除
     */
    public static final int STATE_NEED_ELIMINATE = 3;

    /**
     * 临时的颜色，用于判断当前位置是否可以放置
     */
    public static final int STATE_TEMP_COLOR = 4;

    /**
     * 状态
     */
    private int mState;

    /**
     * Color
     */
    private int mColor;

    public HexSingle(Context context) {
        init(context);
    }

    private void init(Context context) {
        mState = STATE_HIDE;
        mColor = ContextCompat.getColor(context, R.color.hexColorDefault);
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

    public boolean hasColor() {
        return mState == STATE_HAS_COLOR || mState == STATE_NEED_ELIMINATE;
    }

    public void resetColor(Context c) {
        mColor = ContextCompat.getColor(c, R.color.hexColorDefault);
        mState = STATE_EMPTY;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }
}
