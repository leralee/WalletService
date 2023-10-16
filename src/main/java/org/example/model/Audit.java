package org.example.model;

import java.time.LocalDateTime;


public class Audit {

    private final Long playerId;
    private final ActionType actionType;
    private final LocalDateTime timestamp;


    public enum ActionType {
        LOGIN,
        LOGIN_FAILED,
        LOGOUT,
        CREDIT,
        CREDIT_FAILED,
        WITHDRAW,
        WITHDRAW_FAILED,
        REGISTRATION_SUCCESS,
        REGISTRATION_FAILED
    }

    public Audit(long playerId, ActionType actionType, LocalDateTime timestamp) {
        this.playerId = playerId;
        this.actionType = actionType;
        this.timestamp = timestamp;
    }

    public long getPlayerId() {
        return playerId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "playerId=" + playerId +
                ", actionType=" + actionType +
                ", timestamp=" + timestamp +
                '}';
    }
}

