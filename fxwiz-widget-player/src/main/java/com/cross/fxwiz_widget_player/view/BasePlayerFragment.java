package com.cross.fxwiz_widget_player.view;


import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.cross.fxwiz_widget_player.R;
import com.cross.fxwiz_widget_player.utils.PermissionUtils;

/**
 * Created by cross on 2018/5/21.
 * <p>描述:封装成fragment
 */
public abstract class BasePlayerFragment extends Fragment {

	private BasePlayerView mPlayerView;
	private String mTitle;
	private String mMediaUrl;
	private ViewGroup mContainerView;
	private boolean isCreated;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (mPlayerView == null) {
			mPlayerView = (BasePlayerView) inflater.inflate(getLayoutId(), container, false);
		} else {
			ViewParent parent = mPlayerView.getParent();
			if (parent != null) {
				((ViewGroup) parent).removeView(mPlayerView);
			}
		}
		return mPlayerView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (TextUtils.isEmpty(mMediaUrl)) {
			mPlayerView.setUp(mTitle, mMediaUrl);
			mPlayerView.setContainerView(mContainerView);
		}
		isCreated = true;
	}

	private int getLayoutId() {

		return isVideoPlayer() ? R.layout.fragment_layout_player_video : R.layout.fragment_layout_player_live;
	}

	protected abstract boolean isVideoPlayer();

	/**
	 * 设置参数
	 * @param title 视频标题
	 * @param mediaUrl 视频Url
	 * @param containerView 播放器的容器 全屏需要此参数
	 */
	public void setUp(String title, String mediaUrl, ViewGroup containerView){
		this.mTitle = title;
		this.mMediaUrl = mediaUrl;
		this.mContainerView = containerView;
		if (isCreated){
			mPlayerView.setUp(mTitle, mMediaUrl);
			mPlayerView.setContainerView(mContainerView);
		}
	}

	/**
	 * 全屏的返回处理
	 */
	public boolean isInterceptBackPressed() {
		return mPlayerView != null && mPlayerView.isInterceptBackPressed();
	}

	/**
	 * 添加处理横竖屏
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mPlayerView.onActivityConfigurationChanged(newConfig);
	}


	/**
	 * 5.0 权限适配
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PermissionUtils.REQUEST_STORAGE_PERMISSION) {
			for (int i = 0; i < permissions.length; i++) {
				if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
					return;
				}
			}
			mPlayerView.onPermissionSuccess();
		}
	}

	//提供给子类的findViewById方法
	protected final View findViewById(int id) {
		return mPlayerView.findViewById(id);
	}

	protected ViewGroup getContentView() {
		return (ViewGroup) mPlayerView;
	}


}
