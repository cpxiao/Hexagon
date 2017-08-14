package com.cpxiao.hexagon.mode.extra;

import android.content.Context;
import android.graphics.Color;

import java.util.Random;

/**
 * @author cpxiao on 2017/08/14.
 */

public class GameColor {
    private static Random mRandom = new Random();

    public static int getColor(Context c, int colorArrayId) {
        String[] colors = c.getResources().getStringArray(colorArrayId);
        int i = mRandom.nextInt(colors.length);
        return Color.parseColor(colors[i]);
    }

}
