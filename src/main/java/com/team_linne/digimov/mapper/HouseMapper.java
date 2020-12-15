package com.team_linne.digimov.mapper;

import com.team_linne.digimov.dto.CinemaResponse;
import com.team_linne.digimov.dto.HouseRequest;
import com.team_linne.digimov.dto.HouseResponse;
import com.team_linne.digimov.model.House;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class HouseMapper {
    public House toEntity(HouseRequest houseRequest) {
        House house = new House();

        BeanUtils.copyProperties(houseRequest, house);

        return house;
    }

    public HouseResponse toResponse(House house, CinemaResponse cinema) {
        HouseResponse houseResponse = new HouseResponse();

        BeanUtils.copyProperties(house, houseResponse);

        houseResponse.setCinema(cinema);

        return houseResponse;
    }
}
