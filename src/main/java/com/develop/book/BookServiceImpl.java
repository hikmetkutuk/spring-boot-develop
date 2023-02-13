package com.develop.book;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@CacheConfig(cacheNames = "bookCache")
public class BookServiceImpl implements BookService
{
    private final BookRepository bookRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public BookServiceImpl(BookRepository bookRepository)
    {
        this.bookRepository = bookRepository;
    }

    @Override
    @CacheEvict(cacheNames = "books", allEntries = true)
    public Book saveBook(BookCreateRequest bookCreateRequest)
    {
        Book book = new Book
        (
            null,
            bookCreateRequest.getName(),
            bookCreateRequest.getAuthor(),
            bookCreateRequest.getDescription(),
            bookCreateRequest.getPrice()
        );

        logger.info("Saving new book {} to the database", bookCreateRequest.getName());
        return bookRepository.save(book);
    }

    @Override
    @Cacheable(cacheNames = "books")
    public List<Book> getBooks()
    {
        logger.info("Fetching all books");
        return bookRepository.findAll();
    }
}
