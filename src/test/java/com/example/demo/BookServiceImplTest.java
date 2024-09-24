package com.example.demo;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testGetAllBooks_NoFilters_ReturnsAllBooks() {
        List<Book> mockBooks = Arrays.asList(
                new Book("Book 1", "Author 1", 2001, 4.0),
                new Book("Book 2", "Author 2", 2002, 4.5)
        );

        when(bookRepository.findAll(any(Specification.class))).thenReturn(mockBooks);

        List<Book> result = bookService.getAllBooks(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testRateBook_ValidRating_Success() {
        Long bookId = 1L;
        Double newRating = 4.5;
        Book mockBook = new Book("Book 1", "Author 1", 2001, 4.0);
        mockBook.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(bookRepository.save(any(Book.class))).thenReturn(mockBook);

        Book updatedBook = bookService.rateBook(bookId, newRating);

        assertNotNull(updatedBook);
        assertEquals(newRating, updatedBook.getRating());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(mockBook);
    }

    @Test
    public void testRateBook_InvalidRating_ThrowsException() {
        Long bookId = 1L;
        Double invalidRating = 6.0;
        Book mockBook = new Book("Book 1", "Author 1", 2001, 4.0);
        mockBook.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.rateBook(bookId, invalidRating);
        });

        String expectedMessage = "Rating must be between 1 and 5";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }

}
