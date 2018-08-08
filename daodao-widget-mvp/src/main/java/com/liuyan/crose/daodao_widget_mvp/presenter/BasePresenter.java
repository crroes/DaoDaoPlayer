package com.liuyan.crose.daodao_widget_mvp.presenter;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by cross on 2018/8/8.
 * <p>描述:
 */
public class BasePresenter {

	private WeakReference<Activity> mView;

	protected BasePresenter(Activity activity) {
		mView = new WeakReference<>(activity);
	}

	/**
	 * 绑定的View是否已销毁，与WeakReference一起用于避免异常和内存泄漏
	 * @return true 存活 otherwise 已销毁
	 */
	protected boolean isDeath(){
		return mView.get() == null || mView.get().isDestroyed();
	}

	protected Activity getView(){
		return mView.get();
	}
}
