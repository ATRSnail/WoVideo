package com.lt.hm.wovideo.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
/**
 * Created by leonardo on 16/3/21.
 */
public class BaseApplication extends Application {
	protected static Context _context;
	protected static Resources _resource;
	private static String PREF_NAME = "creativelocker.pref";
	private static boolean sIsAtLeastGB;

	static {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			sIsAtLeastGB = true;
		}
	}

	public static synchronized BaseApplication context() {
		return (BaseApplication) _context;
	}



	public static Resources resources() {
		return resources();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static SharedPreferences getPreferences() {
		SharedPreferences pre = context().getSharedPreferences(PREF_NAME, MODE_MULTI_PROCESS);
		return pre;
	}

	public static void apply(SharedPreferences.Editor editor) {
		if (sIsAtLeastGB) {
			editor.apply();
		} else {
			editor.commit();
		}
	}


	// 不同版本的SDK sharepreference 保存数据的 方法不一致。

	public static void set(String key, int value) {
		SharedPreferences.Editor editor = getPreferences().edit();
		editor.putInt(key, value);
		apply(editor);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static SharedPreferences getPreferences(String preName) {
		return context().getSharedPreferences(preName, Context.MODE_WORLD_READABLE);
	}

	public static void saveDispaySize(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		SharedPreferences.Editor editor = getPreferences().edit();
		editor.putInt("screen_width", displayMetrics.widthPixels);
		editor.putInt("screen_height", displayMetrics.heightPixels);
		editor.putFloat("density", displayMetrics.density);
		apply(editor);
	}

	public static int[] getDispaySize() {
		return new int[]{getPreferences().getInt("screen_widht", 480), getPreferences().getInt("screen_height",854)};
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}


}
