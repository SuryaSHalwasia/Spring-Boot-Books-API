package com.halwasia.database.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halwasia.database.TestDataUtil;
import com.halwasia.database.domain.dto.AuthorDTO;
import com.halwasia.database.domain.entities.AuthorEntity;
import com.halwasia.database.services.AuthorService;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private AuthorService authorService;
    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }


    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorDTO authorEntity = TestDataUtil.createAuthorDtoA();
        authorEntity.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor()
            throws Exception {
        AuthorDTO authorEntity = TestDataUtil.createAuthorDtoA();
        authorEntity.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
               MockMvcResultMatchers.jsonPath("$.name").value(authorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorEntity.getAge())
        );


    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorEntity.setId(null);
        authorService.saveAuthor(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.[0].name").value(authorEntity.getName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.[0].age").value(authorEntity.getAge())
                );
    }


    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception
    {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorEntity.setId(null);
        authorService.saveAuthor(authorEntity);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


    @Test
    public void testThatGetAuthorReturnsHttpStatus404WhenAuthorDoesNotExist()
            throws Exception
    {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorReturnsAuthorWhenAuthorExists()
            throws Exception
    {

        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorEntity.setId(null);
        authorService.saveAuthor(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorEntity.getName())

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorEntity.getAge())
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus404WhenNoAuthorExists()
            throws Exception
    {
        AuthorDTO authorEntity = TestDataUtil.createAuthorDtoA();
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists()
            throws Exception
    {
        AuthorEntity authorEntityA = TestDataUtil.createAuthorA();
        AuthorEntity savedAuthor = authorService.saveAuthor(authorEntityA);

        AuthorDTO authorEntity = TestDataUtil.createAuthorDtoA();
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testThatFullUpdateAuthorReturnsExistingAuthor()
            throws Exception
    {
        AuthorEntity authorEntityA = TestDataUtil.createAuthorA();
        AuthorEntity savedAuthor = authorService.saveAuthor(authorEntityA);

        AuthorDTO authorEntity = TestDataUtil.createAuthorDtoB();
        authorEntity.setId(savedAuthor.getId());
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name")
                .value(authorEntity.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age")
                        .value(authorEntity.getAge()));

    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsHttpStatus200Ok() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorService.saveAuthor(authorEntity);

        AuthorDTO authorDTO = TestDataUtil.createAuthorDtoB();
        String authorJson = objectMapper.writeValueAsString(authorDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + authorEntity.getId()).contentType(
                        MediaType.APPLICATION_JSON
                ).content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateReturnsHttpStatus404WhenNoAuthorExists()
            throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();

        AuthorDTO authorDTO = TestDataUtil.createAuthorDtoB();
        String authorJson = objectMapper.writeValueAsString(authorDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + authorEntity.getId()).contentType(
                        MediaType.APPLICATION_JSON
                ).content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsExistingAuthor()
            throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorService.saveAuthor(authorEntity);

        AuthorDTO authorDTO = TestDataUtil.createAuthorDtoB();
        String authorJson = objectMapper.writeValueAsString(authorDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + authorEntity.getId()).contentType(
                        MediaType.APPLICATION_JSON
                ).content(authorJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value(authorDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age")
                        .value(authorDTO.getAge()));
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204() throws Exception
    {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorService.saveAuthor(authorEntity);
        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/"+
                authorEntity.getId())).andExpect(
                        MockMvcResultMatchers.status().isNoContent()
        );

    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus404() throws Exception
    {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/"+
                authorEntity.getId())).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

    }
}
