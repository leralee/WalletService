package org.example.service;

import org.example.exception.InvalidAmountException;
import org.example.exception.TransactionExistsException;
import org.example.model.Audit;
import org.example.model.Player;
import org.example.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @author valeriali on {10.10.2023}
 * @project walletService
 */
public class TransactionServiceTest {
    private TransactionService transactionService;
    private TransactionRepository transactionRepository;
    private PlayerService playerService;
    private AuditService auditService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        playerService = mock(PlayerService.class);
        auditService = mock(AuditService.class);
        transactionService = new TransactionService(transactionRepository, playerService, auditService);
    }

    @Test
    @DisplayName("Успешное снятие средств")
    void withdraw_Successful() throws Exception {
        Player player = new Player();
        player.setId(1L);
        player.setBalance(BigDecimal.valueOf(100));
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        transactionService.withdraw(player, BigDecimal.valueOf(50), transactionId);
        verify(playerService).updateBalance(eq(player), eq(BigDecimal.valueOf(50)));
        verify(auditService).recordAction(eq(player.getId()), eq(Audit.ActionType.WITHDRAW));
    }

    @Test
    @DisplayName("Снятие средств с уже существующим ID")
    void withdraw_ExistingTransactionId() {
        Player player = new Player();
        player.setId(1L);

        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.existsById(transactionId)).thenReturn(true);

        assertThatThrownBy(() -> transactionService.withdraw(player, BigDecimal.TEN, transactionId))
                .isInstanceOf(TransactionExistsException.class);

        verify(playerService, never()).updateBalance(any(Player.class), any(BigDecimal.class));
        verify(auditService).recordAction(anyLong(), eq(Audit.ActionType.WITHDRAW_FAILED));
    }

    @Test
    @DisplayName("Проведение транзакции при существующем ID транзакции")
    void credit_TransactionExists() {
        UUID transactionId = UUID.randomUUID();
        Player player = new Player();

        when(transactionRepository.existsById(transactionId)).thenReturn(true);

        assertThatThrownBy(() -> transactionService.credit(player, BigDecimal.TEN, transactionId))
                .isInstanceOf(TransactionExistsException.class);


        verify(auditService, times(1)).recordAction(player.getId(), Audit.ActionType.CREDIT_FAILED);
        verify(playerService, never()).updateBalance(any(), any());
        verify(transactionRepository, never()).addTransaction(any());
    }

    @Test
    @DisplayName("Пополнение счета с отрицательной или нулевой суммой")
    void credit_NegativeAmount() {
        UUID transactionId = UUID.randomUUID();
        Player player = new Player();
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertThatThrownBy(() -> transactionService.credit(player, BigDecimal.ZERO, transactionId))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    @DisplayName("Снятие суммы больше остатка на счете")
    void withdraw_AmountGreaterThanBalance() {
        Player player = new Player();
        player.setId(1L);
        player.setBalance(BigDecimal.valueOf(40));
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertThatThrownBy(() -> transactionService.withdraw(player, BigDecimal.valueOf(50), transactionId))
                .isInstanceOf(InvalidAmountException.class);

        verify(playerService, never()).updateBalance(any(Player.class), any(BigDecimal.class));
        verify(auditService).recordAction(anyLong(), eq(Audit.ActionType.WITHDRAW_FAILED));
    }

    @Test
    @DisplayName("Снятие отрицательной суммы")
    void withdraw_NegativeAmount() {
        Player player = new Player();
        player.setId(1L);
        player.setBalance(BigDecimal.valueOf(100));
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertThatThrownBy(() -> transactionService.withdraw(player, BigDecimal.valueOf(-10), transactionId))
                .isInstanceOf(InvalidAmountException.class);

        verify(playerService, never()).updateBalance(any(Player.class), any(BigDecimal.class));
        verify(auditService).recordAction(anyLong(), eq(Audit.ActionType.WITHDRAW_FAILED));
    }
}
