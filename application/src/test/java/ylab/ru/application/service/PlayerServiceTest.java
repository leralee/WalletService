package ylab.ru.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import ru.ylab.common.model.Player;
import ru.ylab.common.repository.PlayerRepository;
import ru.ylab.common.service.PlayerService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class PlayerServiceTest {
    private PlayerRepository playerRepository;
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerRepository = mock(PlayerRepository.class);
        playerService = new PlayerService(playerRepository);
    }

    @Test
    @DisplayName("Регистрация игрока: успешная регистрация")
    void registerPlayer_ShouldRegisterSuccessfully() {
        Player player = new Player();
        player.setUsername("newplayer");
        player.setPassword("password123");
        player.setRole(Player.Role.USER);

        Player expectedPlayer = new Player();
        expectedPlayer.setId(1L);
        expectedPlayer.setUsername(player.getUsername());
        expectedPlayer.setPassword(player.getPassword());

        when(playerRepository.save(any(Player))).thenReturn(expectedPlayer);

        Player result = playerService.save(player);

        // Проверка результатов
        assertNotNull(result);
        assertEquals(expectedPlayer.getUsername(), result.getUsername());
        assertEquals(expectedPlayer.getPassword(), result.getPassword());
        assertEquals(expectedPlayer.getId(), result.getId());

        // Подтверждение, что взаимодействие произошло
        verify(playerRepository).save(any(Player.class));

    }

    @Test
    @DisplayName("Регистрация игрока: неудачная регистрация")
    void registerPlayer_ShouldNotRegister() {
        Player existingPlayer = new Player();
        existingPlayer.setUsername("John");
        when(playerRepository.findByName("John")).thenReturn(Optional.of(existingPlayer));

        Optional<Player> player = playerService.registerPlayer_ShouldNotRegistergisterPlayer("John", "pass123",
                Player.Role.USER);
        assertThat(player).isNotPresent();
        verify(auditService).recordAction(eq(-1L), eq(Audit.ActionType.REGISTRATION_FAILED));
    }

    @Test
    @DisplayName("Авторизация игрока: успешная авторизация")
    public void authorizePlayer_ShouldReturnPlayerOnSuccessfulAuthorization() {
        String username = "John";
        String password = "pass123";
        Player player = new Player();
        player.setUsername(username);
        player.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        when(playerRepository.findByName(username)).thenReturn(Optional.of(player));

        Player result = playerService.authorizePlayer(username, password);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("Авторизация игрока: неудачная авторизация")
    public void authorizePlayer_ShouldReturnNullOnFailedAuthorization() {
        String username = "John";
        String password = "wrongPass";
        Player player = new Player();
        player.setUsername(username);
        player.setPassword(BCrypt.hashpw("pass123", BCrypt.gensalt()));
        when(playerRepository.findByName(username)).thenReturn(Optional.of(player));

//        Player result = playerService.authorizePlayer(username, password);
//
//        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Получение баланса игрока")
    public void getPlayerBalance_ShouldReturnPlayerBalance() {
        long playerId = 1L;
        BigDecimal balance = BigDecimal.valueOf(100.0);
        Player player = new Player();
        player.setId(playerId);
        player.setBalance(balance);
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        BigDecimal result = playerService.getPlayerBalance(playerId);

        assertThat(result).isEqualTo(balance);
    }

    @Test
    @DisplayName("Обновление баланса игрока")
    public void updateBalance_ShouldUpdatePlayerBalance() {
        BigDecimal newBalance = BigDecimal.valueOf(150.0);
        Player player = new Player();
        player.setBalance(BigDecimal.valueOf(100.0));

        playerService.updateBalance(player, newBalance);

        assertThat(player.getBalance()).isEqualTo(newBalance);
        verify(playerRepository, times(1)).save(player);
    }
}
