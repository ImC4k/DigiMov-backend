package com.team_linne.digimov.model;

import java.util.UUID;

public class SeatStatus {
    private String status;
    private Long processStartTime;
    private String clientSessionId;

    public SeatStatus() {
    }

    public SeatStatus(String status, Long processStartTime, String clientSessionId) {
        this.status = status;
        this.processStartTime = processStartTime;
        this.clientSessionId = clientSessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getProcessStartTime() {
        return processStartTime;
    }

    public void setProcessStartTime(Long processStartTime) {
        this.processStartTime = processStartTime;
    }

    public String getClientSessionId() {
        return clientSessionId;
    }

    public void setClientSessionId(String clientSessionId) {
        this.clientSessionId = clientSessionId;
    }
}
