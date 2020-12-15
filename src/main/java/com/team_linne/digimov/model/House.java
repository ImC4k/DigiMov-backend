package com.team_linne.digimov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class House {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String cinemaId;
    private String name;
    private Integer capacity;

    public House(String cinemaId, String name, Integer capacity) {
        this.cinemaId = cinemaId;
        this.name = name;
        this.capacity = capacity;
    }
}
