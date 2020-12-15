package com.team_linne.digimov.service;

import com.team_linne.digimov.model.House;
import com.team_linne.digimov.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {
    @Autowired
    private HouseRepository houseRepository;

    public List<House> getAll() {
        return houseRepository.findAll();
    }
}
