package com.cross.daodao_gof_pattern.adapter;

import com.cross.daodao_gof_pattern.adapter.delegate.Banner;
import com.cross.daodao_gof_pattern.adapter.extend.Print;
import com.cross.daodao_gof_pattern.adapter.extend.PrintBanner;

/**
 * Created by cross on 2018/7/31.
 * <p>描述:适配器（包装）模式核心（4/4）请求者
 */

public class ExecuteAdapter {

	public static void execute(Module module) {
		String str = "适配器模式-" + module.value;
		switch (module) {
			case EXTEND:
				Print printExtend = new PrintBanner(str);
				printExtend.printWeak();
				printExtend.printStrong();
				break;
			case DELEGATE:
				com.cross.daodao_gof_pattern.adapter.delegate.Print printDelegate = new com.cross.daodao_gof_pattern.adapter.delegate.PrintBanner(new Banner(str));
				printDelegate.printWeak();
				printDelegate.printStrong();
				break;
			default:
				System.out.println("undefined");
				break;

		}
	}

	/**
	 * 模式
	 */
	public enum Module {
		EXTEND("继承"), DELEGATE("委托");

		private String value;

		Module(String value) {
			this.value = value;
		}
	}
}
