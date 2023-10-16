package org.example.interfaces;

import org.example.model.Player;

import java.util.Optional;

/**
 * @author valeriali on {15.10.2023}
 * @project walletService
 */
public interface IPlayerRepository {
    void save(Player player);

    /**
     * Поиск игрока по идентификатору.
     *
     * @param id Идентификатор игрока.
     * @return Optional объект игрока, если игрок с таким идентификатором найден, иначе Optional.empty().
     */
    Optional<Player> findById(Long id);

    /**
     * Поиск игрока по имени.
     *
     * @param name Имя игрока.
     * @return Optional объект игрока, если игрок с таким именем найден, иначе Optional.empty().
     */
    Optional<Player> findByName(String name);
}
