package com.team_linne.digimov.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;

@Document
public class MovieSession {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String movieId;
    private String houseId;
    private Long startTime;
    private Map<String, Double> prices;
    private Map<Integer, SeatStatus> occupied;

    public MovieSession() {
    }

    public MovieSession(String movieId, String houseId, Long startTime, Map<String, Double> prices, Map<Integer, SeatStatus> occupied) {
        this.movieId = movieId;
        this.houseId = houseId;
        this.startTime = startTime;
        this.prices = prices;
        this.occupied = occupied;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Map<String, Double> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Double> prices) {
        this.prices = prices;
    }

    public Map<Integer, SeatStatus> getOccupied() {
        return occupied;
    }

    public void setOccupied(Map<Integer, SeatStatus> occupied) {
        this.occupied = occupied;
    }
}
