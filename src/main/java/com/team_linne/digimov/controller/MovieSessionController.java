package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.MovieSessionResponse;
import com.team_linne.digimov.mapper.MovieSessionMapper;
import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.repository.MovieSessionRepository;
import com.team_linne.digimov.service.MovieSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
