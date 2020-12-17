package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.OrderRequest;
import com.team_linne.digimov.dto.OrderResponse;
import com.team_linne.digimov.mapper.MovieSessionMapper;
import com.team_linne.digimov.mapper.OrderMapper;
import com.team_linne.digimov.model.Identity;
import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.model.Order;
import com.team_linne.digimov.service.MovieSessionService;
import com.team_linne.digimov.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    MovieSessionService movieSessionService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    MovieSessionMapper movieSessionMapper;

    @GetMapping
    public List<OrderResponse> getAll() {
        return orderService.getAll().stream().map(this::getOrderResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable String id) {
        return getOrderResponse(orderService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestBody OrderRequest orderRequest) {
        Order order = orderMapper.toEntity(orderRequest);
        return getOrderResponse(orderService.create(order, orderRequest.getCreditCardInfo(), orderRequest.getClientSessionId()));
    }

    @PostMapping("/{id}")
    public OrderResponse redirect(@PathVariable String id) {
        return getOrderResponse(orderService.getById(id));
    }

    @PutMapping("/{id}")
    public OrderResponse update(@PathVariable String id, @RequestBody OrderRequest orderUpdate) {
        Order order = orderMapper.toEntity(orderUpdate);
        return getOrderResponse(orderService.update(id, order, orderUpdate.getCreditCardInfo(), orderUpdate.getClientSessionId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        orderService.delete(id);
    }

    private OrderResponse getOrderResponse(Order order) {
        MovieSession movieSession = movieSessionService.getById(order.getMovieSessionId());
        return orderMapper.toResponse(order, movieSessionMapper.toResponse(movieSession));
    }

    @PostMapping("/history")
    public List<OrderResponse> viewOrderHistory(@RequestBody Identity identity) {
        return orderService.getOrderHistoryByIdentity(identity).
                stream().map(this::getOrderResponse).collect(Collectors.toList());

    }
}
