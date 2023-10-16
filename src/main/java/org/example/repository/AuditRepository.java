package org.example.repository;

import org.example.interfaces.IAuditRepository;
import org.example.model.Audit;

import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с записями аудита в приложении Wallet Service.
 * Позволяет сохранять и извлекать записи аудита.
 */
public class AuditRepository implements IAuditRepository {
    private final List<Audit> auditRecords = new ArrayList<>();

    /**
     * Сохраняет запись аудита в базу данных.
     *
     * @param record Запись аудита для сохранения.
     */
    public void addRecord(Audit record) {
        auditRecords.add(record);
    }

    /**
     * Извлекает все записи аудита из базы данных.
     *
     * @return Список записей аудита.
     */
    public List<Audit> getAllRecords() {
        return new ArrayList<>(auditRecords);
    }
}
