package com.liuyan.crose.daodao_widget_mvp.model;

import com.cross.daodaoplayer.presenter.PLoginActivityImpl;

/**
 * Created by cross on 2018/8/8.
 * <p>描述:
 */
public class MLoginImpl implements MLogin {
	private PLoginActivityImpl mPLoginActivity;

	public MLoginImpl(PLoginActivityImpl pLoginActivity) {
		mPLoginActivity = pLoginActivity;
	}

	/**
	 * 登录方法
	 * @param id 用户id
	 * @param password 密码
	 */
	@Override
	public void onLogin(String id, String password) {
		httpsRunInSubThread(id,password);
	}

	/**
	 * 异步网络请求调用服务器登录接口
	 * @param id 用户id
	 * @param password 密码
	 */
	private void httpsRunInSubThread(String id, String password) {
		//调用登录
		//登录结果回调
		onLoginFinish(true,"登录成功");
	}

	//登录回调统一通过此方法回调P层
	private void onLoginFinish(boolean b, String msg) {
		mPLoginActivity.onLoginFinish(b,msg);
	}
}
