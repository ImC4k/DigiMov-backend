package com.team_linne.digimov.service;

import com.sun.tools.javac.jvm.Gen;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}
