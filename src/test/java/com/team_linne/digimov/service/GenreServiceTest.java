package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.GenreNotFoundException;
import com.team_linne.digimov.model.Genre;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.repository.GenreRepository;
import com.team_linne.digimov.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {
    @InjectMocks
    private GenreService genreService;
    @Mock
    private GenreRepository genreRepository;

    @Mock
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;

    @Test
    void should_return_all_genres_when_get_all_given_list_of_genres() {
        //given
        Genre genre1 = new Genre("Romance");
        Genre genre2 = new Genre("Thriller");
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genre1);
        genreList.add(genre2);
        when(genreRepository.findAll()).thenReturn(genreList);

        //when
        final List<Genre> actual = genreService.getAll();

        //then
        assertEquals(genreList, actual);
    }

    @Test
    void should_return_empty_array_when_get_all_given_no_genres() {
        //given
        List<Genre> genreList = new ArrayList<>();
        when(genreRepository.findAll()).thenReturn(genreList);

        //when
        final List<Genre> actual = genreService.getAll();

        //then
        assertEquals(genreList, actual);
    }

    @Test
    void should_return_specific_genre_when_get_by_id_given_list_of_genres_and_valid_genre_id() {
        //given
        Genre genre1 = new Genre("Romance");
        Genre genre2 = new Genre("Thriller");
        genre1.setId("1");
        genre2.setId("2");
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genre1);
        genreList.add(genre2);
        when(genreRepository.findById("1")).thenReturn(Optional.of(genre1));

        //when
        final Genre actual = genreService.getById("1");

        //then
        assertEquals(genre1, actual);
    }

    @Test
    void should_throw_genre_not_found_exception_when_get_by_id_given_list_of_genres_and_invalid_genre_id() {
        //given
        //when
        //then
        assertThrows(GenreNotFoundException.class, () -> genreService.getById("999"), "Genre not found");
    }

    @Test
    void should_return_created_genre_when_create_genre_given_new_genre() {
        //given
        Genre genre1 = new Genre("Romance");
        when(genreRepository.save(genre1)).thenReturn(genre1);

        //when
        final Genre actual = genreService.create(genre1);

        //then
        assertEquals(genre1, actual);
    }

    @Test
    void should_return_updated_genre_when_update_genre_given_genre_id_and_updated_genre_info() {
        //given
        Genre genre1 = new Genre("Romance");
        Genre genre2 = new Genre("Thriller");
        genre1.setId("1");
        when(genreRepository.findById("1")).thenReturn(Optional.of(genre1));
        when(genreRepository.save(any(Genre.class))).thenReturn(genre2);

        //when
        final Genre actual = genreService.update("1", genre2);

        //then
        assertEquals(genre2, actual);
    }

    @Test
    void should_throw_genre_not_found_exception_when_update_genre_given_invalid_genre_id_and_updated_genre_info() {
        //given
        Genre genre = new Genre("Romance");
        genre.setId("1");

        //when
        //then
        assertThrows(GenreNotFoundException.class, () -> genreService.update("999", genre), "Genre not found");
    }

    @Test
    void should_delete_genre_when_delete_genre_given_valid_genre_id() {
        //given
        Genre genre = new Genre("Romance");
        genre.setId("1");

        when(genreRepository.findById("1")).thenReturn(Optional.of(genre));

        //when
        genreService.delete("1");

        //then
        verify(genreRepository, times(1)).deleteById("1");
    }

    @Test
    void should_throw_genre_not_found_exception_when_delete_genre_given_invalid_genre_id() {
        //given
        //when
        //then
        assertThrows(GenreNotFoundException.class, () -> genreService.delete("999"), "Genre not found");
    }

    @Test
    void should_return_movie_without_genre_when_delete_genre_given_movie_with_genre_id() {
        //given
        Genre genre = new Genre("Romance");
        genre.setId("1");
        List<String> genreIds = Collections.singletonList(genre.getId());
        Movie movie = new Movie("movie1", 123, genreIds, "John", "a movie", "movie1.jpg", "8", new ArrayList<>(), "Cantonese");
        when(genreRepository.findById(genre.getId())).thenReturn(Optional.of(genre));

        //when
        genreService.delete(genre.getId());

        //then
        verify(movieService, times(1)).deleteGenreId("1");
    }
}

