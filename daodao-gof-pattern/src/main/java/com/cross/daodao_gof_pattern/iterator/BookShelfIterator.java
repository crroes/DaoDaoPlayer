package com.cross.daodao_gof_pattern.iterator;

/**
 * Created by cross on 2018/7/30.
 * <p>描述:迭代器模式核心（4/4）具体的迭代器
 */

public class BookShelfIterator implements Iterator<Book> {

	private int index;
	private Aggregate<Book> mAggregate;

	public BookShelfIterator(BookShelf bookShelf) {
		mAggregate = bookShelf;
	}

	@Override
	public boolean hasNext() {
		return index < mAggregate.getLength();
	}

	@Override
	public Book next() {

		Book bookAt = mAggregate.index(index);
		index++;
		return bookAt;
	}
}
