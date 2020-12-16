package com.team_linne.digimov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HouseRequest {
    private String cinemaId;
    private String name;
    private Integer capacity;
}
