package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.CinemaResponse;
import com.team_linne.digimov.dto.GenreResponse;
import com.team_linne.digimov.dto.OrderRequest;
import com.team_linne.digimov.dto.OrderResponse;
import com.team_linne.digimov.mapper.*;
import com.team_linne.digimov.model.*;
import com.team_linne.digimov.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private
    OrderService orderService;
    @Autowired
    private
    MovieSessionService movieSessionService;
    @Autowired
    private
    OrderMapper orderMapper;
    @Autowired
    private
    MovieSessionMapper movieSessionMapper;
    @Autowired
    private
    MovieService movieService;
    @Autowired
    private
    HouseService houseService;
    @Autowired
    private
    GenreService genreService;
    @Autowired
    private
    GenreMapper genreMapper;
    @Autowired
    private
    CinemaService cinemaService;
    @Autowired
    private
    CinemaMapper cinemaMapper;
    @Autowired
    private
    MovieMapper movieMapper;
    @Autowired
    private
    HouseMapper houseMapper;

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
        Movie movie = movieService.getById(movieSession.getMovieId());
        House house = houseService.getById(movieSession.getHouseId());
        List<GenreResponse> genres = movie.getGenreIds() != null? movie.getGenreIds().stream().map(id -> genreMapper.toResponse(genreService.getById(id))).collect(Collectors.toList()) : Collections.emptyList();
        CinemaResponse cinema = cinemaMapper.toResponse(cinemaService.getById(house.getCinemaId()));
        return orderMapper.toResponse(order, movieSessionMapper.toResponse(movieSession, movieMapper.toResponse(movie, genres), houseMapper.toResponse(house, cinema)));
    }

    @PostMapping("/history")
    public List<OrderResponse> viewOrderHistory(@RequestBody Identity identity) {
        return orderService.getOrderHistoryByIdentity(identity).
                stream().map(this::getOrderResponse).collect(Collectors.toList());

    }
    @PatchMapping("/{id}")
    public OrderResponse editSeatPosition(@RequestBody List<Integer> seatIndices, @PathVariable String id){
        return getOrderResponse(orderService.updateSeat(seatIndices, id));
    }
}
