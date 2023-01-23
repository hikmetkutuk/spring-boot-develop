package com.develop.book;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/book")
@Api(tags = "Book Controller")
public class BookController
{
    private final BookService bookService;

    public BookController(BookService bookService)
    {
        this.bookService = bookService;
    }

    @PostMapping("/create")
    public ResponseEntity<Book> saveUser(@RequestBody BookCreateRequest bookCreateRequest)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/book/create").toUriString());
        return ResponseEntity.created(uri).body(bookService.saveBook(bookCreateRequest));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Book>> getBooks()
    {
        return ResponseEntity.ok().body(bookService.getBooks());
    }
}
