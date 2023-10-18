package org.example.repository;

import org.example.interfaces.IAuditRepository;
import org.example.model.Audit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с записями аудита в приложении Wallet Service.
 * Позволяет сохранять и извлекать записи аудита.
 */
public class AuditRepository implements IAuditRepository {
    /**
     * Сохраняет запись аудита в базу данных.
     *
     * @param record Запись аудита для сохранения.
     */
    public void addRecord(Audit record) {
        String query = "INSERT INTO wallet.audit (player_id, action_type, timestamp) VALUES (?, ?, ?)";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, record.getPlayerId());
                preparedStatement.setString(2, record.getActionType().toString());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(record.getTimestamp()));

                preparedStatement.executeUpdate();
                } catch (SQLException | ClassNotFoundException e) {
                System.err.println("Ошибка при добавлении записи аудита: "
                        + e.getMessage());
            }
    }

    /**
     * Извлекает все записи аудита из базы данных.
     *
     * @return Список записей аудита.
     */
    public List<Audit> getAllRecords() {
        String query = "SELECT * FROM wallet.audit";
        List<Audit> records = new ArrayList<>();
        ResultSet resultSet = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Audit audit = new Audit();
                audit.setPlayerId(resultSet.getLong("player_id"));
                audit.setActionType(Audit.ActionType.valueOf(resultSet.getString("action_type")));
                audit.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());
                records.add(audit);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Ошибка при получении записи аудита: "
                    + e.getMessage());
        }
        return records;
    }
}
