package com.team_linne.digimov.integration;

import com.team_linne.digimov.model.Genre;
import com.team_linne.digimov.repository.GenreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GenreIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    GenreRepository genreRepository;

    @AfterEach
    void tearDown() { genreRepository.deleteAll(); }

    @Test
    public void should_return_all_genres_when_get_all_genres_given_genres() throws Exception {
        Genre genre = new Genre("comedy");
        genreRepository.save(genre);

        mockMvc.perform(MockMvcRequestBuilders.get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].name").value("comedy"));
    }

    @Test
    public void should_return_specific_genre_when_get_genre_given_valid_genre_id() throws Exception {
        Genre genre = new Genre("comedy");
        genreRepository.save(genre);

        mockMvc.perform(MockMvcRequestBuilders.get("/genres/" + genre.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("comedy"));
    }

    @Test
    public void should_return_404NotFound_when_get_genre_given_invalid_genre_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/genres/" + "5fc8913234ba53396c26a864"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_created_genre_when_create_genre_given_complete_new_genre_info() throws Exception {
        String genreAsJson = "{\n" +
                "    \"name\": \"comedy\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(genreAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("comedy"));
    }
}
