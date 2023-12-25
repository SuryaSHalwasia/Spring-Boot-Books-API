package com.halwasia.database.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halwasia.database.TestDataUtil;
import com.halwasia.database.domain.dto.BookDTO;
import com.halwasia.database.domain.entities.AuthorEntity;
import com.halwasia.database.domain.entities.BookEntity;
import com.halwasia.database.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.awt.print.Book;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private BookService bookService;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.bookService = bookService;
    }


    @Test
    public void testThatCreateBookSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorEntity.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        );

        BookEntity bookEntity = TestDataUtil.createBookA(authorEntity);
        String bookJson = objectMapper.writeValueAsString(bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );


    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsSavedBook() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorEntity.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        );

        BookEntity bookEntity = TestDataUtil.createBookA(authorEntity);
        String bookJson = objectMapper.writeValueAsString(bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn")
                        .value(bookEntity.getIsbn())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.title")
                .value(bookEntity.getTitle()));


    }


    @Test
    public void testThatListBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListBooksReturnsListOfBooks() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        BookEntity bookEntity = TestDataUtil.createBookA(authorEntity);

        bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.[0].isbn")
                                .value(bookEntity.getIsbn())
                ).andExpect(MockMvcResultMatchers.jsonPath("$.[0].title")
                        .value(bookEntity.getTitle()));
    }

    @Test
    public void testThatGetBookReturnsHttpStatus404WhenBookDoesNotExist()
            throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/978-1-2345-6789-5")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetBookReturnsHttpStatus200WhenBookExists()
            throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        bookService.saveBook(bookEntity.getIsbn(), bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + bookEntity.getIsbn())
        ).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testThatGetBookReturnsBookWhenBookExist() throws Exception {

        BookEntity bookEntity = TestDataUtil.createBookA(null);
        bookService.saveBook(bookEntity.getIsbn(), bookEntity);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + bookEntity.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn")
                                .value(bookEntity.getIsbn())
                ).andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value(bookEntity.getTitle()));
    }

    @Test
    public void testThatFullUpdateBookReturnsHttpStatus200WhenBookExists()
            throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        bookService.saveBook(bookEntity.getIsbn(), bookEntity);
        BookDTO bookDTO = TestDataUtil.createBookDtoA(null);

        String bookJson = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateBookReturnsBookWhenBookExists()
            throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        bookService.saveBook(bookEntity.getIsbn(), bookEntity);
        BookDTO bookDTO = TestDataUtil.createBookDtoA(null);

        String bookJson = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.isbn")
                .value(bookEntity.getIsbn())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.title")
                .value(bookDTO.getTitle()));
    }

    @Test
    public void testThatPartialUpdateExistingBookReturnsHttpStatus200Ok()
            throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        BookDTO bookDTO = TestDataUtil.createBookDtoB(null);
        String bookJson = objectMapper.writeValueAsString(bookDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch("/books/" +
                        bookEntity.getIsbn()).contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testThatPartialUpdateReturnsHttpStatus404WhenBookNotFound()
            throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA(null);

        BookDTO bookDTO = TestDataUtil.createBookDtoB(null);
        String bookJson = objectMapper.writeValueAsString(bookDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch("/books/" +
                        bookEntity.getIsbn()).contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testThatPartialUpdateExistingBookReturnsUpdatedBook()
            throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        BookDTO bookDTO = TestDataUtil.createBookDtoB(null);
        String bookJson = objectMapper.writeValueAsString(bookDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch("/books/" +
                        bookEntity.getIsbn()).contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)).andExpect(MockMvcResultMatchers
                .jsonPath("$.isbn").value(bookEntity.getIsbn())).andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value(
                                bookDTO.getTitle()
                        )
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus204() throws Exception
    {
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        bookService.saveBook(bookEntity.getIsbn(),bookEntity);
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/"+
                bookEntity.getIsbn())).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus404WhenBookDoesNotExist()
            throws Exception
    {
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/"+
                bookEntity.getIsbn())).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

    }
}