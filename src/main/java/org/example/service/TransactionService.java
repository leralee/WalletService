package org.example.service;

import org.example.common.AuditService;
import org.example.common.Player;
import org.example.exception.InvalidAmountException;
import org.example.exception.TransactionExistsException;
import org.example.interfaces.ITransactionRepository;

import org.example.model.Transaction;
import org.example.model.Transaction.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сервис для выполнения транзакций в приложении Wallet Service.
 * Предоставляет методы для пополнения и снятия средств со счета игрока.
 */

@Service
public class TransactionService {
    private final ITransactionRepository transactionRepository;
    private final PlayerService playerService;

    private final AuditService auditService;


    public TransactionService(ITransactionRepository transactionRepository, PlayerService playerService, AuditService auditService) {
        this.transactionRepository = transactionRepository;
        this.playerService = playerService;
        this.auditService = auditService;
    }


    /**
     * Зачисляет указанную сумму на счет игрока.
     * <p>
     * Если транзакция с указанным ID уже существует или предоставленная сумма недействительна,
     * выполняется запись неудачного зачисления в аудит и выбрасывается соответствующее исключение.
     * </p>
     *
     * @param player        Игрок, со счета которого производится пополнеие.
     * @param amount        Сумма для зачисления.
     * @param transactionId Уникальный идентификатор транзакции.
     *
     * @throws TransactionExistsException Если транзакция с указанным ID уже существует.
     * @throws InvalidAmountException     Если предоставленная сумма недействительна (<= 0).
     */
    public void credit(Player player, BigDecimal amount, UUID transactionId)
            throws TransactionExistsException, InvalidAmountException {
        if (transactionRepository.existsById(transactionId)) {
            throw new TransactionExistsException("Транзакция с данным ID уже существует.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Сумма должна быть положительной.");
        }

        BigDecimal updatedBalance = player.getBalance().add(amount);
        playerService.updateBalance(player, updatedBalance);

        Transaction transaction = new Transaction(player.getId(), TransactionType.CREDIT, amount, transactionId);
        transactionRepository.addTransaction(transaction);
    }

    /**
     * Снимает указанную сумму со счета игрока.
     * <p>
     * Если транзакция с указанным ID уже существует или недостаточно баланса игрока,
     * выполняется запись неудачного зачисления в аудит и выбрасывается соответствующее исключение.
     * </p>
     *
     * @param player        Игрок, со счета которого производится снятие.
     * @param amount        Сумма для снятия.
     * @param transactionId Уникальный идентификатор транзакции.
     *
     * @throws TransactionExistsException Если транзакция с указанным ID уже существует.
     * @throws InvalidAmountException     Если предоставленная сумма недействительна (<= 0) или баланс игрока недостаточен.
     */
    public void withdraw(Player player, BigDecimal amount, UUID transactionId)
            throws TransactionExistsException, InvalidAmountException {
        checkTransactionIdExists(transactionId, player);
        checkAmountPositive(amount, player);
        checkSufficientBalance(player, amount);

        BigDecimal updatedBalance = player.getBalance().subtract(amount);
        playerService.updateBalance(player, updatedBalance);

        Transaction transaction = new Transaction(player.getId(), TransactionType.DEBIT, amount, transactionId);
        transactionRepository.addTransaction(transaction);
    }

    /**
     * Проверяет наличие транзакции с данным ID в репозитории транзакций.
     * Если такая транзакция уже существует, записывает неудачную операцию в историю и выбрасывает исключение.
     *
     * @param transactionId Уникальный идентификатор транзакции для проверки.
     * @param player Игрок, осуществляющий операцию.
     * @throws TransactionExistsException если транзакция с данным ID уже существует.
     */
    private void checkTransactionIdExists(UUID transactionId, Player player) throws TransactionExistsException {
        if (transactionRepository.existsById(transactionId)) {
            throw new TransactionExistsException("Транзакция с данным ID уже существует.");
        }
    }

    /**
     * Проверяет, является ли указанная сумма положительной.
     * Если сумма не положительная, записывает неудачную операцию в историю и выбрасывает исключение.
     *
     * @param amount Сумма для проверки.
     * @param player Игрок, осуществляющий операцию.
     * @throws InvalidAmountException если указанная сумма не положительная.
     */
    private void checkAmountPositive(BigDecimal amount, Player player) throws InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Сумма должна быть положительной.");
        }
    }

    /**
     * Проверяет, достаточно ли средств на балансе игрока для осуществления операции.
     * Если средств недостаточно, записывает неудачную операцию в историю и выбрасывает исключение.
     *
     * @param player Игрок, осуществляющий операцию.
     * @param amount Требуемая сумма для сравнения с балансом.
     * @throws InvalidAmountException если на балансе игрока недостаточно средств.
     */
    private void checkSufficientBalance(Player player, BigDecimal amount) throws InvalidAmountException {
        if (player.getBalance().compareTo(amount) < 0) {
            throw new InvalidAmountException("На вашем счету недастаточно средств");
        }
    }
}
