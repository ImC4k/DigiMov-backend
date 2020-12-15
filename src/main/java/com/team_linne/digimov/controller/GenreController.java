package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.GenreRequest;
import com.team_linne.digimov.dto.GenreResponse;
import com.team_linne.digimov.mapper.GenreMapper;
import com.team_linne.digimov.model.Genre;
import com.team_linne.digimov.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/genres")
public class GenreController {
    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreMapper genreMapper;

    @GetMapping
    public List<GenreResponse> getAll() {
        return genreService.getAll().stream()
                .map(genreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GenreResponse getById(@PathVariable String id) {
        return genreMapper.toResponse(genreService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreResponse create(@RequestBody GenreRequest genreRequest) {
        Genre genre = genreService.create(genreMapper.toEntity(genreRequest));
        return genreMapper.toResponse(genre);
    }

    @PutMapping("/{id}")
    public GenreResponse update(@PathVariable String id, @RequestBody GenreRequest genreUpdate) {
        Genre genre = genreService.update(id, genreMapper.toEntity(genreUpdate));
        return genreMapper.toResponse(genre);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        genreService.delete(id);
    }
}
