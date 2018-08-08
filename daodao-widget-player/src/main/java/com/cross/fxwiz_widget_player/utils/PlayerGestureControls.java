package com.cross.fxwiz_widget_player.utils;

/**
 * Created by cross on 2018/6/21.
 * <p>描述:滑动手势管理 播放进度、亮度、音量
 */

public interface PlayerGestureControls {

	/**
	 * 手势滑动进度
	 * @param deltaX 滑动的值
	 */
	void doGesturePosition(float deltaX);

	/**
	 * 手势滑动音量
	 * @param deltaY 滑动的距离
	 */
	void doGestureVolume(float deltaY);

	/**
	 * 手势滑动亮度
	 * @param deltaY 滑动的距离
	 */
	void doGestureBrightness(float deltaY);
}
