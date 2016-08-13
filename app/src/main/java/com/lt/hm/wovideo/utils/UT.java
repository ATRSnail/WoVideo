package com.lt.hm.wovideo.utils;

import android.content.Context;
import android.widget.Toast;

import com.lt.hm.wovideo.AppContext;

/**
 * Toast统一管理类
 * 
 */
public class UT {

	private UT() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isShow = true;
	private static Toast toast;
	private static Context context = AppContext.getInstance();

	/**
	 * 短时间显示Toast
	 * 
	 * @param message
	 */
	public static void showShort(CharSequence message) {
		if (isShow) {
			if (toast != null)
				toast.cancel();
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param message
	 */
	public static void showShort(int message) {
		if (isShow) {
			if (toast != null)
				toast.cancel();
			toast = Toast.makeText(context,
					context.getResources().getString(message), Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param message
	 */
	public static void showLong(CharSequence message) {
		if (isShow) {
			if (toast != null)
				toast.cancel();
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			toast.show();
		}
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param message
	 * @param duration
	 */
	public static void show(CharSequence message, int duration) {
		if (isShow) {
			if (toast != null)
				toast.cancel();
			toast = Toast.makeText(context, message, duration);
			toast.show();
		}
	}
	
	public static void show(CharSequence message) {
		showShort(message);
	}
	public static void showOnUi(final CharSequence message) {
//		AppContext.getInstance().runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
////				showShort(message);
//                showNormal(message);
//			}
//		});

	}



	/**
	 * cannot cancel
	 * 
	 * @param message
	 */
	public static void showNormal(CharSequence message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

}