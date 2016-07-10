package com.lt.hm.wovideo.utils;

import android.view.View;

import java.util.Calendar;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/4/8
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {
	public static final int MIN_CLICK_DELAY_TIME = 3000;
	private long lastClickTime = 0;
	@Override
	public void onClick(View v) {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
			lastClickTime = currentTime;
			onNoDoubleClick(v);
		}
	}
	public abstract void onNoDoubleClick(View v);
}
