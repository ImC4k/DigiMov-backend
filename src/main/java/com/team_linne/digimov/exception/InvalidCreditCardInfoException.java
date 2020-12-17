package com.team_linne.digimov.exception;

public class InvalidCreditCardInfoException extends  RuntimeException{

    public static final String INVALID_CREDIT_CARD_INFO = "Invalid credit card info";

    public InvalidCreditCardInfoException() {
        super(INVALID_CREDIT_CARD_INFO);
    }
}
