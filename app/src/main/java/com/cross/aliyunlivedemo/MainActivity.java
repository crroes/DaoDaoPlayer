package com.cross.aliyunlivedemo;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cross.fxwiz_widget_player.utils.PermissionUtils;
import com.cross.fxwiz_widget_player.view.BasePlayerView;

public class MainActivity extends AppCompatActivity {


	private BasePlayerView mPlayerView;

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
		mPlayerView = findViewById(R.id.player_view);

		String url3 = "http://117.131.17.50/depository_yf/asset/zhengshi/1002/068/349/1002068349/media/1002068349_1003692265_91.mp4";
		String url4 = "http://mgcdn.vod.mgspcloud.migucloud.com/vi1/564.1Uh7pg7SJ2TXwnmHsGjiGs.32.TpwNk5.mp4";
		String url5 = "http://mgcdn.vod.mgspcloud.migucloud.com/vi1/564.1FKb3V6h1KrgAaUN17OiH.32.ECl4Mx.mp4";
		String url6 = "https://135zyv3.xw0371.com/20180513/dChjVePU/index.m3u8";
		String url2 = "https://cn-jsnt-dx-v-11.acgvideo.com/vg1/upgcxcode/44/33/24183344/preview_24183344-1-16.mp4?expires=1526565300&platform=html5&ssig=91CAjjj-ouK75bbOhIesqg&oi=2085243982&stime=0&etime=360&nfa=ZGlYLwTu0dW3o1gJGPmYTQ==&dynamic=1&hfa=2026786123&hfb=M2Y2ZWYwZjM2YmRiYmY5MDljYTBiOWE2ZmEwYjJmYTM=&trid=931ca46059e44626a0ad92a7ca7dd934";
		String[] mediaUrls = {url2,url3,url4,url6,url4,url5};//视频
//		String[] mediaUrls = {"https://alhls.quanmin.tv/live/2138956658.m3u8"};//直播
		mPlayerView.setUp("摄影之路",mediaUrls);
	}

	/**
	 * 添加处理横竖屏
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mPlayerView.onActivityConfigurationChanged(newConfig);
	}

	/**
	 * 5.0 权限适配
	 */
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

	private String getShootClass() {
		return "摄影术的诞生\n" + "\n" + "尼埃普斯用暗箱拍摄的照片，为现存最早的照片，拍摄年份为1826年，花了8小时曝光。这张照片一度被世人遗忘，由1898年最后一次在伦敦公开展出，至1952年被历史学家寻回，相隔超过半世纪。\n" + "\n" + "达盖尔在1837年拍摄的静物照片，相信是最早的银版摄影法照片。达盖尔利用窗口光拍摄，令照片更富立体感。\n" + "\n" + "塔尔博特声称在1835年拍摄的照片，拍摄地点为雷卡克修道院。\n" + "现存最早的照片由法国人尼埃普斯[注 2]在1826年[注 3]拍摄[1][2][3]。尼埃普斯将犹太沥青[2]涂在铅锡合金[1][3]版上，曝光8小时获得此影像[1]。他称这种技术为日光蚀刻法[注 4]，但他未及完善这一技术便去世[2]。他的合伙人[2]法国画家达盖尔继续研究，发明了达盖尔摄影法[1][2][3]，又称银版摄影法，在1839年由法国政府买下专利权[1][2]，并于同年8月19日[2]宣告摄影术的诞生。\n" + "\n" + "银版摄影法的优点是照片逼真，而且是正像[1][2]。缺点是从不同的角度观看，照片会由正像变成负像[1]。此外，由于影像是在一层很薄的银上形成，因此容易受损，用手指轻擦也可能会令照片受损[2]。后来法国物理学家斐索想出为照片镀金，并在1840年发明“镀金法”这种保护措施，方法是把氯化金加进硫代硫酸钠的水溶液，再洒在照片上，然后用酒精灯加热，形成黄金保护层[2]。一众摄影师则想出更直接的方法，把照片密封在玻璃镜框中[2][4]。还有，由于使用水银蒸汽显影[1][2][3]，有可能导致摄影师水银中毒。其他缺点包括拍出来的影像是左右颠倒[1][2][3]、复制照片困难，还有拍摄成本高昂[2]。\n" + "\n" + "不过，由于技术已被公开[1][2][3]，银版摄影法在世界各地广为流传，更一度成为主流的摄影方法，至1850年代始被湿版火棉胶摄影法等新方法取代。\n" + "\n" + "作为另一个重要的创始人，英国人塔尔博特[注 5][1][2][3] 于1841年[2][3]发表了卡罗法。据悉，塔尔博在1835年[1][2][3]，用涂上感光物料的高级书写纸张[2][3]，拍摄世上第一张负像照片[1][2][3]，即后来所谓的负片。通过接触式印相[1][2][3]，可获得正像照片，由此开创出由负转正的摄影工艺[2][3]。\n" + "\n" + "卡罗法的拍摄成本较低[2]，又可重复印制相同的照片[1][2][3]。但由于使用纸作为负片，影像也较为模糊[1][2]。相对于达盖尔的大成功，塔尔博特的发明并没有引起太大的注意[2][3]。不过，作为第一种由负转正的摄影工艺，塔尔博特仍被视为摄影的始创者之一。\n" + "\n" + "塔尔博特的《自然的画笔》（The Pencil of Nature），于1844年至1866年之间出版，被视为史上首本以照片作为插图的书籍[1]。但有反对者指，英国植物学家安娜·阿特金斯的《不列颠的海藻照片：蓝晒法印象》（Photographs of British Algae: Cyanotype Impressions）才是[3]。\n" + "\n" + "其他对摄影作出重要贡献的人士，包括英国的约翰·赫歇尔爵士，他创造了photography这个词[3]，又发现硫代硫酸钠可以把卤化银溶在水中[2][3]，解决了定影这个问题[2]。其他重要贡献包括蓝晒法这种古典摄影工艺[3]，它成了工业蓝图的前身。\n" + "\n" + "出身显赫的汤玛斯·威治伍德，在1790年代开始研究硝酸银对光线的反应，并尝试以暗箱拍摄照片，不过以失败告终[2]。威治伍德又把树叶放在涂了硝酸银的白色皮革上，结果皮革未被盖住的部分变成黑色[2]。威治伍德把一些物件放在经处理的纸上，“复制”出这些物件的模样，但无法固定影像，时间一长，影像就消失了[2]。一般认为，威治伍德的照片没有保存下来。\n" + "\n" + "但近年有学者指，其中一张被认为是塔尔博特作品的树叶照片，可能是威治伍德的作品。假如属实，有关照片将会是世上最古老的照片，有超过200年的历史。";
	}
}
