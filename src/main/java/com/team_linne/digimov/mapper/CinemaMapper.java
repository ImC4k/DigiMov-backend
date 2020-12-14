package com.team_linne.digimov.mapper;

import com.team_linne.digimov.dto.CinemaRequest;
import com.team_linne.digimov.dto.CinemaResponse;
import com.team_linne.digimov.model.Cinema;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CinemaMapper {
    public Cinema toEntity(CinemaRequest cinemaRequest) {
        Cinema cinema = new Cinema();

        BeanUtils.copyProperties(cinemaRequest, cinema);

        return cinema;
    }

    public CinemaResponse toResponse(Cinema cinema) {
        CinemaResponse cinemaResponse = new CinemaResponse();

        BeanUtils.copyProperties(cinema, cinemaResponse);

        return cinemaResponse;
    }
}
