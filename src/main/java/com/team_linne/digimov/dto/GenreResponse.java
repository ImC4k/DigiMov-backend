package com.team_linne.digimov.dto;

public class GenreResponse {
    private String id;
    private String name;

    public GenreResponse() {

    }

    public GenreResponse(String id, String name) {
        this.id = id;
        this.name = name;
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
}
