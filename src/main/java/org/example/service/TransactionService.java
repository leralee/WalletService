package org.example.service;

import org.example.interfaces.ITransactionRepository;
import org.example.model.Audit;
import org.example.model.Player;
import org.example.model.Transaction;
import org.example.model.Transaction.TransactionType;

import org.example.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сервис для выполнения транзакций в приложении Wallet Service.
 * Предоставляет методы для пополнения и снятия средств со счета игрока.
 */
public class TransactionService {
    private final ITransactionRepository transactionRepository;
    private final PlayerService playerService;

    private final AuditService auditService;


    public TransactionService(ITransactionRepository transactionRepository, PlayerService playerService,
                              AuditService auditService) {
        this.transactionRepository = transactionRepository;
        this.playerService = playerService;
        this.auditService = auditService;
    }


    /**
     * Выполняет операцию пополнения баланса игрока.
     *
     * @param player        Игрок, чей баланс нужно пополнить.
     * @param amount        Сумма для пополнения.
     * @param transactionId Уникальный идентификатор транзакции.
     * @return true, если операция прошла успешно, иначе - false.
     */
    public void credit(Player player, BigDecimal amount, UUID transactionId)
            throws TransactionExistsException, InvalidAmountException {
        if (transactionRepository.existsById(transactionId)) {
            auditService.recordAction(player.getId(), Audit.ActionType.CREDIT_FAILED);
            throw new TransactionExistsException("Транзакция с данным ID уже существует.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            auditService.recordAction(player.getId(), Audit.ActionType.CREDIT_FAILED);
            throw new InvalidAmountException("Сумма должна быть положительной.");
        }

        BigDecimal updatedBalance = player.getBalance().add(amount);
        playerService.updateBalance(player, updatedBalance);

        Transaction transaction = new Transaction(player.getId(), TransactionType.DEBIT, amount, transactionId);
        transactionRepository.addTransaction(transaction);

        auditService.recordAction(player.getId(), Audit.ActionType.CREDIT);

    }

    /**
     * Списывает заданную сумму с баланса игрока.
     *
     * @param player        Объект игрока, с чьего баланса будет списана сумма.
     * @param amount        Сумма для списания.
     * @param transactionId Идентификатор транзакции.
     * @return true, если транзакция прошла успешно, иначе - false.
     */
    public void withdraw(Player player, BigDecimal amount, UUID transactionId) throws
            TransactionExistsException, InvalidAmountException {

        checkTransactionIdExists(transactionId, player);
        checkAmountPositive(amount, player);
        checkSufficientBalance(player, amount);

        BigDecimal updatedBalance = player.getBalance().subtract(amount);
        playerService.updateBalance(player, updatedBalance);

        Transaction transaction = new Transaction(player.getId(), TransactionType.CREDIT, amount, transactionId);
        transactionRepository.addTransaction(transaction);

        auditService.recordAction(player.getId(), Audit.ActionType.WITHDRAW);
    }

    private void checkTransactionIdExists(UUID transactionId, Player player) throws TransactionExistsException {
        if (transactionRepository.existsById(transactionId)) {
            auditService.recordAction(player.getId(), Audit.ActionType.WITHDRAW_FAILED);
            throw new TransactionExistsException("Транзакция с данным ID уже существует.");
        }
    }

    private void checkAmountPositive(BigDecimal amount, Player player) throws InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            auditService.recordAction(player.getId(), Audit.ActionType.WITHDRAW_FAILED);
            throw new InvalidAmountException("Сумма должна быть положительной.");
        }
    }

    private void checkSufficientBalance(Player player, BigDecimal amount) throws InvalidAmountException {
        if (player.getBalance().compareTo(amount) < 0) {
            auditService.recordAction(player.getId(), Audit.ActionType.WITHDRAW_FAILED);
            throw new InvalidAmountException("На вашем счету недастаточно средств");
        }
    }

    public class TransactionExistsException extends Exception {
        public TransactionExistsException(String message) {
            super(message);
        }
    }

    public class InvalidAmountException extends Exception {
        public InvalidAmountException(String message) {
            super(message);
        }
    }

}
