package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.MovieNotFoundException;
import com.team_linne.digimov.exception.OrderNotFoundException;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    OrderService orderService;
    @Mock
    OrderRepository orderRepository;

    @Test
    void should_return_all_orders_when_get_all_given_list_of_orders() {
        //given
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

    @Test
    void should_return_specific_order_when_get_by_id_given_list_of_orders_and_valid_order_id() {
        //given
        Map<String,Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adults",2);
        customerGroupQuantityMap.put("Student",1);
        Order order1 = new Order("abc@bbc.com","20998372","32", Arrays.asList(14,15), customerGroupQuantityMap,"5105105105105100");
        Order order2 = new Order("cbd@bbc.com","35432312","31", Arrays.asList(15,16), customerGroupQuantityMap,"5105105105105100");
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
        Map<String,Integer> customerGroupQuantityMap = new HashMap<>();
        customerGroupQuantityMap.put("Adults",2);
        customerGroupQuantityMap.put("Student",1);
        Order order = new Order("abc@bbc.com","20998372","32", Arrays.asList(14,15), customerGroupQuantityMap,"5105105105105100");
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
}
