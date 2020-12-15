package com.team_linne.digimov.controller;

import com.team_linne.digimov.dto.CinemaResponse;
import com.team_linne.digimov.dto.HouseRequest;
import com.team_linne.digimov.dto.HouseResponse;
import com.team_linne.digimov.mapper.CinemaMapper;
import com.team_linne.digimov.mapper.HouseMapper;
import com.team_linne.digimov.model.House;
import com.team_linne.digimov.service.CinemaService;
import com.team_linne.digimov.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/houses")
public class HouseController {
    @Autowired
    private HouseService houseService;
    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private CinemaMapper cinemaMapper;

    @GetMapping
    public List<HouseResponse> getAll() {
        return houseService.getAll().stream()
                .map(this::getHouseResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public HouseResponse getById(@PathVariable String id) {
        House house = houseService.getById(id);
        return getHouseResponse(house);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HouseResponse create(@RequestBody HouseRequest houseRequest) {
        House house = houseService.create(houseMapper.toEntity(houseRequest));
        return getHouseResponse(house);
    }

    @PutMapping("/{id}")
    public HouseResponse update(@PathVariable String id, @RequestBody HouseRequest houseUpdate) {
        House house = houseService.update(id, houseMapper.toEntity(houseUpdate));
        return getHouseResponse(house);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        houseService.delete(id);
    }

    private HouseResponse getHouseResponse(House house) {
        CinemaResponse cinema = cinemaMapper.toResponse(cinemaService.getById(house.getCinemaId()));
        return houseMapper.toResponse(house, cinema);
    }
}
