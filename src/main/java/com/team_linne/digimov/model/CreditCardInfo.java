package com.team_linne.digimov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardInfo {
    private String number;
    private ExpiryDate expiryDate;
    private Integer cvv;
    private String holderName;
}
