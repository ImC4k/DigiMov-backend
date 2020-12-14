package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.CinemaRequest;
import com.team_linne.digimov.dto.CinemaResponse;
import com.team_linne.digimov.mapper.CinemaMapper;
import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cinemas")
public class CinemaController {
    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private CinemaMapper cinemaMapper;


    @GetMapping
    public List<CinemaResponse> getAll() {
        return cinemaService.getAll().stream().map(cinemaMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CinemaResponse getById(@PathVariable String id) {
        return cinemaMapper.toResponse(cinemaService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CinemaResponse create(@RequestBody CinemaRequest cinemaRequest) {
        Cinema cinema = cinemaService.create(cinemaMapper.toEntity(cinemaRequest));
        return cinemaMapper.toResponse(cinema);
    }

    @PutMapping("/{id}")
    public CinemaResponse update(@PathVariable String id, @RequestBody CinemaRequest cinemaUpdate) {
        Cinema cinema = cinemaService.update(id, cinemaMapper.toEntity(cinemaUpdate));
        return cinemaMapper.toResponse(cinema);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        cinemaService.delete(id);
    }
}
