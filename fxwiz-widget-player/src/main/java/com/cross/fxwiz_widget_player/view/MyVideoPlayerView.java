package com.cross.fxwiz_widget_player.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class MyVideoPlayerView extends MyLivePlayerView implements View.OnTouchListener {

	protected Timer UPDATE_PROGRESS_TIMER;
	private ProgressTimerTask mProgressTimerTask;

	public TextView currentTimeTextView, totalTimeTextView;
	public SeekBar progressBar;

	protected boolean mTouchingProgressBar;

	protected Dialog mProgressDialog;
	protected ProgressBar mDialogProgressBar;
	protected TextView mDialogSeekTime;
	protected TextView mDialogTotalTime;
	protected ImageView mDialogIcon;
	//TODO 下个版本添加音量控制
	//	protected Dialog mVolumeDialog;
	//	protected ProgressBar mDialogVolumeProgressBar;
	//	protected TextView mDialogVolumeTextView;
	//	protected ImageView mDialogVolumeImageView;
	//	protected Dialog mBrightnessDialog;
	//	protected ProgressBar mDialogBrightnessProgressBar;
	//	protected TextView mDialogBrightnessTextView;


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
//		textureViewContainer.setOnTouchListener(this);
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
					long duration = mDuration;
					currentTimeTextView.setText(PlayerUtils.stringForTime(progress * duration / 10000));
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.i(TAG, "bottomProgress onStartTrackingTouch [" + this.hashCode() + "] ");
				cancelProgressTimer();
				ViewParent vpdown = getParent();
				while (vpdown != null) {
					vpdown.requestDisallowInterceptTouchEvent(true);
					vpdown = vpdown.getParent();
				}
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.w(TAG, "bottomProgress onStopTrackingTouch [" + this.hashCode() + "] ");
				//				onEvent(JZUserAction.ON_SEEK_POSITION);
				ViewParent vpup = getParent();
				while (vpup != null) {
					vpup.requestDisallowInterceptTouchEvent(false);
					vpup = vpup.getParent();
				}
				if (mCurrentState != PlayerState.CURRENT_STATE_PLAYING && mCurrentState != PlayerState.CURRENT_STATE_PAUSE)
					return;
				long time = seekBar.getProgress() * mDuration / 10000;

				mAliyunPlayer.seekTo((int) time);
				changeUiToPreparing();
				Log.w(TAG, "seekTo " + time + " [" + this.hashCode() + "] ");
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
			if (progress != 0)
				progressBar.setProgress(progress);
		}
		if (position != 0)
			currentTimeTextView.setText(PlayerUtils.stringForTime(position));
		totalTimeTextView.setText(PlayerUtils.stringForTime(duration));

	}

	protected float mDownX;
	protected float mDownY;
	protected boolean mChangeVolume;
	protected boolean mChangePosition;
	protected boolean mChangeBrightness;
	protected long mGestureDownPosition;
	//	protected int mGestureDownVolume;
	//	protected float mGestureDownBrightness;
	protected long mSeekTimePosition;

	public static final int THRESHOLD = 80;

	//	public static final int FULL_SCREEN_NORMAL_DELAY = 300;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		int id = v.getId();
		if (id == R.id.surface_container) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.i(TAG, "onTouch surfaceContainer actionDown [" + this.hashCode() + "] ");
					mTouchingProgressBar = true;

					mDownX = x;
					mDownY = y;
					//					mChangeVolume = false;
					//					mChangePosition = false;
					//					mChangeBrightness = false;
					break;
				case MotionEvent.ACTION_MOVE:
					Log.i(TAG, "onTouch surfaceContainer actionMove [" + this.hashCode() + "] ");
					float deltaX = x - mDownX;
					float deltaY = y - mDownY;
					float absDeltaX = Math.abs(deltaX);
					float absDeltaY = Math.abs(deltaY);
					if (mCurrentScreenState == PlayerState.SCREEN_WINDOW_FULLSCREEN) {
						if (!mChangePosition && !mChangeVolume && !mChangeBrightness) {
							if (absDeltaX > THRESHOLD || absDeltaY > THRESHOLD) {
								cancelProgressTimer();
								if (absDeltaX >= THRESHOLD) {
									// 全屏模式下的CURRENT_STATE_ERROR状态下,不响应进度拖动事件.
									// 否则会因为mediaplayer的状态非法导致App Crash
									if (mCurrentState != PlayerState.CURRENT_STATE_ERROR) {
										mChangePosition = true;
										mGestureDownPosition = mAliyunPlayer.getCurrentPosition();
									}
								} /*else {
//									如果y轴滑动距离超过设置的处理范围，那么进行滑动事件处理
									if (mDownX < mScreenWidth * 0.5f) {//左侧改变亮度
										mChangeBrightness = true;
										WindowManager.LayoutParams lp = PlayerUtils.getWindow(getContext()).getAttributes();
										if (lp.screenBrightness < 0) {
											try {
												mGestureDownBrightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
												Log.i(TAG, "current system brightness: " + mGestureDownBrightness);
											} catch (Settings.SettingNotFoundException e) {
												e.printStackTrace();
											}
										} else {
											mGestureDownBrightness = lp.screenBrightness * 255;
											Log.i(TAG, "current activity brightness: " + mGestureDownBrightness);
										}
									} else {//右侧改变声音
										mChangeVolume = true;
										mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
									}
								}*/
							}
						}
					}
					if (mChangePosition) {
						long totalTimeDuration = mDuration;
						mSeekTimePosition = (int) (mGestureDownPosition + deltaX * totalTimeDuration / mScreenWidth);
						if (mSeekTimePosition > totalTimeDuration)
							mSeekTimePosition = totalTimeDuration;
						String seekTime = PlayerUtils.stringForTime(mSeekTimePosition);
						String totalTime = PlayerUtils.stringForTime(totalTimeDuration);
						//                      TODO 进度dialog
						//						showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);
					}
					//					if (mChangeVolume) {
					//						deltaY = -deltaY;
					//						int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					//						int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
					//						mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
					//						//dialog中显示百分比
					//						int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);
					//						showVolumeDialog(-deltaY, volumePercent);
					//					}

					//					if (mChangeBrightness) {
					//						deltaY = -deltaY;
					//						int deltaV = (int) (255 * deltaY * 3 / mScreenHeight);
					//						WindowManager.LayoutParams params = PlayerUtils.getWindow(getContext()).getAttributes();
					//						if (((mGestureDownBrightness + deltaV) / 255) >= 1) {//这和声音有区别，必须自己过滤一下负值
					//							params.screenBrightness = 1;
					//						} else if (((mGestureDownBrightness + deltaV) / 255) <= 0) {
					//							params.screenBrightness = 0.01f;
					//						} else {
					//							params.screenBrightness = (mGestureDownBrightness + deltaV) / 255;
					//						}
					//						PlayerUtils.getWindow(getContext()).setAttributes(params);
					//						//dialog中显示百分比
					//						int brightnessPercent = (int) (mGestureDownBrightness * 100 / 255 + deltaY * 3 * 100 / mScreenHeight);
					//						showBrightnessDialog(brightnessPercent);
					//						//                        mDownY = y;
					//					}
					break;
				case MotionEvent.ACTION_UP:
					Log.i(TAG, "onTouch surfaceContainer actionUp [" + this.hashCode() + "] ");
					mTouchingProgressBar = false;
					dismissProgressDialog();
					//					dismissVolumeDialog();
					//					dismissBrightnessDialog();
					if (mChangePosition) {
						//TODO unknown
						//						onEvent(JZUserAction.ON_TOUCH_SCREEN_SEEK_POSITION);
						//						JZMediaManager.seekTo(mSeekTimePosition);
						long duration = mDuration;
						int progress = (int) (mSeekTimePosition * 10000 / (duration == 0 ? 1 : duration));
						progressBar.setProgress(progress);
					}
					if (mChangeVolume) {
						//						onEvent(JZUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME);
					}
					startProgressTimer();
					break;
			}
		}
		return false;
	}

	public void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
		if (mProgressDialog == null) {
			View localView = LayoutInflater.from(getContext()).inflate(R.layout.layout_player_dialog_progress, null);
			mDialogProgressBar = (ProgressBar) localView.findViewById(R.id.duration_progressbar);
			mDialogSeekTime = (TextView) localView.findViewById(R.id.tv_current);
			mDialogTotalTime = (TextView) localView.findViewById(R.id.tv_duration);
			mDialogIcon = (ImageView) localView.findViewById(R.id.duration_image_tip);
			mProgressDialog = createDialogWithView(localView);
		}
		if (!mProgressDialog.isShowing()) {
			mProgressDialog.show();
		}

		mDialogSeekTime.setText(seekTime);
		mDialogTotalTime.setText(" / " + totalTime);
		mDialogProgressBar.setProgress(totalTimeDuration <= 0 ? 0 : (int) (seekTimePosition * 100 / totalTimeDuration));
		if (deltaX > 0) {
			mDialogIcon.setBackgroundResource(R.drawable.player_forward_icon);
		} else {
			mDialogIcon.setBackgroundResource(R.drawable.player_backward_icon);
		}
		onUiToggleToClear();
	}

	/**
	 * 切换ui到原模式
	 * 1.进度、音量、亮度的滑动之后
	 * 2.解锁后
	 */
	public void onUiToggleToClear() {
		if (mCurrentState == PlayerState.CURRENT_STATE_PREPARING) {
			changeUiToPreparing();
		} else if (mCurrentState == PlayerState.CURRENT_STATE_PLAYING) {
			changeUiToPlayingClear();
		} else if (mCurrentState == PlayerState.CURRENT_STATE_PAUSE) {
			changeUiToPauseShow();
		} else if (mCurrentState == PlayerState.CURRENT_STATE_COMPLETE) {
			changeUiToComplete();
		}
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
		startProgressTimer();
	}

	@Override
	public void changeUiToPlayingClear() {
		updateStartImage();
		startProgressTimer();
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
		cancelProgressTimer();
	}


	private Dialog createDialogWithView(View localView) {

		Dialog dialog = new Dialog(getContext(), R.style.player_style_dialog_progress);
		dialog.setContentView(localView);
		Window window = dialog.getWindow();
		window.addFlags(Window.FEATURE_ACTION_BAR);
		window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		window.setLayout(-2, -2);
		WindowManager.LayoutParams localLayoutParams = window.getAttributes();
		localLayoutParams.gravity = Gravity.CENTER;
		window.setAttributes(localLayoutParams);
		return dialog;
	}

	private void startProgressTimer() {
		Log.i(TAG, "startProgressTimer: " + " [" + this.hashCode() + "] ");
		cancelProgressTimer();
		UPDATE_PROGRESS_TIMER = new Timer();
		mProgressTimerTask = new ProgressTimerTask();
		UPDATE_PROGRESS_TIMER.schedule(mProgressTimerTask, 0, 300);
	}

	private void cancelProgressTimer() {
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
			if (mCurrentState == PlayerState.CURRENT_STATE_PLAYING || mCurrentState == PlayerState.CURRENT_STATE_PAUSE) {
				post(new Runnable() {
					@Override
					public void run() {
						mCurrentPosition = mAliyunPlayer.getCurrentPosition();
						int progress = (int) (mCurrentPosition * 10000 / (mDuration == 0 ? 1 : mDuration));
						setProgressAndText(progress, mCurrentPosition, mDuration);
					}
				});
			}
		}
	}

}
