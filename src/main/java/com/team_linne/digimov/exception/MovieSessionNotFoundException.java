package com.team_linne.digimov.exception;

public class MovieSessionNotFoundException extends RuntimeException {

    public static final String MOVIE_NOT_FOUND = "Movie Session not found";

    public MovieSessionNotFoundException() {
        super(MOVIE_NOT_FOUND);
    }
}
