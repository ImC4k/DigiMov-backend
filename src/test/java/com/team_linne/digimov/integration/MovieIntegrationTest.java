package com.team_linne.digimov.integration;

import com.team_linne.digimov.model.Genre;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.repository.GenreRepository;
import com.team_linne.digimov.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    GenreRepository genreRepository;

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
    }

    @Test
    public void should_return_all_movies_when_get_all_movies_given_movies() throws Exception {
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
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
                .andExpect(jsonPath("$[0].rating").value("8"))
                .andExpect(jsonPath("$[0].cast").isEmpty())
                .andExpect(jsonPath("$[0].language").value("Cantonese"));
    }

    @Test
    public void should_return_specific_movie_when_get_movie_given_valid_movie_id() throws Exception {
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
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
                .andExpect(jsonPath("$.rating").value("8"))
                .andExpect(jsonPath("$.cast").isEmpty())
                .andExpect(jsonPath("$.language").value("Cantonese"));
    }

    @Test
    public void should_return_404_not_found_when_get_movie_given_invalid_movie_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/movies/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_get_movie_given_illegal_movie_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/movies/" + "123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_created_movie_when_create_movie_given_complete_new_movie_info() throws Exception {
        String movieAsJson = "{\n" +
                "    \"name\": \"movie1\",\n" +
                "    \"duration\": \"123\",\n" +
                "    \"genreIds\": [],\n" +
                "    \"director\": \"John\",\n" +
                "    \"description\": \"a movie\",\n" +
                "    \"imageUrl\": \"movie1.jpg\",\n" +
                "    \"rating\": \"8\",\n" +
                "    \"cast\": [],\n" +
                "    \"language\": \"Cantonese\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("movie1"))
                .andExpect(jsonPath("$.duration").value(123))
                .andExpect(jsonPath("$.genres").isEmpty())
                .andExpect(jsonPath("$.director").value("John"))
                .andExpect(jsonPath("$.description").value("a movie"))
                .andExpect(jsonPath("$.imageUrl").value("movie1.jpg"))
                .andExpect(jsonPath("$.rating").value("8"))
                .andExpect(jsonPath("$.cast").isEmpty())
                .andExpect(jsonPath("$.language").value("Cantonese"));
    }

    @Test
    public void should_return_created_incompleted_movie_when_create_movie_given_incomplete_new_movie_info() throws Exception {
        String movieAsJson = "{\n" +
                "    \"name\": \"movie1\",\n" +
                "    \"duration\": \"123\",\n" +
                "    \"genreIds\": [],\n" +
                "    \"director\": \"John\",\n" +
                "    \"rating\": \"8\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("movie1"))
                .andExpect(jsonPath("$.duration").value(123))
                .andExpect(jsonPath("$.genres").isEmpty())
                .andExpect(jsonPath("$.director").value("John"))
                .andExpect(jsonPath("$.description").isEmpty())
                .andExpect(jsonPath("$.imageUrl").isEmpty())
                .andExpect(jsonPath("$.rating").value("8"))
                .andExpect(jsonPath("$.cast").isEmpty())
                .andExpect(jsonPath("$.language").isEmpty());
    }

    @Test
    public void should_return_error_when_create_movie_given_complete_new_movie_info_and_illegal_genre_ids() throws Exception {
        String movieAsJson = "{\n" +
                "    \"name\": \"movie1\",\n" +
                "    \"duration\": \"123\",\n" +
                "    \"genreIds\": [\"Romance\"],\n" +
                "    \"director\": \"John\",\n" +
                "    \"description\": \"a movie\",\n" +
                "    \"imageUrl\": \"movie1.jpg\",\n" +
                "    \"rating\": \"8\",\n" +
                "    \"cast\": [],\n" +
                "    \"language\": \"Cantonese\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_updated_movie_when_update_movie_given_valid_movie_id_and_movie_update_info() throws Exception {
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        movieRepository.save(movie1);
        String movieAsJson = "{\n" +
                "    \"name\": \"movie1\",\n" +
                "    \"duration\": \"456\",\n" +
                "    \"genreIds\": [],\n" +
                "    \"director\": \"John\",\n" +
                "    \"description\": \"a movie\",\n" +
                "    \"imageUrl\": \"movie1.jpg\",\n" +
                "    \"rating\": \"3\",\n" +
                "    \"cast\": [],\n" +
                "    \"language\": \"Cantonese\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/movies/" + movie1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("movie1"))
                .andExpect(jsonPath("$.duration").value(456))
                .andExpect(jsonPath("$.genres").isEmpty())
                .andExpect(jsonPath("$.director").value("John"))
                .andExpect(jsonPath("$.description").value("a movie"))
                .andExpect(jsonPath("$.imageUrl").value("movie1.jpg"))
                .andExpect(jsonPath("$.rating").value("3"))
                .andExpect(jsonPath("$.cast").isEmpty())
                .andExpect(jsonPath("$.language").value("Cantonese"));
    }

    @Test
    public void should_return_404_not_found_when_update_movie_given_invalid_movie_id_and_movie_update_info() throws Exception {
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        movieRepository.save(movie1);
        String movieAsJson = "{\n" +
                "    \"name\": \"movie1\",\n" +
                "    \"duration\": \"456\",\n" +
                "    \"genreIds\": [],\n" +
                "    \"director\": \"John\",\n" +
                "    \"description\": \"a movie\",\n" +
                "    \"imageUrl\": \"movie1.jpg\",\n" +
                "    \"rating\": \"3\",\n" +
                "    \"cast\": [],\n" +
                "    \"language\": \"Cantonese\"\n" + "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/movies/" + "5fc8913234ba53396c26a863")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_update_movie_given_illegal_movie_id_and_movie_update_info() throws Exception {
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        movieRepository.save(movie1);
        String movieAsJson = "{\n" +
                "    \"name\": \"movie1\",\n" +
                "    \"duration\": \"456\",\n" +
                "    \"genreIds\": [],\n" +
                "    \"director\": \"John\",\n" +
                "    \"description\": \"a movie\",\n" +
                "    \"imageUrl\": \"movie1.jpg\",\n" +
                "    \"rating\": \"3\",\n" +
                "    \"cast\": [],\n" +
                "    \"language\": \"Cantonese\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/movies/" + "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_delete_movie_when_delete_movie_given_valid_movie_id() throws Exception {
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        movieRepository.save(movie1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/movies/" + movie1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_404_not_found_when_delete_movie_given_invalid_movie_id() throws Exception {
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        movieRepository.save(movie1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/movies/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_delete_movie_given_illegal_movie_id() throws Exception {
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        movieRepository.save(movie1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/movies/" + "123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_movie_without_genre_when_delete_genre_given_movie_with_genre_id() throws Exception {
        Genre genre = new Genre("Romance");
        Genre savedGenreInDB = genreRepository.save(genre);
        List<String> genreIds = Collections.singletonList(savedGenreInDB.getId());
        Movie movie = new Movie("movie1",123, genreIds,"John","a movie","movie1.jpg","8", new ArrayList<>(), "Cantonese");

        Movie savedMovieInDB = movieRepository.save(movie);

        mockMvc.perform(MockMvcRequestBuilders.delete("/genres/" + savedGenreInDB.getId())).andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/" + savedMovieInDB.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genres").isEmpty());
    }
}
