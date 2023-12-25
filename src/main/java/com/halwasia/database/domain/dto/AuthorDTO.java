package com.halwasia.database.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor  //always use with Jackson
@Builder
public class AuthorDTO {
    private Long id;
    private String name;
    private Integer age;
}
