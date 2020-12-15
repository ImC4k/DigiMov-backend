package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.MovieNotFoundException;
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

    public MovieSession getById(String id) {
        return this.movieSessionRepository.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    public MovieSession create(MovieSession movieSession) {
        return this.movieSessionRepository.insert(movieSession);
    }

    public MovieSession update(String id, MovieSession movieSessionUpdate) {
        MovieSession movieSession = this.getById(id);
        movieSessionUpdate.setId(movieSession.getId());
        return this.movieSessionRepository.save(movieSessionUpdate);
    }

    public void delete(String id) {
        MovieSession movieSession = this.getById(id);
        this.movieSessionRepository.deleteById(movieSession.getId());
    }
}
