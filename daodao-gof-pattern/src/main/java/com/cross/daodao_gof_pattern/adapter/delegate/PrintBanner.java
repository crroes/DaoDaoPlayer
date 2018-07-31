package com.cross.daodao_gof_pattern.adapter.delegate;

/**
 * Created by cross on 2018/7/31.
 * <p>描述:适配器（包装）模式核心（3/4）适配类adapter
 */

public class PrintBanner extends Print{

	private Banner mBanner;

	public PrintBanner(Banner banner) {
		mBanner = banner;
	}

	@Override
	public void printWeak() {
		mBanner.showWithParen();
		System.out.println("括弧");
	}

	@Override
	public void printStrong() {
		mBanner.showWithAster();
		System.out.println("星号");
	}
}
