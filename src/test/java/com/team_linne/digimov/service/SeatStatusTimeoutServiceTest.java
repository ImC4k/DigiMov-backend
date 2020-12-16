package com.team_linne.digimov.service;

import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.model.SeatStatus;
import com.team_linne.digimov.repository.MovieSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SeatStatusTimeoutServiceTest {
    @InjectMocks
    private SeatStatusTimeoutService seatStatusTimeoutService;

    @Mock
    private MovieSessionService movieSessionService;
    @Mock
    private MovieSessionRepository movieSessionRepository;



    @Test
    void should_auto_remove_seat_when_start_seat_status_countdown_given_seat_status_is_in_process_and_valid_movie_session() {
        //given
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(1, new SeatStatus("in process", 123L, "clientId"));
        occupied.put(4, new SeatStatus("Sold", null, null));
        occupied.put(5, new SeatStatus("Sold", null, null));
        MovieSession movieSession = new MovieSession("mov1", "111", 10000L, new HashMap<>(), occupied);
        seatStatusTimeoutService.setTimeoutInSeconds(1000);
        when(movieSessionService.getById(movieSession.getId())).thenReturn(movieSession);
        //when
        seatStatusTimeoutService.startSeatStatusCountdown(movieSession.getId(), 1);
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArgumentCaptor<MovieSession> movieSessionArgumentCaptor = ArgumentCaptor.forClass(MovieSession.class);
        verify(movieSessionService, times(1)).update(any(), movieSessionArgumentCaptor.capture());

        //then
        MovieSession actual = movieSessionArgumentCaptor.getValue();
        assertFalse(actual.getOccupied().containsKey(1));
    }

    @Test
    void should_not_call_update_when_start_seat_status_countdown_given_seat_status_is_not_in_process_and_valid_movie_session() {
        //given
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(1, new SeatStatus("sold", 123L, "clientId"));
        occupied.put(4, new SeatStatus("sold", null, null));
        occupied.put(5, new SeatStatus("sold", null, null));
        MovieSession movieSession = new MovieSession("mov1", "111", 10000L, new HashMap<>(), occupied);
        seatStatusTimeoutService.setTimeoutInSeconds(1000);
        when(movieSessionService.getById(movieSession.getId())).thenReturn(movieSession);
        //when
        seatStatusTimeoutService.startSeatStatusCountdown(movieSession.getId(), 1);
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //then
        verify(movieSessionService, times(0)).update(any(), any());
    }

    @Test
    void should_not_call_update_when_start_seat_status_countdown_given_seat_index_does_not_exist() {
        //given
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(1, new SeatStatus("sold", 123L, "clientId"));
        occupied.put(4, new SeatStatus("sold", null, null));
        occupied.put(5, new SeatStatus("sold", null, null));
        MovieSession movieSession = new MovieSession("mov1", "111", 10000L, new HashMap<>(), occupied);
        seatStatusTimeoutService.setTimeoutInSeconds(1000);
        when(movieSessionService.getById(movieSession.getId())).thenReturn(movieSession);
        //when
        seatStatusTimeoutService.startSeatStatusCountdown(movieSession.getId(), 999);
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //then
        verify(movieSessionService, times(0)).update(any(), any());
    }
}
