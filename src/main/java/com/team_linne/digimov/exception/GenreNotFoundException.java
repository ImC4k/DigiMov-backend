package com.team_linne.digimov.exception;

public class GenreNotFoundException extends  RuntimeException{

    public static final String GENRE_NOT_FOUND = "Genre not found";

    public GenreNotFoundException() {
        super(GENRE_NOT_FOUND);
    }
}
