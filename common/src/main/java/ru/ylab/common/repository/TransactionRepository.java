package ru.ylab.common.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ylab.common.interfaces.ITransactionRepository;
import ru.ylab.common.model.Player;
import ru.ylab.common.model.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Репозиторий для хранения и управления транзакциями.
 */
@Repository
public class TransactionRepository implements ITransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Добавляет новую транзакцию в репозиторий.
     *
     * @param transaction Объект транзакции, который необходимо добавить.
     */
    public void addTransaction(Transaction transaction) {
        String query = "INSERT INTO wallet.transaction " +
                "(transaction_id, player_id, amount, transaction_type, timestamp) VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(query,
                    transaction.getTransactionId(),
                    transaction.getPlayerId(),
                    transaction.getAmount(),
                    transaction.getTransactionType().toString(),
                    Timestamp.valueOf(transaction.getTimestamp()));
        } catch (DataAccessException e) {
            System.err.println("Ошибка при добавлении транзакции: " + e.getMessage());
        }
    }

    /**
     * Проверяет существование транзакции по указанному идентификатору.
     *
     * @param transactionId Идентификатор транзакции для проверки.
     * @return true, если транзакция с таким идентификатором существует, иначе false.
     */
    public boolean existsById(UUID transactionId) {
        String query = "SELECT COUNT(*) FROM wallet.transaction WHERE transaction_id = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(query, new Object[]{transactionId}, Integer.class);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            System.err.println("Ошибка при проверке наличия транзакции: " + e.getMessage());
            return false;
        }
    }

}
