package com.halwasia.database.domain.dto;

import com.halwasia.database.domain.entities.AuthorEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor  //always use with Jackson
@Builder
public class BookDTO {
    private String isbn;
    private String title;
    private AuthorDTO authorDTO;
}
