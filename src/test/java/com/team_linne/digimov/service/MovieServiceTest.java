package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.MovieNotFoundException;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.repository.GenreRepository;
import com.team_linne.digimov.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @InjectMocks
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private GenreService genreService;
    @Mock
    private GenreRepository genreRepository;

    @Test
    void should_return_all_movies_when_get_all_given_list_of_movies() {
        //given
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        Movie movie2 = new Movie("movie2", 156, new ArrayList<>(), "James", "a good movie", "movie2.jpg", "9", new ArrayList<>(), "English");
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
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        Movie movie2 = new Movie("movie2", 156, new ArrayList<>(), "James", "a good movie", "movie2.jpg", "9", new ArrayList<>(), "English");
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

    @Test
    void should_return_created_movie_when_create_movie_given_new_movie() {
        //given
        Movie movie = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        when(movieRepository.save(movie)).thenReturn(movie);

        //when
        final Movie actual = movieService.create(movie);

        //then
        assertEquals(movie, actual);
    }

    @Test
    void should_return_updated_movie_when_update_movie_given_movie_id_and_updated_movie_info() {
        //given
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        Movie movie2 = new Movie("movie2", 156, new ArrayList<>(), "James", "a good movie", "movie2.jpg", "9", new ArrayList<>(), "English");
        movie1.setId("1");
        when(movieRepository.findById("1")).thenReturn(Optional.of(movie1));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie2);

        //when
        final Movie actual = movieService.update("1", movie2);

        //then
        assertEquals(movie2, actual);
    }

    @Test
    void should_throw_movie_not_found_exception_when_update_movie_given_invalid_movie_id_and_updated_movie_info() {
        //given
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        movie1.setId("1");

        //when
        //then
        assertThrows(MovieNotFoundException.class, () -> {
            movieService.update("999", movie1);
        }, "Movie not found");
    }

    @Test
    void should_delete_movie_when_delete_movie_given_valid_movie_id() {
        //given
        Movie movie1 = new Movie("movie1", 123, new ArrayList<>(), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        movie1.setId("1");
        when(movieRepository.findById("1")).thenReturn(Optional.of(movie1));

        //when
        movieService.delete("1");

        //then
        verify(movieRepository, times(1)).deleteById("1");
    }

    @Test
    void should_throw_movie_not_found_exception_when_delete_movie_given_invalid_movie_id() {
        //given
        //when
        //then
        assertThrows(MovieNotFoundException.class, () -> {
            movieService.delete("999");
        }, "Movie not found");
    }

    @Test
    void should_call_update_2_time_when_delete_genre_id_given_2_movies_originally_contained_test_id() {
        Movie movie1 = new Movie("movie1", 123, Stream.of("1", "2").collect(Collectors.toList()), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        Movie movie2 = new Movie("movie2", 123, Stream.of("1", "2").collect(Collectors.toList()), "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        when(movieRepository.findAllByGenreIdsIn(any())).thenReturn(Stream.of(movie1, movie2).collect(Collectors.toList()));

        // when
        movieService.deleteGenreId("1");

        // then
        verify(movieRepository, times(2)).save(any());
    }
}

