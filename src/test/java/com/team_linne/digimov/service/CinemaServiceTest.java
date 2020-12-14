package com.team_linne.digimov.service;

import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.repository.CinemaRepository;
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
public class CinemaServiceTest {

    @InjectMocks
    private CinemaService cinemaService;
    @Mock
    private CinemaRepository cinemaRepository;

    @Test
    void should_return_all_cinemas_when_get_all_given_list_of_cinemas() {
        //given
        Cinema cinema1 = new Cinema("cinema 1", "hong kong", "cinema1.jpg");
        Cinema cinema2 = new Cinema("cinema 2", "hong kong", "cinema2.jpg");
        List<Cinema> cinemaList = new ArrayList<>();
        cinemaList.add(cinema1);
        cinemaList.add(cinema2);
        when(cinemaRepository.findAll()).thenReturn(cinemaList);

        //when
        final List<Cinema> actual = cinemaService.getAll();

        //then
        assertEquals(cinemaList, actual);
    }

    @Test
    void should_return_empty_array_when_get_all_given_no_cinemas() {
        //given
        List<Cinema> cinemaList = new ArrayList<>();
        when(cinemaRepository.findAll()).thenReturn(cinemaList);

        //when
        final List<Cinema> actual = cinemaService.getAll();

        //then
        assertEquals(cinemaList, actual);
    }
}
