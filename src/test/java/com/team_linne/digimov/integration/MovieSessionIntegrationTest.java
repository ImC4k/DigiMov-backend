package com.team_linne.digimov.integration;

import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.model.SeatStatus;
import com.team_linne.digimov.repository.MovieSessionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieSessionIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MovieSessionRepository movieSessionRepository;

    @AfterEach
    void tearDown() {
        movieSessionRepository.deleteAll();
    }

//    @Test
//    public void should_return_all_movie_session_when_get_all_given_movie_session_list() {
//        //given
//        Map<String, Double> prices = new HashMap<>();
//        prices.put("Student", 50.0);
//        prices.put("Adult", 100.0);
//
//        Map<Integer, SeatStatus> occupied = new HashMap<>();
////        occupied.put(1, new SeatStatus(""))
//
//        MovieSession movieSession = new MovieSession("123", 10000L, prices, );
//
//        //when
//
//
//        //then
//
//    }

}
