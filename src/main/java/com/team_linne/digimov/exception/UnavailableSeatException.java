package com.team_linne.digimov.exception;

public class UnavailableSeatException extends RuntimeException{

    public static final String UNAVAILABLE_SEAT = "Unavailable seat";

    public UnavailableSeatException() {
        super(UNAVAILABLE_SEAT);
    }
}
