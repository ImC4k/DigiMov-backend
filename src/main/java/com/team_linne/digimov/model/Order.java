package com.team_linne.digimov.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.Map;

@Document
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Order {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String email;
    private String movieSessionId;
    private List<Integer> bookedSeatIndices;
    private Map<String, Integer> customerGroupQuantityMap;
    private String creditCardNumber;

    public Order(String email, String movieSessionId, List<Integer> bookedSeatIndices, Map<String, Integer> customerGroupQuantityMap, String creditCardNumber) {
        this.email = email;
        this.movieSessionId = movieSessionId;
        this.bookedSeatIndices = bookedSeatIndices;
        this.customerGroupQuantityMap = customerGroupQuantityMap;
        this.creditCardNumber = creditCardNumber;
    }
}
