package com.halwasia.database.services.impl;

import com.halwasia.database.domain.entities.AuthorEntity;
import com.halwasia.database.repositories.AuthorRepository;
import com.halwasia.database.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository)
    {
        this.authorRepository = authorRepository;
    }
    @Override
    public AuthorEntity saveAuthor(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    @Override
    public List<AuthorEntity> findAll() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(),
                false).collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorEntity> findOne(long id) {
        return authorRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return this.authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity) {
        authorEntity.setId(id);
        return authorRepository.findById(id).map(existingAuthor -> {
            Optional.ofNullable(authorEntity.getName()).ifPresent(
                    existingAuthor::setName
            );
            Optional.ofNullable(authorEntity.getAge()).ifPresent(
                    existingAuthor::setAge
            );
            return authorRepository.save(existingAuthor);
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
