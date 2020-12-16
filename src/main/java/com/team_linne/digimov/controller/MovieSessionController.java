package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.*;
import com.team_linne.digimov.mapper.MovieSessionMapper;
import com.team_linne.digimov.service.MovieSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/movie_sessions")
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

    @PatchMapping("/{id}")
    public MovieSessionResponse patchSeatIndices(@PathVariable String id, @RequestBody MovieSessionPatchRequest movieSessionPatchRequest) {
        return movieSessionMapper.toResponse(this.movieSessionService.patch(id, movieSessionPatchRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        this.movieSessionService.delete(id);
    }

    @GetMapping(params={"cinema", "sessionStatus"})
    public List<MovieSessionResponse> getUpcomingMovieSessionsByCinema(@RequestParam("cinema") String cinemaId, @RequestParam("sessionStatus") String sessionStatus) {
        if (sessionStatus.equals("upcoming")) {
            return movieSessionService.getUpcomingMovieSessionsByCinemaId(cinemaId).stream().map(movieSessionMapper::toResponse).collect(Collectors.toList());
        }
        else {
            return getMovieSessionsByCinema(cinemaId);
        }
    }

    @GetMapping(params={"cinema"})
    public List<MovieSessionResponse> getMovieSessionsByCinema(@RequestParam("cinema") String cinemaId) {
        return movieSessionService.getAllByCinemaId(cinemaId).stream().map(movieSessionMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping(params={"movie", "sessionStatus"})
    public List<MovieSessionResponse> getUpcomingMovieSessionsByMovie(@RequestParam("movie") String movieId, @RequestParam("sessionStatus") String sessionStatus) {
        if (sessionStatus.equals("upcoming")) {
            return movieSessionService.getUpcomingMovieSessionsByMovieId(movieId).stream().map(movieSessionMapper::toResponse).collect(Collectors.toList());
        }
        else {
            return getMovieSessionsByMovie(movieId);
        }
    }

    @GetMapping(params={"movie"})
    public List<MovieSessionResponse> getMovieSessionsByMovie(@RequestParam("movie") String movieId) {
        return movieSessionService.getAllByMovieId(movieId).stream().map(movieSessionMapper::toResponse).collect(Collectors.toList());
    }

}
