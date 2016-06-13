package com.cpxiao.hexagon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by cpxiao on 6/13/16.
 * BaseActivity
 */
public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		//隐藏状态栏部分（电池电量、时间等部分）
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
				.LayoutParams.FLAG_FULLSCREEN);


	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
