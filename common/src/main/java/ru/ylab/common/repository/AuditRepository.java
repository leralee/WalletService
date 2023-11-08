package ru.ylab.common.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ylab.common.model.Audit;

import java.sql.Timestamp;
import java.util.List;


/**
 * Репозиторий для работы с записями аудита в приложении Wallet Service.
 * Позволяет сохранять и извлекать записи аудита.
 */

@Repository
public class AuditRepository implements IAuditRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuditRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Сохраняет запись аудита в базу данных.
     *
     * @param record Запись аудита для сохранения.
     */
    public void addRecord(Audit record) {
        String query = "INSERT INTO wallet.audit (player_id, action_type, timestamp) VALUES (?, ?, ?)";
        jdbcTemplate.update(query,
                record.getPlayerId(),
                record.getActionType().toString(),
                Timestamp.valueOf(record.getTimestamp()));
    }

    /**
     * Извлекает все записи аудита из базы данных.
     *
     * @return Список записей аудита.
     */
    public List<Audit> getAllRecords() {
        String query = "SELECT * FROM wallet.audit";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Audit.class));
    }
}
