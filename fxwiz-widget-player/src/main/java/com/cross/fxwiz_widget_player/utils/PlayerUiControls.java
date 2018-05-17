package com.cross.fxwiz_widget_player.utils;

/**
 * Created by cross on 2018/5/15.
 * <p>描述:ui切换接口
 */

public interface PlayerUiControls {
	void changeUiToPreparing();//准备中（加载中）
	void changeUiToPlayingShow();//播放中带控制
	void changeUiToPlayingClear();//播放中不带控制UI
	void changeUiToPauseShow();//暂停
	void changeUiToComplete();//播放完成
	void changeUiToError();//播放错误
	void changeLockUi();//锁屏
	void hideUiControls();//隐藏控制ui
}
