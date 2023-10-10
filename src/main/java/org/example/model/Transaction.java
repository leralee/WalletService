package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public class Transaction {
    private UUID transactionId;
    private long playerId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime timestamp;

    public enum TransactionType {
        DEBIT,
        CREDIT
    }

    public Transaction(long playerId, TransactionType transactionType, BigDecimal amount, UUID transactionId) {
        this.transactionId = transactionId;
        this.playerId = playerId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getTransactionId() {
        return transactionId;
    }


    public long getPlayerId() {
        return playerId;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }


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
