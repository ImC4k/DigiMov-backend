package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.CinemaNotFoundException;
import com.team_linne.digimov.exception.MovieNotFoundException;
import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.repository.CinemaRepository;
import com.team_linne.digimov.repository.MovieRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @InjectMocks
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;

    @Test
    void should_return_all_movies_when_get_all_given_list_of_movies() {
        //given
        Movie movie1 = new Movie("movie1",123, new ArrayList<>(),"John","a movie","movie1.jpg","8");
        Movie movie2 = new Movie("movie2",156, new ArrayList<>(),"James","a good movie","movie2.jpg","9");
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie1);
        movieList.add(movie2);
        when(movieRepository.findAll()).thenReturn(movieList);

        //when
        final List<Movie> actual = movieService.getAll();

        //then
        assertEquals(movieList, actual);
    }

    @Test
    void should_return_empty_array_when_get_all_given_no_movies() {
        //given
        List<Movie> movieList = new ArrayList<>();
        when(movieRepository.findAll()).thenReturn(movieList);

        //when
        final List<Movie> actual = movieService.getAll();

        //then
        assertEquals(movieList, actual);
    }

    @Test
    void should_return_specific_movie_when_get_by_id_given_list_of_movies_and_valid_movie_id() {
        //given
        Movie movie1 = new Movie("movie1",123, new ArrayList<>(),"John","a movie","movie1.jpg","8");
        Movie movie2 = new Movie("movie2",156, new ArrayList<>(),"James","a good movie","movie2.jpg","9");
        movie1.setId("1");
        movie2.setId("2");
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie1);
        movieList.add(movie2);
        when(movieRepository.findById("1")).thenReturn(Optional.of(movie1));

        //when
        final Movie actual = movieService.getById("1");

        //then
        assertEquals(movie1, actual);
    }

    @Test
    void should_throw_movie_not_found_exception_when_get_by_id_given_list_of_movies_and_invalid_movie_id() {
        //given
        //when
        //then
        assertThrows(MovieNotFoundException.class, () -> {
            movieService.getById("999");
        }, "Movie not found");
    }
}
