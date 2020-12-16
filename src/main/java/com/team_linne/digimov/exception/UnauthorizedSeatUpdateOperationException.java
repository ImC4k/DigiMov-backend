package com.team_linne.digimov.exception;

public class UnauthorizedSeatUpdateOperationException extends RuntimeException {
    private static final String INVALID_SEAT_UPDATE_OPERATION = "Invalid seat update operation";
    public UnauthorizedSeatUpdateOperationException() {
        super(INVALID_SEAT_UPDATE_OPERATION);
    }
}
