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

    @Test
    public void should_return_specific_house_when_get_house_given_valid_house_id() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());

        House house1 = new House(cinema.getId(), "house 1", 200);
        houseRepository.save(house1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/houses/" + house1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cinema.id").value(cinema.getId()))
                .andExpect(jsonPath("$.name").value("house 1"))
                .andExpect(jsonPath("$.capacity").value(200));
    }

    @Test
    public void should_return_404_not_found_when_get_house_given_invalid_house_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/houses/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_get_house_given_illegal_house_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/houses/" + "123"))
                .andExpect(status().isBadRequest());
    }
}
