package com.team_linne.digimov.service;

import com.sun.tools.javac.jvm.Gen;
import com.team_linne.digimov.exception.CinemaNotFoundException;
import com.team_linne.digimov.exception.GenreNotFoundException;
import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.model.Genre;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.repository.GenreRepository;
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
public class GenreServiceTest {
    @InjectMocks
    private GenreService genreService;
    @Mock
    private GenreRepository genreRepository;

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
        assertThrows(GenreNotFoundException.class, () -> {
            genreService.getById("999");
        }, "Genre not found");
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

}
