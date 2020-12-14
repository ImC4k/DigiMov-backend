package com.team_linne.digimov.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
public class Movie {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
    private Integer duration;
    private List<String> genreIdList;
    private String director;
    private String description;
    private String imageUrl;
    private String rating;

    public Movie() {

    }

    public Movie(String name, Integer duration, List<String> genreIdList, String director, String description, String imageUrl, String rating) {
        this.name = name;
        this.duration = duration;
        this.genreIdList = genreIdList;
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

    public List<String> getGenreIdList() {
        return genreIdList;
    }

    public void setGenreIdList(List<String> genreIdList) {
        this.genreIdList = genreIdList;
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
