package com.cross.fxwiz_widget_player.utils;

/**
 * Created by cross on 2018/5/14.
 * <p>描述:播放器的状态
 */

public class PlayerState {

	//状态
	public static final int CURRENT_STATE_ERROR = 0; //异常
	public static final int CURRENT_STATE_NORMAL = 1; //正常
	public static final int CURRENT_STATE_PREPARING = 2; //准备中
	public static final int CURRENT_STATE_PLAYING = 3; //播放中
	public static final int CURRENT_STATE_PAUSE = 4; //暂停
	public static final int CURRENT_STATE_STOP = 5; //停止
	public static final int CURRENT_STATE_COMPLETE = 6; //完成

	//屏幕
	public static final int SCREEN_WINDOW_NORMAL = 0;
	public static final int SCREEN_WINDOW_FULLSCREEN = 1;//全屏

	//锁的状态（锁定状态下所有控制view隐藏）
	public static final int SCREEN_WINDOW_UNLOCK = 1001;//未锁
	public static final int SCREEN_WINDOW_LOCK = 1002;//加锁

}
