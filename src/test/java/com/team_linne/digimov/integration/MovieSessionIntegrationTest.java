package com.team_linne.digimov.integration;

import com.team_linne.digimov.dto.MovieSessionPatchRequest;
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

@SpringBootTest(properties = {"timeout.value=1000"})
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
    void should_return_updated_movie_session_which_seat_indices_are_in_process_when_patch_given_original_seat_indices_were_available_and_valid_movie_session_id() throws Exception {
        //given
        MovieSession movieSession = new MovieSession("mov1", "hse1", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        MovieSession inserted = movieSessionRepository.insert(movieSession);

        String movieSessionPatchRequest = "{\n" +
                "    \"bookedSeatIndices\": [1,2],\n" +
                "    \"clientSessionId\": \"qwerty\"\n" +
                "}";

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/moviesessions/" + inserted.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionPatchRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.movieId").value("mov1"))
                .andExpect(jsonPath("$.houseId").value("hse1"))
                .andExpect(jsonPath("$.startTime").value("10000"))
                .andExpect(jsonPath("$.prices").isEmpty())
                .andExpect(jsonPath("$.occupied.1.status").value("in process"))
                .andExpect(jsonPath("$.occupied.2.status").value("in process"));
    }

    @Test
    void should_return_403_forbidden_when_patch_given_original_seat_indices_were_sold_and_valid_movie_session_id() throws Exception {
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(1, new SeatStatus("sold", 10000L, "qazwsxedc"));
        occupied.put(2, new SeatStatus("sold", 10000L, "qazwsxedc"));

        MovieSession movieSession = new MovieSession("mov1", "hse1", 10000L, new HashMap<String, Double>(), occupied);
        MovieSession inserted = movieSessionRepository.insert(movieSession);

        String movieSessionPatchRequest = "{\n" +
                "    \"bookedSeatIndices\": [1,2],\n" +
                "    \"clientSessionId\": \"qwerty\"\n" +
                "}";

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/moviesessions/" + inserted.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionPatchRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    void should_return_404_not_found_when_patch_given_invalid_movie_session_id() throws Exception {
        //given
        MovieSession movieSession = new MovieSession("mov1", "hse1", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        MovieSession inserted = movieSessionRepository.insert(movieSession);

        //when
        String movieSessionPatchRequest = "{\n" +
                "    \"bookedSeatIndices\": [1,2],\n" +
                "    \"clientSessionId\": \"qwerty\"\n" +
                "}";

        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/moviesessions/" + "5fc8913234ba53396c26a863")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionPatchRequest))
                .andExpect(status().isNotFound());

    }

    @Test
    void should_return_401_unauthorized_when_patch_given_original_seat_indices_were_in_process_and_valid_movie_session_id_but_client_session_id_does_not_match() throws Exception {
        //given
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(1, new SeatStatus("in process", 10000L, "qazwsxedc"));
        occupied.put(2, new SeatStatus("in process", 10000L, "qazwsxedc"));

        MovieSession movieSession = new MovieSession("mov1", "hse1", 10000L, new HashMap<String, Double>(), occupied);
        MovieSession inserted = movieSessionRepository.insert(movieSession);

        //when
        String movieSessionPatchRequest = "{\n" +
                "    \"bookedSeatIndices\": [1,2],\n" +
                "    \"clientSessionId\": \"qwerty\"\n" +
                "}";
        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/moviesessions/" + inserted.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionPatchRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_return_movie_session_with_seat_indices_removed_from_occupied_when_patch_given_update_to_in_process_was_successful_but_timed_out() {
        //given


        //when


        //then
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
