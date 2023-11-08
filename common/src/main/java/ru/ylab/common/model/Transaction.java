package ru.ylab.common.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Класс, представляющий собой транзакцию в системе. Содержит информацию о транзакции,
 * такую как идентификатор транзакции, идентификатор игрока, сумма транзакции,
 * тип транзакции и время транзакции.
 */
public class Transaction {
    private UUID transactionId;
    private long playerId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime timestamp;

    /**
     * Перечисление возможных типов транзакций.
     */
    public enum TransactionType {
        DEBIT,
        CREDIT
    }

    /**
     * Конструктор для создания новой транзакции.
     *
     * @param playerId Идентификатор игрока.
     * @param transactionType Тип транзакции (DEBIT или CREDIT).
     * @param amount Сумма транзакции.
     * @param transactionId Уникальный идентификатор транзакции.
     */
    public Transaction(long playerId, TransactionType transactionType, BigDecimal amount, UUID transactionId) {
        this.transactionId = transactionId;
        this.playerId = playerId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Возвращает ID транзакции.
     *
     * @return ID транзакции
     */
    public UUID getTransactionId() {
        return transactionId;
    }

    /**
     * Устанавливает ID транзакции.
     *
     * @param transactionId уникальный идентификатор транзакции
     */
    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Возвращает ID игрока.
     *
     * @return ID игрока
     */
    public long getPlayerId() {
        return playerId;
    }

    /**
     * Устанавливает ID игрока.
     *
     * @param playerId идентификатор игрока
     */
    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    /**
     * Возвращает сумму транзакции.
     *
     * @return сумма транзакции
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Устанавливает сумму транзакции.
     *
     * @param amount сумма транзакции
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Возвращает тип транзакции.
     *
     * @return тип транзакции
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * Устанавливает тип транзакции.
     *
     * @param transactionType тип транзакции
     */
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * Возвращает временную метку транзакции.
     *
     * @return временная метка транзакции
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Устанавливает временную метку транзакции.
     *
     * @param timestamp временная метка создания или обработки транзакции
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }


    /**
     * Представляет объект транзакции в виде строки.
     *
     * @return Строковое представление транзакции.
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", playerId=" + playerId +
                ", amount=" + amount +
                ", transactionType=" + transactionType +
                ", timestamp=" + timestamp +
                '}';
    }
}
