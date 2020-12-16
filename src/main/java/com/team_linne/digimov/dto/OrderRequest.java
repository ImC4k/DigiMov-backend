package com.team_linne.digimov.dto;

import com.team_linne.digimov.model.CreditCardInfo;
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
public class OrderRequest {
    private String movieSessionId;
    private List<Integer> bookedSeatIndices;
    private Map<String,Integer> customerGroupQuantityMap;
    private String email;
    private CreditCardInfo creditCardInfo;
    private String clientSessionId;
}
