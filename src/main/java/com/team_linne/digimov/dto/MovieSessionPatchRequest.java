package com.team_linne.digimov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MovieSessionPatchRequest {
    List<Integer> bookedSeatIndices;
    String clientSessionId;
}
