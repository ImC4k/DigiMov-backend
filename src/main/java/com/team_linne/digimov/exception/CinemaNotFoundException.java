package com.team_linne.digimov.exception;

public class CinemaNotFoundException extends RuntimeException{
    private static final String CINEMA_NOT_FOUND = "Cinema not found";
    public CinemaNotFoundException() {
        super(CINEMA_NOT_FOUND);
    }
}
