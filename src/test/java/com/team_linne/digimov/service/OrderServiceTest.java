package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.*;
import com.team_linne.digimov.model.*;
import com.team_linne.digimov.repository.MovieSessionRepository;
import com.team_linne.digimov.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    public static final String IN_PROCESS = "in process";
    public static final String SOLD = "sold";
    @InjectMocks
    OrderService orderService;
    @InjectMocks
    MovieSessionService movieSessionService;
    @Mock
    OrderRepository orderRepository;
    @Mock
    MovieSessionRepository movieSessionRepository;

    @Test
    void should_return_all_orders_when_get_all_given_list_of_orders() {
        //given
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adults", 2);
        customerGroupQuantityMap.put("Student", 1);
        Order order = new Order("abc@bbc.com", "32", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.findAll()).thenReturn(orderList);

        //when
        final List<Order> actual = orderService.getAll();

        //then
        assertEquals(orderList, actual);
    }

    @Test
    void should_return_empty_array_when_get_all_given_no_orders() {
        //given
        List<Order> orderList = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(orderList);

        //when
        final List<Order> actual = orderService.getAll();

        //then
        assertEquals(orderList, actual);
    }

    @Test
    void should_return_specific_order_when_get_by_id_given_list_of_orders_and_valid_order_id() {
        //given
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adults", 2);
        customerGroupQuantityMap.put("Student", 1);
        Order order1 = new Order("abc@bbc.com", "32", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        Order order2 = new Order("cbd@bbc.com", "31", Arrays.asList(15, 16), customerGroupQuantityMap, "5105105105105100");
        order1.setId("1");
        order2.setId("2");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        when(orderRepository.findById("1")).thenReturn(Optional.of(order1));

        //when
        final Order actual = orderService.getById("1");

        //then
        assertEquals(order1, actual);
    }

    @Test
    void should_throw_order_not_found_exception_when_get_by_id_given_list_of_orders_and_invalid_order_id() {
        //given
        //when
        //then
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getById("999");
        }, "Order not found");
    }

    @Test
    void should_delete_order_when_delete_order_given_valid_order_id() {
        //given
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adults", 2);
        customerGroupQuantityMap.put("Student", 1);
        Order order = new Order("abc@bbc.com", "32", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        order.setId("1");
        when(orderRepository.findById("1")).thenReturn(Optional.of(order));

        //when
        orderService.delete("1");

        //then
        verify(orderRepository, times(1)).deleteById("1");
    }

    @Test
    void should_throw_order_not_found_exception_when_delete_order_given_invalid_order_id() {
        //given
        //when
        //then
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.delete("999");
        }, "Order not found");
    }

    @Test
    void should_return_created_order_and_update_seats_status_movie_session_occupied_when_create_order_given_valid_movie_session_id_and_ticket_types_are_found_in_movie_session() {
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
        movieSession1.setId("1");
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        Order order = new Order("abc@bbc.com", "1", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        when(orderRepository.save(order)).thenReturn(order);
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));
        //when
        Order actual = orderService.create(order, creditCardInfo, clientSessionId);

        //then
        assertEquals(order, actual);
        assertEquals(movieSessionService.getById("1").getOccupied().get(14).getStatus(), SOLD);
        assertEquals(movieSessionService.getById("1").getOccupied().get(15).getStatus(), SOLD);
    }

    @Test
    void should_throw_invalid_customer_group_exception_when_create_order_given_non_exist_ticket_type_in_movie_session() {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("1");
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        Order order = new Order("abc@bbc.com", "1", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));
        //when
        //then
        assertThrows(InvalidCustomerGroupException.class, () -> {
            orderService.create(order, creditCardInfo, clientSessionId);
        });
    }

    @Test
    void should_throw_movie_session_not_found_exception_when_create_order_given_invalid_movie_session_id() {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        Order order = new Order("abc@bbc.com", "1", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");

        //when
        //then
        assertThrows(MovieSessionNotFoundException.class, () -> {
            orderService.create(order, creditCardInfo, clientSessionId);
        });
    }

    @Test
    void should_throw_invalid_credit_card_info_exception_when_create_order_given_invalid_credit_card_number() {
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("123", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("1");
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        Order order = new Order("abc@bbc.com", "1", Arrays.asList(14, 15), customerGroupQuantityMap, "123");
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));
        //when
        //then
        assertThrows(InvalidCreditCardInfoException.class, () -> {
            orderService.create(order, creditCardInfo, clientSessionId);
        });
    }

    @Test
    void should_throw_invalid_credit_card_info_exception_when_create_order_given_valid_credit_card_number_but_expired() {
        ExpiryDate expiryDate = new ExpiryDate("4", "1998");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("1");
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        Order order = new Order("abc@bbc.com", "1", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));
        //when
        //then
        assertThrows(InvalidCreditCardInfoException.class, () -> {
            orderService.create(order, creditCardInfo, clientSessionId);
        });
    }

    @Test
    void should_throw_unavailable_seat_exception_when_create_order_given_any_of_bookedSeatIndices_is_sold() {
        ExpiryDate expiryDate = new ExpiryDate("4", "2045");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        SeatStatus seatStatus1 = new SeatStatus(SOLD, 1000L, "123456");
        SeatStatus seatStatus2 = new SeatStatus(IN_PROCESS, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus1);
        occupied.put(15, seatStatus2);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("1");
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        Order order = new Order("abc@bbc.com", "1", Arrays.asList(13, 14, 15), customerGroupQuantityMap, "5105105105105100");
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));
        //when
        //then
        assertThrows(UnavailableSeatException.class, () -> {
            orderService.create(order, creditCardInfo, clientSessionId);
        });
    }

    @Test
    void should_throw_unavailable_seat_exception_when_create_order_given_seats_are_in_progress_but_different_clientSessionId() {
        ExpiryDate expiryDate = new ExpiryDate("4", "2045");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        SeatStatus seatStatus = new SeatStatus(IN_PROCESS, 1000L, "654321");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("1");
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        Order order = new Order("abc@bbc.com", "1", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));
        //when
        //then
        assertThrows(UnavailableSeatException.class, () -> {
            orderService.create(order, creditCardInfo, clientSessionId);
        });
    }

    @Test
    void should_return_updated_order_and_update_seat_status_when_update_order_given_valid_order_id_and_order_update_info() {
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
        movieSession1.setId("1");
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        Order order = new Order("abc@bbc.com", "1", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        order.setId("1");
        occupied.put(19, seatStatus);
        occupied.put(20, seatStatus);
        Order expected = new Order("new@bbc.com", "1", Arrays.asList(19, 20), customerGroupQuantityMap, "5105105105105100");
        when(orderRepository.findById("1")).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(expected);
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));
        //when
        Order actual = orderService.update("1", expected, creditCardInfo, clientSessionId);

        assertEquals(expected, actual);
        assertNull(occupied.get(14));
        assertNull(occupied.get(15));
        assertEquals(SOLD,occupied.get(19).getStatus());
        assertEquals(SOLD,occupied.get(20).getStatus());
    }

    @Test
    void should_throw_order_id_not_found_exception_when_update_order_given_invalid_order_id_and_order_update_info() {
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
        movieSession1.setId("1");
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        Order order = new Order("abc@bbc.com", "1", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        order.setId("1");

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.update("999", order, creditCardInfo, clientSessionId);
        }, "Order not found");
    }
    @Test
    void should_return_list_of_order_when_get_oder_history_given_email_and_creditcardnumber() {
        //given
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adults", 2);
        customerGroupQuantityMap.put("Student", 1);
        Order order = new Order("abc@bbc.com", "32", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.findAllByEmailAndCreditCardNumber(any(),any())).thenReturn(orderList);

        //when
        final List<Order> actual = orderService.getOrderHistoryByIdentity(new Identity("abc@bbc.com","5105105105105100"));

        //then
        assertEquals(orderList, actual);
    }

    @Test
    void should_return_updated_order_and_update_seat_status_when_update_seat_given_valid_seat_indices() {
        //given
        ExpiryDate expiryDate = new ExpiryDate("4", "2043");
        CreditCardInfo creditCardInfo = new CreditCardInfo("5105105105105100", expiryDate, 406, "Jackie");
        String clientSessionId = "123456";
        Map<String, Double> prices = new HashMap<>();
        prices.put("Adult", 100D);
        prices.put("Student", 60D);
        SeatStatus seatStatus = new SeatStatus(SOLD, 1000L, "123456");
        Map<Integer, SeatStatus> occupied = new HashMap<>();
        occupied.put(14, seatStatus);
        occupied.put(15, seatStatus);
        MovieSession movieSession1 = new MovieSession("mov1", "111", 10000L, prices, occupied);
        movieSession1.setId("1");
        Map<String, Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adult", 2);
        customerGroupQuantityMap.put("Student", 1);
        Order order = new Order("abc@bbc.com", "1", Arrays.asList(14, 15), customerGroupQuantityMap, "5105105105105100");
        order.setId("1");
        Map<Integer, SeatStatus> occupied2 = new HashMap<>();
        occupied2.put(19, seatStatus);
        occupied2.put(20, seatStatus);
        Order order2 = new Order("abc@bbc.com", "1", Arrays.asList(19, 20), customerGroupQuantityMap, "5105105105105100");
        order2.setId("1");
        MovieSession movieSession2 = new MovieSession("mov1", "111", 10000L, prices, occupied2);
        when(orderRepository.findById("1")).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order2);
        when(movieSessionRepository.findById("1")).thenReturn(Optional.of(movieSession1));
        when(movieSessionRepository.save(any())).thenReturn(movieSession2);
        //when
        Order actual = orderService.updateSeat(Arrays.asList(19,20),"1");
        //then
        assertEquals(Arrays.asList(19,20),actual.getBookedSeatIndices());
        assertEquals(SOLD,movieSessionRepository.findById(actual.getMovieSessionId()).get().getOccupied().get(19).getStatus());
        assertEquals(SOLD,movieSessionRepository.findById(actual.getMovieSessionId()).get().getOccupied().get(20).getStatus());
        assertNull(movieSessionRepository.findById(actual.getMovieSessionId()).get().getOccupied().get(14));
        assertNull(movieSessionRepository.findById(actual.getMovieSessionId()).get().getOccupied().get(15));
    }



}
