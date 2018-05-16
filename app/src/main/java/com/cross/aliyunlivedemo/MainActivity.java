package com.cross.aliyunlivedemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
		String url = "https://cn-zjwz3-dx-v-11.acgvideo.com/vg2/upgcxcode/08/48/13784808/preview_13784808-1-16.mp4?expires=1526473200&platform=html5&ssig=V6Dy94gM-77Gc3xENUqrBQ&oi=2085243982&stime=0&etime=360&nfa=ZGlYLwTu0dW3o1gJGPmYTQ==&dynamic=1&hfa=2026842662&hfb=M2Y2ZWYwZjM2YmRiYmY5MDljYTBiOWE2ZmEwYjJmYTM=&trid=b052af499edb46928fb1512cfcd39a06";//直播
		mPlayerView.setUp(url,"摄影之路");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mPlayerView.onActivityConfigurationChanged(newConfig);
	}
}
