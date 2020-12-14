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

    public Movie create(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie update(String id, Movie movieUpdate) {
        Movie movie = this.getById(id);
        movieUpdate.setId(movie.getId());
        return this.create(movieUpdate);
    }

    public void delete(String id) {
        Movie movie = this.getById(id);
        movieRepository.deleteById(movie.getId());
    }
}
