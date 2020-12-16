package com.team_linne.digimov.repository;

import com.team_linne.digimov.model.House;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends MongoRepository<House, String> {
    List<House> findByCinemaId(String cinemaId);
}
