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
    //    /**
    //     * 隐藏不可见,Gone
    //     */
    //    public static final int STATE_GONE = 0;
    //    /**
    //     * 隐藏不可见,invisible
    //     */
    //    public static final int STATE_INVISIBLE = 1;
    //    /**
    //     * 可见,visible
    //     */
    //    public static final int STATE_VISIBLE = 2;
    //
    //    /**
    //     * 空白的，可填充
    //     */
    //    public static final int STATE_EMPTY = 3;
    //
    //    /**
    //     * 已填充
    //     */
    //    public static final int STATE_HAS_COLOR = 4;
    //
    //    /**
    //     * 待消除
    //     */
    //    public static final int STATE_NEED_ELIMINATE = 5;
    //
    //    /**
    //     * 临时的颜色，用于判断当前位置是否可以放置
    //     */
    //    public static final int STATE_TEMP_COLOR = 6;
    //
    //    /**
    //     * 状态
    //     */
    //    private int mState;

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
        //        mState = STATE_INVISIBLE;
        mHexState = HexState.VISIBLE;
        mColor = ContextCompat.getColor(context, R.color.hexColorDefault);
    }


    //    public void setState(int state) {
    //        mState = state;
    //    }
    //
    //    public int getState() {
    //        return mState;
    //    }

    public boolean isGone() {
        return mHexState == HexState.GONE;
        //        return mState == STATE_INVISIBLE;
    }

    public boolean isInvisible() {
        return mHexState == HexState.INVISIBLE;
        //        return mState == STATE_INVISIBLE;
    }

    //    public boolean hasColor() {
    //        return mState == STATE_HAS_COLOR || mState == STATE_NEED_ELIMINATE;
    //    }
    public boolean hasColor() {
        return mHexState == HexState.STATE_HAS_COLOR || mHexState == HexState.STATE_NEED_ELIMINATE;
    }

    public void resetColor(Context c) {
        mColor = ContextCompat.getColor(c, R.color.hexColorDefault);
        //        mState = STATE_EMPTY;
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
