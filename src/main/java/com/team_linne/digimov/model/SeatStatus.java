package com.team_linne.digimov.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SeatStatus {
    private String status;
    private Long processStartTime;
    private String clientSessionId;
}
