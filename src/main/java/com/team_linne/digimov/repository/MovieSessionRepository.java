package com.team_linne.digimov.repository;

import com.team_linne.digimov.model.MovieSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieSessionRepository extends MongoRepository<MovieSession, String> {
}
