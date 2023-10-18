package org.example.repository;

import org.example.interfaces.ITransactionRepository;
import org.example.model.Transaction;

import java.sql.*;
import java.util.UUID;

/**
 * Репозиторий для хранения и управления транзакциями.
 */
public class TransactionRepository implements ITransactionRepository {

    /**
     * Добавляет новую транзакцию в репозиторий.
     *
     * @param transaction Объект транзакции, который необходимо добавить.
     */
    public void addTransaction(Transaction transaction) {
        String query = "INSERT INTO wallet.transaction " +
                "(transaction_id, player_id, amount, transaction_type, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, transaction.getTransactionId());
            preparedStatement.setLong(2, transaction.getPlayerId());
            preparedStatement.setBigDecimal(3, transaction.getAmount());
            preparedStatement.setString(4, transaction.getTransactionType().toString());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(transaction.getTimestamp()));

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Ошибка при добавлении транзакции: "
                    + e.getMessage());
        }
    }

    /**
     * Проверяет существование транзакции по указанному идентификатору.
     *
     * @param transactionId Идентификатор транзакции для проверки.
     * @return true, если транзакция с таким идентификатором существует, иначе false.
     */
    public boolean existsById(UUID transactionId) {
        String query = "SELECT * FROM wallet.transaction WHERE transaction_id = ?";
        boolean exists = false;

        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, transactionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                exists = true;
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Ошибка при проверке наличия записи: " + e.getMessage());
        }
        return exists;
    }
}
