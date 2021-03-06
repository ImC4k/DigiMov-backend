package com.team_linne.digimov.repository;

import com.team_linne.digimov.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findAllByEmailAndCreditCardNumber(String email, String creditCardNumber);

    List<Order> findAllByMovieSessionId(String movieSessionId);
}
