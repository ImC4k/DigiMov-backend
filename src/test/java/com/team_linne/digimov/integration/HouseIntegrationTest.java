package com.team_linne.digimov.integration;

import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.model.House;
import com.team_linne.digimov.repository.CinemaRepository;
import com.team_linne.digimov.repository.HouseRepository;
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
public class HouseIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    CinemaRepository cinemaRepository;

    @AfterEach
    void tearDown() {
        houseRepository.deleteAll();
    }

    @Test
    public void should_return_all_houses_when_get_all_houses_given_houses() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());

        House house1 = new House(cinema.getId(), "house 1", 200);
        houseRepository.save(house1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/houses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cinema.id").value(cinema.getId()))
                .andExpect(jsonPath("$[0].name").value("house 1"))
                .andExpect(jsonPath("$[0].capacity").value(200));
    }
}
