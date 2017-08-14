package com.cpxiao.hexagon.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.androidutils.library.utils.SoundPoolUtils;
import com.cpxiao.AppConfig;
import com.cpxiao.hexagon.mode.HexState;
import com.cpxiao.hexagon.mode.extra.Extra;
import com.cpxiao.hexagon.imps.OnGameListener;
import com.cpxiao.hexagon.mode.MultiHexBase;
import com.cpxiao.hexagon.mode.SingleHex;
import com.cpxiao.hexagon.mode.MultiHexStore;

import java.util.HashMap;


/**
 * GameView
 *
 * @author cpxiao on 2016/3/28.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = GameView.class.getSimpleName();
    private static final boolean DEBUG = AppConfig.DEBUG;

    /**
     * 游戏类型 5、6、7、8...
     */
    private int mGameMode;

    /**
     * 分数
     */
    private int mScore = 0;

    /**
     * 游戏中心区域
     */
    private MultiHexStore mMultiHexStore;
    private RectF mHexStoreRectF;

    /**
     * 待选块
     */
    private static final int mMultiHexBaseCount = 3;
    private MultiHexBase[] mMultiHexBaseArray;
    private RectF[] mMultiHexBaseRectFArray;

    /**
     * 选择块的下标
     */
    private int mHexBaseSelectedIndex = -1;

    /**
     * 三种状态的尺寸
     */
    private float paddingLR;//左右两边的padding值
    private float mRadiusOutL;
    private float mRadiusOutM;
    private float mRadiusOutS;
    private float mRadiusInnerL;
    private float mRadiusInnerM;
    private float mRadiusInnerS;

    private Paint mPaint;

    /**
     * 音效id
     */
    private static final int SOUND_POOL_CLEAR = 0;

    private SurfaceHolder mSurfaceHolder;

    private GameView(Context context) {
        super(context);
    }

    public GameView(Context context, int gameMode) {
        this(context);
        init(context, gameMode);
    }

    private void init(Context context, int gameMode) {
        mGameMode = gameMode;
        initHexStore(context, gameMode);
        initHexBase(context);

        //实例SurfaceHolder
        mSurfaceHolder = getHolder();
        //为SurfaceView添加状态监听
        mSurfaceHolder.addCallback(this);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);//抗锯齿

        SoundPoolUtils.getInstance().createSoundPool(20);
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(SOUND_POOL_CLEAR, R.raw.clear);
        SoundPoolUtils.getInstance().loadSound(context, map);
    }


    private void initHexStore(Context context, int gameMode) {
        //        int countX = gameMode * 4 - 3;
        //        int countY = gameMode * 2 - 1;

        //        mMultiHexStore = new MultiHexStore(context, countX, countY);
        mMultiHexStore = new MultiHexStore(context, gameMode);

        //        for (int y = 0; y < countY; y++) {
        //            for (int x = 0; x < countX; x++) {
        //                SingleHex singleHex = mMultiHexStore.mHexArray[y][x];
        //                if ((gameMode + y + x) % 2 == 0) {
        //                    //这种情况设置为Gone
        //                    singleHex.setHexState(HexState.GONE);
        //                    continue;
        //                }
        //                int tmp_y;
        //                if (y < gameMode) {
        //                    tmp_y = y;
        //                } else {
        //                    tmp_y = (gameMode - 1) * 2 - y;
        //                }
        //                if (x >= gameMode - 1 - tmp_y && x <= gameMode * 3 - 3 + tmp_y) {
        //                    //在此范围内的设置为visible
        //                    //                    singleHex.setState(SingleHex.STATE_EMPTY);
        //                    singleHex.setHexState(HexState.STATE_EMPTY);
        //                } else {
        //                    //设置为invisible
        //                    singleHex.setHexState(HexState.INVISIBLE);
        //                }
        //            }
        //        }
    }

    private void initHexBase(Context context) {
        mMultiHexBaseArray = new MultiHexBase[mMultiHexBaseCount];
        for (int i = 0; i < mMultiHexBaseCount; i++) {
            mMultiHexBaseArray[i] = new MultiHexBase(context, mGameMode);
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        paddingLR = 0.05F * screenWidth;
        float w = screenWidth - 2 * paddingLR;
        initRadius(w, screenHeight);
        initHexStoreRectF(w);
        initHexBaseRectF(screenWidth);
        myDraw();
    }

    /**
     * 初始化六边形边长
     */
    private void initRadius(float w, float h) {

        float per = 0.95f;
        float perM = 0.8f;
        float perS = 0.6f;


        mRadiusOutL = getHexagonRadius(w, h);
        mRadiusOutM = mRadiusOutL;
        mRadiusOutS = mRadiusOutL * perS;

        mRadiusInnerL = mRadiusOutL * per;
        mRadiusInnerM = mRadiusOutM * perM * per;
        mRadiusInnerS = mRadiusOutS * per;

    }

    private void initHexStoreRectF(float w) {

        mHexStoreRectF = new RectF();
        mHexStoreRectF.left = paddingLR;
        mHexStoreRectF.right = mHexStoreRectF.left + w;
        mHexStoreRectF.top = 0.0f;
        mHexStoreRectF.bottom = mHexStoreRectF.top + mRadiusOutL * ((2 * mGameMode) * (1.0f + mDeltaY) + (mGameMode - 1));
    }

    private void initHexBaseRectF(float w) {
        mMultiHexBaseRectFArray = new RectF[mMultiHexBaseCount];
        for (int i = 0; i < mMultiHexBaseCount; i++) {
            mMultiHexBaseRectFArray[i] = new RectF();
        }
        float paddingLR = mRadiusOutL / 2;
        float width = (w - paddingLR * 2) / mMultiHexBaseCount;

        for (int i = 0; i < mMultiHexBaseRectFArray.length; i++) {
            mMultiHexBaseRectFArray[i].left = paddingLR + width * i;
            mMultiHexBaseRectFArray[i].right = mMultiHexBaseRectFArray[i].left + width;
            mMultiHexBaseRectFArray[i].top = (mGameMode * 3) * mRadiusOutL;
            mMultiHexBaseRectFArray[i].bottom = mMultiHexBaseRectFArray[i].top + width;
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private OnGameListener mOnGameListener;

    public void setGameListener(OnGameListener listener) {
        mOnGameListener = listener;
    }

    /**
     * 点击坐标
     */
    private float mEventX;
    private float mEventY;
    /**
     * 重新定位点击位置
     */

    private float mEventDeltaX = 0F;
    private float mEventDeltaY = 0F;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mEventX = event.getX() + mEventDeltaX;
        mEventY = event.getY() + mEventDeltaY;


        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            mHexBaseSelectedIndex = isOneHexBaseSelected(mEventX, mEventY);
            if (mHexBaseSelectedIndex >= 0) {
                mEventDeltaY = -mRadiusOutL * (mMultiHexBaseArray[mHexBaseSelectedIndex].mCountY * 2 + 1);
                mEventDeltaX = -(mEventX - mMultiHexBaseRectFArray[mHexBaseSelectedIndex].left);
                mEventX += mEventDeltaX;
                mEventY += mEventDeltaY;
            }
        } else if (MotionEvent.ACTION_MOVE == event.getAction()) {
            int indexY = getIndexY(mEventY, mHexStoreRectF.top, mHexStoreRectF.bottom, mMultiHexStore.mCountY);
            int indexX = getIndexX(indexY, mEventX, mHexStoreRectF.left, mHexStoreRectF.right, mMultiHexStore.mCountX);

            if (mHexBaseSelectedIndex != -1) {
                boolean isCanBePlace = isCanBePlace(indexX, indexY, mMultiHexStore, mMultiHexBaseArray[mHexBaseSelectedIndex]);
                if (isCanBePlace) {
                    updateHexStore(indexX, indexY, mMultiHexBaseArray[mHexBaseSelectedIndex], false);
                } else {
                    clearTempColor();
                }
            }
        } else if (MotionEvent.ACTION_UP == event.getAction()) {
            int indexY = getIndexY(mEventY, mHexStoreRectF.top, mHexStoreRectF.bottom, mMultiHexStore.mCountY);
            int indexX = getIndexX(indexY, mEventX, mHexStoreRectF.left, mHexStoreRectF.right, mMultiHexStore.mCountX);
            if (mHexBaseSelectedIndex != -1) {
                boolean isCanBePlace = isCanBePlace(indexX, indexY, mMultiHexStore, mMultiHexBaseArray[mHexBaseSelectedIndex]);
                if (isCanBePlace) {
                    updateHexStore(indexX, indexY, mMultiHexBaseArray[mHexBaseSelectedIndex], true);
                    mScore += mMultiHexBaseArray[mHexBaseSelectedIndex].mVisibleHexCount;
                    mMultiHexBaseArray[mHexBaseSelectedIndex] = new MultiHexBase(getContext(), mGameMode);
                    updateHexBase(mHexBaseSelectedIndex);
                }
            }

            mHexBaseSelectedIndex = -1;
            mEventDeltaX = 0;
            mEventDeltaY = 0;
        }

        /*先逻辑判断，再判断是否game over，防止可以消除但提示game over。*/
        logic();
        isGameOver();

        myDraw();
        return true;

    }

    private void updateHexBase(int mHexBaseSelectedIndex) {
        mMultiHexBaseArray[mHexBaseSelectedIndex] = new MultiHexBase(getContext(), mGameMode);
    }

    private boolean isGameOver() {
        for (MultiHexBase multiHexBase : mMultiHexBaseArray) {
            for (int y = 0; y < mMultiHexStore.mCountY; y++) {
                for (int x = 0; x < mMultiHexStore.mCountX; x++) {
                    if (isCanBePlace(x, y, mMultiHexStore, multiHexBase)) {
                        return false;
                    }
                }
            }
        }
        if (mOnGameListener != null) {
            mOnGameListener.onGameOver();
        }
        return true;
    }

    /**
     * @param eventY eventY
     * @param yStart yStart
     * @param yEnd   yEnd
     * @param num    num
     * @return int
     */
    private int getIndexY(float eventY, float yStart, float yEnd, int num) {
        float h = (yEnd - yStart) / num;
        return (int) ((eventY + h / 2 - yStart) / h);
    }

    /**
     * @param indexY indexY
     * @param eventX eventX
     * @param xStart xStart
     * @param xEnd   xEnd
     * @param num    num
     * @return int
     */
    private int getIndexX(int indexY, float eventX, float xStart, float xEnd, int num) {
        float w = (xEnd - xStart) / num;
        float tmpX = ((eventX + w / 2 - xStart) / w);

        if (mHexBaseSelectedIndex < 0) {
            return 0;
        }

        boolean isX0Y0Gone = mMultiHexBaseArray[mHexBaseSelectedIndex].mHexArray[0][0].isGone();//不能单纯判断第一个元素
        //        boolean isX0Y0Gone = mMultiHexBaseArray[mHexBaseSelectedIndex].isX0Y0Show();

        int result;
        if (isEvenNumber(mGameMode)) {
            if (isEvenNumber(indexY)) {
                if (isX0Y0Gone) {
                    result = getEvenNumber(tmpX);
                } else {
                    result = getOddNumber(tmpX);
                }
            } else {
                if (isX0Y0Gone) {
                    result = getOddNumber(tmpX);
                } else {
                    result = getEvenNumber(tmpX);
                }
            }
        } else {
            if (isEvenNumber(indexY)) {
                if (isX0Y0Gone) {
                    result = getOddNumber(tmpX);
                } else {
                    result = getEvenNumber(tmpX);
                }
            } else {
                if (isX0Y0Gone) {
                    result = getEvenNumber(tmpX);
                } else {
                    result = getOddNumber(tmpX);
                }
            }
        }
        return result;


    }

    /**
     * 判断是否为偶数
     *
     * @param num num
     * @return boolean
     */
    private boolean isEvenNumber(int num) {
        return num >= 0 && num % 2 == 0;
    }

    /**
     * 获得最近的偶数，如：2.56取2，3.56取4，4.56取4，5.56取6
     *
     * @param num num
     * @return int
     */
    private int getEvenNumber(float num) {
        float mod = num - 2 * ((int) num / 2);
        int tmpResult = 2 * ((int) num / 2);
        if (mod < 1.0f) {
            return tmpResult;
        } else {
            return tmpResult + 2;
        }
    }

    /**
     * 获得最近的奇数，如：2.56取3，3.56取3，4.56取5，5.56取5
     *
     * @param num num
     * @return int
     */
    private int getOddNumber(float num) {
        int tmpResult = 2 * ((int) num / 2);
        return tmpResult + 1;
    }

    /**
     * 判断是否可消除，并计算相应得分
     */
    private void logic() {
        /*判断"横"行*/
        int clearLine0 = 0;
        int clearHexNum0 = 0;
        for (int y = 0; y < mMultiHexStore.mCountY; y++) {
            boolean isFull = true;
            int showNumber = 0;
            for (int x = 0; x < mMultiHexStore.mCountX; x++) {
                if (!mMultiHexStore.mHexArray[y][x].isGone()) {
                    if (mMultiHexStore.mHexArray[y][x].hasColor()) {
                        showNumber++;
                    } else {
                        isFull = false;
                        break;
                    }
                }
            }
            //该行有值且已填满,将此行数据状态设为待消除
            if (isFull && showNumber > 0) {
                clearLine0++;
                clearHexNum0 += showNumber;
                for (int x = 0; x < mMultiHexStore.mCountX; x++) {
                    if (!mMultiHexStore.mHexArray[y][x].isGone()) {
                        if (mMultiHexStore.mHexArray[y][x].hasColor()) {
                            //                            mMultiHexStore.mHexArray[y][x].setState(SingleHex.STATE_NEED_ELIMINATE);
                            mMultiHexStore.mHexArray[y][x].setHexState(HexState.STATE_NEED_ELIMINATE);
                        }
                    }
                }
            }
        }


        /*判断"/"行*/
        int clearLine1 = 0;
        int clearHexNum1 = 0;
        for (int i = 0; i < mMultiHexStore.mCountX + mMultiHexStore.mCountY - 1; i++) {
            boolean isFull = true;
            int showNumber = 0;
            for (int y = 0; y < mMultiHexStore.mCountY; y++) {
                int x = i - y;
                if (x < 0 || x >= mMultiHexStore.mCountX) {
                    continue;
                }
                if (!mMultiHexStore.mHexArray[y][x].isGone()) {
                    if (mMultiHexStore.mHexArray[y][x].hasColor()) {
                        showNumber++;
                    } else {
                        isFull = false;
                        break;
                    }
                }
            }
            if (isFull && showNumber > 0) {
                clearLine1++;
                clearHexNum1 += showNumber;
                for (int y = 0; y < mMultiHexStore.mCountY; y++) {
                    int x = i - y;
                    if (x < 0 || x >= mMultiHexStore.mCountX) {
                        continue;
                    }
                    if (!mMultiHexStore.mHexArray[y][x].isGone()) {
                        if (mMultiHexStore.mHexArray[y][x].hasColor()) {
                            //                            mMultiHexStore.mHexArray[y][x].setState(SingleHex.STATE_NEED_ELIMINATE);
                            mMultiHexStore.mHexArray[y][x].setHexState(HexState.STATE_NEED_ELIMINATE);
                        }
                    }
                }
            }
        }

        /*判断"\"行*/
        int clearLine2 = 0;
        int clearHexNum2 = 0;
        for (int i = 0; i < mMultiHexStore.mCountX + mMultiHexStore.mCountY - 1; i++) {
            boolean isFull = true;
            int showNumber = 0;
            for (int y = 0; y < mMultiHexStore.mCountY; y++) {
                int x = mMultiHexStore.mCountX - (i - y);
                if (x < 0 || x >= mMultiHexStore.mCountX) {
                    continue;
                }
                if (!mMultiHexStore.mHexArray[y][x].isGone()) {
                    if (mMultiHexStore.mHexArray[y][x].hasColor()) {
                        showNumber++;
                    } else {
                        isFull = false;
                        break;
                    }
                }
            }
            if (isFull && showNumber > 0) {
                clearLine2++;
                clearHexNum2 += showNumber;
                for (int y = 0; y < mMultiHexStore.mCountY; y++) {
                    int x = mMultiHexStore.mCountX - (i - y);
                    if (x < 0 || x >= mMultiHexStore.mCountX) {
                        continue;
                    }
                    if (!mMultiHexStore.mHexArray[y][x].isGone()) {
                        if (mMultiHexStore.mHexArray[y][x].hasColor()) {
                            //                            mMultiHexStore.mHexArray[y][x].setState(SingleHex.STATE_NEED_ELIMINATE);
                            mMultiHexStore.mHexArray[y][x].setHexState(HexState.STATE_NEED_ELIMINATE);
                        }
                    }
                }
            }
        }


        /*消除数据*/
        for (int y = 0; y < mMultiHexStore.mCountY; y++) {
            for (int x = 0; x < mMultiHexStore.mCountX; x++) {
                SingleHex singleHex = mMultiHexStore.mHexArray[y][x];
                //                if (singleHex.getState() == SingleHex.STATE_NEED_ELIMINATE) {
                //                    singleHex.resetColor(getContext().getApplicationContext());
                //                }
                if (singleHex.getHexState() == HexState.STATE_NEED_ELIMINATE) {
                    singleHex.resetColor(getContext().getApplicationContext());
                }
            }
        }

        /*计算此次得分*/
        int clearLineTotal = clearLine0 + clearLine1 + clearLine2;
        boolean isSoundOn = PreferencesUtils.getBoolean(getContext(), Extra.Key.SETTING_SOUND, Extra.Key.SETTING_SOUND_DEFAULT);
        for (int i = 0; i < clearLineTotal; i++) {
            if (isSoundOn) {
                SoundPoolUtils.getInstance().play(SOUND_POOL_CLEAR);
            }
        }
        int clearHexNumTotal = clearHexNum0 + clearHexNum1 + clearHexNum2;
        mScore += clearHexNumTotal * (clearLineTotal + 1);
        if (mOnGameListener != null) {
            mOnGameListener.onScoreChange(mScore);
        }
    }


    /**
     * @param indexX indexX
     * @param indexY indexY
     * @param data   data
     * @param isSave isSave
     */
    private void updateHexStore(int indexX, int indexY, MultiHexBase data, boolean isSave) {
        clearTempColor();

        //更新
        for (int y = 0; y < data.mCountY; y++) {
            for (int x = 0; x < data.mCountX; x++) {
                if (!data.mHexArray[y][x].isGone() && !data.mHexArray[y][x].isInvisible()) {
                    SingleHex hexSingle = mMultiHexStore.mHexArray[y + indexY][x + indexX];
                    hexSingle.setColor(data.mColor);
                    if (isSave) {
                        //                        hexSingle.setState(SingleHex.STATE_HAS_COLOR);
                        hexSingle.setHexState(HexState.STATE_HAS_COLOR);
                    } else {
                        //                        hexSingle.setState(SingleHex.STATE_TEMP_COLOR);
                        hexSingle.setHexState(HexState.STATE_TEMP_COLOR);
                    }
                }
            }
        }
    }

    /**
     * 清空临时颜色
     */
    private void clearTempColor() {
        for (int y = 0; y < mMultiHexStore.mCountY; y++) {
            for (int x = 0; x < mMultiHexStore.mCountX; x++) {
                if (!mMultiHexStore.mHexArray[y][x].isGone()) {
                    SingleHex hexSingle = mMultiHexStore.mHexArray[y][x];
                    //                    if (hexSingle.getState() == SingleHex.STATE_TEMP_COLOR) {
                    //                        hexSingle.resetColor(getContext().getApplicationContext());
                    //                    }
                    if (hexSingle.getHexState() == HexState.STATE_TEMP_COLOR) {
                        hexSingle.resetColor(getContext().getApplicationContext());
                    }
                }
            }
        }
    }

    private int isOneHexBaseSelected(float eventX, float eventY) {
        for (int i = 0; i < mMultiHexBaseRectFArray.length; i++) {
            if (isPointInRectF(eventX, eventY, mMultiHexBaseRectFArray[i].left, mMultiHexBaseRectFArray[i].top, mMultiHexBaseRectFArray[i].right, mMultiHexBaseRectFArray[i].bottom, 0)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 判断一个点是否在rectF内
     *
     * @param eventX eventX
     * @param eventY eventY
     * @param left   left
     * @param top    top
     * @param right  right
     * @param bottom bottom
     * @param margin margin，放大缩小rectF区域
     * @return boolean
     */
    private boolean isPointInRectF(float eventX, float eventY, float left, float top, float right, float bottom, float margin) {
        return eventX <= right + margin && eventX >= left - margin && eventY <= bottom + margin && eventY >= top - margin;
    }


    public float getHexagonRadius(float width, float height) {
        if (width <= 0 || height <= 0 || mGameMode <= 0) {
            return 0;
        }
        return (float) (width / (mGameMode * 2.0 - 1) / Math.sqrt(3.0));
    }

    private void myDraw() {
        Canvas canvas = mSurfaceHolder.lockCanvas();

        drawBackground(canvas);
        drawHexStore(canvas);
        drawHexBase(canvas);


        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRGB(255, 255, 255);
    }

    private void drawHexStore(Canvas canvas) {
        drawHexArray(canvas, mMultiHexStore.mHexArray, mHexStoreRectF, mRadiusOutL, mRadiusInnerL, mPaint);
    }

    private void drawHexBase(Canvas canvas) {
        //绘制待选项
        for (int i = 0; i < mMultiHexBaseArray.length; i++) {
            if (i != mHexBaseSelectedIndex) {
                drawHexArray(canvas, mMultiHexBaseArray[i].mHexArray, mMultiHexBaseRectFArray[i], mRadiusOutS, mRadiusInnerS, mPaint);
            }
        }
        //绘制选中项
        if (mHexBaseSelectedIndex >= 0 && mHexBaseSelectedIndex < mMultiHexBaseArray.length) {
            drawHexArray(canvas, mMultiHexBaseArray[mHexBaseSelectedIndex].mHexArray, mEventX, mEventY, mRadiusOutM, mRadiusInnerM, mPaint);
        }
    }

    /**
     * 在Y轴方向增加delY个方块高度
     */
    private float mDeltaY = 0.08f;

    /**
     * 绘制六边形组合图
     */
    private void drawHexArray(Canvas canvas, SingleHex[][] hexArray, float xTopLeft, float yTopLeft, float rOut, float rInner, Paint paint) {
        if (hexArray == null || hexArray.length <= 0 || hexArray[0].length <= 0) {
            return;
        }

        for (int y = 0; y < hexArray.length; y++) {
            for (int x = 0; x < hexArray[0].length; x++) {
                if (!hexArray[y][x].isGone() && !hexArray[y][x].isInvisible()) {
                    float centerX = (float) (xTopLeft + (x + 1) * rOut * Math.cos(Math.PI / 6.0F));
                    float centerY = yTopLeft + rOut + y * rOut * (1.5f + mDeltaY);
                    paint.setColor(hexArray[y][x].getColor());
                    //                    if (hexArray[y][x].getState() == SingleHex.STATE_TEMP_COLOR) {
                    //                        paint.setAlpha(160);
                    //                    } else {
                    //                        paint.setAlpha(255);
                    //                    }
                    if (hexArray[y][x].getHexState() == HexState.STATE_TEMP_COLOR) {
                        paint.setAlpha(160);
                    } else {
                        paint.setAlpha(255);
                    }
                    drawHexagonColor(canvas, centerX, centerY, rInner, paint);
                }
            }
        }
    }

    /**
     * 绘制六边形组合图
     */
    private void drawHexArray(Canvas canvas, SingleHex[][] data, RectF rectF, float rOut, float rInner, Paint paint) {
        drawHexArray(canvas, data, rectF.left, rectF.top, rOut, rInner, paint);
    }

    /**
     * 绘制单个六边形
     *
     * @param canvas  画布
     * @param centerX 中心点x坐标
     * @param centerY 中心点y坐标
     * @param r       中心点与角的距离r
     * @param angle   角与中心连线和x轴的夹角最小值
     */
    private void drawHexagonColor(Canvas canvas, float centerX, float centerY, float r, double angle, Paint paint) {
        Path path = new Path();
        for (int i = 0; i <= 6; i++) {
            int x = (int) (centerX + Math.cos((angle + i * 60) / 180 * Math.PI) * r);
            int y = (int) (centerY + Math.sin((angle + i * 60) / 180 * Math.PI) * r);
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制单个六边形
     *
     * @param canvas  画布
     * @param centerX 中心点x坐标
     * @param centerY 中心点y坐标
     * @param r       中心点与角的距离r
     */
    private void drawHexagonColor(Canvas canvas, float centerX, float centerY, float r, Paint paint) {
        drawHexagonColor(canvas, centerX, centerY, r, 30, paint);
    }

    /**
     * @param placeX   x
     * @param placeY   y
     * @param hexStore 游戏区
     * @param hexBase  待选区
     * @return boolean
     */
    public boolean isCanBePlace(int placeX, int placeY, MultiHexStore hexStore, MultiHexBase hexBase) {
        if (hexStore == null || hexBase == null) {
            return false;
        }
        if (placeX < 0 || placeY < 0 || placeX >= hexStore.mCountX || placeY >= hexStore.mCountY) {
            return false;
        }

        //刚好错位，不能放在此位置
        if (hexStore.mHexArray[placeY][placeX].isGone() != hexBase.mHexArray[0][0].isGone()) {
            Log.d(TAG, "isCanBePlace: ");
            return false;
        }
        for (int y = 0; y < hexBase.mCountY; y++) {
            for (int x = 0; x < hexBase.mCountX; x++) {
                if (!hexBase.mHexArray[y][x].isGone() && !hexBase.mHexArray[y][x].isInvisible()) {
                    //左边、上边越界
                    if (placeY + y < 0 || placeX + x < 0) {
                        return false;
                    }
                    //右边、下边越界
                    if (placeY + y >= hexStore.mCountY || placeX + x >= hexStore.mCountX) {
                        return false;
                    }
                    SingleHex singleHex = hexStore.mHexArray[placeY + y][placeX + x];
                    //已填充
                    if (singleHex.hasColor()) {
                        return false;
                    }
                    //此位置为显示Gone或者invisible
                    if (singleHex.isGone() || singleHex.isInvisible()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
