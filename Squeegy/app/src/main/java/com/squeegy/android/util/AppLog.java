package com.squeegy.android.util;

import android.util.Log;

public class AppLog {

	private static boolean IS_PRINT_LOG = true;

	public static void d(final String tag, final String msg) {
		if (IS_PRINT_LOG) {
			Log.d(tag, msg);
		}
	}

	public static void v(final String tag, final String msg) {
		if (IS_PRINT_LOG) {
			Log.v(tag, msg);
		}
	}

	public static void w(final String tag, final String msg) {
		if (IS_PRINT_LOG) {
			Log.w(tag, msg);
		}
	}

	public static void e(final String tag, final String msg) {
		if (IS_PRINT_LOG) {
			Log.e(tag, msg);
		}
	}

}
