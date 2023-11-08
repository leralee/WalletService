package ylab.ru.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ylab.common.model.ActionType;
import ru.ylab.common.model.Audit;
import ru.ylab.common.repository.AuditRepository;
import ru.ylab.common.service.AuditService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AuditServiceTest {

    private AuditRepository auditRepository;
    private AuditService auditService;

    @BeforeEach
    void setUp() {
        auditRepository = mock(AuditRepository.class);
        auditService = new AuditService(auditRepository);
    }

    @Test
    @DisplayName("Запись действия: Должен добавить запись в репозиторий")
    void recordAction_ShouldAddRecordToRepository() {
        Long playerId = 1L;
        ActionType actionType = ActionType.REGISTRATION_SUCCESS;

        auditService.recordAction(playerId, actionType);

        verify(auditRepository).addRecord(argThat(record ->
                Objects.equals(record.getPlayerId(), playerId) &&
                        record.getActionType().equals(actionType)
        ));
    }

    @Test
    @DisplayName("Получить записи: Должен вернуть все записи из репозитория")
    void getRecords_ShouldReturnAllRecordsFromRepository() {
        List<Audit> expectedRecords = Arrays.asList(
                new Audit(1L, ActionType.REGISTRATION_SUCCESS, LocalDateTime.now()),
                new Audit(2L, ActionType.LOGIN, LocalDateTime.now())
        );
        when(auditRepository.getAllRecords()).thenReturn(expectedRecords);

        List<Audit> actualRecords = auditService.getRecords();

        assertThat(actualRecords).isEqualTo(expectedRecords);
    }
}
