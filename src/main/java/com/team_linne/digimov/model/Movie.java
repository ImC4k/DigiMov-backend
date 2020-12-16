package com.team_linne.digimov.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Movie {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
    private Integer duration;
    private List<String> genreIds;
    private String director;
    private String description;
    private String imageUrl;
    private String rating;
    private List<String> cast;
    private String language;

    public Movie(String name, Integer duration, List<String> genreIds, String director, String description, String imageUrl, String rating, List<String> cast, String language) {
        this.name = name;
        this.duration = duration;
        this.genreIds = genreIds;
        this.director = director;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.cast = cast;
        this.language = language;
    }
}
