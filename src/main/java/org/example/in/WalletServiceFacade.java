package org.example.in;

import org.example.exception.InvalidAmountException;
import org.example.exception.TransactionExistsException;
import org.example.model.Audit;
import org.example.model.Player;
import org.example.service.AuditService;
import org.example.service.PlayerService;
import org.example.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class WalletServiceFacade {
    private final PlayerService playerService;
    private final TransactionService transactionService;
    private final AuditService auditService;

    public WalletServiceFacade(PlayerService playerService,
                               TransactionService transactionService, AuditService auditService) {
        this.playerService = playerService;
        this.transactionService = transactionService;
        this.auditService = auditService;
    }

    public Optional<Player> registerPlayer(String username, String password) {
        return playerService.registerPlayer(username, password, Player.Role.USER);
    }

    public Player authorizePlayer(String username, String password) {
        return playerService.authorizePlayer(username, password);
    }

    public BigDecimal getPlayerBalance(Long id) {
        return playerService.getPlayerBalance(id);
    }

    public void credit(Player player, BigDecimal amount, UUID transactionId) throws
            InvalidAmountException, TransactionExistsException {
        transactionService.credit(player, amount, transactionId);
    }

    public void withdraw(Player player, BigDecimal amount, UUID transactionId)
            throws InvalidAmountException, TransactionExistsException {
        transactionService.withdraw(player, amount, transactionId);
    }

    public List<Audit> getRecords() {
        return auditService.getRecords();
    }

    public void logout(Long id) {
        auditService.recordAction(id, Audit.ActionType.LOGOUT);
    }
}
