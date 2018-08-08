package com.image.selector.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;


/**
 * Created by cross on 2018/1/23.
 * <p>描述: 拍照缓存，文件提供者处理
 */

public class CameraUtils {

	//拍照的请求码
	public static final int REQUEST_CAMERA_CODE = 0x00000012;
	private static String photoPath;
	private static File sDirPath;
	private static String sProvideString;

	public static void setPathAndProvide(File dirPath,String provideString) {
		sDirPath = dirPath;
		sProvideString = provideString;
	}

	/**
	 * 拍照获取图片
	 */
	public static void selectPicFromCamera(Fragment fragment){

		if (sDirPath == null ||TextUtils.isEmpty(sProvideString)){
			Log.e("CameraUtils","请提供照片缓存路径和Provide");
			return;
		}
		String filename = System.currentTimeMillis() + ".jpg";
		//这里使用项目的图片缓存路径
		File pictureDir = sDirPath;
		File outputImage = new File(pictureDir, filename);

		photoPath = outputImage.getAbsolutePath();
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}
			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri imageUri;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			imageUri = FileProvider.getUriForFile(fragment.getContext(), sProvideString, outputImage);
		} else {
			imageUri = Uri.fromFile(outputImage);
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		fragment.startActivityForResult(intent, REQUEST_CAMERA_CODE);

	}

	public static String getPhotoPath(){
		return photoPath;
	}

}
