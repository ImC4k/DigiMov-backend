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
    private String openingHours;
    private String hotline;

    public Cinema(String name, String address, String imageUrl, String openingHours, String hotline) {
        this.name = name;
        this.address = address;
        this.imageUrl = imageUrl;
        this.openingHours = openingHours;
        this.hotline = hotline;
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

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }
}
