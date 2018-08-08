package com.cross.fxwiz_widget_player.utils;

/**
 * Created by cross on 2018/6/5.
 * <p>描述: 播放器的Context生命周期接口
 */

public interface IPlayerContext {

	void onContextResume();
	void onContextPause();
	void onContextStop();
	void onContextDestroy();

}
