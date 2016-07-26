package com.lt.hm.wovideo.utils;

import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/24
 */
public abstract class NoDoubleItemClickListener implements AdapterView.OnItemClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 2000;
    private long lastClickTime = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleItemClick(parent, view, position, id);

        }
    }

    //	@Override
//	public void onClick(View v) {
//		long currentTime = Calendar.getInstance().getTimeInMillis();
//		if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
//			lastClickTime = currentTime;
//			onNoDoubleClick(v);
//		}
//	}
    public abstract void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id);
}
