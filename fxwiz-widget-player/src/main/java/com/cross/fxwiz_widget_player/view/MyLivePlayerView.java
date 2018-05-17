package com.cross.fxwiz_widget_player.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.aliyun.vodplayer.media.AliyunLiveTimeShift;
import com.cross.fxwiz_widget_player.R;

/**
 * Created by cross on 2018/5/14.
 * <p>描述:直播播放器自定义view
 */

public class MyLivePlayerView extends BasePlayerView {

	private AliyunLiveTimeShift mAliyunLiveTimeShift;

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
	protected void initChild() {
		mAliyunLiveTimeShift = new AliyunLiveTimeShift();
	}

	@Override
	protected void setMediaSource() {

		long currentSeconds = System.currentTimeMillis() / 1000;
		//有鉴权地址
		mAliyunLiveTimeShift.setUrl("http://pull-videocall.aliyuncs.com/timeline/test.m3u8");
		mAliyunLiveTimeShift.setTimeLineUrl("http://pull-videocall.aliyuncs.com/openapi/timeline/query?lhs_start=1&app=timeline&stream=test&format=ts&lhs_start_unix_s_0=" + (currentSeconds - 5 * 60) + "&lhs_end_unix_s_0=" + (currentSeconds + 5 * 60));

		//无鉴权地址
		//        mAliyunLiveTimeShift.setUrl("http://cctv1.cntv.cdnpe.com/timeline/cctv5td.m3u8");
		//        mAliyunLiveTimeShift.setTimeLineUrl("http://cctv1.cntv.cdnpe.com/openapi/timeline/query?app=timeline&stream=cctv5td&format=ts&lhs_start_unix_s_0="
		//                + ( currentSeconds -5*60 ) + "&lhs_end_unix_s_0="+ (currentSeconds + 5*60));
		mAliyunPlayer.prepareAsync(mAliyunLiveTimeShift);
	}

	@Override
	public void changeUiToPreparing() {

	}

	@Override
	public void changeUiToPlayingShow() {

	}

	@Override
	public void changeUiToPlayingClear() {

	}

	@Override
	public void changeUiToPauseShow() {

	}

	@Override
	public void changeUiToComplete() {

	}

	@Override
	public void changeUiToError() {

	}


	@Override
	public void hideUiControls() {
	}
}
