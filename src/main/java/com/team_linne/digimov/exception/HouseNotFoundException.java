package com.team_linne.digimov.exception;

public class HouseNotFoundException extends RuntimeException{
    public HouseNotFoundException() {
        super("House not found");
    }
}
