package com.team_linne.digimov.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.Map;

@Document
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class Order {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String email;
    private String userId;
    private String movieSessionId;
    private List<Integer> bookedSeatIndices;
    private Map<String,Integer> customerGroupQuantityMap;
    private String creditCardNumber;

    public Order(String email, String userId, String movieSessionId, List<Integer> bookedSeatIndices, Map<String, Integer> customerGroupQuantityMap, String creditCardNumber) {
        this.email = email;
        this.userId = userId;
        this.movieSessionId = movieSessionId;
        this.bookedSeatIndices = bookedSeatIndices;
        this.customerGroupQuantityMap = customerGroupQuantityMap;
        this.creditCardNumber = creditCardNumber;
    }
}
