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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        Cinema cinema1 = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00","12345678");
        Cinema cinema2 = new Cinema("cinema 2", "hong kong", "cinema2.jpg", "8:00-23:00","12345678");
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

    @Test
    void should_return_specific_cinema_when_get_by_id_given_list_of_cinemas_and_valid_cinema_id() {
        //given
        Cinema cinema1 = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00","12345678");
        Cinema cinema2 = new Cinema("cinema 2", "hong kong", "cinema2.jpg", "8:00-23:00","12345678");
        cinema1.setId("1");
        cinema2.setId("2");
        List<Cinema> cinemaList = new ArrayList<>();
        cinemaList.add(cinema1);
        cinemaList.add(cinema2);
        when(cinemaRepository.findById("1")).thenReturn(Optional.of(cinema1));

        //when
        final Cinema actual = cinemaService.getById("1");

        //then
        assertEquals(cinema1, actual);
    }

    @Test
    void should_return_null_when_get_by_id_given_list_of_cinemas_and_invalid_cinema_id() {
        //given
        when(cinemaRepository.findById("3")).thenReturn(Optional.empty());

        //when
        final Cinema actual = cinemaService.getById("3");

        //then
        assertNull(actual);
    }
}
