package com.team_linne.digimov.service;

import com.team_linne.digimov.dto.MovieSessionPatchRequest;
import com.team_linne.digimov.exception.InvalidSeatUpdateOperationException;
import com.team_linne.digimov.exception.MovieNotFoundException;
import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.model.SeatStatus;
import com.team_linne.digimov.repository.MovieSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        MovieSession movieSession2 = new MovieSession("mov2", "222", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
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
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        MovieSession movieSession2 = new MovieSession("mov2", "222", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());

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
        MovieSession movieSession = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        when(movieSessionRepository.insert(movieSession)).thenReturn(movieSession);

        //when
        MovieSession actual = movieSessionService.create(movieSession);

        //then
        assertEquals(movieSession, actual);
    }

    @Test
    void should_return_updated_movie_session_when_update_movie_session_given_movie_session_id_and_updated_movie_session_info() {
        //given
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        MovieSession movieSession2 = new MovieSession("mov2", "222", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());

        movieSession1.setId("1");

        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));
        when(movieSessionRepository.save(any(MovieSession.class))).thenReturn(movieSession2);

        //when
        final MovieSession actual = movieSessionService.update("1", movieSession2);

        //then
        assertEquals(movieSession2, actual);
    }

    @Test
    void should_throw_MovieSessionNotFoundException_when_update_given_invalid_movie_session_id() {
        //given
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSession1.setId("1");

        //when
        //then
        assertThrows(MovieNotFoundException.class, () -> {
            movieSessionService.update("999", movieSession1);
        }, "Movie not found");
    }

    @Test
    void should_return_movie_sesison_with_seat_indices_status_updated_when_patch_given_patch_request_valid_and_those_seats_were_available() {
        //given
        MovieSessionPatchRequest movieSessionPatchRequest = new MovieSessionPatchRequest(Stream.of(1, 2, 3).collect(Collectors.toList()), "randomClientSessionId");
        Map<String, Double> prices = new HashMap<>();
        prices.put("Student", 50.0);
        prices.put("Adult", 100.0);

        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(4, new SeatStatus("Sold", null, null));
        occupied.put(5, new SeatStatus("Sold", null, null));

        MovieSession originalMovieSession = new MovieSession("movieId", "houseId", 1608018488L, prices, occupied);
        when(movieSessionRepository.findById(originalMovieSession.getId())).thenReturn(Optional.of(originalMovieSession));

        //when
        movieSessionService.patch(originalMovieSession.getId(), movieSessionPatchRequest);
        ArgumentCaptor<MovieSession> movieSessionArgumentCaptor = ArgumentCaptor.forClass(MovieSession.class);
        verify(movieSessionRepository, times(1)).save(movieSessionArgumentCaptor.capture());


        //then
        final MovieSession actual = movieSessionArgumentCaptor.getValue();
        assertEquals("in process", actual.getOccupied().get(1).getStatus());
        assertEquals("in process", actual.getOccupied().get(2).getStatus());
        assertEquals("in process", actual.getOccupied().get(3).getStatus());
    }

    @Test
    void should_throw_InvalidSeatUpdateOperationException_when_patch_given_patch_request_requests_contains_seats_that_are_not_available() {
        //given
        MovieSessionPatchRequest movieSessionPatchRequest = new MovieSessionPatchRequest(Stream.of(1, 2, 3).collect(Collectors.toList()), "randomClientSessionId");
        Map<String, Double> prices = new HashMap<>();
        prices.put("Student", 50.0);
        prices.put("Adult", 100.0);

        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(1, new SeatStatus("Sold", null, null));
        occupied.put(4, new SeatStatus("Sold", null, null));
        occupied.put(5, new SeatStatus("Sold", null, null));

        MovieSession originalMovieSession = new MovieSession("movieId", "houseId", 1608018488L, prices, occupied);
        when(movieSessionRepository.findById(originalMovieSession.getId())).thenReturn(Optional.of(originalMovieSession));

        //when
        Exception exception = assertThrows(InvalidSeatUpdateOperationException.class, () -> movieSessionService.patch(originalMovieSession.getId(), movieSessionPatchRequest));

        //then
        assertEquals("Invalid seat update operation", exception.getMessage());
    }

    @Test
    void should_delete_movie_session_when_delete_movie_session_given_valid_movie_session_id() {
        //given
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSession1.setId("1");
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));

        //when
        movieSessionService.delete("1");

        //then
        verify(movieSessionRepository, times(1)).deleteById("1");
    }

    @Test
    void should_throw_MovieSessionNotFoundException_when_delete_movie_session_given_invalid_movie_session_id() {
        assertThrows(MovieNotFoundException.class, () -> {
            movieSessionService.delete("999");
        }, "Movie not found");
    }

}
