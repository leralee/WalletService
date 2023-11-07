package org.example.service;

import org.example.common.AuditService;
import org.example.common.Player;
import org.example.exception.PlayerExistsException;
import org.example.interfaces.IPlayerRepository;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Сервис для управления игроками в приложении Wallet Service.
 * Предоставляет методы для регистрации, авторизации и получения баланса игрока.
 */

@Service
public class PlayerService {
    private final IPlayerRepository playerRepository;

    public PlayerService(IPlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    /**
     * Регистрирует нового игрока с заданными учетными данными и ролью.
     *
     * @return Optional содержащий объект игрока, если регистрация прошла успешно.
     */
    public Optional<Player> save(Player player) throws PlayerExistsException {
        if (playerRepository.findByName(player.getUsername()).isPresent()) {
            throw new PlayerExistsException("Пользователем с данным логином уже существует.");
        }

        String hashedPassword = BCrypt.hashpw(player.getPassword(), BCrypt.gensalt());
        player.setPassword(hashedPassword);
        player.setBalance(BigDecimal.ZERO);
        player.setRole(Player.Role.USER);

        playerRepository.save(player);

        return Optional.of(player);

    }

    /**
     * Авторизует игрока с заданными учетными данными.
     *
     * @return Объект игрока, если авторизация прошла успешно, иначе - null.
     */
    public Player authorizePlayer(Player player) {
        return playerRepository.findByName(player.getUsername())
                .filter(playerFromDB -> BCrypt.checkpw(player.getPassword(), playerFromDB.getPassword()))
                .orElse(null);
    }

    /**
     * Получает текущий баланс игрока.
     *
     * @param playerId Идентификатор игрока.
     * @return Баланс игрока.
     */
    public BigDecimal getPlayerBalance(long playerId) {
        return playerRepository.findById(playerId)
                .map(Player::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Обновляет баланс указанного игрока и сохраняет изменения в репозитории.
     *
     * @param player     Объект игрока, чей баланс требуется обновить.
     * @param newBalance Новое значение баланса для указанного игрока.
     */
    public void updateBalance(Player player, BigDecimal newBalance) {
        player.setBalance(newBalance);
        playerRepository.update(player.getId(), player);
    }

    public Optional<Player> findByUsername(String username) {
        return playerRepository.findByName(username);
    }
}
