package com.cross.daodaoplayer.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by cross on 2018/7/27.
 * <p>描述:文件处理帮助类
 */

public class FileUtils {

	private static final String PATH_ROOT ="com.daodao";
	/**
	 * 获取系统APP外部sdcard的root存储目录
	 * @return root存储目录
	 */
	public static File getRootDir() {
		String strRootDir = PATH_ROOT;
		File rootDir;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File dir = Environment.getExternalStorageDirectory();
			rootDir = new File(dir.getAbsolutePath() + File.separator + strRootDir);
		} else {
			rootDir = new File(strRootDir);
		}
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		return rootDir;
	}
}
