package org.example.interfaces;

import org.example.model.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author valeriali on {15.10.2023}
 * @project walletService
 */
public interface ITransactionRepository {

    /**
     * Добавляет новую транзакцию в репозиторий.
     *
     * @param transaction Объект транзакции, который необходимо добавить.
     */
    void addTransaction(Transaction transaction);

    /**
     * Проверяет существование транзакции по указанному идентификатору.
     *
     * @param transactionId Идентификатор транзакции для проверки.
     * @return true, если транзакция с таким идентификатором существует, иначе false.
     */
    boolean existsById(UUID transactionId);
}
