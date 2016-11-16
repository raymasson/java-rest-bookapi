package com.pluralsight.repositories;

import java.util.ArrayList;
import java.util.List;

import com.pluralsight.models.BookModel;

public class BookRepository {
	
	public List<BookModel> getAll(){
		List<BookModel> books = new ArrayList<BookModel>();
		
		BookModel book = new BookModel();
		book.setTitle("Les misérables");
		book.setAuthor("Victor Hugo");
		book.setGenre("Historical");
		book.setRead(true);
		
		BookModel book2 = new BookModel();
		book2.setTitle("Les misérables 2");
		book2.setAuthor("Victor Hugo 2");
		book2.setGenre("Historical 2");
		book2.setRead(false);
		
		books.add(book);
		books.add(book2);
		
		return books;
	}
}
