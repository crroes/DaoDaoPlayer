package com.cross.daodao_gof_pattern.adapter.extend;

/**
 * Created by cross on 2018/7/31.
 * <p>描述:适配器（包装）模式核心（2/4）被适配类adapted
 */

public class Banner {
	private String mString;

	public Banner(String string) {
		mString = string;
	}

	public void showWithParen(){
		System.out.println("(" + mString + ")");
	}

	public void showWithAster(){
		System.out.println("*" + mString + "*");
	}
}
