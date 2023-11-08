package ylab.ru.application.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ylab.common.interfaces.IPlayerRepository;
import ru.ylab.common.model.Player;

import java.util.Optional;

/**
 * Репозиторий для хранения и управления игроками.
 */

@Repository
public class PlayerRepository implements IPlayerRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Сохраняет игрока в репозиторий.
     *
     * @param player Объект игрока, который необходимо сохранить.
     */
    public void save(Player player) {
        String query = "INSERT INTO wallet.player (role, username, password, balance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, player.getRole(), player.getUsername(), player.getPassword(), player.getBalance());
    }

    public void update(Long id, Player player) {
        String query = "UPDATE wallet.player SET role = ?, username = ?, password = ?, balance = ? WHERE id = ?";
        System.out.println();
        jdbcTemplate.update(query, player.getRole().toString(), player.getUsername(), player.getPassword(), player.getBalance(), id);
    }

    /**
     * Поиск игрока по идентификатору.
     *
     * @param id Идентификатор игрока.
     * @return Optional объект игрока, если игрок с таким идентификатором найден, иначе Optional.empty().
     */
    public Optional<Player> findById(Long id) {
        String query = "SELECT * FROM wallet.player WHERE id = ?";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Player.class), id).stream().findAny();
    }

    /**
     * Поиск игрока по имени.
     *
     * @param username Имя игрока.
     * @return Optional объект игрока, если игрок с таким именем найден, иначе Optional.empty().
     */
    public Optional<Player> findByName(String username) {
        String query = "SELECT * FROM wallet.player WHERE username = ?";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Player.class), username).stream().findAny();
    }
}
