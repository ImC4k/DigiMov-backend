package com.team_linne.digimov.repository;

import com.team_linne.digimov.model.MovieSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieSessionRepository extends MongoRepository<MovieSession, String> {
    List<MovieSession> findAllByHouseIdInAndStartTimeGreaterThan(List<String> houseIds, Long startTime);
    List<MovieSession> findAllByHouseIdIn(List<String> houseIds);
    List<MovieSession> findAllByMovieIdAndStartTimeGreaterThan(String movieId, Long startTime);
    List<MovieSession> findAllByMovieId(String movieId);
}
