package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Класс, представляющий собой транзакцию в системе. Содержит информацию о транзакции,
 * такую как идентификатор транзакции, идентификатор игрока, сумма транзакции,
 * тип транзакции и временной штамп транзакции.
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
     * @return Уникальный идентификатор транзакции.
     */
    public UUID getTransactionId() {
        return transactionId;
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
