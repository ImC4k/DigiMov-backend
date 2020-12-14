package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.GenreResponse;
import com.team_linne.digimov.dto.MovieRequest;
import com.team_linne.digimov.dto.MovieResponse;
import com.team_linne.digimov.mapper.GenreMapper;
import com.team_linne.digimov.mapper.MovieMapper;
import com.team_linne.digimov.model.Genre;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.service.GenreService;
import com.team_linne.digimov.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private GenreService genreService;

    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private GenreMapper genreMapper;

    @GetMapping
    public List<MovieResponse> getAll() {
        return movieService.getAll().stream()
                .map(this::getMovieResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MovieResponse getById(@PathVariable String id) {
        Movie movie = movieService.getById(id);
        return getMovieResponse(movie);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@RequestBody MovieRequest movieRequest) {
        Movie movie = movieService.create(movieMapper.toEntity(movieRequest));
        return getMovieResponse(movie);
    }

    @PutMapping("/{id}")
    public MovieResponse update(@PathVariable String id, @RequestBody MovieRequest movieUpdate) {
        Movie movie = movieService.update(id, movieMapper.toEntity(movieUpdate));
        return getMovieResponse(movie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        movieService.delete(id);
    }


    private MovieResponse getMovieResponse(Movie movie) {
        List<GenreResponse> genres = movie.getGenreIds().stream().map(id -> genreMapper.toResponse(genreService.getById(id))).collect(Collectors.toList());
        return movieMapper.toResponse(movie, genres);
    }

}
