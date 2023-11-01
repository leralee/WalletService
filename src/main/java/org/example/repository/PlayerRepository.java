package org.example.repository;

import org.example.interfaces.IPlayerRepository;
import org.example.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

/**
 * Репозиторий для хранения и управления игроками.
 */

@Repository
public class PlayerRepository implements IPlayerRepository {

    private final DatabaseConnection databaseConnection;

    @Autowired
    public PlayerRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Сохраняет игрока в репозиторий. Если у игрока нет идентификатора,
     * ему будет присвоен новый.
     *
     * @param player Объект игрока, который необходимо сохранить.
     */
    public void save(Player player) {
        if (player.getId() == null) {
            String query = "INSERT INTO wallet.player (role, username, password, balance) VALUES (?, ?, ?, ?)";
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, player.getRole().toString());
                preparedStatement.setString(2, player.getUsername());
                preparedStatement.setString(3, player.getPassword());
                preparedStatement.setBigDecimal(4, player.getBalance());

                preparedStatement.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                System.err.println("Ошибка при добавлении пользователя: " + e.getMessage());
            }
        } else {
            String updateQuery = "UPDATE wallet.player SET role = ?, username = ?, password = ?, balance = ? WHERE id = ?";
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, player.getRole().toString());
                preparedStatement.setString(2, player.getUsername());
                preparedStatement.setString(3, player.getPassword());
                preparedStatement.setBigDecimal(4, player.getBalance());
                preparedStatement.setLong(5, player.getId());

                preparedStatement.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                System.err.println("Ошибка при обновлении пользователя: " + e.getMessage());
            }
        }
    }

    /**
     * Поиск игрока по идентификатору.
     *
     * @param id Идентификатор игрока.
     * @return Optional объект игрока, если игрок с таким идентификатором найден, иначе Optional.empty().
     */
    public Optional<Player> findById(Long id) {
        String query = "SELECT * FROM wallet.player WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Player player = new Player();
                player.setId(resultSet.getLong("id"));
                player.setUsername(resultSet.getString("username"));
                player.setPassword(resultSet.getString("password"));
                player.setRole(Player.Role.valueOf(resultSet.getString("role")));
                player.setBalance(resultSet.getBigDecimal("balance"));

                return Optional.of(player);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Ошибка нахождения пользователя: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Поиск игрока по имени.
     *
     * @param username Имя игрока.
     * @return Optional объект игрока, если игрок с таким именем найден, иначе Optional.empty().
     */
    public Optional<Player> findByName(String username) {
        String query = "SELECT * FROM wallet.player WHERE username = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Player player = new Player();
                player.setId(resultSet.getLong("id"));
                player.setUsername(resultSet.getString("username"));
                player.setPassword(resultSet.getString("password"));
                player.setRole(Player.Role.valueOf(resultSet.getString("role")));
                player.setBalance(resultSet.getBigDecimal("balance"));

                return Optional.of(player);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
