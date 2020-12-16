package com.team_linne.digimov.integration;

import com.team_linne.digimov.dto.MovieSessionPatchRequest;
import com.team_linne.digimov.model.*;
import com.team_linne.digimov.repository.CinemaRepository;
import com.team_linne.digimov.repository.HouseRepository;
import com.team_linne.digimov.repository.MovieRepository;
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

import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"timeout.value=1000"})
@AutoConfigureMockMvc
public class MovieSessionIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieSessionRepository movieSessionRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private MovieRepository movieRepository;

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
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions"))
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

        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions/" + movieSession.getId()))
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
        mockMvc.perform(MockMvcRequestBuilders.post("/movie_sessions")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/movie_sessions")
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
        mockMvc.perform(MockMvcRequestBuilders.put("/movie_sessions/" + movieSession1.getId())
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

        mockMvc.perform(MockMvcRequestBuilders.put("/movie_sessions/" + "5fc8913234ba53396c26a863")
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

        mockMvc.perform(MockMvcRequestBuilders.put("/movie_sessions/" + "zxxxcc")
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
        mockMvc.perform(MockMvcRequestBuilders.patch("/movie_sessions/" + inserted.getId())
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
        mockMvc.perform(MockMvcRequestBuilders.patch("/movie_sessions/" + inserted.getId())
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
        mockMvc.perform(MockMvcRequestBuilders.patch("/movie_sessions/" + "5fc8913234ba53396c26a863")
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
        mockMvc.perform(MockMvcRequestBuilders.patch("/movie_sessions/" + inserted.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionPatchRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_allow_other_client_session_id_update_seat_status_when_patch_given_first_client_session_timed_out() throws Exception {
        //given
        MovieSession movieSession = new MovieSession("mov1", "hse1", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        MovieSession inserted = movieSessionRepository.insert(movieSession);

        String movieSessionPatchRequest = "{\n" +
                "    \"bookedSeatIndices\": [1],\n" +
                "    \"clientSessionId\": \"qwerty\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/movie_sessions/" + inserted.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionPatchRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.movieId").value("mov1"))
                .andExpect(jsonPath("$.houseId").value("hse1"))
                .andExpect(jsonPath("$.startTime").value("10000"))
                .andExpect(jsonPath("$.prices").isEmpty())
                .andExpect(jsonPath("$.occupied.1.status").value("in process"));

        sleep(1500);

        //when
        //then
        String new_movieSessionPatchRequest = "{\n" +
                "    \"bookedSeatIndices\": [1],\n" +
                "    \"clientSessionId\": \"qaazx\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/movie_sessions/" + inserted.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new_movieSessionPatchRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.movieId").value("mov1"))
                .andExpect(jsonPath("$.houseId").value("hse1"))
                .andExpect(jsonPath("$.startTime").value("10000"))
                .andExpect(jsonPath("$.prices").isEmpty())
                .andExpect(jsonPath("$.occupied.1.status").value("in process"));
    }

    @Test
    void should_allow_other_client_session_id_update_seat_status_when_patch_given_first_client_session_not_timed_out_yet() throws Exception {
        //given
        MovieSession movieSession = new MovieSession("mov1", "hse1", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        MovieSession inserted = movieSessionRepository.insert(movieSession);

        String movieSessionPatchRequest = "{\n" +
                "    \"bookedSeatIndices\": [1],\n" +
                "    \"clientSessionId\": \"qwerty\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/movie_sessions/" + inserted.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieSessionPatchRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.movieId").value("mov1"))
                .andExpect(jsonPath("$.houseId").value("hse1"))
                .andExpect(jsonPath("$.startTime").value("10000"))
                .andExpect(jsonPath("$.prices").isEmpty())
                .andExpect(jsonPath("$.occupied.1.status").value("in process"));

        //when
        //then
        String new_movieSessionPatchRequest = "{\n" +
                "    \"bookedSeatIndices\": [1],\n" +
                "    \"clientSessionId\": \"qaazx\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/movie_sessions/" + inserted.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new_movieSessionPatchRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void should_delete_movie_when_delete_movie_given_valid_movie_id() throws Exception {
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSessionRepository.insert(movieSession1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/movie_sessions/" + movieSession1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_404_not_found_when_delete_movie_session_given_invalid_movie_id() throws Exception {
        //given
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSessionRepository.insert(movieSession1);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/movie_sessions/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_delete_movie_session_given_invalid_movie_id() throws Exception {
        //given
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, new HashMap<String, Double>(), new HashMap<Integer, SeatStatus>());
        movieSessionRepository.insert(movieSession1);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/movie_sessions/" + "vbcxbcxb"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_movie_sessions_with_matching_cinema_id_when_get_all_by_cinema_id_given_movie_session_repository_contains_movie_sessions_and_valid_cinema_id() throws Exception {
        //given
        Cinema cinema1 = cinemaRepository.save(new Cinema());
        Cinema cinema2 = cinemaRepository.save(new Cinema());

        House house1 = houseRepository.save(new House(cinema1.getId(), "", null));
        House house2 = houseRepository.save(new House(cinema1.getId(), "", null));
        House house3 = houseRepository.save(new House(cinema2.getId(), "", null));

        Movie movie = movieRepository.save(new Movie());

        MovieSession movieSession1 = movieSessionRepository.save(MovieSession.builder().movieId(movie.getId()).houseId(house1.getId()).build());
        MovieSession movieSession2 = movieSessionRepository.save(MovieSession.builder().movieId(movie.getId()).houseId(house2.getId()).build());
        MovieSession movieSession3 = movieSessionRepository.save(MovieSession.builder().movieId(movie.getId()).houseId(house3.getId()).build());

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions?cinema=" + cinema1.getId()))
//                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].houseId").value(house1.getId()))
                .andExpect(jsonPath("$[1].houseId").value(house2.getId()))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void should_return_movie_sessions_with_matching_cinema_id_and_start_time_later_than_now_when_get_upcoming_movie_sessions_by_cinema_id_given_movie_session_repository_contains_movie_sessions_and_cinema_id() throws Exception {
        //given
        Cinema cinema1 = cinemaRepository.save(new Cinema());
        Cinema cinema2 = cinemaRepository.save(new Cinema());

        House house1 = houseRepository.save(new House(cinema1.getId(), "", null));
        House house2 = houseRepository.save(new House(cinema1.getId(), "", null));
        House house3 = houseRepository.save(new House(cinema2.getId(), "", null));

        Movie movie = movieRepository.save(new Movie());

        MovieSession movieSession1 = movieSessionRepository.save(MovieSession.builder().movieId(movie.getId()).houseId(house1.getId()).startTime(System.currentTimeMillis() + 100000).build());
        MovieSession movieSession2 = movieSessionRepository.save(MovieSession.builder().movieId(movie.getId()).houseId(house2.getId()).startTime(System.currentTimeMillis() - 100000).build());
        MovieSession movieSession3 = movieSessionRepository.save(MovieSession.builder().movieId(movie.getId()).houseId(house3.getId()).build());

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions?cinema=" + cinema1.getId() + "&sessionStatus=upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movieSession1.getId()))
                .andExpect(jsonPath("$[0].houseId").value(house1.getId()))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void should_return_404NotFound_when_get_all_by_cinema_id_given_invalid_cinema_id() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions?cinema=" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_404NotFound_when_get_upcoming_movie_sessions_by_cinema_id_given_invalid_cinema_id() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions?cinema=" + "5fc8913234ba53396c26a863" + "&sessionStatus=upcoming"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_movie_sessions_with_matching_movie_id_when_get_all_by_movie_id_given_movie_session_repository_contains_movie_sessions_and_valid_movie_id() throws Exception {
        //given
        Movie movie1 = movieRepository.save(new Movie());
        Movie movie2 = movieRepository.save(new Movie());

        MovieSession movieSession1 = movieSessionRepository.save(MovieSession.builder().movieId(movie1.getId()).build());
        MovieSession movieSession2 = movieSessionRepository.save(MovieSession.builder().movieId(movie1.getId()).build());
        MovieSession movieSession3 = movieSessionRepository.save(MovieSession.builder().movieId(movie2.getId()).build());

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions?movie=" + movie1.getId()))
//                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(movie1.getId()))
                .andExpect(jsonPath("$[1].movieId").value(movie1.getId()))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void should_return_movie_sessions_with_matching_movie_id_and_start_time_later_than_now_when_get_upcoming_movie_sessions_by_movie_id_given_movie_session_repository_contains_movie_sessions_and_movie_id() throws Exception {
        //given
        Movie movie1 = movieRepository.save(new Movie());
        Movie movie2 = movieRepository.save(new Movie());

        MovieSession movieSession1 = movieSessionRepository.save(MovieSession.builder().movieId(movie1.getId()).startTime(System.currentTimeMillis() + 100000).build());
        MovieSession movieSession2 = movieSessionRepository.save(MovieSession.builder().movieId(movie1.getId()).startTime(System.currentTimeMillis() - 100000).build());
        MovieSession movieSession3 = movieSessionRepository.save(MovieSession.builder().movieId(movie2.getId()).build());

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions?movie=" + movie1.getId() + "&sessionStatus=upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movieSession1.getId()))
                .andExpect(jsonPath("$[0].movieId").value(movie1.getId()))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void should_return_404NotFound_when_get_all_by_movie_id_given_invalid_movie_id() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions?cinema=" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_404NotFound_when_get_upcoming_movie_sessions_by_movie_id_given_invalid_movie_id() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions?movie=" + "5fc8913234ba53396c26a863" + "&sessionStatus=upcoming"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_delete_movie_session_when_delete_house_given_movie_session_with_house_id() throws Exception {
        Movie movie = movieRepository.save(new Movie());
        House house = houseRepository.save(new House());
        MovieSession movieSession1 = movieSessionRepository.save(MovieSession.builder().movieId(movie.getId()).houseId(house.getId()).startTime(System.currentTimeMillis() + 100000).build());

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/houses/" + house.getId()));

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions/" + movieSession1.getId()))
                .andExpect(status().isNotFound());
    }
}
