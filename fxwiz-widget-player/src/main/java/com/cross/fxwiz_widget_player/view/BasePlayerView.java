package com.cross.fxwiz_widget_player.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.cross.fxwiz_widget_player.R;
import com.cross.fxwiz_widget_player.utils.OnClickNoMutiListener;
import com.cross.fxwiz_widget_player.utils.PermissionUtils;
import com.cross.fxwiz_widget_player.utils.PlayerState;
import com.cross.fxwiz_widget_player.utils.PlayerUiControls;
import com.cross.fxwiz_widget_player.utils.PlayerUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cross on 2018/5/14.
 * <p>描述: 播放器基类
 * 需要在activity中调用的方法
 * 1. {@link #setUp(String[], String)}
 * 2. {@link #onPermissionSuccess()}
 * 3. {@link #onActivityConfigurationChanged(Configuration)}
 * 4.
 */

public abstract class BasePlayerView extends FrameLayout implements PlayerUiControls {

	protected final String TAG = this.getClass().getName();
	protected AliyunVodPlayer mAliyunPlayer;
	private SurfaceView mSurfaceView;
	protected String[] mMediaUrls;
	protected int mMediaIndex;
	protected String mMediaTitle;
	private boolean isAutoPlay = false;
	protected int mCurrentState = PlayerState.CURRENT_STATE_NORMAL; //播放状态
	protected int mCurrentScreenState;//屏幕状态
	protected int mCurrentScreenLockState = PlayerState.SCREEN_WINDOW_UNLOCK;//ui锁状态
	protected boolean isMoving;//是否正在滑动进度
	protected long mDuration;//总长度
	protected long mCurrentPosition;//当前的长度位置
	private Timer HIDE_CONTROL_VIEW_TIMER;//屏幕隐藏任务timer


	public ImageView backButton;
	public TextView titleTextView;
	public ImageView startButton;
	public ImageView fullscreenButton;
	public ViewGroup bottomContainer;

	public ProgressBar loadingProgressBar;
	public ImageView thumbImageView;
	public TextView replayTextView;
	public TextView clarity;
	public TextView mRetryBtn;
	private ImageButton mLockButton;
	public LinearLayout mRetryLayout;

	protected int mScreenWidth;
	protected int mScreenHeight;
	private int mInitialWidth;
	private int mInitialHeight;
	protected AudioManager mAudioManager;

	public ViewGroup textureViewContainer;
	public ViewGroup topContainer;
	private HideUiTimerTask mHideUiTimerTask;


	public BasePlayerView(@NonNull Context context) {
		this(context, null);
	}

	public BasePlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BasePlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	protected abstract int getLayoutId();

	/**
	 * finViewById
	 */
	private void initView() {

		titleTextView = findViewById(R.id.title);//标题
		backButton = findViewById(R.id.back);//返回箭头
		startButton = findViewById(R.id.start);
		fullscreenButton = findViewById(R.id.fullscreen);//全屏按钮
		thumbImageView = findViewById(R.id.thumb);//首帧图片
		loadingProgressBar = findViewById(R.id.loading);//加载进度动画
		replayTextView = findViewById(R.id.replay_text);//重播text
		clarity = findViewById(R.id.clarity);//透明？
		mRetryBtn = findViewById(R.id.retry_btn);//点击重试
		mRetryLayout = findViewById(R.id.retry_layout);//视频加载失败
		mLockButton = findViewById(R.id.ib_lock); //锁
		bottomContainer = findViewById(R.id.layout_bottom);//底部容器

		loadingProgressBar.setVisibility(VISIBLE);
		startButton.setVisibility(INVISIBLE);
		thumbImageView.setVisibility(GONE);

		fullscreenButton.setOnClickListener(mOnClickListener);
		mLockButton.setOnClickListener(mOnClickListener);
		thumbImageView.setOnClickListener(mOnClickListener);
		backButton.setOnClickListener(mOnClickListener);
		mRetryBtn.setOnClickListener(mOnClickListener);
		startButton.setOnClickListener(mOnClickListener);
		if (clarity != null) {
			clarity.setOnClickListener(mOnClickListener);
		}

		textureViewContainer = findViewById(R.id.surface_container);
		topContainer = findViewById(R.id.layout_top);
		textureViewContainer.setOnClickListener(mOnClickListener);//视图窗口的点击事件

		mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
		mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_UP:
				Log.d(TAG, "Touch event");
				if (mCurrentState == PlayerState.CURRENT_STATE_PLAYING || mCurrentScreenState == PlayerState.CURRENT_STATE_PAUSE) {
					if (mCurrentScreenState == PlayerState.SCREEN_WINDOW_FULLSCREEN) {
						mLockButton.setVisibility(VISIBLE);
					}
					if (mCurrentScreenLockState != PlayerState.SCREEN_WINDOW_LOCK) {
						changeUiToPlayingShow();
					}
					startHideUiTimer();
				}
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 隐藏UI的延时任务
	 */
	private void startHideUiTimer() {
		cancelHideUiTimer();
		HIDE_CONTROL_VIEW_TIMER = new Timer();
		mHideUiTimerTask = new HideUiTimerTask();
		HIDE_CONTROL_VIEW_TIMER.schedule(mHideUiTimerTask, 2500);
	}

	private void cancelHideUiTimer() {
		if (HIDE_CONTROL_VIEW_TIMER != null) {
			HIDE_CONTROL_VIEW_TIMER.cancel();
		}
		if (mHideUiTimerTask != null) {
			mHideUiTimerTask.cancel();
		}
	}

	/**
	 * 初始化自定义View视图
	 */
	private void init() {

		View.inflate(getContext(), getLayoutId(), this);
		mAliyunPlayer = new AliyunVodPlayer(getContext());
		mSurfaceView = findViewById(R.id.surfaceView);

		initView();

		mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

			public void surfaceCreated(SurfaceHolder holder) {
				//				holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
				holder.setKeepScreenOn(true);
				Log.d(TAG, "AlivcPlayer onSurfaceCreated." + mAliyunPlayer);

				// Important: surfaceView changed from background to front, we need reset surface to mediaplayer.
				// 对于从后台切换到前台,需要重设surface;部分手机锁屏也会做前后台切换的处理
				if (mAliyunPlayer != null) {
					mAliyunPlayer.setSurface(mSurfaceView.getHolder().getSurface());
				}

				Log.d(TAG, "AlivcPlayeron SurfaceCreated over.");
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
				Log.d(TAG, "onSurfaceChanged is valid ? " + holder.getSurface().isValid());
				if (mAliyunPlayer != null) {
					mAliyunPlayer.surfaceChanged();
				}
				if (mCurrentState == PlayerState.CURRENT_STATE_PAUSE) {
					//暂停退到后台再重新打开
					changeUiToPauseShow();
				} else if (mCurrentState == PlayerState.CURRENT_STATE_PLAYING) {
					//播放中退到后台再重新打开
					start();
					changeUiToPlayingClear();
				}

			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				Log.d(TAG, "onSurfaceDestroy.");
				mAliyunPlayer.pause();
			}
		});

		//设置播放器监听
		mAliyunPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
			@Override
			public void onPrepared() {
				//准备完成时触发
				Log.d(TAG, "准备完成时触发");
				mCurrentState = PlayerState.CURRENT_STATE_PREPARING;
				if (!isAutoPlay) {
					start();
				}
				mDuration = mAliyunPlayer.getDuration();

			}
		});

		mAliyunPlayer.setOnFirstFrameStartListener(new IAliyunVodPlayer.OnFirstFrameStartListener() {
			@Override
			public void onFirstFrameStart() {
				//首帧显示触发
				Log.d(TAG, "首帧显示触发");
				mCurrentState = PlayerState.CURRENT_STATE_PLAYING;
				titleTextView.setText(mMediaTitle);
				if (mCurrentScreenLockState != PlayerState.SCREEN_WINDOW_LOCK) {
					changeUiToPlayingShow();
				}
				startHideUiTimer();
			}
		});

		mAliyunPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
			@Override
			public void onError(int arg0, int arg1, String msg) {
				//出错时处理，查看接口文档中的错误码和错误消息
				Log.d(TAG, "出错时处理，查看接口文档中的错误码和错误消息" + "\n" + "arg0 = " + arg1 + "\n" + "arg1 = " + arg1 + "\n" + "msg = " + msg);
				mCurrentState = PlayerState.CURRENT_STATE_ERROR;
			}
		});

		mAliyunPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
			@Override
			public void onCompletion() {
				//播放正常完成时触发
				Log.d(TAG, "播放正常完成时触发");
				mCurrentState = PlayerState.CURRENT_STATE_COMPLETE;
				mMediaIndex = mMediaIndex + 1 <= mMediaUrls.length ? mMediaIndex + 1 : 0;
				play();
			}
		});

		mAliyunPlayer.setOnSeekLiveCompletionListener(new IAliyunVodPlayer.OnSeekLiveCompletionListener() {
			@Override
			public void onSeekLiveCompletion(long l) {
				Log.d(TAG, "seek完成时" + l);
			}
		});
		mAliyunPlayer.setOnSeekCompleteListener(new IAliyunVodPlayer.OnSeekCompleteListener() {
			@Override
			public void onSeekComplete() {
				//seek完成时触发
				Log.d(TAG, "seek完成时触发");
				if (mCurrentState == PlayerState.CURRENT_STATE_PAUSE) {
					start();
				} else {
					mCurrentState = PlayerState.CURRENT_STATE_PLAYING;
					changeUiToPlayingShow();
				}
			}
		});
		mAliyunPlayer.setOnStoppedListner(new IAliyunVodPlayer.OnStoppedListener() {
			@Override
			public void onStopped() {
				//使用stop功能时触发
				Log.d(TAG, "使用stop功能时触发");
				mCurrentState = PlayerState.CURRENT_STATE_STOP;

			}
		});

		mAliyunPlayer.setOnChangeQualityListener(new IAliyunVodPlayer.OnChangeQualityListener() {
			@Override
			public void onChangeQualitySuccess(String finalQuality) {
				//清晰度切换成功时触发
				Log.d(TAG, "清晰度切换成功时触发");
			}

			@Override
			public void onChangeQualityFail(int code, String msg) {
				//清晰度切换失败时触发
				Log.d(TAG, "清晰度切换失败时触发");
			}
		});
		mAliyunPlayer.setOnCircleStartListener(new IAliyunVodPlayer.OnCircleStartListener() {
			@Override
			public void onCircleStart() {
				//循环播放开始
				Log.d(TAG, "循环播放开始");
			}
		});


		initChild();

	}

	public void updateStartImage() {

		if (mCurrentState == PlayerState.CURRENT_STATE_PLAYING) {
			startButton.setVisibility(VISIBLE);
			startButton.setImageResource(R.drawable.jz_click_pause_selector);
			replayTextView.setVisibility(INVISIBLE);
		} else if (mCurrentState == PlayerState.CURRENT_STATE_ERROR) {
			startButton.setVisibility(INVISIBLE);
			replayTextView.setVisibility(INVISIBLE);
		} else if (mCurrentState == PlayerState.CURRENT_STATE_COMPLETE) {
			startButton.setVisibility(VISIBLE);
			startButton.setImageResource(R.drawable.jz_click_replay_selector);
			replayTextView.setVisibility(VISIBLE);
		} else {
			startButton.setImageResource(R.drawable.jz_click_play_selector);
			replayTextView.setVisibility(INVISIBLE);

		}
	}


	/**
	 * 同意权限时重新初始化
	 */
	public void onPermissionSuccess() {
		play();
	}

	/**
	 * 子类可重写，在基类初始化方法最后调用
	 */
	protected void initChild() {
	}


	public void setUp(@Nullable String title,@NonNull String ... url) {

		this.mMediaUrls = url;
		this.mMediaTitle = title;
		PermissionUtils permission = new PermissionUtils(getContext(), true);
		boolean hasPermission = permission.checkPermissions(PermissionUtils.REQUEST_STORAGE_PERMISSION, PermissionUtils.STORAGE_PERMISSION);
		if (!hasPermission) {
			//在没有权限的时候将申请权限并不做初始化，适配同意权限后播放需要在载体activity中的权限回调方法中调用onPermissionSuccess（）
			return;
		}
		play();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mInitialWidth == 0) {

			mInitialHeight = getMeasuredHeight();
			mInitialWidth = getMeasuredWidth();
		}
	}

	/**
	 * 监听器
	 */
	private OnClickNoMutiListener mOnClickListener = new OnClickNoMutiListener() {
		@Override
		public void onClick(View v) {
			Log.w(TAG, "screenState   :" + mCurrentScreenState + "  ] ");
			if (isMultipleClick()) {
				return;
			}
			int vId = v.getId();
			if (vId == R.id.start) {
				if (mCurrentState == PlayerState.CURRENT_STATE_NORMAL) {
					//					if (!JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("file") && !JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("/") && !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
					//						showWifiDialog();
					//						return;
					//					}
					start();
				} else if (mCurrentState == PlayerState.CURRENT_STATE_PLAYING) {
					pause();
				} else if (mCurrentState == PlayerState.CURRENT_STATE_PAUSE) {
					startHideUiTimer();
					start();
					//					ssad
				} else if (mCurrentState == PlayerState.CURRENT_STATE_COMPLETE) {
					play();
				}
			} else if (vId == R.id.fullscreen) {
				Log.i(TAG, "onClick fullscreen [" + this.hashCode() + "] ");
				if (mCurrentState == PlayerState.CURRENT_STATE_COMPLETE)
					return;
				if (mCurrentScreenState == PlayerState.SCREEN_WINDOW_FULLSCREEN) {
					startWindowVertical();
				} else {
					startWindowHorizontal();
				}
			} else if (vId == R.id.back) {
				if (mCurrentScreenState == PlayerState.SCREEN_WINDOW_FULLSCREEN) {
					//回到竖屏
					startWindowVertical();
				} else if (mCurrentScreenState == PlayerState.SCREEN_WINDOW_NORMAL && PlayerUtils.getAppCompActivity(getContext()) != null) {
					((AppCompatActivity) getContext()).finish();
				}

			} else if (vId == R.id.ib_lock) {
				changeLockUi();
			}
		}
	};

	//设置横屏
	private void startWindowHorizontal() {
		AppCompatActivity appCompActivity = PlayerUtils.getAppCompActivity(getContext());
		if (appCompActivity != null) {
			appCompActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	//设置竖屏
	private void startWindowVertical() {
		AppCompatActivity appCompActivity = PlayerUtils.getAppCompActivity(getContext());
		if (appCompActivity != null) {
			appCompActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

	}

	/**
	 * activity监听屏幕方向切换是调用此方法
	 *
	 * @param newConfig {@link Configuration}
	 */
	public void onActivityConfigurationChanged(Configuration newConfig) {
		//切换为竖屏
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.i(TAG, " 切换为竖屏");
			mCurrentScreenState = PlayerState.SCREEN_WINDOW_NORMAL;
			fullscreenButton.setImageResource(R.drawable.jz_enlarge);
		}
		//切换为横屏
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.i(TAG, "切换为横屏");
			mCurrentScreenState = PlayerState.SCREEN_WINDOW_FULLSCREEN;
			fullscreenButton.setImageResource(R.drawable.jz_shrink);
		}
		onScreenOrientationChange();
	}

	/**
	 * 处理屏幕方向切换时的ui
	 */
	private void onScreenOrientationChange() {
		AppCompatActivity appCompActivity = PlayerUtils.getAppCompActivity(getContext());
		ViewGroup.LayoutParams layoutParams = getLayoutParams();
		if (mCurrentScreenState == PlayerState.SCREEN_WINDOW_FULLSCREEN) {

			if (appCompActivity != null) {
				appCompActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
			layoutParams.width = mScreenHeight;
			layoutParams.height = mScreenWidth;
			mLockButton.setImageResource(R.drawable.icon_unlock);
			mLockButton.setVisibility(VISIBLE);
		} else {
			if (appCompActivity != null) {
				appCompActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
			layoutParams.width = mInitialWidth;
			layoutParams.height = mInitialHeight;
			mLockButton.setVisibility(INVISIBLE);
		}
		setLayoutParams(layoutParams);

	}

	protected abstract void setMediaSource();

	/**
	 * 播放
	 */
	private void play() {
		if (mAliyunPlayer != null) {
			mAliyunPlayer.stop();
		}

		setMediaSource();
		mAliyunPlayer.start();
	}

	/**
	 * 开始
	 */
	protected void start() {
		if (mAliyunPlayer != null) {
			mAliyunPlayer.start();
			if (mCurrentState == PlayerState.CURRENT_STATE_PAUSE) {
				mCurrentState = PlayerState.CURRENT_STATE_PLAYING;
				changeUiToPlayingShow();
			}
		}
	}

	/**
	 * 暂停
	 */
	protected void pause() {
		if (mAliyunPlayer != null) {
			mAliyunPlayer.pause();
			mCurrentState = PlayerState.CURRENT_STATE_PAUSE;
			changeUiToPauseShow();
		}
	}

	/**
	 * 停止
	 */
	protected void stop() {
		if (mAliyunPlayer != null) {
			mAliyunPlayer.stop();
			mCurrentState = PlayerState.CURRENT_STATE_STOP;
		}
	}

	@Override
	public void changeLockUi() {

		if (mCurrentScreenLockState == PlayerState.SCREEN_WINDOW_UNLOCK) {
			mLockButton.setImageResource(R.drawable.icon_lock);
			mCurrentScreenLockState = PlayerState.SCREEN_WINDOW_LOCK;
			hideUiControls();
		} else if (mCurrentScreenLockState == PlayerState.SCREEN_WINDOW_LOCK) {
			mCurrentScreenLockState = PlayerState.SCREEN_WINDOW_UNLOCK;
			mLockButton.setImageResource(R.drawable.icon_unlock);
			if (mCurrentState == PlayerState.CURRENT_STATE_PLAYING) {
				changeUiToPlayingShow();
			} else if (mCurrentState == PlayerState.CURRENT_STATE_PAUSE) {
				changeUiToPauseShow();
			} else if (mCurrentState == PlayerState.CURRENT_STATE_COMPLETE) {
				changeUiToComplete();
			} else if (mCurrentState == PlayerState.CURRENT_STATE_ERROR) {
				changeUiToError();
			}
		}
	}

	private void hideUi() {
		mLockButton.setVisibility(INVISIBLE);
		hideUiControls();
	}

	public class HideUiTimerTask extends TimerTask {

		@Override
		public void run() {

			AppCompatActivity appCompActivity = PlayerUtils.getAppCompActivity(getContext());
			if (appCompActivity != null && !appCompActivity.isDestroyed() && mCurrentState == PlayerState.CURRENT_STATE_PLAYING && !isMoving) {
				post(new Runnable() {
					@Override
					public void run() {
						hideUi();
					}
				});
			}
		}
	}

}
