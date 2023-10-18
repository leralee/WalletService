package org.example.model;

import java.time.LocalDateTime;

/**
 * Класс представляющий собой запись аудита. Используется для отслеживания
 * различных действий, связанных с игроками в системе.
 */
public class Audit {

    private Long playerId;
    private ActionType actionType;
    private LocalDateTime timestamp;

    /**
     * Перечисление возможных типов действий, которые могут быть зафиксированы в записи аудита.
     */
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

    /**
     * Создает новую запись аудита.
     *
     * @param playerId Идентификатор игрока, выполнившего действие.
     * @param actionType Тип действия.
     * @param timestamp Временная метка, когда было выполнено действие.
     */
    public Audit(Long playerId, ActionType actionType, LocalDateTime timestamp) {
        this.playerId = playerId;
        this.actionType = actionType;
        this.timestamp = timestamp;
    }

    public Audit() {
    }

    /**
     * @return Идентификатор игрока, связанного с этой записью аудита.
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * @return Тип действия, связанный с этой записью аудита.
     */
    public ActionType getActionType() {
        return actionType;
    }

    /**
     * @return Временная метка, связанная с этой записью аудита.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Устанавливает идентификатор игрока.
     *
     * @param playerId Идентификатор игрока.
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Устанавливает тип действия.
     *
     * @param actionType Тип действия.
     */
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    /**
     * Устанавливает временную метку.
     *
     * @param timestamp Временная метка действия.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Представляет объект в виде строки.
     *
     * @return Строковое представление объекта.
     */
    @Override
    public String toString() {
        return "Audit{" +
                "playerId=" + playerId +
                ", actionType=" + actionType +
                ", timestamp=" + timestamp +
                '}';
    }
}

