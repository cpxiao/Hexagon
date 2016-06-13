package com.cpxiao.hexagon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cpxiao.commonlibrary.utils.LogUtils;
import com.cpxiao.commonlibrary.utils.MediaPlayerUtils;
import com.cpxiao.commonlibrary.utils.PreferencesUtils;
import com.cpxiao.commonlibrary.utils.SoundPoolUtils;

import java.util.HashMap;


/**
 * Created by cpxiao on 3/28/16.
 * GameView
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = LogUtils.TAG + GameView.class.getSimpleName();
	/**
	 * 游戏类型 3、4、5、6、7...
	 */
	private int mGameType;

	/**
	 * 分数、最高分
	 */
	private int mScore = 0;
	private int mBestScore = 0;

	/**
	 * 游戏中心区域
	 */
	private HexStore mHexStore;
	private RectF mHexStoreRectF;

	/**
	 * 待选块
	 */
	int mHexBaseNumber = 3;
	private HexBase[] mHexBase;
	private RectF[] mHexBaseRectF;

	/**
	 * 选择块的下标
	 */
	private int mHexBaseSelectedIndex = -1;

	/**
	 * 三种状态的尺寸
	 */
	private float mRadiusOutL;
	private float mRadiusOutM;
	private float mRadiusOutS;
	private float mRadiusInnerL;
	private float mRadiusInnerM;
	private float mRadiusInnerS;

	private Paint mPaint;

	private SurfaceHolder mSurfaceHolder;


	private GameView(Context context) {
		super(context);
	}

	public GameView(Context context, int gameType) {
		this(context);
		init(context, gameType);
	}

	private static final int SOUND_POOL_CLEAR = 0;

	private void init(Context context, int gameType) {
		mGameType = gameType;
		mBestScore = PreferencesUtils.getInt(context, ExtraKey.KEY_BEST_SCORE, 0);
		initHexStore(context, gameType);
		initHexBase(context);

		//实例SurfaceHolder
		mSurfaceHolder = getHolder();
		//为SurfaceView添加状态监听
		mSurfaceHolder.addCallback(this);

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);

		MediaPlayerUtils.getInstance().init(context, R.raw.hexagon_bg_sound);
		MediaPlayerUtils.getInstance().start();


		SoundPoolUtils.getInstance().createSoundPool(20);
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(SOUND_POOL_CLEAR, R.raw.clear);
		SoundPoolUtils.getInstance().loadSound(context, map);
	}


	private void initHexStore(Context context, int gameType) {
		int mBlockX = gameType * 4 - 3;
		int mBlockY = gameType * 2 - 1;

		mHexStore = new HexStore(context, mBlockX, mBlockY);

		for (int y = 0; y < mBlockY; y++) {
			for (int x = 0; x < mBlockX; x++) {
				if ((gameType + y + x) % 2 == 0) {
					//这种情况不显示
					continue;
				}
				int tmp_y;
				if (y < gameType) {
					tmp_y = y;
				} else {
					tmp_y = (gameType - 1) * 2 - y;
				}
				if (x >= gameType - 1 - tmp_y && x <= gameType * 3 - 3 + tmp_y) {
					mHexStore.mHexagonBlocks[y][x].setState(HexSingle.STATE_EMPTY);
				}
			}
		}
	}

	private void initHexBase(Context context) {
		mHexBase = new HexBase[mHexBaseNumber];
		for (int i = 0; i < mHexBaseNumber; i++) {
			mHexBase[i] = new HexBase(context);
		}
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		int screenWidth = getWidth();
		int screenHeight = getHeight();

		initRadius(screenWidth, screenHeight);
		initHexStoreRectF(screenWidth);
		initHexBaseRectF(screenWidth);
		myDraw();
	}

	private void initHexStoreRectF(int w) {
		mHexStoreRectF = new RectF();
		mHexStoreRectF.left = 0.0f;
		mHexStoreRectF.right = mHexStoreRectF.left + w;
		mHexStoreRectF.top = 0.0f;
		mHexStoreRectF.bottom = mHexStoreRectF.top + mRadiusOutL * ((2 * mGameType) * (1.0f + mDeltaY) + (mGameType - 1));
	}

	private void initHexBaseRectF(int w) {
		mHexBaseRectF = new RectF[mHexBaseNumber];
		for (int i = 0; i < mHexBaseNumber; i++) {
			mHexBaseRectF[i] = new RectF();
		}
		float paddingLR = mRadiusOutL / 2;
		float width = (w - paddingLR * 2) / mHexBaseNumber;

		for (int i = 0; i < mHexBaseRectF.length; i++) {
			mHexBaseRectF[i].left = paddingLR + width * i;
			mHexBaseRectF[i].right = mHexBaseRectF[i].left + width;
			mHexBaseRectF[i].top = (mGameType * 3) * mRadiusOutL;
			mHexBaseRectF[i].bottom = mHexBaseRectF[i].top + width;
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
				mEventDeltaY = -mRadiusOutL * (mHexBase[mHexBaseSelectedIndex].mBlockY * 2 + 1);
				mEventDeltaX = -(mEventX - mHexBaseRectF[mHexBaseSelectedIndex].left);
				mEventX += mEventDeltaX;
				mEventY += mEventDeltaY;
			}
		} else if (MotionEvent.ACTION_UP == event.getAction()) {
			int indexY = getIndexY(mEventY, mHexStoreRectF.top, mHexStoreRectF.bottom, mHexStore.mBlockY);
			int indexX = getIndexX(indexY, mEventX, mHexStoreRectF.left, mHexStoreRectF.right, mHexStore.mBlockX);

			if (mHexBaseSelectedIndex != -1) {
				boolean isCanBePlace = isCanBePlace(indexX, indexY, mHexStore, mHexBase[mHexBaseSelectedIndex]);
				if (isCanBePlace) {
					updateHexStore(indexX, indexY, mHexBase[mHexBaseSelectedIndex], true);
					mScore += mHexBase[mHexBaseSelectedIndex].mShowHexNumber;
					mHexBase[mHexBaseSelectedIndex] = new HexBase(getContext());
					updateHexBase(mHexBaseSelectedIndex);
				}
			}

			mHexBaseSelectedIndex = -1;
			mEventDeltaX = 0;
			mEventDeltaY = 0;
		} else if (MotionEvent.ACTION_MOVE == event.getAction()) {
			int indexY = getIndexY(mEventY, mHexStoreRectF.top, mHexStoreRectF.bottom, mHexStore.mBlockY);
			int indexX = getIndexX(indexY, mEventX, mHexStoreRectF.left, mHexStoreRectF.right, mHexStore.mBlockX);

			if (mHexBaseSelectedIndex != -1) {
				boolean isCanBePlace = isCanBePlace(indexX, indexY, mHexStore, mHexBase[mHexBaseSelectedIndex]);
				if (isCanBePlace) {
					updateHexStore(indexX, indexY, mHexBase[mHexBaseSelectedIndex], false);
				} else {
					clearTempColor();
				}
			}
		}

		isGameOver();
		logic();

		myDraw();
		return true;

	}

	private void updateHexBase(int mHexBaseSelectedIndex) {
		mHexBase[mHexBaseSelectedIndex] = new HexBase(getContext());
	}

	private boolean isGameOver() {
		for (HexBase aMHexBase : mHexBase) {
			for (int y = 0; y < mHexStore.mBlockY; y++) {
				for (int x = 0; x < mHexStore.mBlockX; x++) {
					if (isCanBePlace(x, y, mHexStore, aMHexBase)) {
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
		boolean isShow = !mHexBase[mHexBaseSelectedIndex].mHexagonBlocks[0][0].isHide();
		int result;
		if (isEvenNumber(mGameType)) {
			if (isEvenNumber(indexY)) {
				if (isShow) {
					result = getOddNumber(tmpX);
				} else {
					result = getEvenNumber(tmpX);
				}
			} else {
				if (isShow) {
					result = getEvenNumber(tmpX);
				} else {
					result = getOddNumber(tmpX);
				}
			}
		} else {
			if (isEvenNumber(indexY)) {
				if (isShow) {
					result = getEvenNumber(tmpX);
				} else {
					result = getOddNumber(tmpX);
				}
			} else {
				if (isShow) {
					result = getOddNumber(tmpX);
				} else {
					result = getEvenNumber(tmpX);
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
		/**判断"横"行*/
		int clearLine0 = 0;
		int clearHexNum0 = 0;
		for (int y = 0; y < mHexStore.mBlockY; y++) {
			boolean isFull = true;
			int showNumber = 0;
			for (int x = 0; x < mHexStore.mBlockX; x++) {
				if (!mHexStore.mHexagonBlocks[y][x].isHide()) {
					if (mHexStore.mHexagonBlocks[y][x].hasColor()) {
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
				for (int x = 0; x < mHexStore.mBlockX; x++) {
					if (!mHexStore.mHexagonBlocks[y][x].isHide()) {
						if (mHexStore.mHexagonBlocks[y][x].hasColor()) {
							mHexStore.mHexagonBlocks[y][x].setState(HexSingle.STATE_NEED_ELIMINATE);
						}
					}
				}
			}
		}


		/**判断"/"行*/
		int clearLine1 = 0;
		int clearHexNum1 = 0;
		for (int i = 0; i < mHexStore.mBlockX + mHexStore.mBlockY - 1; i++) {
			boolean isFull = true;
			int showNumber = 0;
			for (int y = 0; y < mHexStore.mBlockY; y++) {
				int x = i - y;
				if (x < 0 || x >= mHexStore.mBlockX) {
					continue;
				}
				if (!mHexStore.mHexagonBlocks[y][x].isHide()) {
					if (mHexStore.mHexagonBlocks[y][x].hasColor()) {
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
				for (int y = 0; y < mHexStore.mBlockY; y++) {
					int x = i - y;
					if (x < 0 || x >= mHexStore.mBlockX) {
						continue;
					}
					if (!mHexStore.mHexagonBlocks[y][x].isHide()) {
						if (mHexStore.mHexagonBlocks[y][x].hasColor()) {
							mHexStore.mHexagonBlocks[y][x].setState(HexSingle.STATE_NEED_ELIMINATE);
						}
					}
				}
			}
		}

		/**判断"\"行*/
		int clearLine2 = 0;
		int clearHexNum2 = 0;
		for (int i = 0; i < mHexStore.mBlockX + mHexStore.mBlockY - 1; i++) {
			boolean isFull = true;
			int showNumber = 0;
			for (int y = 0; y < mHexStore.mBlockY; y++) {
				int x = mHexStore.mBlockX - (i - y);
				if (x < 0 || x >= mHexStore.mBlockX) {
					continue;
				}
				if (!mHexStore.mHexagonBlocks[y][x].isHide()) {
					if (mHexStore.mHexagonBlocks[y][x].hasColor()) {
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
				for (int y = 0; y < mHexStore.mBlockY; y++) {
					int x = mHexStore.mBlockX - (i - y);
					if (x < 0 || x >= mHexStore.mBlockX) {
						continue;
					}
					if (!mHexStore.mHexagonBlocks[y][x].isHide()) {
						if (mHexStore.mHexagonBlocks[y][x].hasColor()) {
							mHexStore.mHexagonBlocks[y][x].setState(HexSingle.STATE_NEED_ELIMINATE);
						}
					}
				}
			}
		}


		/**消除数据*/
		for (int y = 0; y < mHexStore.mBlockY; y++) {
			for (int x = 0; x < mHexStore.mBlockX; x++) {
				if (mHexStore.mHexagonBlocks[y][x].getState() == HexSingle.STATE_NEED_ELIMINATE) {
					mHexStore.mHexagonBlocks[y][x].resetColor(getContext().getApplicationContext());
				}
			}
		}

		/**计算此次得分*/
		int clearLineTotal = clearLine0 + clearLine1 + clearLine2;
		for (int i = 0; i < clearLineTotal; i++) {
			SoundPoolUtils.getInstance().play(SOUND_POOL_CLEAR);
		}
		int clearHexNumTotal = clearHexNum0 + clearHexNum1 + clearHexNum2;
		mScore += clearHexNumTotal * (clearLineTotal + 1);
		mBestScore = Math.max(mScore, mBestScore);
		if (mOnGameListener != null) {
			mOnGameListener.onScoreChange(mScore, mBestScore);
		}
	}


	/**
	 * @param indexX indexX
	 * @param indexY indexY
	 * @param data   data
	 * @param isSave isSave
	 */
	private void updateHexStore(int indexX, int indexY, HexBase data, boolean isSave) {
		clearTempColor();


		//更新
		for (int y = 0; y < data.mBlockY; y++) {
			for (int x = 0; x < data.mBlockX; x++) {
				if (!data.mHexagonBlocks[y][x].isHide()) {
					HexSingle hexSingle = mHexStore.mHexagonBlocks[y + indexY][x + indexX];
					hexSingle.setColor(data.mColor);
					if (isSave) {
						hexSingle.setState(HexSingle.STATE_HAS_COLOR);
					} else {
						hexSingle.setState(HexSingle.STATE_TEMP_COLOR);
					}
				}
			}
		}
	}

	/**
	 * 清空临时颜色
	 */
	private void clearTempColor() {
		for (int y = 0; y < mHexStore.mBlockY; y++) {
			for (int x = 0; x < mHexStore.mBlockX; x++) {
				if (!mHexStore.mHexagonBlocks[y][x].isHide()) {
					HexSingle hexSingle = mHexStore.mHexagonBlocks[y][x];
					if (hexSingle.getState() == HexSingle.STATE_TEMP_COLOR) {
						hexSingle.resetColor(getContext().getApplicationContext());
					}
				}
			}
		}
	}

	private int isOneHexBaseSelected(float eventX, float eventY) {
		for (int i = 0; i < mHexBaseRectF.length; i++) {
			if (eventX <= mHexBaseRectF[i].right && eventX >= mHexBaseRectF[i].left && eventY <= mHexBaseRectF[i].bottom && eventY >= mHexBaseRectF[i].top) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 初始化六边形边长
	 */
	private void initRadius(int w, int h) {

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

	public float getHexagonRadius(int width, int height) {
		if (width <= 0 || height <= 0 || mGameType <= 0) {
			return 0;
		}
		return (float) ((float) width / (mGameType * 2.0 - 1) / Math.sqrt(3.0));
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
		drawHexagons(canvas, mHexStore.mHexagonBlocks, mHexStoreRectF, mRadiusOutL, mRadiusInnerL, mPaint);
	}

	private void drawHexBase(Canvas canvas) {
		//绘制待选项
		for (int i = 0; i < mHexBase.length; i++) {
			if (i != mHexBaseSelectedIndex) {
				drawHexagons(canvas, mHexBase[i].mHexagonBlocks, mHexBaseRectF[i], mRadiusOutS, mRadiusInnerS, mPaint);
			}
		}
		//绘制选中项
		if (mHexBaseSelectedIndex >= 0 && mHexBaseSelectedIndex < mHexBase.length) {
			drawHexagons(canvas, mHexBase[mHexBaseSelectedIndex].mHexagonBlocks, mEventX, mEventY, mRadiusOutM, mRadiusInnerM, mPaint);
		}
	}

	/**
	 * 在Y轴方向增加delY个方块高度
	 */
	private float mDeltaY = 0.08f;

	/**
	 * 绘制六边形组合图
	 */
	private void drawHexagons(Canvas canvas, HexSingle[][] data, float xTopLeft, float yTopLeft, float rOut, float rInner, Paint paint) {
		if (data == null || data.length <= 0 || data[0].length <= 0) {
			return;
		}

		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[0].length; x++) {
				if (!data[y][x].isHide()) {
					float centerX = (float) (xTopLeft + (x + 1) * rOut * Math.cos(Math.PI / 6.0));
					float centerY = yTopLeft + rOut + y * rOut * (1.5f + mDeltaY);
					paint.setColor(data[y][x].getColor());
					if (data[y][x].getState() == HexSingle.STATE_TEMP_COLOR) {
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
	private void drawHexagons(Canvas canvas, HexSingle[][] data, RectF rectF, float rOut, float rInner, Paint paint) {
		drawHexagons(canvas, data, rectF.left, rectF.top, rOut, rInner, paint);
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
	public boolean isCanBePlace(int placeX, int placeY, HexStore hexStore, HexBase hexBase) {
		if (hexStore == null || hexBase == null) {
			return false;
		}
		if (placeX < 0 || placeY < 0 || placeX >= hexStore.mBlockX || placeY >= hexStore.mBlockY) {
			return false;
		}

		//刚好错位，不能放在此位置
		if (hexStore.mHexagonBlocks[placeY][placeX].isHide() != hexBase.mHexagonBlocks[0][0].isHide()) {
			return false;
		}
		for (int y = 0; y < hexBase.mBlockY; y++) {
			for (int x = 0; x < hexBase.mBlockX; x++) {
				if (!hexBase.mHexagonBlocks[y][x].isHide()) {
					//左边、上边越界
					if (placeY + y < 0 || placeX + x < 0) {
						return false;
					}
					//右边、下边越界
					if (placeY + y >= hexStore.mBlockY || placeX + x >= hexStore.mBlockX) {
						return false;
					}
					//已填充
					if (hexStore.mHexagonBlocks[placeY + y][placeX + x].hasColor()) {
						return false;
					}
					//此位置不显示
					if (hexStore.mHexagonBlocks[placeY + y][placeX + x].isHide()) {
						return false;
					}
				}
			}
		}
		return true;
	}


}
