package org.example.service;

import org.example.model.Audit;
import org.example.model.Player;
import org.example.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void credit_TransactionExists() {
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.existsById(transactionId)).thenReturn(true);

        assertFalse(transactionService.credit(new Player(), BigDecimal.TEN, transactionId));
    }

    @Test
    void credit_NegativeAmount() {
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertFalse(transactionService.credit(new Player(), BigDecimal.valueOf(-10), transactionId));
    }

    @Test
    void withdraw_Successful() {
        Player player = new Player();
        player.setId(1L);
        player.setBalance(BigDecimal.valueOf(100));
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertTrue(transactionService.withdraw(player, BigDecimal.valueOf(50), transactionId));
        verify(playerService).updateBalance(eq(player), eq(BigDecimal.valueOf(50)));
        verify(auditService).recordAction(eq(player.getId()), eq(Audit.ActionType.WITHDRAW));
    }

    @Test
    void withdraw_AmountGreaterThanBalance() {
        Player player = new Player();
        player.setId(1L);
        player.setBalance(BigDecimal.valueOf(40));
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertFalse(transactionService.withdraw(player, BigDecimal.valueOf(50), transactionId));
        verify(playerService, never()).updateBalance(any(Player.class), any(BigDecimal.class));
        verify(auditService, never()).recordAction(anyLong(), eq(Audit.ActionType.WITHDRAW));
    }

    @Test
    void withdraw_NegativeAmount() {
        Player player = new Player();
        player.setId(1L);
        player.setBalance(BigDecimal.valueOf(100));
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertFalse(transactionService.withdraw(player, BigDecimal.valueOf(-10), transactionId));
        verify(playerService, never()).updateBalance(any(Player.class), any(BigDecimal.class));
        verify(auditService, never()).recordAction(anyLong(), eq(Audit.ActionType.WITHDRAW));
    }

    @Test
    void withdraw_ExistingTransactionId() {
        Player player = new Player();
        player.setId(1L);
        player.setBalance(BigDecimal.valueOf(100));
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.existsById(transactionId)).thenReturn(true);

        assertFalse(transactionService.withdraw(player, BigDecimal.valueOf(50), transactionId));
        verify(playerService, never()).updateBalance(any(Player.class), any(BigDecimal.class));
        verify(auditService, never()).recordAction(anyLong(), eq(Audit.ActionType.WITHDRAW));
    }
}
