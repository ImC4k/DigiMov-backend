package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.MovieRequest;
import com.team_linne.digimov.dto.MovieResponse;
import com.team_linne.digimov.mapper.MovieMapper;
import com.team_linne.digimov.model.Movie;
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
    private MovieMapper movieMapper;

    @GetMapping
    public List<MovieResponse> getAll() {
        return movieService.getAll().stream()
                .map(movieMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MovieResponse getById(@PathVariable String id) {
        return movieMapper.toResponse(movieService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@RequestBody MovieRequest movieRequest) {
        Movie movie = movieService.create(movieMapper.toEntity(movieRequest));
        return movieMapper.toResponse(movie);
    }

    @PutMapping("/{id}")
    public MovieResponse update(@PathVariable String id, @RequestBody MovieRequest movieUpdate) {
        Movie movie = movieService.update(id, movieMapper.toEntity(movieUpdate));
        return movieMapper.toResponse(movie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        movieService.delete(id);
    }

}
