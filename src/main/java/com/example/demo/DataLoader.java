package com.example.demo;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;

    public DataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() == 0) {
            Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", 1925, 4.5);
            Book book2 = new Book("1984", "George Orwell", 1949, 4.7);
            Book book3 = new Book("To Kill a Mockingbird", "Harper Lee", 1960, 4.8);

            bookRepository.saveAll(Arrays.asList(book1, book2, book3));

            System.out.println("Mock data initialized");
        } else {
            System.out.println("Data already exists");
        }
    }
}
