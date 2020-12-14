package com.team_linne.digimov.dto;

import java.util.List;

public class MovieRequest {
    private String name;
    private Integer duration;
    private List<String> genreIds;
    private String director;
    private String description;
    private String imageUrl;
    private String rating;

    public MovieRequest() {

    }

    public MovieRequest(String name, Integer duration, List<String> genreIds, String director, String description, String imageUrl, String rating) {
        this.name = name;
        this.duration = duration;
        this.genreIds = genreIds;
        this.director = director;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
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

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
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
