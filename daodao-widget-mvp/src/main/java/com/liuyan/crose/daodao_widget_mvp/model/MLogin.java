package com.liuyan.crose.daodao_widget_mvp.model;

/**
 * Created by cross on 2018/8/8.
 * <p>描述:
 */
public interface MLogin {

	/**
	 * 登录
	 * @param id 用户id
	 * @param password 密码
	 */
	void onLogin(String id, String password);
}
