package com.team_linne.digimov.repository;

import com.team_linne.digimov.model.Cinema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends MongoRepository<Cinema, String> {
}
