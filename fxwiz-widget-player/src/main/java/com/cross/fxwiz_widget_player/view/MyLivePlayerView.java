package com.cross.fxwiz_widget_player.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.cross.fxwiz_widget_player.R;
import com.cross.fxwiz_widget_player.utils.PlayerState;

/**
 * Created by cross on 2018/5/14.
 * <p>描述:直播播放器自定义view
 */

class MyLivePlayerView extends BasePlayerView {

	AliyunLocalSource.AliyunLocalSourceBuilder mSourceBuilder;

	public MyLivePlayerView(@NonNull Context context) {
		super(context);
	}

	public MyLivePlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLivePlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	@Override
	protected int getLayoutId() {
		return R.layout.layout_player_live;
	}

	@Override
	protected void setMediaSource() {

		if (mSourceBuilder == null) {
			mSourceBuilder = new AliyunLocalSource.AliyunLocalSourceBuilder();
		}
		Log.i(TAG, mMediaBean.getVideoUrl() + "");
		mSourceBuilder.setSource(mMediaBean.getVideoUrl());
		AliyunLocalSource mLocalSource = mSourceBuilder.build();
		mAliyunPlayer.prepareAsync(mLocalSource);
	}

	/**
	 * 四种情况
	 * 1.开始
	 * 2.拖动进度条
	 * 3.错误跳转
	 * 4.完成播放重新播放
	 */
	@Override
	public void changeUiToPreparing() {
		updateStartImage();
		loadingProgressBar.setVisibility(VISIBLE);
		errorRetryLayout.setVisibility(GONE);
		replayTextView.setVisibility(GONE);
	}

	/**
	 * 三种情况
	 * 1.加载中跳转(锁屏状态时直接影藏ui)
	 * 2.暂停跳转
	 * 3.播放中界面点击
	 * 4.快进完成
	 */
	@Override
	public void changeUiToPlayingShow() {
		loadingProgressBar.setVisibility(INVISIBLE);
		if (mCurrentScreenLockState == PlayerState.SCREEN_WINDOW_LOCK) {
			//全屏加载中锁屏时
			return;
		}
		updateStartImage();
		thumbImageView.setVisibility(GONE);
		controlsUiChange(true);
		startHideUiTimer();
	}

	/**
	 * 1.三秒隐藏
	 */
	@Override
	public void changeUiToPlayingClear() {
		controlsUiChange(false);
	}

	/**
	 * 1.播放中暂停
	 */
	@Override
	public void changeUiToPauseShow() {
		updateStartImage();
		controlsUiChange(true);
	}

	/**
	 * 1.显示播放完成
	 * 2.隐藏播放完成
	 */
	@Override
	public void changeUiToComplete() {
		updateStartImage();
		replayTextView.setVisibility(VISIBLE);
		topContainer.setVisibility(VISIBLE);
		bottomContainer.setVisibility(INVISIBLE);
	}

	/**
	 * 两种情况跳错误页面
	 * 1.播放中
	 * 2.加载中
	 */
	@Override
	public void changeUiToError() {
		updateStartImage();
		loadingProgressBar.setVisibility(INVISIBLE);
		errorRetryLayout.setVisibility(VISIBLE);
		bottomContainer.setVisibility(INVISIBLE);
	}

	@Override
	public void hideUiControls() {
		controlsUiChange(false);
	}

	/**
	 * 播放器界面ui的延时影藏与显示
	 *
	 * @param isShow show if true
	 */
	protected void controlsUiChange(boolean isShow) {
		if (isShow) {
			topContainer.setVisibility(VISIBLE);
			bottomContainer.setVisibility(VISIBLE);
			startButton.setVisibility(VISIBLE);
		} else {
			topContainer.setVisibility(INVISIBLE);
			bottomContainer.setVisibility(INVISIBLE);
			startButton.setVisibility(INVISIBLE);
		}
	}

	//分享图标的显示与影藏
	private void shareIconChange(boolean isShow) {

		if (mCurrentScreenState == PlayerState.SCREEN_WINDOW_FULLSCREEN) {
			shareImageView.setVisibility(GONE);
		} else {
			if (isShow) {
				shareImageView.setVisibility(VISIBLE);
			} else {
				shareImageView.setVisibility(GONE);
			}
		}
	}

}
