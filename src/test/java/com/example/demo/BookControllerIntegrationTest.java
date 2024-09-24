package com.example.demo;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
//@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    private Book mockBook1;
    private Book mockBook2;

    @BeforeEach
    public void setup() {
        mockBook1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", 1925, 4.5);
        mockBook1.setId(1L);
        mockBook2 = new Book("1984", "George Orwell", 1949, 4.7);
        mockBook2.setId(2L);
    }

    @Test
    public void testGetBooks_ReturnsBooks() throws Exception {
        when(bookRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(mockBook1, mockBook2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("The Great Gatsby"))
                .andExpect(jsonPath("$[1].title").value("1984"));
    }

    @Test
    public void testGetBooks_WithAuthorFilter_ReturnsFilteredBooks() throws Exception {
        when(bookRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(mockBook2));

        mockMvc.perform(get("/api/books")
                        .param("author", "George Orwell"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].author").value("George Orwell"));
    }

    @Test
    public void testRateBook_ValidRating_Success() throws Exception {
        Double newRating = 5.0;
        mockBook1.setRating(newRating);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook1));
        when(bookRepository.save(any(Book.class))).thenReturn(mockBook1);

        mockMvc.perform(post("/api/books/1/rate")
                        .param("rating", newRating.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(newRating));
    }

    @Test
    public void testRateBook_InvalidRating_ReturnsBadRequest() throws Exception {
        Double invalidRating = 6.0;
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook1));

        mockMvc.perform(post("/api/books/1/rate")
                        .param("rating", invalidRating.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Rating must be between 1 and 5"));
    }

}

