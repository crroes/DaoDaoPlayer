package com.cross.daodao_gof_pattern;

import com.cross.daodao_gof_pattern.iterator.Book;
import com.cross.daodao_gof_pattern.iterator.BookShelf;
import com.cross.daodao_gof_pattern.iterator.Iterator;

import org.junit.Test;

/**
 * Created by cross on 2018/7/30.
 * <p>描述:
 */

public class PatternTest {

	@Test
	public void iterator(){
		BookShelf bookShelf = new BookShelf();
		bookShelf.appendBook(new Book("Around the World in 80 Days"));
		bookShelf.appendBook(new Book("Bible"));
		bookShelf.appendBook(new Book("Cinderella"));
		bookShelf.appendBook(new Book("Daddy Long Legs"));
		Iterator<Book> iterator = bookShelf.iterator();
		while (iterator.hasNext()) {
			Book next = iterator.next();
			System.out.println(next.getName());
		}
	}
}
