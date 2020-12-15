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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    public void should_return_all_movie_session_when_get_all_given_movie_session_list() throws Exception {
        //given
        Map<String, Double> prices = new HashMap<>();
        prices.put("Student", 50.0);
        prices.put("Adult", 100.0);

        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(1, new SeatStatus("Available", 10000L, "555"));
        occupied.put(2, new SeatStatus("Sold", 10000L, "555"));

        MovieSession movieSession = new MovieSession("mov1", "123", 10000L, prices, occupied);

        movieSessionRepository.save(movieSession);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/moviesessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].movieId").value("mov1"))
                .andExpect(jsonPath("$[0].houseId").value("123"))
                .andExpect(jsonPath("$[0].startTime").value(10000L))
                .andExpect(jsonPath("$[0].prices").isMap())
                .andExpect(jsonPath("$[0].occupied").isMap());
    }

}
