package com.team_linne.digimov.mapper;

import com.team_linne.digimov.dto.MovieSessionResponse;
import com.team_linne.digimov.dto.OrderRequest;
import com.team_linne.digimov.dto.OrderResponse;
import com.team_linne.digimov.model.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order toEntity(OrderRequest orderRequest) {
        Order order = new Order();
        BeanUtils.copyProperties(orderRequest,order);
        order.setCreditCardNumber(orderRequest.getCreditCardInfo().getNumber());
        return order;
    }

    public OrderResponse toResponse(Order order, MovieSessionResponse movieSession) {
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(order,orderResponse);
        orderResponse.setMovieSession(movieSession);
        return orderResponse;
    }
}
