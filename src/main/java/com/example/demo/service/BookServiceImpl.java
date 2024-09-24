package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks(Optional<String> title, Optional<String> author, Optional<Integer> year, Optional<Double> rating) {
        return bookRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            title.ifPresent(t -> predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + t.toLowerCase() + "%")
            ));
            author.ifPresent(a -> predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + a.toLowerCase() + "%")
            ));
            year.ifPresent(y -> predicates.add(
                    criteriaBuilder.equal(root.get("year"), y)
            ));
            rating.ifPresent(r -> predicates.add(
                    criteriaBuilder.equal(root.get("rating"), r)
            ));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public Book rateBook(Long id, Double rating) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        book.setRating(rating);

        return bookRepository.save(book);
    }

    @Override
    public Book saveBook(Book newBook) {
        return bookRepository.save(newBook);
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            throw new EntityNotFoundException("Book not found");
        }
        Book existingBook = optionalBook.get();
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setYear(updatedBook.getYear());
        existingBook.setRating(updatedBook.getRating());
        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found");
        }
        bookRepository.deleteById(id);
    }
}
