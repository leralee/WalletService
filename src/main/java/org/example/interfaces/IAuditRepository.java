package org.example.interfaces;

import org.example.model.Audit;

import java.util.List;

/**
 * @author valeriali on {15.10.2023}
 * @project walletService
 */
public interface IAuditRepository {

    /**
     * Сохраняет запись аудита в базу данных.
     *
     * @param record Запись аудита для сохранения.
     */
    void addRecord(Audit record);

    /**
     * Извлекает все записи аудита из базы данных.
     *
     * @return Список записей аудита.
     */
    List<Audit> getAllRecords();
}
