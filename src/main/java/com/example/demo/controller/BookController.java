package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> author,
            @RequestParam Optional<Integer> year,
            @RequestParam Optional<Double> rating) {

        List<Book> books = bookService.getAllBooks(title, author, year, rating);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<Book> rateBook(
            @PathVariable Long id,
            @RequestParam Double rating) {

        Book updatedBook = bookService.rateBook(id, rating);
        return ResponseEntity.ok(updatedBook);
    }

    @PostMapping("/create")
    public ResponseEntity<Book> createBook(@RequestBody Book newBook) {
        Book savedBook = bookService.saveBook(newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestBody Book updatedBook) {

        Book existingBook = bookService.updateBook(id, updatedBook);
        return ResponseEntity.ok(existingBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
