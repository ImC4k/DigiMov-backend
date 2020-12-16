package com.team_linne.digimov.exception;

public class InvalidSeatUpdateOperationException extends RuntimeException{

    private static final String INVALID_SEAT_UPDATE_OPERATION = "Invalid seat update operation";

    public InvalidSeatUpdateOperationException() {
        super(INVALID_SEAT_UPDATE_OPERATION);
    }
}
