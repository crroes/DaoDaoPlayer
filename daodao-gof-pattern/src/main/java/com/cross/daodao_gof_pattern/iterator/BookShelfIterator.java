package com.cross.daodao_gof_pattern.iterator;

/**
 * Created by cross on 2018/7/30.
 * <p>描述:迭代器模式核心（4/4）具体的迭代器
 */

public class BookShelfIterator implements Iterator<Book> {

	private int index;
	private BookShelf mBookShelf;

	public BookShelfIterator(BookShelf bookShelf) {
		mBookShelf = bookShelf;
	}

	@Override
	public boolean hasNext() {

		return index < mBookShelf.getLength();
	}

	@Override
	public Book next() {

		Book bookAt = mBookShelf.getBookAt(index);
		index++;
		return bookAt;
	}
}
