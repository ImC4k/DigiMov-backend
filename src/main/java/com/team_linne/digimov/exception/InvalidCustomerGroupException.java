package com.team_linne.digimov.exception;

public class InvalidCustomerGroupException extends RuntimeException{

    public static final String INVALID_CUSTOMER_GROUP = "Invalid customer group";

    public InvalidCustomerGroupException() {
        super(INVALID_CUSTOMER_GROUP);
    }
}
