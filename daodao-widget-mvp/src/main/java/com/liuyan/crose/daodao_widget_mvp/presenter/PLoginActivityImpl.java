package com.liuyan.crose.daodao_widget_mvp.presenter;

import android.app.Activity;

import com.cross.daodaoplayer.LoginActivity;
import com.cross.daodaoplayer.model.MLogin;
import com.cross.daodaoplayer.model.MLoginImpl;

/**
 * Created by cross on 2018/8/8.
 * <p>描述:
 */
public class PLoginActivityImpl extends BasePresenter implements PLoginActivity {

	private MLogin mMLogin;

	public PLoginActivityImpl(Activity loginActivity) {
		super(loginActivity);
		mMLogin = new MLoginImpl(this);
	}


	@Override
	public void login(String id, String password) {
		mMLogin.onLogin(id,password);
	}

	//暴露给model层的方法
	public void onLoginFinish(boolean isSuccess,String msg){

		if (!isDeath()){
			runInMainThread(isSuccess,msg);
		}else {
			//log
		}
	}

	private void runInMainThread(boolean isSuccess, String msg) {
		((LoginActivity) getView()).onLoginFinish(isSuccess,msg);
	}
}
