package com.cross.aliyunlivedemo;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.cross.fxwiz_widget_player.utils.PermissionUtils;
import com.cross.fxwiz_widget_player.view.BasePlayerView;

public class MainActivity extends AppCompatActivity {


	private BasePlayerView mPlayerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {

//		FrameLayout frameLayout = findViewById(R.id.fl_group);
//		mPlayerView = new MyVideoPlayerView(this);
//		mPlayerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//		frameLayout.addView(mPlayerView);
		mPlayerView = findViewById(R.id.player_view);

		String url = "https://vod.mudu.tv/media-join-slice/wpzkwc6qc82dcgkjbub6xt80q.m3u8";//回放
		String url3 = "http://117.131.17.50/depository_yf/asset/zhengshi/1002/068/349/1002068349/media/1002068349_1003692265_91.mp4";
		String url4 = "http://mgcdn.vod.mgspcloud.migucloud.com/vi1/564.1Uh7pg7SJ2TXwnmHsGjiGs.32.TpwNk5.mp4";
		String url5 = "http://mgcdn.vod.mgspcloud.migucloud.com/vi1/564.1FKb3V6h1KrgAaUN17OiH.32.ECl4Mx.mp4";
		String url6 = "https://135zyv3.xw0371.com/20180513/dChjVePU/index.m3u8";
//		String[] mediaUrls = {url3,url4,url6,url,url4,url5};//视频
		String[] mediaUrls = {"https://alhls.quanmin.tv/live/2138956658.m3u8"};//直播
		mPlayerView.setUp(mediaUrls,"摄影之路");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mPlayerView.onActivityConfigurationChanged(newConfig);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PermissionUtils.REQUEST_STORAGE_PERMISSION){
			for (int i = 0; i < permissions.length; i++) {
				if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
					return;
				}
			}
			mPlayerView.onPermissionSuccess();
		}
	}
}
