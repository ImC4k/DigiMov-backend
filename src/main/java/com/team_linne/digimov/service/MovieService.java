package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.MovieNotFoundException;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.repository.GenreRepository;
import com.team_linne.digimov.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    GenreRepository genreRepository;

    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    public Movie getById(String id) {
        return movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    public Movie create(Movie movie) {
        movie.getGenreIds().forEach(id -> genreRepository.findById(id));
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

    public void deleteGenreId(String genreId) {
        List<Movie> moviesWithGenreId = movieRepository.findAllByGenreIdsIn(Collections.singletonList(genreId));
        moviesWithGenreId.forEach(movie -> {
            List<String> genreIds = movie.getGenreIds();
            genreIds.remove(genreId);
            movie.setGenreIds(genreIds);
            movieRepository.save(movie);
        });
    }
}
