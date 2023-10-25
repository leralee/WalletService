package org.example.dto;

import java.time.LocalDateTime;

/**
 * @author valeriali on {22.10.2023}
 * @project walletService
 */
public class AuditDTO {
    private Long playerId;
    private String actionType;
    private LocalDateTime timestamp;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
