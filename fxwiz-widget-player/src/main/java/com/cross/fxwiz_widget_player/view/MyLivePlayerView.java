package com.cross.fxwiz_widget_player.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.cross.fxwiz_widget_player.R;

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
		mSourceBuilder.setSource(mMediaUrls[mMediaIndex]);
		AliyunLocalSource mLocalSource = mSourceBuilder.build();
		mAliyunPlayer.prepareAsync(mLocalSource);
	}

	@Override
	public void changeUiToPreparing() {
		startButton.setVisibility(INVISIBLE);
		loadingProgressBar.setVisibility(VISIBLE);
	}

	@Override
	public void changeUiToPlayingShow() {
		updateStartImage();
		loadingProgressBar.setVisibility(INVISIBLE);
		controlsUiChange(true);
	}

	@Override
	public void changeUiToPlayingClear() {
		updateStartImage();
	}

	@Override
	public void changeUiToPauseShow() {
		updateStartImage();
		controlsUiChange(true);
	}

	@Override
	public void changeUiToComplete() {
		updateStartImage();

	}

	@Override
	public void changeUiToError() {
		updateStartImage();

	}

	@Override
	public void hideUiControls() {
		controlsUiChange(false);

	}

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

}
