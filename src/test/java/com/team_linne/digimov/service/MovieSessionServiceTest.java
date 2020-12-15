package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.MovieNotFoundException;
import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.model.SeatStatus;
import com.team_linne.digimov.repository.MovieSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieSessionServiceTest {
    @InjectMocks
    private MovieSessionService movieSessionService;
    @Mock
    private MovieSessionRepository movieSessionRepository;

    @Test
    public void should_return_all_movie_session_when_get_all_given_list_of_movie_session() {
        //given String houseId, Long startTime, Map<String, Double> prices, Map<Integer, SeatStatus> occupied
        MovieSession movieSession1 = new MovieSession("111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        MovieSession movieSession2 = new MovieSession("222", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        List<MovieSession> movieSessionList = new ArrayList<>();
        movieSessionList.add(movieSession1);
        movieSessionList.add(movieSession2);
        when(movieSessionRepository.findAll()).thenReturn(movieSessionList);

        //when
        final List<MovieSession> actual = movieSessionService.getAll();

        //then
        assertEquals(movieSessionList, actual);
    }
    
    @Test
    public void should_return_empty_list_when_get_all_given_no_movie_session() {
        //given
        List<MovieSession> movieSessionList = new ArrayList<>();
        when(movieSessionRepository.findAll()).thenReturn(movieSessionList);

        //when
        final List<MovieSession> actual = movieSessionService.getAll();
        
        //then
        assertEquals(movieSessionList, actual);
    }

    @Test
    void should_return_specific_movie_session_when_get_by_id_given_list_of_movie_session_and_valid_movie_session_id() {
        //given
        MovieSession movieSession1 = new MovieSession("111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        MovieSession movieSession2 = new MovieSession("222", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());

        movieSession1.setId("1");
        movieSession2.setId("2");

        List<MovieSession> movieSessionList = new ArrayList<>();
        movieSessionList.add(movieSession1);
        movieSessionList.add(movieSession2);
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));

        //when
        final MovieSession actual = movieSessionService.getById("1");

        //then
        assertEquals(movieSession1, actual);
    }

    @Test
    public void should_throw_MovieSessionNotFoundException_when_get_by_id_given_invalid_movie_session_id() {
        //given
        assertThrows(MovieNotFoundException.class, () -> {
            movieSessionService.getById("5fc8913234ba53396c26a863");
        }, "Movie Session not found");
    }

    @Test
    public void should_return_created_movie_session_when_create_movie_session_given_new_movie_session() {
        //given
        MovieSession movieSession = new MovieSession("111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        when(movieSessionRepository.insert(movieSession)).thenReturn(movieSession);

        //when
        MovieSession actual = movieSessionService.create(movieSession);

        //then
        assertEquals(movieSession, actual);
    }

    @Test
    void should_return_updated_movie_session_when_update_movie_session_given_movie_session_id_and_updated_movie_session_info() {
        //given
        MovieSession movieSession1 = new MovieSession("111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        MovieSession movieSession2 = new MovieSession("222", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());

        movieSession1.setId("1");

        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));
        when(movieSessionRepository.save(any(MovieSession.class))).thenReturn(movieSession2);

        //when
        final MovieSession actual = movieSessionService.update("1", movieSession2);

        //then
        assertEquals(movieSession2, actual);
    }

    @Test
    public void should_throw_MovieSessionNotFoundException_when_update_given_invalid_movie_session_id() {
        //given
        MovieSession movieSession1 = new MovieSession("111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSession1.setId("1");

        //when
        //then
        assertThrows(MovieNotFoundException.class, () -> {
            movieSessionService.update("999", movieSession1);
        }, "Movie not found");
    }


    @Test
    void should_delete_movie_session_when_delete_movie_session_given_valid_movie_session_id() {
        //given
        MovieSession movieSession1 = new MovieSession("111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSession1.setId("1");
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));

        //when
        movieSessionService.delete("1");

        //then
        verify(movieSessionRepository, times(1)).deleteById("1");
    }



}
