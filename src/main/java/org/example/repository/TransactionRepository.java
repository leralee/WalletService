package org.example.repository;

import org.example.model.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Репозиторий для хранения и управления транзакциями.
 */
public class TransactionRepository {
    private final Map<UUID, Transaction> transactions = new HashMap<>();

    /**
     * Добавляет новую транзакцию в репозиторий.
     *
     * @param transaction Объект транзакции, который необходимо добавить.
     */
    public void addTransaction(Transaction transaction) {
        transactions.put(transaction.getTransactionId(), transaction);
    }

    /**
     * Проверяет существование транзакции по указанному идентификатору.
     *
     * @param transactionId Идентификатор транзакции для проверки.
     * @return true, если транзакция с таким идентификатором существует, иначе false.
     */
    public boolean existsById(UUID transactionId) {
        return transactions.containsKey(transactionId);
    }
}
