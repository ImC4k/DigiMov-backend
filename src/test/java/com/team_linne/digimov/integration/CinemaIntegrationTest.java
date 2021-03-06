package com.team_linne.digimov.integration;

import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.repository.CinemaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    @BeforeEach
    void tearDown() {
        cinemaRepository.deleteAll();
    }

    @Test
    public void should_return_all_cinemas_when_get_all_cinemas_given_cinemas() throws Exception {
        Cinema cinema1 = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00", "12345678");
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

    @Test
    public void should_return_specific_cinema_when_get_cinema_given_valid_cinema_id() throws Exception {
        Cinema cinema1 = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00", "12345678");
        cinemaRepository.save(cinema1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/cinemas/" + cinema1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("cinema 1"))
                .andExpect(jsonPath("$.address").value("hong kong"))
                .andExpect(jsonPath("$.imageUrl").value("cinema1.jpg"))
                .andExpect(jsonPath("$.openingHours").value("8:00-23:00"))
                .andExpect(jsonPath("$.hotline").value("12345678"));
    }

    @Test
    public void should_return_404NotFound_when_get_cinema_given_invalid_cinema_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/cinemas/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_get_cinema_given_illegal_cinema_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/cinemas/" + "123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_created_cinema_when_create_cinema_given_complete_new_cinema_info() throws Exception {
        String cinemaAsJson = "{\n" +
                "    \"name\": \"cinema 1\",\n" +
                "    \"address\": \"hong kong\",\n" +
                "    \"openingHours\": \"8:00-23:00\",\n" +
                "    \"hotline\": \"12345678\",\n" +
                "    \"imageUrl\": \"cinema1.jpg\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cinemaAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("cinema 1"))
                .andExpect(jsonPath("$.address").value("hong kong"))
                .andExpect(jsonPath("$.imageUrl").value("cinema1.jpg"))
                .andExpect(jsonPath("$.openingHours").value("8:00-23:00"))
                .andExpect(jsonPath("$.hotline").value("12345678"));
    }

    @Test
    public void should_return_created_incompleted_cinema_when_create_cinema_given_incomplete_new_cinema_info() throws Exception {
        String cinemaAsJson = "{\n" +
                "    \"name\": \"cinema 1\",\n" +
                "    \"address\": \"hong kong\",\n" +
                "    \"imageUrl\": \"cinema1.jpg\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cinemaAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("cinema 1"))
                .andExpect(jsonPath("$.address").value("hong kong"))
                .andExpect(jsonPath("$.imageUrl").value("cinema1.jpg"))
                .andExpect(jsonPath("$.openingHours").isEmpty())
                .andExpect(jsonPath("$.hotline").isEmpty());
    }

    @Test
    public void should_return_updated_cinema_when_update_cinema_given_valid_cinema_id_and_cinema_update_info() throws Exception {
        Cinema cinema1 = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00", "12345678");
        cinemaRepository.save(cinema1);
        String cinemaAsJson = "{\n" +
                "    \"name\": \"cinema 1 updated\",\n" +
                "    \"address\": \"hong kong island\",\n" +
                "    \"openingHours\": \"8:00-23:00\",\n" +
                "    \"hotline\": \"12345678\",\n" +
                "    \"imageUrl\": \"cinema1.jpg\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/cinemas/" + cinema1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(cinemaAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("cinema 1 updated"))
                .andExpect(jsonPath("$.address").value("hong kong island"))
                .andExpect(jsonPath("$.imageUrl").value("cinema1.jpg"))
                .andExpect(jsonPath("$.openingHours").value("8:00-23:00"))
                .andExpect(jsonPath("$.hotline").value("12345678"));
    }

    @Test
    public void should_return_404NotFound_when_update_cinema_given_invalid_cinema_id_and_cinema_update_info() throws Exception {
        Cinema cinema1 = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00", "12345678");
        cinemaRepository.save(cinema1);
        String cinemaAsJson = "{\n" +
                "    \"name\": \"cinema 1 updated\",\n" +
                "    \"address\": \"hong kong island\",\n" +
                "    \"openingHours\": \"8:00-23:00\",\n" +
                "    \"hotline\": \"12345678\",\n" +
                "    \"imageUrl\": \"cinema1.jpg\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/cinemas/" + "5fc8913234ba53396c26a863")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cinemaAsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_update_cinema_given_illegal_cinema_id_and_cinema_update_info() throws Exception {
        Cinema cinema1 = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00", "12345678");
        cinemaRepository.save(cinema1);
        String cinemaAsJson = "{\n" +
                "    \"name\": \"cinema 1 updated\",\n" +
                "    \"address\": \"hong kong island\",\n" +
                "    \"openingHours\": \"8:00-23:00\",\n" +
                "    \"hotline\": \"12345678\",\n" +
                "    \"imageUrl\": \"cinema1.jpg\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/cinemas/" + "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cinemaAsJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_delete_cinema_when_delete_cinema_given_valid_cinema_id() throws Exception {
        Cinema cinema1 = new Cinema("cinema 1", "hong kong", "cinema1.jpg", "8:00-23:00", "12345678");
        cinemaRepository.save(cinema1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/cinemas/" + cinema1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_404NotFound_when_delete_cinema_given_invalid_cinema_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/cinemas/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_delete_cinema_given_illegal_cinema_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/cinemas/" + "123"))
                .andExpect(status().isBadRequest());
    }
}
