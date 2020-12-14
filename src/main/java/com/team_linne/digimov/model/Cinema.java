package com.team_linne.digimov.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
public class Cinema {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
    private String address;
    private String imageUrl;

    public Cinema(String name, String address, String imageUrl) {
        this.name = name;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    public Cinema() {

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
