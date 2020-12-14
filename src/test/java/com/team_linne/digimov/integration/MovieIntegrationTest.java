package com.team_linne.digimov.integration;

import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MovieRepository movieRepository;

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
    }

    @Test
    public void should_return_all_movies_when_get_all_movies_given_movies() throws Exception {
        Movie movie1 = new Movie("movie1",123, new ArrayList<>(),"John","a movie","movie1.jpg","8");
        movieRepository.save(movie1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].name").value("movie1"))
                .andExpect(jsonPath("$[0].duration").value(123))
                .andExpect(jsonPath("$[0].genres").isEmpty())
                .andExpect(jsonPath("$[0].director").value("John"))
                .andExpect(jsonPath("$[0].description").value("a movie"))
                .andExpect(jsonPath("$[0].imageUrl").value("movie1.jpg"))
                .andExpect(jsonPath("$[0].rating").value("8"));
    }

    @Test
    public void should_return_specific_movie_when_get_movie_given_valid_movie_id() throws Exception {
        Movie movie1 = new Movie("movie1",123, new ArrayList<>(),"John","a movie","movie1.jpg","8");
        movieRepository.save(movie1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/movies/" + movie1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("movie1"))
                .andExpect(jsonPath("$.duration").value(123))
                .andExpect(jsonPath("$.genres").isEmpty())
                .andExpect(jsonPath("$.director").value("John"))
                .andExpect(jsonPath("$.description").value("a movie"))
                .andExpect(jsonPath("$.imageUrl").value("movie1.jpg"))
                .andExpect(jsonPath("$.rating").value("8"));
    }

    @Test
    public void should_return_404_not_found_when_get_movie_given_invalid_movie_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/movies/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }
}
