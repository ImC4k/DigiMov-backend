package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.GenreNotFoundException;
import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.model.Genre;
import com.team_linne.digimov.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    @Autowired
    GenreRepository genreRepository;
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    public Genre getById(String id) {
        return genreRepository.findById(id).orElseThrow(GenreNotFoundException::new);
    }

    public Genre create(Genre genre) {
        return genreRepository.save(genre);
    }
}
