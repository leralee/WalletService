package org.example.repository;

import org.example.interfaces.IPlayerRepository;
import org.example.model.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Репозиторий для хранения и управления игроками.
 */
public class PlayerRepository implements IPlayerRepository {

    private final Map<Long, Player> playerStorage = new HashMap<>();
    private long currentId = 1;

    /**
     * Сохраняет игрока в репозиторий. Если у игрока нет идентификатора,
     * ему будет присвоен новый.
     *
     * @param player Объект игрока, который необходимо сохранить.
     */
    public void save(Player player) {
        if (player.getId() == null) {
            player.setId(currentId++);
        }
        playerStorage.put(player.getId(), player);
    }

    /**
     * Поиск игрока по идентификатору.
     *
     * @param id Идентификатор игрока.
     * @return Optional объект игрока, если игрок с таким идентификатором найден, иначе Optional.empty().
     */
    public Optional<Player> findById(Long id) {
        return Optional.ofNullable(playerStorage.get(id));
    }

    /**
     * Поиск игрока по имени.
     *
     * @param name Имя игрока.
     * @return Optional объект игрока, если игрок с таким именем найден, иначе Optional.empty().
     */
    public Optional<Player> findByName(String name) {
        return playerStorage.values().stream()
                .filter(player -> player.getUsername().equals(name))
                .findFirst();
    }
}
