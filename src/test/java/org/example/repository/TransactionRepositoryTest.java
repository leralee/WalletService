package org.example.repository;

import org.example.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class TransactionRepositoryTest {
    private TransactionRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new TransactionRepository();
    }

    @Test
    @DisplayName("Добавление транзакции: Должен добавлять транзакцию в репозиторий")
    public void addTransaction_ShouldAddTransactionToRepository() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction(1L, Transaction.TransactionType.DEBIT,
                BigDecimal.TEN, transactionId);
        repository.addTransaction(transaction);

        assertThat(repository.existsById(transactionId)).isTrue();
    }

    @Test
    @DisplayName("Существование по ID: Должен возвращать true, если существует")
    public void existsById_ShouldReturnTrueIfExists() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction(1L, Transaction.TransactionType.CREDIT,
                BigDecimal.TEN, transactionId);
        repository.addTransaction(transaction);

        assertThat(repository.existsById(transactionId)).isTrue();
    }

    @Test
    @DisplayName("Существование по ID: Должен возвращать false, если не существует")
    public void existsById_ShouldReturnFalseIfNotExists() {
        UUID nonExistentTransactionId = UUID.randomUUID();
        assertThat(repository.existsById(nonExistentTransactionId)).isFalse();
    }
}
