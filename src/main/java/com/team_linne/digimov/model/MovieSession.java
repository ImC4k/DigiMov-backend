package com.team_linne.digimov.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MovieSession {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String movieId;
    private String houseId;
    private Long startTime;
    private Map<String, Double> prices;
    private Map<Integer, SeatStatus> occupied;

    public MovieSession(String movieId, String houseId, Long startTime, Map<String, Double> prices, Map<Integer, SeatStatus> occupied) {
        this.movieId = movieId;
        this.houseId = houseId;
        this.startTime = startTime;
        this.prices = prices;
        this.occupied = occupied;
    }
}
