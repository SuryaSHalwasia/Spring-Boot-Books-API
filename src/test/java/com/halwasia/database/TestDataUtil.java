package com.halwasia.database;

import com.halwasia.database.domain.dto.AuthorDTO;
import com.halwasia.database.domain.dto.BookDTO;
import com.halwasia.database.domain.entities.AuthorEntity;
import com.halwasia.database.domain.entities.BookEntity;

public final class TestDataUtil {
    private TestDataUtil(){}


    public static AuthorEntity createAuthorA() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Abigail Rose")
                .age(80)
                .build();
    }

    public static AuthorDTO createAuthorDtoA() {
        return AuthorDTO.builder()
                .id(1L)
                .name("Abigail Rose")
                .age(80)
                .build();
    }

    public static AuthorEntity createAuthorB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("Thomas Cronin")
                .age(44)
                .build();
    }

    public static AuthorDTO createAuthorDtoB() {
        return AuthorDTO.builder()
                .id(2L)
                .name("Thomas Cronin")
                .age(44)
                .build();
    }

    public static AuthorEntity createAuthorC() {
        return AuthorEntity.builder()
                .id(3L)
                .name("Jesse A Cassey")
                .age(24)
                .build();
    }

    public static AuthorDTO createAuthorDtoC() {
        return AuthorDTO.builder()
                .id(3L)
                .name("Jesse A Cassey")
                .age(24)
                .build();
    }

    public static BookEntity createBookA(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-0")
                .title("The Shadow of the Attic")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDTO createBookDtoA(final AuthorDTO authorDTO) {
        return BookDTO.builder()
                .isbn("978-1-2345-6789-0")
                .title("The Shadow of the Attic")
                .authorDTO(authorDTO)
                .build();
    }

    public static BookDTO createBookDtoB(final AuthorDTO authorDTO) {
        return BookDTO.builder()
                .isbn("978-1-2345-6789-1")
                .title("Beyond the Horizon")
                .authorDTO(authorDTO)
                .build();
    }

    public static BookEntity createBookC(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-2")
                .title("The Last Ember")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDTO createBookDtoC(final AuthorDTO authorDTO) {
        return BookDTO.builder()
                .isbn("978-1-2345-6789-2")
                .title("The Last Ember")
                .authorDTO(authorDTO)
                .build();
    }
}
