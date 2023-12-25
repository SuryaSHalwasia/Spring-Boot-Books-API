package com.halwasia.database.controllers;

import com.halwasia.database.mappers.Mapper;
import com.halwasia.database.domain.dto.BookDTO;
import com.halwasia.database.domain.entities.BookEntity;
import com.halwasia.database.services.BookService;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Log
public class BookController {
    private BookService bookService;

    private Mapper<BookEntity, BookDTO> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDTO> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping(path = "/books/{isbn}", consumes = {"application/json"})
    public ResponseEntity<BookDTO> createUpdateBook(@RequestBody BookDTO bookDTO,
                                                    @PathVariable("isbn") String isbn)
    {
        BookEntity bookEntity = bookMapper.mapFrom(bookDTO);
        boolean exists = bookService.isExists(isbn);
        BookDTO savedBook = bookMapper.mapTo(bookService.saveBook(isbn,bookEntity));
        if(exists)
            return new ResponseEntity<>(savedBook,
                HttpStatus.OK);
        return new ResponseEntity<>(savedBook,
                HttpStatus.CREATED);
    }

    @GetMapping(path = "/books")
    public List<BookDTO> listBooks()
    {
        return bookService.findAll().stream().map(bookMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> getBook(@PathVariable("isbn") String isbn)
    {
        Optional<BookEntity> bookEntity = bookService.findOne(isbn);
        return bookEntity.map(
                bookEntity1 -> {
                    BookDTO bookDTO = bookMapper.mapTo(bookEntity1);
                    return new ResponseEntity<>(bookDTO,HttpStatus.OK);
                }
        ).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> partialUpdateBook(@PathVariable("isbn") String isbn,
                                                     @RequestBody BookDTO bookDTO)
    {
        if(!bookService.isExists(isbn))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        BookEntity bookEntity = bookMapper.mapFrom(bookDTO);
        return new ResponseEntity<>(bookMapper.mapTo(bookService
                .partialUpdate(isbn, bookEntity)),HttpStatus.OK);
    }

    @DeleteMapping("/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn)
    {
        if(!bookService.isExists(isbn))
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        bookService.deleteBook(isbn);
        return new ResponseEntity((HttpStatus.NO_CONTENT));
    }


}
