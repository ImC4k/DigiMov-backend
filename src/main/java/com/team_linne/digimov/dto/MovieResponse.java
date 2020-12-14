package com.team_linne.digimov.dto;

import java.util.List;

public class MovieResponse {
    private String id;
    private String name;
    private Integer duration;
    private List<GenreResponse> genres;
    private String director;
    private String description;
    private String imageUrl;
    private String rating;

    public MovieResponse() {

    }

    public MovieResponse(String id, String name, Integer duration, List<GenreResponse> genres, String director, String description, String imageUrl, String rating) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.genres = genres;
        this.director = director;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<GenreResponse> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreResponse> genres) {
        this.genres = genres;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
