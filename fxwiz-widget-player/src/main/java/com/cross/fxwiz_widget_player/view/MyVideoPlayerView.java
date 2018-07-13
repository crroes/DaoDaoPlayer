package com.cross.fxwiz_widget_player.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cross.fxwiz_widget_player.R;
import com.cross.fxwiz_widget_player.utils.PlayerState;
import com.cross.fxwiz_widget_player.utils.PlayerUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cross on 2018/5/14.
 * <p>描述:视频器播放自定义view
 */

class MyVideoPlayerView extends MyLivePlayerView implements View.OnTouchListener {

	protected Timer UPDATE_PROGRESS_TIMER;
	private ProgressTimerTask mProgressTimerTask;

	public TextView currentTimeTextView, totalTimeTextView;
	public SeekBar progressBar;

	public MyVideoPlayerView(@NonNull Context context) {
		super(context);
	}

	public MyVideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public MyVideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.layout_player_video;
	}

	@Override
	protected void initChild() {

		currentTimeTextView = (TextView) findViewById(R.id.current);
		totalTimeTextView = (TextView) findViewById(R.id.total);
		progressBar = (SeekBar) findViewById(R.id.bottom_seek_progress);
		setSeekBarListener();
	}

	/**
	 * 进度条监听
	 */
	private void setSeekBarListener() {
		progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					//设置这个progres对应的时间，给textview
					long duration = mMediaBean.getDuration();
					currentTimeTextView.setText(PlayerUtils.stringForTime(progress * duration / 10000));
					//Log.d(TAG, "bottomProgress onProgressChanged [" + this.hashCode() + "] ");
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				cancelProgressTimer();
				mCurrentState = PlayerState.CURRENT_STATE_PREPARING;
				ViewParent vpdown = getParent();
				while (vpdown != null) {
					vpdown.requestDisallowInterceptTouchEvent(true);
					vpdown = vpdown.getParent();
				}
				Log.w(TAG, "bottomProgress onStartTrackingTouch [" + this.hashCode() + "] ");
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				cancelProgressTimer();
				ViewParent vpup = getParent();
				while (vpup != null) {
					vpup.requestDisallowInterceptTouchEvent(false);
					vpup = vpup.getParent();
				}
				long time = seekBar.getProgress() * mMediaBean.getDuration() / 10000;

				//毫秒
				if (time < 1000) {
					//处理无法定位到0的bug
					mAliyunPlayer.replay();
					Log.w(TAG, "seekTo start");
					return;
				} else if (seekBar.getProgress() == 10000) {
					//快进到结束
					time = time - 1000;
				}
				mAliyunPlayer.seekTo((int) time);
				mMediaBean.setCurrentPosition(time);
				changeUiToPreparing();
				Log.w(TAG, "seekTo onStopTrackingTouch time:" + PlayerUtils.stringForTime(time) + " time = " + time + " . [" + this.hashCode() + "] ");
			}
		});
	}

	/**
	 * 设置播放进度条和时间显示
	 *
	 * @param progress 百分比
	 * @param position 当前时间
	 * @param duration 总时间
	 */
	private void setProgressAndText(int progress, long position, long duration) {
		if (!mTouchingProgressBar) {
			if (progress != 0) {
				progressBar.setProgress(progress);
			}
		}
		if (position != 0) {
			currentTimeTextView.setText(PlayerUtils.stringForTime(position));
		}
		totalTimeTextView.setText(PlayerUtils.stringForTime(duration));

	}

	/**
	 * 手势滑动进度
	 *
	 * @param deltaX 滑动的值
	 */
	@Override
	public void doGesturePosition(float deltaX) {

		long totalTimeDuration = mMediaBean.getDuration();//总时长

		long remainTime = totalTimeDuration - mMediaBean.getCurrentPosition();
		long fixedValue = 1000 * 60 * 6; //6分钟
		fixedValue = remainTime >= fixedValue ? fixedValue : remainTime;
		mGestureSeekToPosition = (int) (mGestureSeekToPosition + deltaX * fixedValue / mScreenWidth);//滑动一屏幕的值
		if (mGestureSeekToPosition > totalTimeDuration)
			mGestureSeekToPosition = totalTimeDuration;
		String seekTime = PlayerUtils.stringForTime(mGestureSeekToPosition);
		String totalTime = PlayerUtils.stringForTime(totalTimeDuration);
		//进度dialog
		showPositionProgressDialog(deltaX, seekTime, mGestureSeekToPosition, totalTime, totalTimeDuration);
	}

	/**
	 * 滑动进度
	 *
	 * @param progress  进度百分比
	 * @param seekTime  seekTo 时间
	 * @param totalTime 总时间
	 */
	@Override
	protected void setPositionProgress(int progress, long seekTime, long totalTime) {
		bottomContainer.setVisibility(VISIBLE);
		setProgressAndText(progress, seekTime, totalTime);
	}

	@Override
	public void changeUiToPlayingShow() {
		super.changeUiToPlayingShow();
		startProgressTimer();
	}

	@Override
	public void changeUiToPreparing() {
		super.changeUiToPreparing();
		cancelProgressTimer();
	}

	@Override
	public void changeUiToPauseShow() {
		super.changeUiToPauseShow();
		cancelProgressTimer();
	}

	@Override
	public void changeUiToComplete() {
		super.changeUiToComplete();
		cancelProgressTimer();
	}

	@Override
	public void changeUiToError() {
		super.changeUiToError();
		cancelProgressTimer();
	}

	/**
	 * 启动进度timer的情况
	 * 1.进入播放状态
	 */
	private void startProgressTimer() {
		cancelProgressTimer();
		UPDATE_PROGRESS_TIMER = new Timer();
		mProgressTimerTask = new ProgressTimerTask();
		UPDATE_PROGRESS_TIMER.schedule(mProgressTimerTask, 0, 300);
	}

	/**
	 * 取消进度timer的情况
	 * 1.暂停
	 * 2.加载
	 * 3.错误
	 * 4.完成
	 */
	protected void cancelProgressTimer() {
		if (UPDATE_PROGRESS_TIMER != null) {
			UPDATE_PROGRESS_TIMER.cancel();
		}
		if (mProgressTimerTask != null) {
			mProgressTimerTask.cancel();
		}
	}

	/**
	 * 播放进度的TimerTask
	 */
	private class ProgressTimerTask extends TimerTask {
		@Override
		public void run() {
			//正在播放
			if (mCurrentState == PlayerState.CURRENT_STATE_PLAYING) {
				post(new Runnable() {
					@Override
					public void run() {

						long currentPosition = mAliyunPlayer.getCurrentPosition();
						mMediaBean.setCurrentPosition(currentPosition);
						int progress = (int) (currentPosition * 10000 / (mMediaBean.getDuration() == 0 ? 1 : mMediaBean.getDuration()));
						setProgressAndText(progress, currentPosition, mMediaBean.getDuration());
						//Log.d(TAG, "currentPosition ProgressTimerTask "+currentPosition + " [" + this.hashCode() + "] ");
					}
				});
			}
		}
	}

}
