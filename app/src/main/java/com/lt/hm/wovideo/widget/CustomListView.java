package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * @author leonardo
 * @create_date 16/1/11
 * @version 1.0
 */
public class CustomListView extends ListView {
	public CustomListView(Context context) {
		super(context);
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}


	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return ev.getAction() == MotionEvent.ACTION_MOVE || super.dispatchTouchEvent(ev);
	}
}
