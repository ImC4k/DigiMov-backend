package com.team_linne.digimov.exception;

public class MovieSessionOverException extends RuntimeException {
    private static final String MOVIE_SESSION_OVER = "Movie session already over";

    public MovieSessionOverException() {
        super(MOVIE_SESSION_OVER);
    }
}
