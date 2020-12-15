package com.team_linne.digimov.service;

import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.model.Order;
import com.team_linne.digimov.repository.OrderRepository;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    OrderService orderService;
    @Mock
    OrderRepository orderRepository;

    @Test
    void should_return_all_orders_when_get_all_given_list_of_orders() {
        //given
//          private String email;
//    private String userId;
//    private String movieSessionId;
//    private List<Integer> bookedSeatIndices;
//    private Map<String,Integer> customerGroupQuantityMap;
//    private String creditCardNumber;
        Map<String,Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adults",2);
        customerGroupQuantityMap.put("Student",1);
        Order order = new Order("abc@bbc.com","20998372","32", Arrays.asList(14,15), customerGroupQuantityMap,"5105105105105100");
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
}
