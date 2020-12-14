package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.CinemaNotFoundException;
import com.team_linne.digimov.model.Cinema;
import com.team_linne.digimov.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaService {
    @Autowired
    CinemaRepository cinemaRepository;

    public List<Cinema> getAll() {
        return cinemaRepository.findAll();
    }

    public Cinema getById(String id) {
        return cinemaRepository.findById(id).orElseThrow(CinemaNotFoundException::new);
    }

    public Cinema create(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }
}
