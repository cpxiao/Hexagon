package com.cpxiao.hexagon;

/**
 * Extra
 *
 * @author cpxiao on 2016/8/31
 */
public final class Extra {

    /**
     * Intent或Bundle的name
     */
    public static final class Name {
        /**
         * 游戏类型
         */
        public static final String GAME_MODE = "GAME_MODE";
    }

    /**
     * SharedPreferences 的key
     */
    public static final class Key {

        /**
         * R5最高分，默认为0分
         */
        public static final String BEST_SCORE = "BEST_SCORE";
        /**
         * R6最高分，默认为0分
         */
        public static final String BEST_SCORE_R6 = "BEST_SCORE_R6";
        /**
         * R7最高分，默认为0分
         */
        public static final String BEST_SCORE_R7 = "BEST_SCORE_R7";

        /**
         * 音效开关，默认开
         */
        public static final String SETTING_SOUND = "SETTING_SOUND";
        public static final boolean SETTING_SOUND_DEFAULT = true;

        /**
         * 音乐开关，默认开
         */
        public static final String SETTING_MUSIC = "SETTING_MUSIC";
        public static final boolean SETTING_MUSIC_DEFAULT = true;


    }

}
