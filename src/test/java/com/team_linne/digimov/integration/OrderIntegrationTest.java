package com.team_linne.digimov.integration;

import com.team_linne.digimov.exception.*;
import com.team_linne.digimov.model.*;
import com.team_linne.digimov.repository.*;
import com.team_linne.digimov.service.OrderService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderIntegrationTest {
    @Autowired
    private
    MockMvc mockMvc;
    @Autowired
    private
    OrderRepository orderRepository;
    @Autowired
    private
    MovieSessionRepository movieSessionRepository;
    @Autowired
    private
    OrderService orderService;
    @Autowired
    private
    MovieRepository movieRepository;
    @Autowired
    private
    CinemaRepository cinemaRepository;
    @Autowired
    private
    HouseRepository houseRepository;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        movieSessionRepository.deleteAll();
    }

    public static final String IN_PROCESS = "in process";
    public static final String SOLD = "sold";

    @Test
    public void should_return_all_order_when_get_all_orders_given_orders() throws Exception {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        Movie movie = movieRepository.save(new Movie());
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house = houseRepository.save(House.builder().cinemaId(cinema.getId()).build());
        MovieSession movieSession1 = new MovieSession(movie.getId(), house.getId(), 10000L, prices, occupied);
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        movieSessionRepository.save(movieSession1);
        Order order = new Order("abc@bbc.com", movieSession1.getId(), Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        orderService.create(order, creditCardInfo, clientSessionId);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].email").value("abc@bbc.com"))
                .andExpect(jsonPath("$[0].bookedSeatIndices[0]").value("14"))
                .andExpect(jsonPath("$[0].bookedSeatIndices[1]").value("15"))
                .andExpect(jsonPath("$[0].customerGroupQuantityMap", Matchers.hasKey("Adult")))
                .andExpect(jsonPath("$[0].customerGroupQuantityMap", Matchers.hasKey("Student")))
                .andExpect(jsonPath("$[0].creditCardNumber").value("5105105105105100"))
                .andExpect(jsonPath("$[0].movieSession").isNotEmpty());
    }

    @Test
    public void should_return_specific_order_when_get_order_given_orders() throws Exception {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        Movie movie = movieRepository.save(new Movie());
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house = houseRepository.save(House.builder().cinemaId(cinema.getId()).build());
        MovieSession movieSession1 = new MovieSession(movie.getId(), house.getId(), 10000L, prices, occupied);
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        movieSessionRepository.save(movieSession1);
        Order order = new Order("abc@bbc.com", movieSession1.getId(), Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        orderService.create(order, creditCardInfo, clientSessionId);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.email").value("abc@bbc.com"))
                .andExpect(jsonPath("$.bookedSeatIndices[0]").value("14"))
                .andExpect(jsonPath("$.bookedSeatIndices[1]").value("15"))
                .andExpect(jsonPath("$.customerGroupQuantityMap", Matchers.hasKey("Adult")))
                .andExpect(jsonPath("$.customerGroupQuantityMap", Matchers.hasKey("Student")))
                .andExpect(jsonPath("$.creditCardNumber").value("5105105105105100"))
                .andExpect(jsonPath("$.movieSession").isNotEmpty());
    }

    @Test
    public void should_return_404_not_found_when_get_order_given_invalid_order_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + "5fc8913234ba53396c26a863"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_bad_request_when_get_order_given_illegal_order_id() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + "123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_created_order_when_create_order_given_valid_movie_session_id_and_valid_credit_card_info_and_seats_are_available() throws Exception {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        Movie movie = movieRepository.save(new Movie());
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house = houseRepository.save(House.builder().cinemaId(cinema.getId()).build());
        MovieSession movieSession1 = new MovieSession(movie.getId(), house.getId(), 10000L, prices, occupied);
        movieSession1.setId("5fc8913234ba53396c26a863");
        movieSessionRepository.save(movieSession1);
        String orderAsJson = "{\n" +
                "    \"movieSessionId\": \"5fc8913234ba53396c26a863\",\n" +
                "    \"bookedSeatIndices\": [\n" +
                "        14,\n" +
                "        15\n" +
                "    ],\n" +
                "    \"customerGroupQuantityMap\": {\n" +
                "        \"Adult\": 2,\n" +
                "        \"Student\": 2\n" +
                "    },\n" +
                "    \"email\": \"abc@bbc.com\",\n" +
                "    \"creditCardInfo\": {\n" +
                "        \"number\": \"5105105105105100\",\n" +
                "        \"expiryDate\": {\n" +
                "            \"month\": \"4\",\n" +
                "            \"year\": \"2043\"\n" +
                "        },\n" +
                "        \"cvv\": 460,\n" +
                "        \"holderName\": \"Jackie\"\n" +
                "    },\n" +
                "    \"clientSessionId\": \"123456\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.email").value("abc@bbc.com"))
                .andExpect(jsonPath("$.bookedSeatIndices[0]").value("14"))
                .andExpect(jsonPath("$.bookedSeatIndices[1]").value("15"))
                .andExpect(jsonPath("$.customerGroupQuantityMap", Matchers.hasKey("Adult")))
                .andExpect(jsonPath("$.customerGroupQuantityMap", Matchers.hasKey("Student")))
                .andExpect(jsonPath("$.creditCardNumber").value("5105105105105100"))
                .andExpect(jsonPath("$.movieSession").isNotEmpty());
    }

    @Test
    public void should_400_return_bad_request_when_create_order_given_non_exist_ticket_type_in_movie_session() throws Exception {
        Map<String, Double> prices = new HashMap<>();
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("5fc8913234ba53396c26a863");
        movieSessionRepository.save(movieSession1);
        String orderAsJson = "{\n" +
                "    \"movieSessionId\": \"5fc8913234ba53396c26a863\",\n" +
                "    \"bookedSeatIndices\": [\n" +
                "        14,\n" +
                "        15\n" +
                "    ],\n" +
                "    \"customerGroupQuantityMap\": {\n" +
                "        \"Adult\": 2,\n" +
                "        \"Student\": 2\n" +
                "    },\n" +
                "    \"email\": \"abc@bbc.com\",\n" +
                "    \"creditCardInfo\": {\n" +
                "        \"number\": \"5105105105105100\",\n" +
                "        \"expiryDate\": {\n" +
                "            \"month\": \"4\",\n" +
                "            \"year\": \"2043\"\n" +
                "        },\n" +
                "        \"cvv\": 460,\n" +
                "        \"holderName\": \"Jackie\"\n" +
                "    },\n" +
                "    \"clientSessionId\": \"123456\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidCustomerGroupException));
    }

    @Test
    public void should_404_not_found_when_create_order_given_invalid_movie_session_id() throws Exception {
        String orderAsJson = "{\n" +
                "    \"movieSessionId\": \"5fc8913234ba53396c26a863\",\n" +
                "    \"bookedSeatIndices\": [\n" +
                "        14,\n" +
                "        15\n" +
                "    ],\n" +
                "    \"customerGroupQuantityMap\": {\n" +
                "        \"Adult\": 2,\n" +
                "        \"Student\": 2\n" +
                "    },\n" +
                "    \"email\": \"abc@bbc.com\",\n" +
                "    \"creditCardInfo\": {\n" +
                "        \"number\": \"5105105105105100\",\n" +
                "        \"expiryDate\": {\n" +
                "            \"month\": \"4\",\n" +
                "            \"year\": \"2043\"\n" +
                "        },\n" +
                "        \"cvv\": 460,\n" +
                "        \"holderName\": \"Jackie\"\n" +
                "    },\n" +
                "    \"clientSessionId\": \"123456\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MovieSessionNotFoundException));
    }

    @Test
    public void should_return_400_bad_request_when_create_order_given_invalid_credit_card_number() throws Exception {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("5fc8913234ba53396c26a863");
        movieSessionRepository.save(movieSession1);
        String orderAsJson = "{\n" +
                "    \"movieSessionId\": \"5fc8913234ba53396c26a863\",\n" +
                "    \"bookedSeatIndices\": [\n" +
                "        14,\n" +
                "        15\n" +
                "    ],\n" +
                "    \"customerGroupQuantityMap\": {\n" +
                "        \"Adult\": 2,\n" +
                "        \"Student\": 2\n" +
                "    },\n" +
                "    \"email\": \"abc@bbc.com\",\n" +
                "    \"creditCardInfo\": {\n" +
                "        \"number\": \"123\",\n" +
                "        \"expiryDate\": {\n" +
                "            \"month\": \"4\",\n" +
                "            \"year\": \"2043\"\n" +
                "        },\n" +
                "        \"cvv\": 460,\n" +
                "        \"holderName\": \"Jackie\"\n" +
                "    },\n" +
                "    \"clientSessionId\": \"123456\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidCreditCardInfoException));
    }

    @Test
    public void should_return_400_bad_request_when_create_order_given_valid_credit_card_number_but_expired() throws Exception {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("5fc8913234ba53396c26a863");
        movieSessionRepository.save(movieSession1);
        String orderAsJson = "{\n" +
                "    \"movieSessionId\": \"5fc8913234ba53396c26a863\",\n" +
                "    \"bookedSeatIndices\": [\n" +
                "        14,\n" +
                "        15\n" +
                "    ],\n" +
                "    \"customerGroupQuantityMap\": {\n" +
                "        \"Adult\": 2,\n" +
                "        \"Student\": 2\n" +
                "    },\n" +
                "    \"email\": \"abc@bbc.com\",\n" +
                "    \"creditCardInfo\": {\n" +
                "        \"number\": \"5105105105105100\",\n" +
                "        \"expiryDate\": {\n" +
                "            \"month\": \"4\",\n" +
                "            \"year\": \"1998\"\n" +
                "        },\n" +
                "        \"cvv\": 460,\n" +
                "        \"holderName\": \"Jackie\"\n" +
                "    },\n" +
                "    \"clientSessionId\": \"123456\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidCreditCardInfoException));
    }

    @Test
    public void should_return_400_bad_request_when_create_order_given_any_of_bookedSeatIndices_is_sold() throws Exception {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus1 = new SeatStatus(IN_PROCESS, 1000L, "123456");
        SeatStatus seatStatus2 = new SeatStatus(SOLD, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus1);
        occupied.put(15, seatStatus2);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("5fc8913234ba53396c26a863");
        movieSessionRepository.save(movieSession1);
        String orderAsJson = "{\n" +
                "    \"movieSessionId\": \"5fc8913234ba53396c26a863\",\n" +
                "    \"bookedSeatIndices\": [\n" +
                "        14,\n" +
                "        15\n" +
                "    ],\n" +
                "    \"customerGroupQuantityMap\": {\n" +
                "        \"Adult\": 2,\n" +
                "        \"Student\": 2\n" +
                "    },\n" +
                "    \"email\": \"abc@bbc.com\",\n" +
                "    \"creditCardInfo\": {\n" +
                "        \"number\": \"5105105105105100\",\n" +
                "        \"expiryDate\": {\n" +
                "            \"month\": \"4\",\n" +
                "            \"year\": \"2043\"\n" +
                "        },\n" +
                "        \"cvv\": 460,\n" +
                "        \"holderName\": \"Jackie\"\n" +
                "    },\n" +
                "    \"clientSessionId\": \"123456\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnavailableSeatException));
    }

    @Test
    public void should_return_400_bad_request_when_create_order_given_seats_are_in_progress_but_different_clientSessionId() throws Exception {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("5fc8913234ba53396c26a863");
        movieSessionRepository.save(movieSession1);
        String orderAsJson = "{\n" +
                "    \"movieSessionId\": \"5fc8913234ba53396c26a863\",\n" +
                "    \"bookedSeatIndices\": [\n" +
                "        14,\n" +
                "        15\n" +
                "    ],\n" +
                "    \"customerGroupQuantityMap\": {\n" +
                "        \"Adult\": 2,\n" +
                "        \"Student\": 2\n" +
                "    },\n" +
                "    \"email\": \"abc@bbc.com\",\n" +
                "    \"creditCardInfo\": {\n" +
                "        \"number\": \"5105105105105100\",\n" +
                "        \"expiryDate\": {\n" +
                "            \"month\": \"4\",\n" +
                "            \"year\": \"2043\"\n" +
                "        },\n" +
                "        \"cvv\": 460,\n" +
                "        \"holderName\": \"Jackie\"\n" +
                "    },\n" +
                "    \"clientSessionId\": \"999999\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnavailableSeatException));
    }

    @Test
    public void shoud_return_updated_order_when_update_order_given_valid_order_id_and_order_update_info() throws Exception {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus1 = new SeatStatus(IN_PROCESS, 1000L, "123456");
        SeatStatus seatStatus2 = new SeatStatus(IN_PROCESS, 1000L, "999999");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus1);
        occupied.put(15, seatStatus1);
        occupied.put(19, seatStatus2);
        occupied.put(20, seatStatus2);
        Movie movie = movieRepository.save(new Movie());
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house = houseRepository.save(House.builder().cinemaId(cinema.getId()).build());
        MovieSession movieSession1 = new MovieSession(movie.getId(), house.getId(), 10000L, prices, occupied);
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        movieSession1.setId("5fc8913234ba53396c26a863");
        movieSessionRepository.save(movieSession1);
        Order order = new Order("abc@bbc.com", movieSession1.getId(), Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        orderService.create(order, creditCardInfo, clientSessionId);
        String orderAsJson = "{\n" +
                "    \"movieSessionId\": \"5fc8913234ba53396c26a863\",\n" +
                "    \"bookedSeatIndices\": [\n" +
                "        19,\n" +
                "        20\n" +
                "    ],\n" +
                "    \"customerGroupQuantityMap\": {\n" +
                "        \"Adult\": 1,\n" +
                "        \"Student\": 4\n" +
                "    },\n" +
                "    \"email\": \"newEmail@bbc.com\",\n" +
                "    \"creditCardInfo\": {\n" +
                "        \"number\": \"5105105105105100\",\n" +
                "        \"expiryDate\": {\n" +
                "            \"month\": \"4\",\n" +
                "            \"year\": \"2043\"\n" +
                "        },\n" +
                "        \"cvv\": 460,\n" +
                "        \"holderName\": \"Jackie\"\n" +
                "    },\n" +
                "    \"clientSessionId\": \"999999\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/" + order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.email").value("newEmail@bbc.com"))
                .andExpect(jsonPath("$.bookedSeatIndices[0]").value("19"))
                .andExpect(jsonPath("$.bookedSeatIndices[1]").value("20"))
                .andExpect(jsonPath("$.customerGroupQuantityMap", Matchers.hasKey("Adult")))
                .andExpect(jsonPath("$.customerGroupQuantityMap", Matchers.hasKey("Student")))
                .andExpect(jsonPath("$.creditCardNumber").value("5105105105105100"))
                .andExpect(jsonPath("$.movieSession").isNotEmpty());
    }

    @Test
    public void shoud_return_404_not_found_when_update_order_given_invalid_order_id_and_order_update_info() throws Exception {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus1 = new SeatStatus(IN_PROCESS, 1000L, "123456");
        SeatStatus seatStatus2 = new SeatStatus(IN_PROCESS, 1000L, "999999");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus1);
        occupied.put(15, seatStatus1);
        occupied.put(19, seatStatus2);
        occupied.put(20, seatStatus2);

        Movie movie = movieRepository.save(new Movie());
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house = houseRepository.save(House.builder().cinemaId(cinema.getId()).build());
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        movieSession1.setId("5fc8913234ba53396c26a863");
        movieSessionRepository.save(movieSession1);
        Order order = new Order("abc@bbc.com", movieSession1.getId(), Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        orderService.create(order, creditCardInfo, clientSessionId);
        String orderAsJson = "{\n" +
                "    \"movieSessionId\": \"5fc8913234ba53396c26a863\",\n" +
                "    \"bookedSeatIndices\": [\n" +
                "        19,\n" +
                "        20\n" +
                "    ],\n" +
                "    \"customerGroupQuantityMap\": {\n" +
                "        \"Adult\": 1,\n" +
                "        \"Student\": 4\n" +
                "    },\n" +
                "    \"email\": \"newEmail@bbc.com\",\n" +
                "    \"creditCardInfo\": {\n" +
                "        \"number\": \"5105105105105100\",\n" +
                "        \"expiryDate\": {\n" +
                "            \"month\": \"4\",\n" +
                "            \"year\": \"2043\"\n" +
                "        },\n" +
                "        \"cvv\": 460,\n" +
                "        \"holderName\": \"Jackie\"\n" +
                "    },\n" +
                "    \"clientSessionId\": \"999999\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/5fc8913234ba53396c26a860")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof OrderNotFoundException));
    }

    @Test
    public void should_return_no_content_when_delete_order_given_valid_order_id() throws Exception {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        movieSessionRepository.save(movieSession1);
        Order order = new Order("abc@bbc.com", movieSession1.getId(), Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        orderService.create(order, creditCardInfo, clientSessionId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/" + order.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_404_not_found_when_delete_order_given_invalid_order_id() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/5fc8913234ba53396c26a860"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_order_history_when_view_order_history_given_email_and_credit_card_number() throws Exception {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo1 = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        CreditCardInfo creditCardInfo2 = new CreditCardInfo("5105105105105101", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        occupied.put(16, seatStatus);
        occupied.put(17, seatStatus);
        Movie movie = movieRepository.save(new Movie());
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house = houseRepository.save(House.builder().cinemaId(cinema.getId()).build());
        MovieSession movieSession1 = new MovieSession(movie.getId(), house.getId(), 10000L, prices, occupied);
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        movieSessionRepository.save(movieSession1);
        Order order1 = new Order("abc@bbc.com", movieSession1.getId(), Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        Order order2 = new Order("abc@bbc.com", movieSession1.getId(), Arrays.asList(16, 17), customerGroupQuantityMap, "5105105105105101");
        orderService.create(order1, creditCardInfo1, clientSessionId);
        orderService.create(order2, creditCardInfo2, clientSessionId);
        String identityAsJson = "{\n" +
                "    \"email\": \"abc@bbc.com\",\n" +
                "    \"cardNumber\": \"5105105105105100\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/orders/history").contentType(MediaType.APPLICATION_JSON).content(identityAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].email").value("abc@bbc.com"))
                .andExpect(jsonPath("$[0].bookedSeatIndices[0]").value("14"))
                .andExpect(jsonPath("$[0].bookedSeatIndices[1]").value("15"))
                .andExpect(jsonPath("$[0].customerGroupQuantityMap", Matchers.hasKey("Adult")))
                .andExpect(jsonPath("$[0].customerGroupQuantityMap", Matchers.hasKey("Student")))
                .andExpect(jsonPath("$[0].creditCardNumber").value("5105105105105100"))
                .andExpect(jsonPath("$[0].movieSession").isNotEmpty());
    }

    @Test
    public void should_null_when_view_order_history_given_email_and_credit_card_number_and_no_matching_order() throws Exception {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo1 = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        CreditCardInfo creditCardInfo2 = new CreditCardInfo("5105105105105101", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        occupied.put(16, seatStatus);
        occupied.put(17, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        movieSessionRepository.save(movieSession1);
        Order order1 = new Order("abc@bbc.com", movieSession1.getId(), Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        Order order2 = new Order("abc@bbc.com", movieSession1.getId(), Arrays.asList(16, 17), customerGroupQuantityMap, "5105105105105101");
        orderService.create(order1, creditCardInfo1, clientSessionId);
        orderService.create(order2, creditCardInfo2, clientSessionId);
        String identityAsJson = "{\n" +
                "    \"email\": \"abc@bbc.com\",\n" +
                "    \"cardNumber\": \"5105105105105102\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/orders/history").contentType(MediaType.APPLICATION_JSON).content(identityAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    public void should_return_updated_seat_when_update_seat_given_seat_indeice() throws Exception {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus1 = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus1);
        occupied.put(15, seatStatus1);
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        Movie movie = movieRepository.save(new Movie());
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house = houseRepository.save(House.builder().cinemaId(cinema.getId()).build());
        MovieSession movieSession = movieSessionRepository.save(new MovieSession(movie.getId(), house.getId(), 10000000000000L, prices, occupied));
        Order order2 = orderService.create(new Order("abc@bbc.com", movieSession.getId(), Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100"), creditCardInfo, clientSessionId);
        String orderAsJson = "[19,21]";
        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/" + order2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.email").value("abc@bbc.com"))
                .andExpect(jsonPath("$.bookedSeatIndices[0]").value("19"))
                .andExpect(jsonPath("$.bookedSeatIndices[1]").value("21"))
                .andExpect(jsonPath("$.customerGroupQuantityMap", Matchers.hasKey("Adult")))
                .andExpect(jsonPath("$.customerGroupQuantityMap", Matchers.hasKey("Student")))
                .andExpect(jsonPath("$.creditCardNumber").value("5105105105105100"))
                .andExpect(jsonPath("$.movieSession.occupied", Matchers.hasKey("19")))
                .andExpect(jsonPath("$.movieSession.occupied", Matchers.hasKey("21")))
                .andExpect(jsonPath("$.movieSession.occupied.14").doesNotExist())
                .andExpect(jsonPath("$.movieSession.occupied.15").doesNotExist());


    }

    @Test
    public void should_throw400_status_when_update_seat_given_invalid_seat_index() throws Exception {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus1 = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus1);
        occupied.put(15, seatStatus1);
        occupied.put(19, seatStatus1);
        occupied.put(20, seatStatus1);
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        Movie movie = movieRepository.save(new Movie());
        Cinema cinema = cinemaRepository.save(new Cinema());
        House house = houseRepository.save(House.builder().cinemaId(cinema.getId()).build());
        MovieSession movieSession = movieSessionRepository.save(new MovieSession(movie.getId(), house.getId(), 10000L, prices, occupied));
        Order order2 = orderService.create(new Order("abc@bbc.com", movieSession.getId(), Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100"), creditCardInfo, clientSessionId);
        String orderAsJson = "[19,20]";
        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/" + order2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderAsJson))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_delete_order_when_delete_movie_session_given_order_with_movie_session_id() throws Exception {
        Movie movie = movieRepository.save(new Movie());
        House house = houseRepository.save(new House());
        MovieSession movieSession = movieSessionRepository.save(MovieSession.builder().movieId(movie.getId()).houseId(house.getId()).startTime(System.currentTimeMillis() + 100000).build());
        Order order = orderRepository.save(Order.builder().movieSessionId(movieSession.getId()).build());
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/movie_sessions/" + movieSession.getId()));

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/movie_sessions/" + movieSession.getId()))
                .andExpect(status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + order.getId()))
                .andExpect(status().isNotFound());
    }


}
