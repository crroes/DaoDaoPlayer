package com.cross.daodao_gof_pattern.adapter.extend;


/**
 * Created by cross on 2018/7/31.
 * <p>描述:适配器（包装）模式核心（3/4）适配类adapter
 */

public class PrintBanner extends Banner implements Print{

	public PrintBanner(String string) {
		super(string);
	}

	@Override
	public void printWeak() {
		showWithParen();
		System.out.println("括弧");
	}

	@Override
	public void printStrong() {
		showWithAster();
		System.out.println("星号");
	}
}
