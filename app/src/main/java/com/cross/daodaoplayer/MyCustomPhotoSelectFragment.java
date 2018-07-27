package com.cross.daodaoplayer;

import com.cross.daodaoplayer.utils.FileUtils;
import com.image.selector.view.CustomPhotoSelectFragment;

import java.io.File;

/**
 * Created by cross on 2018/7/27.
 * <p>描述:
 */

public class MyCustomPhotoSelectFragment extends CustomPhotoSelectFragment {
	@Override
	protected File getPathDir() {
		return FileUtils.getRootDir();
	}

	@Override
	protected String getProvideString() {
		return MyApplication.getContext().getPackageName();
	}
}
