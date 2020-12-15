package com.team_linne.digimov.service;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
    

}
