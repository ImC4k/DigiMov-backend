package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.MovieRequest;
import com.team_linne.digimov.dto.MovieResponse;
import com.team_linne.digimov.dto.MovieSessionRequest;
import com.team_linne.digimov.dto.MovieSessionResponse;
import com.team_linne.digimov.mapper.MovieSessionMapper;
import com.team_linne.digimov.service.MovieSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/moviesessions")
public class MovieSessionController {
    @Autowired
    private MovieSessionService movieSessionService;

    @Autowired
    private MovieSessionMapper movieSessionMapper;

    @GetMapping
    public List<MovieSessionResponse> getAll() {
        return this.movieSessionService.getAll().stream().map(movieSessionMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MovieSessionResponse getById(@PathVariable String id) {
        return movieSessionMapper.toResponse(movieSessionService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieSessionResponse create(@RequestBody MovieSessionRequest movieSessionRequest) {
        return movieSessionMapper.toResponse(movieSessionService.create(movieSessionMapper.toEntity(movieSessionRequest)));
    }

    @PutMapping("/{id}")
    public MovieSessionResponse update(@PathVariable String id, @RequestBody MovieSessionRequest movieSessionUpdate) {
        return movieSessionMapper.toResponse(movieSessionService.update(id, movieSessionMapper.toEntity(movieSessionUpdate)));
    }
}
