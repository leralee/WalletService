package org.example.service;

import org.example.model.Audit;
import org.example.model.Player;
import org.example.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class PlayerServiceTest {
    private PlayerRepository playerRepository;
    private AuditService auditService;
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerRepository = mock(PlayerRepository.class);
        auditService = mock(AuditService.class);
        playerService = new PlayerService(playerRepository, auditService);
    }

    @Test
    @DisplayName("Регистрация игрока: успешная регистрация")
    void registerPlayer_ShouldRegisterSuccessfully() {
        when(playerRepository.findByName("John")).thenReturn(Optional.empty());

        doAnswer(invocation -> {
            Player playerArg = invocation.getArgument(0);
            playerArg.setId(1L);
            return null;
        }).when(playerRepository).save(any(Player.class));

        Optional<Player> player = playerService.registerPlayer("John", "password123", Player.Role.USER);

        assertThat(player).isPresent();
        assertThat(player.get().getUsername()).isEqualTo("John");

        verify(auditService).recordAction(eq(1L), eq(Audit.ActionType.REGISTRATION_SUCCESS));
    }

    @Test
    @DisplayName("Регистрация игрока: неудачная регистрация")
    void registerPlayer_ShouldNotRegister() {
        Player existingPlayer = new Player();
        existingPlayer.setUsername("John");
        when(playerRepository.findByName("John")).thenReturn(Optional.of(existingPlayer));

        Optional<Player> player = playerService.registerPlayer("John", "pass123",
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

        Player result = playerService.authorizePlayer(username, password);

        assertThat(result).isNull();
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
