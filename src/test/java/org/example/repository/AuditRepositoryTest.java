package org.example.repository;

import org.example.model.Audit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AuditRepositoryTest {
    private AuditRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new AuditRepository();
    }

    @Test
    @DisplayName("Добавление записи: Должен добавить запись аудита")
    public void addRecord_ShouldAddAuditRecord() {
        Audit audit = new Audit(1L, Audit.ActionType.LOGIN, LocalDateTime.now());
        repository.addRecord(audit);

        List<Audit> records = repository.getAllRecords();
        assertThat(records).contains(audit);
    }

    @Test
    @DisplayName("Получить все записи: Должен вернуть все добавленные записи")
    public void getAllRecords_ShouldReturnAllAddedRecords() {
        Audit audit1 = new Audit(1L, Audit.ActionType.LOGIN, LocalDateTime.now());
        Audit audit2 = new Audit(2L, Audit.ActionType.LOGOUT, LocalDateTime.now());

        repository.addRecord(audit1);
        repository.addRecord(audit2);

        List<Audit> records = repository.getAllRecords();
        assertThat(records).hasSize(2).contains(audit1, audit2);
    }
}
