package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.HouseNotFoundException;
import com.team_linne.digimov.model.House;
import com.team_linne.digimov.repository.CinemaRepository;
import com.team_linne.digimov.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {
    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private CinemaService cinemaService;

    public List<House> getAll() {
        return houseRepository.findAll();
    }

    public House getById(String id) {
        return houseRepository.findById(id).orElseThrow(HouseNotFoundException::new);
    }

    public House create(House houseCreate) {
        cinemaService.getById(houseCreate.getCinemaId());
        return houseRepository.save(houseCreate);
    }

    public House update(String id, House houseUpdate) {
        this.getById(id);
        houseUpdate.setId(id);
        return this.create(houseUpdate);
    }

    public void delete(String id) {
        this.getById(id);
        houseRepository.deleteById(id);
    }

    public List<House> getByCinemaId(String cinemaId) {
        cinemaService.getById(cinemaId);
        return houseRepository.findAllByCinemaId(cinemaId);
    }

    public void deleteHouseWithCinemaId(String cinemaId) {
        List<House> housesWithCinemaId = houseRepository.findAllByCinemaId(cinemaId);
        housesWithCinemaId.forEach(house -> this.delete(house.getId()));
    }
}
