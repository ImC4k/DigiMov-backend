package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.*;
import com.team_linne.digimov.mapper.*;
import com.team_linne.digimov.model.House;
import com.team_linne.digimov.model.Movie;
import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    @Autowired
    private MovieService movieService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    private GenreService genreService;
    @Autowired
    private CinemaMapper cinemaMapper;
    @Autowired
    private CinemaService cinemaService;

    @GetMapping
    public List<MovieSessionResponse> getAll() {
        return this.movieSessionService.getAll().stream().map(this::getSessionResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MovieSessionResponse getById(@PathVariable String id) {
        return this.getSessionResponse(movieSessionService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieSessionResponse create(@RequestBody MovieSessionRequest movieSessionRequest) {
        return this.getSessionResponse(movieSessionService.create(movieSessionMapper.toEntity(movieSessionRequest)));
    }

    @PutMapping("/{id}")
    public MovieSessionResponse update(@PathVariable String id, @RequestBody MovieSessionRequest movieSessionUpdate) {
        return this.getSessionResponse(movieSessionService.update(id, movieSessionMapper.toEntity(id, movieSessionUpdate)));
    }

    @PatchMapping("/{id}")
    public MovieSessionResponse patchSeatIndices(@PathVariable String id, @RequestBody MovieSessionPatchRequest movieSessionPatchRequest) {
        return this.getSessionResponse(this.movieSessionService.patch(id, movieSessionPatchRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        this.movieSessionService.delete(id);
    }

    @GetMapping(params={"cinema", "sessionStatus"})
    public List<MovieSessionResponse> getUpcomingMovieSessionsByCinema(@RequestParam("cinema") String cinemaId, @RequestParam("sessionStatus") String sessionStatus) {
        if (sessionStatus.equals("upcoming")) {
            return movieSessionService.getUpcomingMovieSessionsByCinemaId(cinemaId).stream().map(this::getSessionResponse).collect(Collectors.toList());
        }
        else {
            return getMovieSessionsByCinema(cinemaId);
        }
    }

    @GetMapping(params={"cinema"})
    public List<MovieSessionResponse> getMovieSessionsByCinema(@RequestParam("cinema") String cinemaId) {
        return movieSessionService.getAllByCinemaId(cinemaId).stream().map(this::getSessionResponse).collect(Collectors.toList());
    }

    @GetMapping(params={"movie", "sessionStatus"})
    public List<MovieSessionResponse> getUpcomingMovieSessionsByMovie(@RequestParam("movie") String movieId, @RequestParam("sessionStatus") String sessionStatus) {
        if (sessionStatus.equals("upcoming")) {
            return movieSessionService.getUpcomingMovieSessionsByMovieId(movieId).stream().map(this::getSessionResponse).collect(Collectors.toList());
        }
        else {
            return getMovieSessionsByMovie(movieId);
        }
    }

    @GetMapping(params={"movie"})
    public List<MovieSessionResponse> getMovieSessionsByMovie(@RequestParam("movie") String movieId) {
        return movieSessionService.getAllByMovieId(movieId).stream().map(this::getSessionResponse).collect(Collectors.toList());
    }


    private MovieSessionResponse getSessionResponse(MovieSession movieSession) {
        Movie movie = movieService.getById(movieSession.getMovieId());
        House house = houseService.getById(movieSession.getHouseId());
        List<GenreResponse> genres = movie.getGenreIds() != null? movie.getGenreIds().stream().map(id -> genreMapper.toResponse(genreService.getById(id))).collect(Collectors.toList()) : Collections.emptyList();
        CinemaResponse cinema = cinemaMapper.toResponse(cinemaService.getById(house.getCinemaId()));
        return movieSessionMapper.toResponse(movieSession, movieMapper.toResponse(movie, genres), houseMapper.toResponse(house, cinema));
    }
}
