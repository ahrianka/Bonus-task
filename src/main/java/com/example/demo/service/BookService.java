package com.example.demo.service;

import com.example.demo.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> getAllBooks(Optional<String> title, Optional<String> author, Optional<Integer> year, Optional<Double> rating);

    Book rateBook(Long id, Double rating);


    Book saveBook(Book newBook);

    Book updateBook(Long id, Book updatedBook);

    void deleteBook(Long id);
}