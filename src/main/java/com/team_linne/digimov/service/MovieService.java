package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.MovieNotFoundException;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    public Movie getById(String id) {
        return movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
    }
}
