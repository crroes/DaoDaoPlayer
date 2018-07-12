package com.cross.daodaoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cross.fxwiz_widget_player.utils.MediaBean;
import com.cross.fxwiz_widget_player.view.BasePlayerFragment;
import com.cross.fxwiz_widget_player.view.MyVideoPlayerFragment;

public class MainActivity extends AppCompatActivity {


	private BasePlayerFragment mPlayerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		TextView textView = (TextView) findViewById(R.id.tv_class);
		textView.setText(getShootClass());

	}


	private void initView() {

		//		FrameLayout frameLayout = findViewById(R.id.fl_group);
		//		mPlayerView = new MyVideoPlayerView(this);
		//		mPlayerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		//		frameLayout.addView(mPlayerView);
		mPlayerView = new MyVideoPlayerFragment();

		ViewGroup mContainerView = findViewById(R.id.player_view);
		getSupportFragmentManager().beginTransaction().replace(R.id.player_view,mPlayerView).commit();

		String url3 = "http://117.131.17.50/depository_yf/asset/zhengshi/1002/068/349/1002068349/media/1002068349_1003692265_91.mp4";
		String url4 = "http://mgcdn.vod.mgspcloud.migucloud.com/vi1/564.1Uh7pg7SJ2TXwnmHsGjiGs.32.TpwNk5.mp4";
		String url5 = "http://mgcdn.vod.mgspcloud.migucloud.com/vi1/564.1FKb3V6h1KrgAaUN17OiH.32.ECl4Mx.mp4";
		String url6 = "https://135zyv3.xw0371.com/20180513/dChjVePU/index.m3u8";
		//		String[] mediaUrls = {url4,url3,url6,url5};//视频
				String mediaUrls = url3;//视频
//		String[] mediaUrls = {"http://alhls.cdn.zhanqi.tv/zqlive/88682_pyqjm.m3u8"};//直播
		MediaBean mediaBean = new MediaBean("摄影之路", url5, MediaBean.MediaType.VIDEO);

		mPlayerView.setUp(mediaBean,mContainerView);
	}


	/**
	 * 全屏的返回处理
	 */
	@Override
	public void onBackPressed() {
		if (mPlayerView == null || !mPlayerView.isInterceptBackPressed()) {
			super.onBackPressed();
		}
	}


	private String getShootClass() {
		return "曝光过程\n" + "\n" + "商业产品的广告摄影\n" + "摄影的本质是捕捉光的艺术，所以曝光准确与否就成为了技术层面最核心的问题。\n" + "\n" + "曝光是相机把光（通常是外界影像通过镜头内部的透镜组聚焦后）记录进感光媒体里的过程。光有强有弱，所以通过准确的光圈，快门和感光度的组合而成像。而准确曝光存在不同的光圈、快门和感光度的组合，那正是摄影的艺术所在。大光圈减低景深突出主题，而小光圈增加景深。快的快门凝固精彩的一刻，慢的快门反之，通常慢快门增加一张相的动感，如流水，晚上的车流等等。当用慢快门时，常用三角架固定摄影设备以保图像的清楚。\n" + "\n" + "后期制作\n" + "照相进行完成后，介质所存留的影像信息必须通过转换而再度为人眼所读取。具体方法依赖于感光手段和介质特性。对于胶片照相机，会有定影，显影，放大等化学过程。对于数码照相机，则需要处理器对数据进行计算，再通过电子设备输出。\n" + "\n" + "数码后期 随着数码相机的广泛使用，使用电脑图片处理软件处理拍摄好的数码文件，并进行二次创作，得到作者想要的图像，数码技术的发展，给摄影带来了质的飞跃。\n" + "\n" + "所用设备\n" + "一般来讲，人们使用可见光照相，最常用到的是照相机。因场景和用途的不同，照相机有着非常多的分类。综合来讲，照相机都要有几个基本的部分以保证曝光过程，这包括：\n" + "\n" + "感光介质\n" + "成像透镜\n" + "曝光时间控制机构\n" + "胶卷暗盒\n" + "有时，人们还会采用小孔成像的实验设备进行照相，这只需要一个有小孔的暗盒、感光介质和控制光照时间方法。\n" + "\n" + "非可见光照相\n" + "由于有些介质可以感受可见光之外电磁波的照射，因而相应的照相技术得以开发。 最为普及的是红外线照相，甚至很多民用照相机不需改装就可进行红外线照相。在普通相机内装入红外感光胶卷，即可进行照相。但是，相应的辐射强度计算还不能足够精确的给出暴光所需时间。类似情况也出现在恒星摄影中。不同波长的电磁波辐射都可以由介质保存，再显现为人眼可读的图片。普通照相是为了记录人们所见的场景，而非可见光照相则显示了人眼所不可能识别的镜像。";
	}
}
