package org.example.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * @author valeriali on {10.10.2023}
 * @project walletService
 */
public class PlayerRepositoryTest {

    private PlayerRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new PlayerRepository();
    }

    @Test
    public void save_ShouldSavePlayer() {
        Player player = new Player();
        player.setUsername("John");
        repository.save(player);

        Optional<Player> retrievedPlayer = repository.findById(1L);
        assertTrue(retrievedPlayer.isPresent());
        assertEquals("John", retrievedPlayer.get().getUsername());
    }

    @Test
    public void findById_ShouldReturnEmptyForUnknownId() {
        Optional<Player> retrievedPlayer = repository.findById(99L);
        assertFalse(retrievedPlayer.isPresent());
    }

    @Test
    public void findByName_ShouldRetrieveCorrectPlayer() {
        Player player1 = new Player();
        player1.setUsername("John");
        repository.save(player1);

        Player player2 = new Player();
        player2.setUsername("Jake");
        repository.save(player2);

        Optional<Player> retrievedPlayer = repository.findByName("Jake");
        assertTrue(retrievedPlayer.isPresent());
        assertEquals("Jake", retrievedPlayer.get().getUsername());
    }

    @Test
    public void findByName_ShouldReturnEmptyForUnknownName() {
        Optional<Player> retrievedPlayer = repository.findByName("Unknown");
        assertFalse(retrievedPlayer.isPresent());
    }


}
