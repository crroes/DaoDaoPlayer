package com.cross.fxwiz_widget_player.view;


import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.cross.fxwiz_widget_player.R;
import com.cross.fxwiz_widget_player.utils.MediaBean;
import com.cross.fxwiz_widget_player.utils.PermissionUtils;

/**
 * Created by cross on 2018/5/21.
 * <p>描述:播放器封装成fragment
 */
public abstract class BasePlayerFragment extends Fragment {

	private BasePlayerView mPlayerView;
	private ViewGroup mContainerView;
	private boolean isCreated;
	private OnPlayerStatusChangeListener mOnPlayerStatusChangeListener;
	private MediaBean mMediaBean;

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
		if (mMediaBean != null) {
			setPlayerData();
		}
		isCreated = true;
	}

	private int getLayoutId() {

		return isVideoPlayer() ? R.layout.fragment_layout_player_video : R.layout.fragment_layout_player_live;
	}

	protected abstract boolean isVideoPlayer();

	/**
	 * 设置参数
	 *
	 * @param mediaBean     {@link MediaBean}
	 * @param containerView 播放器的容器 全屏需要此参数
	 */
	public void setUp(@NonNull MediaBean mediaBean, ViewGroup containerView) {
		mMediaBean = mediaBean;
		mContainerView = containerView;
		if (isCreated) {
			setPlayerData();
		}
	}

	/**
	 * 设置播放器数据
	 */
	private void setPlayerData() {
		mPlayerView.setUp(mMediaBean,this);
		mPlayerView.setContainerView(mContainerView);
		if (mOnPlayerStatusChangeListener != null) {
			mPlayerView.setOnPlayerStatusChangeListener(mOnPlayerStatusChangeListener);
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
	 * 6.0 权限适配
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PermissionUtils.REQUEST_STORAGE_PERMISSION_CODE) {
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

	/**
	 * 设置播放器状态监听
	 *
	 * @param listener {@link OnPlayerStatusChangeListener}
	 */
	public void setOnPlayerStatusChangeListener(OnPlayerStatusChangeListener listener) {
		this.mOnPlayerStatusChangeListener = listener;
		if (isCreated && mMediaBean != null) {
			mPlayerView.setOnPlayerStatusChangeListener(mOnPlayerStatusChangeListener);
		}
	}

//----------------同步播放器和View的生命周期 start----------------------
	@Override
	public void onPause() {
		super.onPause();
		mPlayerView.onContextPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mPlayerView.onContextResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPlayerView.onContextDestroy();
	}

	@Override
	public void onStop() {
		super.onStop();
		mPlayerView.onContextStop();
	}

//----------------同步播放器和View的生命周期 end----------------------

	/**
	 * 视频播放状态监听器
	 */
	public abstract static class OnPlayerStatusChangeListener {

		private String TAG = "OnPlayerStatusChangeListener";

		//开始
		public void onStart() {
			Log.i(TAG, "onStart()");
		}

		//继续播放
		public void onContinue(long length) {
			Log.i(TAG, "onContinue() length = " + length);
		}

		//暂停
		public void onPause(long length) {
			Log.i(TAG, "onPause() length = " + length);
		}

		//异常
		public void onError(long length) {
			Log.i(TAG, "onError() length = " + length);
		}

		//停止
		public void onStop(long length) {
			Log.i(TAG, "onStop() length = " + length);
		}

		//完成
		public void onComplete() {
			Log.i(TAG, "onComplete()");
		}

	}

}
