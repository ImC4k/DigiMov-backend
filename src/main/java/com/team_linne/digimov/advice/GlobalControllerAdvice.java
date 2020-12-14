package com.team_linne.digimov.advice;

import com.team_linne.digimov.exception.CinemaNotFoundException;
import com.team_linne.digimov.exception.GenreNotFoundException;
import com.team_linne.digimov.exception.MovieNotFoundException;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({MovieNotFoundException.class})
    public ErrorResponse handleMovieNotFound(MovieNotFoundException exception){
        return new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.name());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({GenreNotFoundException.class})
    public ErrorResponse handleGenreNotFound(GenreNotFoundException exception){
        return new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.name());
    }
}
