package org.example.service;

import org.example.model.Audit;
import org.example.repository.AuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * @author valeriali on {10.10.2023}
 * @project walletService
 */
public class AuditServiceTest {

    private AuditRepository auditRepository;
    private AuditService auditService;

    @BeforeEach
    void setUp() {
        auditRepository = mock(AuditRepository.class);
        auditService = new AuditService(auditRepository);
    }

    @Test
    void recordAction_ShouldAddRecordToRepository() {
        Long playerId = 1L;
        Audit.ActionType actionType = Audit.ActionType.REGISTRATION_SUCCESS;

        auditService.recordAction(playerId, actionType);

        verify(auditRepository).addRecord(argThat(record ->
                Objects.equals(record.getPlayerId(), playerId) &&
                        record.getActionType().equals(actionType)
        ));
    }

    @Test
    void getRecords_ShouldReturnAllRecordsFromRepository() {
        List<Audit> expectedRecords = Arrays.asList(
                new Audit(1L, Audit.ActionType.REGISTRATION_SUCCESS, LocalDateTime.now()),
                new Audit(2L, Audit.ActionType.LOGIN, LocalDateTime.now())
        );
        when(auditRepository.getAllRecords()).thenReturn(expectedRecords);

        List<Audit> actualRecords = auditService.getRecords();

        assertEquals(expectedRecords, actualRecords);
    }
}
