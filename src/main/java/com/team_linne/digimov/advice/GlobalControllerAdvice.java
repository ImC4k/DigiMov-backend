package com.team_linne.digimov.advice;

import com.team_linne.digimov.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({CinemaNotFoundException.class, MovieNotFoundException.class, GenreNotFoundException.class, MovieSessionNotFoundException.class, HouseNotFoundException.class, OrderNotFoundException.class})
    public ErrorResponse handleModelNotFound(Exception exception) {
        return new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.name());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, InvalidCustomerGroupException.class, InvalidCreditCardInfoException.class, UnavailableSeatException.class})
    public ErrorResponse handleIllegalArgument(IllegalArgumentException exception) {
        return new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.name());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidSeatUpdateOperationException.class)
    public ErrorResponse handleInvalidSeatUpdateOperation(InvalidSeatUpdateOperationException exception) {
        return new ErrorResponse(exception.getMessage(), HttpStatus.FORBIDDEN.name());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedSeatUpdateOperationException.class)
    public ErrorResponse handleUnauthorizedSeatUpdateOperation(UnauthorizedSeatUpdateOperationException exception) {
        return new ErrorResponse(exception.getMessage(), HttpStatus.FORBIDDEN.name());
    }
}
