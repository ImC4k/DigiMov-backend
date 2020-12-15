package com.team_linne.digimov.service;

import com.team_linne.digimov.model.Order;
import com.team_linne.digimov.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    public List<Order> getAll() {
        return orderRepository.findAll();
    }
}
