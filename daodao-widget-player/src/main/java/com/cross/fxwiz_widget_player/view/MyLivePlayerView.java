package com.cross.fxwiz_widget_player.view;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.cross.fxwiz_widget_player.R;
import com.cross.fxwiz_widget_player.utils.PlayerState;
import com.cross.fxwiz_widget_player.utils.PlayerUtils;

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
	protected void cancelProgressTimer() {

	}

	@Override
	protected void setPositionProgress(int progress, long seekTime, long totalTime) {
		Log.d(TAG, "直播不支持滑动进度");
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
	 * 手势滑动音量
	 *
	 * @param deltaY 滑动每个间隔的距离
	 */
	@Override
	public void doGestureVolume(float deltaY) {
		deltaY = -deltaY;
		int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float deltaScreenY = max * deltaY * 2 / mScreenHeight;
		mGestureCurrentVolume = mGestureCurrentVolume + deltaScreenY;
		if (mGestureCurrentVolume > max) {
			mGestureCurrentVolume = max;
		}
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) mGestureCurrentVolume, 0);
		//dialog中显示百分比
		int volumePercent = (int) (mGestureCurrentVolume * 100 / max);
		showVolumeDialog(volumePercent);
	}

	/**
	 * 手势滑动亮度
	 *
	 * @param deltaY 滑动每个间隔的距离
	 */
	@Override
	public void doGestureBrightness(float deltaY) {
		deltaY = -deltaY;
		float deltaScreenY = deltaY * 2 / (float) mScreenHeight;//滑动所占屏幕的百分比（采用1/2屏幕）
		mGestureCurrentBrightness = mGestureCurrentBrightness + deltaScreenY;
		if (mGestureCurrentBrightness > 1) {
			//这和声音有区别，必须自己过滤一下负值
			mGestureCurrentBrightness = 1;
		} else if (mGestureCurrentBrightness <= 0) {
			mGestureCurrentBrightness = 0.001f;
		}
		mWindowLayoutParams.screenBrightness = mGestureCurrentBrightness;

		PlayerUtils.getAppCompActivity(getContext()).getWindow().setAttributes(mWindowLayoutParams);

		//dialog中显示百分比
		int brightnessPercent = (int) (mGestureCurrentBrightness * 100);
		showBrightnessDialog(brightnessPercent);
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
		errorRetryLayout.setVisibility(GONE);
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
		loadingProgressBar.setVisibility(INVISIBLE);
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
			//处理锁后第一次解锁无法直接触发控制ui
			topContainer.setVisibility(GONE);
			bottomContainer.setVisibility(GONE);
			startButton.setVisibility(GONE);
		}
	}

}
