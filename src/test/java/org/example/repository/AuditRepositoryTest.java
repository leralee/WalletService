package org.example.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.Audit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author valeriali on {10.10.2023}
 * @project walletService
 */
public class AuditRepositoryTest {
    private AuditRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new AuditRepository();
    }

    @Test
    public void addRecord_ShouldAddAuditRecord() {
        Audit audit = new Audit(1L, Audit.ActionType.LOGIN, LocalDateTime.now());
        repository.addRecord(audit);

        List<Audit> records = repository.getAllRecords();
        assertTrue(records.contains(audit));
    }

    @Test
    public void getAllRecords_ShouldReturnAllAddedRecords() {
        Audit audit1 = new Audit(1L, Audit.ActionType.LOGIN, LocalDateTime.now());
        Audit audit2 = new Audit(2L, Audit.ActionType.LOGOUT, LocalDateTime.now());

        repository.addRecord(audit1);
        repository.addRecord(audit2);

        List<Audit> records = repository.getAllRecords();
        assertEquals(2, records.size());
        assertTrue(records.contains(audit1));
        assertTrue(records.contains(audit2));
    }
}
