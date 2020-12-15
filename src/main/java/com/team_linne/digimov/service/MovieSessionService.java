package com.team_linne.digimov.service;

import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.repository.MovieSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MovieSessionService {
    @Autowired
    private MovieSessionRepository movieSessionRepository;

    public List<MovieSession> getAll() {
        return this.movieSessionRepository.findAll();
    }
}
