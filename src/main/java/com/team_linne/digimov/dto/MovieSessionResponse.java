package com.team_linne.digimov.dto;

import com.team_linne.digimov.model.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MovieSessionResponse {
    private String id;
    private MovieResponse movie;
    private HouseResponse house;
    private Long startTime;
    private Map<String, Double> prices;
    private Map<Integer, SeatStatus> occupied;

    public MovieSessionResponse(MovieResponse movie, HouseResponse house, Long startTime, Map<String, Double> prices, Map<Integer, SeatStatus> occupied) {
        this.movie = movie;
        this.house = house;
        this.startTime = startTime;
        this.prices = prices;
        this.occupied = occupied;
    }
}
