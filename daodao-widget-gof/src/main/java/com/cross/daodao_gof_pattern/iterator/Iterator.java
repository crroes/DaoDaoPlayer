package com.cross.daodao_gof_pattern.iterator;

/**
 * Created by cross on 2018/7/30.
 * <p>描述:迭代器模式核心（1/4）Iterator接口
 */

public interface Iterator<T> {
	boolean hasNext();
	T next();
}
