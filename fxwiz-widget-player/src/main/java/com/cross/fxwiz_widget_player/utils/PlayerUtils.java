package com.cross.fxwiz_widget_player.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ContextThemeWrapper;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by cross on 2018/5/15.
 * <p>描述:工具类
 */

public class PlayerUtils {


	public static String stringForTime(long timeMs) {
		if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
			return "00:00";
		}
		long totalSeconds = timeMs / 1000;
		int seconds = (int) (totalSeconds % 60);
		int minutes = (int) ((totalSeconds / 60) % 60);
		int hours = (int) (totalSeconds / 3600);
		StringBuilder stringBuilder = new StringBuilder();
		Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * Get AppCompatActivity from context
	 *
	 * @param context context
	 * @return AppCompatActivity if it's not null
	 */
	public static FragmentActivity getAppCompActivity(Context context) {
		if (context == null) return null;
		if (context instanceof FragmentActivity) {
			return (FragmentActivity) context;
		} else if (context instanceof ContextThemeWrapper) {
			return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
		}
		return null;
	}

	/**
	 * Get activity from context object
	 *
	 * @param context context
	 * @return object of Activity or null if it is not Activity
	 */
	private static Activity scanForActivity(Context context) {
		if (context == null) return null;

		if (context instanceof Activity) {
			return (Activity) context;
		} else if (context instanceof ContextWrapper) {
			return scanForActivity(((ContextWrapper) context).getBaseContext());
		}

		return null;
	}

}
