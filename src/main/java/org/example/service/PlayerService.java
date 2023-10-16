package org.example.service;

import org.example.interfaces.IPlayerRepository;
import org.example.model.Audit;
import org.example.model.Player;
import org.example.repository.PlayerRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Сервис для управления игроками в приложении Wallet Service.
 * Предоставляет методы для регистрации, авторизации и получения баланса игрока.
 */
public class PlayerService {
    private final IPlayerRepository playerRepository;
    private final AuditService auditService;


    public PlayerService(IPlayerRepository playerRepository, AuditService auditService) {
        this.playerRepository = playerRepository;
        this.auditService = auditService;
    }


    /**
     * Регистрирует нового игрока с заданными учетными данными и ролью.
     *
     * @param username Имя пользователя для регистрации.
     * @param password Пароль пользователя.
     * @param role     Роль игрока.
     * @return Optional содержащий объект игрока, если регистрация прошла успешно.
     */
    public Optional<Player> registerPlayer(String username, String password, Player.Role role) {
        if (playerRepository.findByName(username).isPresent()) {
            auditService.recordAction(-1L, Audit.ActionType.REGISTRATION_FAILED);
            return Optional.empty();
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Player player = new Player();
        player.setUsername(username);
        player.setPassword(hashedPassword);
        player.setBalance(BigDecimal.valueOf(0.0));
        player.setRole(role);

        playerRepository.save(player);

        auditService.recordAction(player.getId(), Audit.ActionType.REGISTRATION_SUCCESS);

        return Optional.of(player);

    }

    /**
     * Авторизует игрока с заданными учетными данными.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @return Объект игрока, если авторизация прошла успешно, иначе - null.
     */
    public Player authorizePlayer(String username, String password) {
        Player authorizedPlayer = playerRepository.findByName(username)
                .filter(player -> BCrypt.checkpw(password, player.getPassword()))
                .orElse(null);
        if (authorizedPlayer != null) {
            auditService.recordAction(authorizedPlayer.getId(), Audit.ActionType.LOGIN);
        } else {
            auditService.recordAction(null, Audit.ActionType.LOGIN_FAILED);
        }

        return authorizedPlayer;
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
        playerRepository.save(player);
    }

    public void logout(Player currentPlayer) {
        auditService.recordAction(currentPlayer.getId(), Audit.ActionType.LOGOUT);
    }

}
