package com.cross.daodao_gof_pattern.iterator;

/**
 * Created by cross on 2018/7/30.
 * <p>描述:迭代器模式核心（2/4）集合接口
 */

public interface Aggregate<T> {
	Iterator iterator();
}
