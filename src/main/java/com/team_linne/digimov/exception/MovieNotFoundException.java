package com.team_linne.digimov.exception;

public class MovieNotFoundException extends RuntimeException{

    public static final String MOVIE_NOT_FOUND = "Movie not found";

    public MovieNotFoundException() {
        super(MOVIE_NOT_FOUND);
    }
}
