package com.team_linne.digimov.dto;

public class GenreRequest {
    private String name;

    public GenreRequest() {

    }

    public GenreRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
