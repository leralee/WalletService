package org.example.repository;

import org.example.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author valeriali on {10.10.2023}
 * @project walletService
 */
public class TransactionRepositoryTest {
    private TransactionRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new TransactionRepository();
    }

    @Test
    public void addTransaction_ShouldAddTransactionToRepository() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction(1L, Transaction.TransactionType.DEBIT,
                BigDecimal.TEN, transactionId);
        repository.addTransaction(transaction);

        assertTrue(repository.existsById(transactionId));
    }

    @Test
    public void existsById_ShouldReturnTrueIfExists() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction(1L, Transaction.TransactionType.CREDIT,
                BigDecimal.TEN, transactionId);
        repository.addTransaction(transaction);

        assertTrue(repository.existsById(transactionId));
    }

    @Test
    public void existsById_ShouldReturnFalseIfNotExists() {
        UUID nonExistentTransactionId = UUID.randomUUID();
        assertFalse(repository.existsById(nonExistentTransactionId));
    }
}
