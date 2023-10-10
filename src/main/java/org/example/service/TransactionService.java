package org.example.service;

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
    private final TransactionRepository transactionRepository;
    private final PlayerService playerService;

    private final AuditService auditService;


    public TransactionService(TransactionRepository transactionRepository, PlayerService playerService,
                              AuditService auditService) {
        this.transactionRepository = transactionRepository;
        this.playerService = playerService;
        this.auditService = auditService;
    }



    /**
     * Выполняет операцию пополнения баланса игрока.
     *
     * @param player Игрок, чей баланс нужно пополнить.
     * @param amount Сумма для пополнения.
     * @param transactionId Уникальный идентификатор транзакции.
     * @return true, если операция прошла успешно, иначе - false.
     */
    public boolean credit(Player player, BigDecimal amount, UUID transactionId) {
        if (transactionRepository.existsById(transactionId)) {
            System.out.println("Транзакция с данным ID уже существует.");
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Сумма кредита должна быть положительной.");
            return false;
        }

        BigDecimal updatedBalance = player.getBalance().add(amount);
        playerService.updateBalance(player, updatedBalance);

        Transaction transaction = new Transaction(player.getId(), TransactionType.DEBIT, amount, transactionId);
        transactionRepository.addTransaction(transaction);

        return true;
    }

    /**
     * Списывает заданную сумму с баланса игрока.
     *
     * @param player Объект игрока, с чьего баланса будет списана сумма.
     * @param amount Сумма для списания.
     * @param transactionId Идентификатор транзакции.
     * @return true, если транзакция прошла успешно, иначе - false.
     */
    public boolean withdraw(Player player, BigDecimal amount, UUID transactionId) {
        if (transactionRepository.existsById(transactionId)) {
            System.out.println("Транзакция с данным ID уже существует.");
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Сумма для снятия должна быть положительной.");
            return false;
        }

        if (player.getBalance().compareTo(amount) < 0) {
            System.out.println("На вашем счету недостаточно средств.");
            return false;
        }

        BigDecimal updatedBalance = player.getBalance().subtract(amount);
        playerService.updateBalance(player, updatedBalance);

        Transaction transaction = new Transaction(player.getId(), TransactionType.CREDIT, amount, transactionId);
        transactionRepository.addTransaction(transaction);
        return true;
    }

}
