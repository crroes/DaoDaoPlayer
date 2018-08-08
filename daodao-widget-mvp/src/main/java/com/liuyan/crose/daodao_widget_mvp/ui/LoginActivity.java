package com.liuyan.crose.daodao_widget_mvp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.liuyan.crose.daodao_widget_mvp.R;
import com.liuyan.crose.daodao_widget_mvp.presenter.PLoginActivity;
import com.liuyan.crose.daodao_widget_mvp.presenter.PLoginActivityImpl;

public class LoginActivity extends AppCompatActivity {

	PLoginActivity mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mPresenter = new PLoginActivityImpl(this);

	}

	private void onLogin(String id,String password) {
		mPresenter.login(id,password);
	}

	public void onLoginFinish(boolean isSuccess , String msg){
		if (isSuccess){
			//登录成功
		}else {
			//登录失败
		}
	}

}
