package ru.ylab.common;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для ведения журнала аудита в приложении Wallet Service.
 * Позволяет записывать различные действия, производимые игроками, для последующего анализа.
 */

@Service
public class AuditService  {
    private final IAuditRepository auditRepository;

    public AuditService(IAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    /**
     * Сервис для ведения журнала аудита в приложении Wallet Service.
     * Позволяет записывать различные действия, производимые игроками, для последующего анализа.
     */
    public void recordAction(Long playerId, ActionType actionType) {
        Audit record = new Audit(playerId, actionType, LocalDateTime.now());
        auditRepository.addRecord(record);
    }

    /**
     * Извлекает все записи аудита.
     *
     * @return Список записей аудита.
     */
    public List<Audit> getRecords() {
        return auditRepository.getAllRecords();
    }
}
