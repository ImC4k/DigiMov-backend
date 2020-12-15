package com.team_linne.digimov.integration;

import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.model.SeatStatus;
import com.team_linne.digimov.repository.MovieSessionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    @Test
    public void should_return_specific_movie_session_when_get_movie_session_given_valid_movie_session_id() throws Exception {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Student", 50.0);
        prices.put("Adult", 100.0);

        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(1, new SeatStatus("Available", 10000L, "555"));
        occupied.put(2, new SeatStatus("Sold", 10000L, "555"));

        MovieSession movieSession = new MovieSession("mov1", "123", 10000L, prices, occupied);

        movieSessionRepository.save(movieSession);

        mockMvc.perform(MockMvcRequestBuilders.get("/moviesessions/" + movieSession.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.movieId").value("mov1"))
                .andExpect(jsonPath("$.houseId").value("123"))
                .andExpect(jsonPath("$.startTime").value(10000L))
                .andExpect(jsonPath("$.prices").isMap())
                .andExpect(jsonPath("$.occupied").isMap());
    }

    @Test
    public void should_return_404_not_found_when_get_movie_session_given_invalid_session_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/movies/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_get_movie_session_given_illegal_session_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/movies/" + "123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_created_movie_session_when_create_movie_session_given_complete_new_movie_session_info() throws Exception {
        String movieSessionAsJson = "{\n" +
                "    \"movieId\": \"mov1\",\n" +
                "    \"houseId\": \"house1\",\n" +
                "    \"startTime\": 10000,\n" +
                "    \"prices\": {\n" +
                "        \"Elderly\": 25.0,\n" +
                "        \"Adult\": 100.0\n" +
                "    },\n" +
                "    \"occupied\": {\n" +
                "        \"1\" : {\n" +
                "            \"status\": \"Sold\",\n" +
                "            \"processStartTime\": 1000,\n" +
                "            \"clientSessionId\": \"r32rewfge\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/moviesessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.movieId").value("mov1"))
                .andExpect(jsonPath("$.houseId").value("house1"))
                .andExpect(jsonPath("$.startTime").value(10000L))
                .andExpect(jsonPath("$.prices").isMap())
                .andExpect(jsonPath("$.occupied").isMap());
    }

    @Test
    public void should_return_created_incomplete_movie_session_when_create_movie_session_given_incomplete_new_movie_session_info() throws Exception {
        String movieSessionAsJson = "{\n" +
                "    \"movieId\": \"mov1\",\n" +
                "    \"houseId\": \"house1\",\n" +
                "    \"prices\": {\n" +
                "        \"Elderly\": 25.0,\n" +
                "        \"Adult\": 100.0\n" +
                "    }\n" +
                "}";


        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/moviesessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.movieId").value("mov1"))
                .andExpect(jsonPath("$.houseId").value("house1"))
                .andExpect(jsonPath("$.startTime").isEmpty())
                .andExpect(jsonPath("$.prices").isMap())
                .andExpect(jsonPath("$.occupied").isEmpty());
    }

    @Test
    public void should_return_updated_movie_session_when_update_movie_given_valid_movie_session_id_and_movie_session_update_info() throws Exception {
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSessionRepository.insert(movieSession1);

        String movieSessionAsJson = "{\n" +
                "    \"movieId\": \"mov1\",\n" +
                "    \"houseId\": \"house1\",\n" +
                "    \"startTime\": 10000,\n" +
                "    \"prices\": {\n" +
                "        \"Elderly\": 25.0,\n" +
                "        \"Adult\": 100.0\n" +
                "    },\n" +
                "    \"occupied\": {\n" +
                "        \"1\" : {\n" +
                "            \"status\": \"Sold\",\n" +
                "            \"processStartTime\": 1000,\n" +
                "            \"clientSessionId\": \"r32rewfge\"\n" +
                "        }\n" +
                "    }\n" +
                "}";


        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/moviesessions/" + movieSession1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.movieId").value("mov1"))
                .andExpect(jsonPath("$.houseId").value("house1"))
                .andExpect(jsonPath("$.startTime").value(10000L))
                .andExpect(jsonPath("$.prices").isMap())
                .andExpect(jsonPath("$.occupied").isMap());
    }

    @Test
    public void should_return_404_not_found_when_update_movie_session_given_invalid_movie_session_id_and_movie_session_update_info() throws Exception {
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSessionRepository.insert(movieSession1);

        String movieSessionAsJson = "{\n" +
                "    \"movieId\": \"mov1\",\n" +
                "    \"houseId\": \"house1\",\n" +
                "    \"startTime\": 10000,\n" +
                "    \"prices\": {\n" +
                "        \"Elderly\": 25.0,\n" +
                "        \"Adult\": 100.0\n" +
                "    },\n" +
                "    \"occupied\": {\n" +
                "        \"1\" : {\n" +
                "            \"status\": \"Sold\",\n" +
                "            \"processStartTime\": 1000,\n" +
                "            \"clientSessionId\": \"r32rewfge\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/moviesessions/" + "5fc8913234ba53396c26a863")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionAsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_update_movie_session_given_invalid_movie_session_id_and_movie_session_update_info() throws Exception {
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSessionRepository.insert(movieSession1);

        String movieSessionAsJson = "{\n" +
                "    \"movieId\": \"mov1\",\n" +
                "    \"houseId\": \"house1\",\n" +
                "    \"startTime\": 10000,\n" +
                "    \"prices\": {\n" +
                "        \"Elderly\": 25.0,\n" +
                "        \"Adult\": 100.0\n" +
                "    },\n" +
                "    \"occupied\": {\n" +
                "        \"1\" : {\n" +
                "            \"status\": \"Sold\",\n" +
                "            \"processStartTime\": 1000,\n" +
                "            \"clientSessionId\": \"r32rewfge\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/moviesessions/" + "zxxxcc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionAsJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_delete_movie_when_delete_movie_given_valid_movie_id() throws Exception {
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSessionRepository.insert(movieSession1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/moviesessions/" + movieSession1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_404_not_found_when_delete_movie_session_given_invalid_movie_id() throws Exception {
        //given
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSessionRepository.insert(movieSession1);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/moviesessions/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_delete_movie_session_given_invalid_movie_id() throws Exception {
        //given
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSessionRepository.insert(movieSession1);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/moviesessions/" + "vbcxbcxb"))
                .andExpect(status().isBadRequest());
    }

}
