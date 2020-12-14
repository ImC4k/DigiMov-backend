package com.team_linne.digimov.advice;

import com.team_linne.digimov.exception.CinemaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({CinemaNotFoundException.class})
    public ErrorResponse handleCinemaNotFound(CinemaNotFoundException exception){
        return new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.name());
    }
}