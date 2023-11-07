package org.example.in;

import org.example.common.Player;
import org.example.exception.InvalidAmountException;
import org.example.exception.PlayerExistsException;
import org.example.exception.TransactionExistsException;
import org.example.common.Audit;

import org.example.common.AuditService;
import org.example.service.PlayerService;
import org.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Фасад для работы с сервисами Wallet.
 * <p>
 * Этот класс предоставляет высокоуровневый интерфейс для работы с различными сервисами Wallet,
 * такими как сервис игроков, сервис транзакций и сервис аудита.
 * </p>
 */

@Component
public class WalletServiceFacade {
    private final PlayerService playerService;
    private final TransactionService transactionService;
    private final AuditService auditService;

    /**
     * Конструктор фасада сервиса Wallet.
     *
     * @param playerService Сервис для работы с игроками.
     * @param transactionService Сервис для работы с транзакциями.
     * @param auditService Сервис для аудита операций.
     */

    @Autowired
    public WalletServiceFacade(PlayerService playerService,
                               TransactionService transactionService, AuditService auditService) {
        this.playerService = playerService;
        this.transactionService = transactionService;
        this.auditService = auditService;
    }


    public Optional<Player> save(Player player) throws PlayerExistsException {
        return playerService.save(player);
    }


    public Player authorizePlayer(Player player) {
        return playerService.authorizePlayer(player);
    }

    /**
     * Возвращает баланс игрока по его идентификатору.
     *
     * @param id Идентификатор игрока.
     * @return Баланс игрока.
     */
    public BigDecimal getPlayerBalance(Long id) {
        return playerService.getPlayerBalance(id);
    }

    /**
     * Зачисляет сумму на счет игрока.
     *
     * @param player Игрок, на счет которого будет зачислена сумма.
     * @param amount Сумма для зачисления.
     * @param transactionId Идентификатор транзакции.
     * @throws InvalidAmountException Если указана недопустимая сумма.
     * @throws TransactionExistsException Если транзакция с таким идентификатором уже существует.
     */
    public void credit(Player player, BigDecimal amount, UUID transactionId) throws
            InvalidAmountException, TransactionExistsException {
        transactionService.credit(player, amount, transactionId);
    }

    /**
     * Снимает сумму со счета игрока.
     *
     * @param player Игрок, со счета которого будет снята сумма.
     * @param amount Сумма для снятия.
     * @param transactionId Идентификатор транзакции.
     * @throws InvalidAmountException Если указана недопустимая сумма.
     * @throws TransactionExistsException Если транзакция с таким идентификатором уже существует.
     */
    public void withdraw(Player player, BigDecimal amount, UUID transactionId)
            throws InvalidAmountException, TransactionExistsException {
        transactionService.withdraw(player, amount, transactionId);
    }

    /**
     * Получает список записей аудита.
     *
     * @return Список записей аудита.
     */
    public List<Audit> getRecords() {
        return auditService.getRecords();
    }

    /**
     * Записывает действие выхода игрока.
     *
     * @param id Идентификатор игрока, который вышел из системы.
     */
}
