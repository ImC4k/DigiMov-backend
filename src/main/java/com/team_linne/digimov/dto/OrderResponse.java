package com.team_linne.digimov.dto;

import com.team_linne.digimov.model.MovieSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private  String email;
    private List<Integer> bookedSeatIndices;
    private Map<String,Integer> customerGroupQuantityMap;
    private MovieSessionResponse movieSession;
    private String creditCardNumber;
}
