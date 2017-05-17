package com.lt.hm.wovideo.utils;

import android.util.Log;

public class TLog {
	public static final String LOG_TAG = "WoVideo";
	// 运行时 屏蔽 日志输出
	public static boolean DEBUG = true;

	public TLog() {
		        /* cannot be instantiated */
//		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static final void analytics(String log) {
		if (DEBUG)
			Log.d(LOG_TAG, log);
	}

	public static final void error(String log) {
		if (DEBUG)
			Log.e(LOG_TAG, "" + log);
	}

	public static final void log(String log) {
		if (DEBUG)
			Log.i(LOG_TAG, log);
	}

	public static final void log(String tag, String log) {
		if (DEBUG)
			Log.i(tag, log);
	}

	public static final void logv(String log) {
		if (DEBUG)
			Log.v(LOG_TAG, log);
	}

	public static final void warn(String log) {
		if (DEBUG)
			Log.w(LOG_TAG, log);
	}
}
