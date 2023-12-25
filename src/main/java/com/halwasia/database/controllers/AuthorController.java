package com.halwasia.database.controllers;

import com.halwasia.database.domain.dto.AuthorDTO;
import com.halwasia.database.domain.entities.AuthorEntity;
import com.halwasia.database.mappers.Mapper;
import com.halwasia.database.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    AuthorService authorService;

    private Mapper<AuthorEntity, AuthorDTO> authorMapper;
    public AuthorController(AuthorService authorService,
                            Mapper<AuthorEntity,AuthorDTO> authorMapper)
    {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }
    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO author)
    {
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        return new ResponseEntity<>(authorMapper.mapTo(authorService
                .saveAuthor(authorEntity)), HttpStatus.CREATED);
    }

    @GetMapping(path = "/authors")
    public List<AuthorDTO> listAuthors()
    {
        List<AuthorEntity> authors = authorService.findAll();
        return authors.stream().map(authorMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable("id") Long id)
    {
        Optional<AuthorEntity> author = authorService.findOne(id);
        return author.map(authorEntity -> {
            AuthorDTO authorDTO = authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(authorDTO,HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> fullUpdateAuthor(@PathVariable("id") Long id,
                                                      @RequestBody AuthorDTO authorDTO)
    {
        if(!authorService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        authorDTO.setId(id);
        return new ResponseEntity<>(authorMapper.mapTo(authorService.saveAuthor
                (authorMapper.mapFrom(authorDTO))),HttpStatus.OK);
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> partialUpdateAuthor(@PathVariable("id") Long id,
                                                         @RequestBody AuthorDTO authorDTO)
    {
        if(!authorService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDTO);
        AuthorEntity updatedEntity = authorService.partialUpdate(id, authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(updatedEntity),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable("id") Long id)
    {
        if(!authorService.isExists(id))
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        authorService.deleteAuthor(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
