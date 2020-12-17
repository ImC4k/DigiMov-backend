package com.team_linne.digimov.model;

import lombok.Data;

@Data
public class Identity {
    private String email;
    private String cardNumber;

    public Identity(String email, String cardNumber) {
        this.email = email;
        this.cardNumber = cardNumber;
    }

    public Identity() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
