package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.CinemaNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void should_throw_cinema_not_found_exception_when_get_by_id_given_list_of_cinemas_and_invalid_cinema_id() {
        //given
        //when
        //then
        assertThrows(CinemaNotFoundException.class, () -> {
            cinemaService.getById("999");
        }, "Cinema not found");
    }

    @Test
    void should_return_created_cinema_when_create_cinema_given_new_cinema() {
        //given
        Cinema cinema = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00","12345678");
        when(cinemaRepository.save(cinema)).thenReturn(cinema);

        //when
        final Cinema actual = cinemaService.create(cinema);

        //then
        assertEquals(cinema, actual);
    }

    @Test
    void should_return_updated_cinema_when_update_cinema_given_cinema_id_and_updated_cinema_info() {
        //given
        Cinema cinema = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00","12345678");
        Cinema cinema2 = new Cinema("cinema 2", "hong kong", "cinema2.jpg", "8:00-23:00","12345678");
        cinema.setId("1");
        when(cinemaRepository.findById("1")).thenReturn(Optional.of(cinema));
        when(cinemaRepository.save(any(Cinema.class))).thenReturn(cinema2);

        //when
        final Cinema actual = cinemaService.update("1", cinema2);

        //then
        assertEquals(cinema2, actual);
    }

    @Test
    void should_throw_cinema_not_found_exception_when_update_cinema_given_invalid_cinema_id_and_updated_cinema_info() {
        //given
        Cinema cinema2 = new Cinema("cinema 2", "hong kong", "cinema2.jpg", "8:00-23:00","12345678");
        cinema2.setId("1");

        //when
        //then
        assertThrows(CinemaNotFoundException.class, () -> {
            cinemaService.update("999", cinema2);
        }, "Cinema not found");
    }

    @Test
    void should_delete_cinema_when_delete_cinema_given_valid_cinema_id() {
        //given
        Cinema cinema = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00","12345678");
        cinema.setId("1");
        when(cinemaRepository.findById("1")).thenReturn(Optional.of(cinema));

        //when
        cinemaService.delete("1");

        //then
        verify(cinemaRepository, times(1)).deleteById("1");
    }
}
