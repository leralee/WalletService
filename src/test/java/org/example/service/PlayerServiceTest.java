package org.example.service;

import org.example.model.Audit;
import org.example.model.Player;
import org.example.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author valeriali on {10.10.2023}
 * @project walletService
 */
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
    void registerPlayer_ShouldRegisterSuccessfully() {
        when(playerRepository.findByName("John")).thenReturn(Optional.empty());

        doAnswer(invocation -> {
            Player playerArg = invocation.getArgument(0);
            playerArg.setId(1L);
            return null;
        }).when(playerRepository).save(any(Player.class));

        Optional<Player> player = playerService.registerPlayer("John", "password123", Player.Role.USER);

        assertTrue(player.isPresent());
        assertEquals("John", player.get().getUsername());

        verify(auditService).recordAction(1L, Audit.ActionType.REGISTRATION_SUCCESS);
    }

    @Test
    void registerPlayer_ShouldNotRegister() {
        Player existingPlayer = new Player();
        existingPlayer.setUsername("John");
        when(playerRepository.findByName("John")).thenReturn(Optional.of(existingPlayer));

        Optional<Player> player = playerService.registerPlayer("John", "password123",
                Player.Role.USER);
        assertFalse(player.isPresent());
        verify(auditService).recordAction(eq(-1L), eq(Audit.ActionType.REGISTRATION_FAILED));
    }

    @Test
    public void authorizePlayer_ShouldReturnPlayerOnSuccessfulAuthorization() {
        String username = "John";
        String password = "pass123";
        Player player = new Player();
        player.setUsername(username);
        player.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        when(playerRepository.findByName(username)).thenReturn(Optional.of(player));

        Player result = playerService.authorizePlayer(username, password);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    public void authorizePlayer_ShouldReturnNullOnFailedAuthorization() {
        String username = "John";
        String password = "wrongPass";
        Player player = new Player();
        player.setUsername(username);
        player.setPassword(BCrypt.hashpw("pass123", BCrypt.gensalt()));
        when(playerRepository.findByName(username)).thenReturn(Optional.of(player));

        Player result = playerService.authorizePlayer(username, password);

        assertNull(result);
    }

    @Test
    public void getPlayerBalance_ShouldReturnPlayerBalance() {
        long playerId = 1L;
        BigDecimal balance = BigDecimal.valueOf(100.0);
        Player player = new Player();
        player.setId(playerId);
        player.setBalance(balance);
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        BigDecimal result = playerService.getPlayerBalance(playerId);

        assertEquals(balance, result);
    }

    @Test
    public void updateBalance_ShouldUpdatePlayerBalance() {
        BigDecimal newBalance = BigDecimal.valueOf(150.0);
        Player player = new Player();
        player.setBalance(BigDecimal.valueOf(100.0));

        playerService.updateBalance(player, newBalance);

        assertEquals(newBalance, player.getBalance());
        verify(playerRepository, times(1)).save(player);
    }
}
