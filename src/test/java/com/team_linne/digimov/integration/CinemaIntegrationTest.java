package com.team_linne.digimov.integration;

import com.jayway.jsonpath.JsonPath;
import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.repository.CinemaRepository;
import com.team_linne.digimov.service.CinemaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CinemaIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    CinemaRepository cinemaRepository;

    @AfterEach
    void tearDown() {
        cinemaRepository.deleteAll();
    }

    @Test
    public void should_return_all_cinemas_when_getAll_given_cinemas() throws Exception {
        Cinema cinema1 = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00","12345678");
        cinemaRepository.save(cinema1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/cinemas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].name").value("cinema 1"))
                .andExpect(jsonPath("$[0].address").value("hong kong"))
                .andExpect(jsonPath("$[0].imageUrl").value("cinema1.jpg"))
                .andExpect(jsonPath("$[0].openingHours").value("8:00-23:00"))
                .andExpect(jsonPath("$[0].hotline").value("12345678"));
    }
}
