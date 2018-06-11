package com.cross.fxwiz_widget_player.utils;

import android.support.annotation.NonNull;

/**
 * Created by cross on 2018/6/2.
 * <p>描述:视频的bean
 */

public class MediaBean {

	private String title; //视频的标题
	private String videoUrl; //视频的url
	private long currentPosition; //当前播放的的时间 单位ms
	private long duration; //总时间
	private boolean isShareIconHide;//是否隐藏分享图标（服务器配置）
	private MediaType type;//视频类型
	private OnClickActionListener mOnClickActionListener;//播放器点击事件监听器

	public MediaBean(String title, @NonNull String videoUrl, MediaType type) {
		this.title = title;
		this.videoUrl = videoUrl;
		this.type = type;
	}

	public OnClickActionListener getOnClickActionListener() {
		return mOnClickActionListener;
	}

	public void setOnClickActionListener(OnClickActionListener onClickActionListener) {
		mOnClickActionListener = onClickActionListener;
	}

	public boolean isShareIconHide() {
		return isShareIconHide;
	}

	public void setShareIconHide(boolean shareIconHide) {
		isShareIconHide = shareIconHide;
	}

	public long getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(long currentPosition) {
		this.currentPosition = currentPosition;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public MediaType getType() {
		return type;
	}

	public void setType(MediaType type) {
		this.type = type;
	}

	/**
	 * 直播or视频
	 */
	public enum MediaType{
		VIDEO, LIVE
	}

	/**
	 * 播放器点击动作监听器
	 */
	public interface OnClickActionListener{

		void onClickShareIcon();//点击分享

	}
}
