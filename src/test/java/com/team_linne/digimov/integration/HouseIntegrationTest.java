package com.team_linne.digimov.integration;

import com.team_linne.digimov.dto.OrderResponse;
import com.team_linne.digimov.model.*;
import com.team_linne.digimov.repository.*;
import org.junit.jupiter.api.AfterEach;
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
public class HouseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieSessionRepository movieSessionRepository;
    @Autowired
    private OrderRepository orderRepository;

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

    @Test
    public void should_return_created_house_when_create_house_given_complete_new_house_info() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());

        String houseAsJson = "{\n" +
                "    \"cinemaId\": \"" + cinema.getId() + "\",\n" +
                "    \"name\": \"house 1\",\n" +
                "    \"capacity\": \"200\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/houses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(houseAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cinema.id").value(cinema.getId()))
                .andExpect(jsonPath("$.name").value("house 1"))
                .andExpect(jsonPath("$.capacity").value(200));
    }

    @Test
    public void should_return_400_bad_request_when_create_house_given_complete_new_house_info_and_illegal_id() throws Exception {
        String houseAsJson = "{\n" +
                "    \"cinemaId\": \"123\",\n" +
                "    \"name\": \"house 1\",\n" +
                "    \"capacity\": \"200\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/houses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(houseAsJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_created_incomplete_house_when_create_house_given_incomplete_new_house_info() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());

        String houseAsJson = "{\n" +
                "    \"cinemaId\": \"" + cinema.getId() + "\",\n" +
                "    \"name\": \"house 1\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/houses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(houseAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cinema.id").value(cinema.getId()))
                .andExpect(jsonPath("$.name").value("house 1"))
                .andExpect(jsonPath("$.capacity").isEmpty());
    }

    @Test
    public void should_return_updated_house_when_update_house_given_valid_house_id_and_house_update_info() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house1 = new House(cinema.getId(), "house 1", 200);
        houseRepository.save(house1);

        String houseAsJson = "{\n" +
                "    \"cinemaId\": \"" + cinema.getId() + "\",\n" +
                "    \"name\": \"house 2\",\n" +
                "    \"capacity\": \"300\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/houses/" + house1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(houseAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cinema.id").value(cinema.getId()))
                .andExpect(jsonPath("$.name").value("house 2"))
                .andExpect(jsonPath("$.capacity").value(300));
    }

    @Test
    public void should_return_404_not_found_when_update_house_given_invalid_house_id_and_house_update_info() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house1 = new House(cinema.getId(), "house 1", 200);
        houseRepository.save(house1);

        String houseAsJson = "{\n" +
                "    \"cinemaId\": \"" + cinema.getId() + "\",\n" +
                "    \"name\": \"house 2\",\n" +
                "    \"capacity\": \"300\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/houses/5fc8913234ba53396c26a863")
                .contentType(MediaType.APPLICATION_JSON)
                .content(houseAsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_update_house_given_illegal_house_id_and_house_update_info() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house1 = new House(cinema.getId(), "house 1", 200);
        houseRepository.save(house1);

        String houseAsJson = "{\n" +
                "    \"cinemaId\": \"" + cinema.getId() + "\",\n" +
                "    \"name\": \"house 2\",\n" +
                "    \"capacity\": \"300\"\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/houses/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(houseAsJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_delete_house_when_delete_house_given_valid_house_id() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house1 = new House(cinema.getId(), "house 1", 200);
        houseRepository.save(house1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/houses/" + house1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_404_not_found_when_delete_house_given_invalid_house_id() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house1 = new House(cinema.getId(), "house 1", 200);
        houseRepository.save(house1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/houses/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_delete_house_given_illegal_house_id() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house1 = new House(cinema.getId(), "house 1", 200);
        houseRepository.save(house1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/houses/" + "123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_delete_house_movie_session_and_order_when_delete_cinema_given_cinema_id() throws Exception {
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house = houseRepository.save(new House(cinema.getId(), "house 1", 200));
        Movie movie = movieRepository.save(new Movie());
        MovieSession movieSession = movieSessionRepository.save(MovieSession.builder().movieId(movie.getId()).houseId(house.getId()).startTime(System.currentTimeMillis() + 100000).build());
        Order order = orderRepository.save(Order.builder().movieSessionId(movieSession.getId()).build());
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/cinemas/" + cinema.getId()));

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/cinemas/" + cinema.getId()))
                .andExpect(status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.get("/houses/" + house.getId()))
                .andExpect(status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions/" + movieSession.getId()))
                .andExpect(status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + order.getId()))
                .andExpect(status().isNotFound());
    }
}
