package com.develop.book;

import java.util.List;

public interface BookService
{
    Book saveBook(BookCreateRequest bookCreateRequest);
    List<Book> getBooks();
}
