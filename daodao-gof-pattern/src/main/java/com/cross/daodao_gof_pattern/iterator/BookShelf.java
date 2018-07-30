package com.cross.daodao_gof_pattern.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cross on 2018/7/30.
 * <p>描述:迭代器模式核心（3/4）具体集合
 */

public class BookShelf implements Aggregate {

	private List<Book> books = new ArrayList<>();
	private int last;

	@Override
	public Iterator<Book> iterator() {
		return new BookShelfIterator(this);
	}

	public int getLength(){
		return last;
	}

	public Book getBookAt(int index){
		return books.get(index);
	}

	public void appendBook(Book book){
		books.add(book);
		last++;
	}
}
