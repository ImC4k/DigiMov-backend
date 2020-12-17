package com.team_linne.digimov.exception;

public class OrderNotFoundException extends RuntimeException{

    public static final String ORDER_NOT_FOUND = "Order not found";

    public OrderNotFoundException() {
        super(ORDER_NOT_FOUND);
    }
}
