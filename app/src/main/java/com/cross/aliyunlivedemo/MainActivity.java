package com.cross.aliyunlivedemo;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.cross.fxwiz_widget_player.utils.PermissionUtils;
import com.cross.fxwiz_widget_player.view.MyVideoPlayerView;

public class MainActivity extends AppCompatActivity {


	private MyVideoPlayerView mPlayerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mPlayerView = findViewById(R.id.player_view);
//		String url = "https://vod.mudu.tv/media-join-slice/wpzkwc6qc82dcgkjbub6xt80q.m3u8";//回放
		String url = "http://vali-dns.cp31.ott.cibntv.net/67713D46D974771C4EB3644ED/03000801005AAEBEBCD4150855D03EF13B890D-C9D8-F015-2F52-618A85F028F8.mp4?ccode=0501&duration=217&expire=18000&psid=06418742b21fddf7f8a82a23e98907b2&sp=&ups_client_netip=7c4a4c4e&ups_ts=1526527858&ups_userid=&utid=HtVpE6nljl8CAXxKTE7aky8G&vid=XMjUwOTY3MjgwMA%3D%3D&vkey=Bba0e0d315ae618888feaacfa01d48933&s=949e6001aa4d11e6bdbb";//直播
		String url3 = "http://117.131.17.50/depository_yf/asset/zhengshi/1002/068/349/1002068349/media/1002068349_1003692265_91.mp4";
		String url4 = "http://mgcdn.vod.mgspcloud.migucloud.com/vi1/564.1Uh7pg7SJ2TXwnmHsGjiGs.32.TpwNk5.mp4";
		String url5 = "http://mgcdn.vod.mgspcloud.migucloud.com/vi1/564.1FKb3V6h1KrgAaUN17OiH.32.ECl4Mx.mp4";
		String url6 = "https://135zyv3.xw0371.com/20180513/dChjVePU/index.m3u8";
		String[] strings = {url4,url6,url3,url,url4,url5};
		mPlayerView.setUp(strings,"摄影之路");
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
