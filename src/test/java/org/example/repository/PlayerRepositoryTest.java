package org.example.repository;

import org.example.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerRepositoryTest {

    private PlayerRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new PlayerRepository();
    }

    @Test
    @DisplayName("Сохранение: Должен сохранять игрока")
    public void save_ShouldSavePlayer() {
        Player player = new Player();
        player.setUsername("John");
        repository.save(player);

        Optional<Player> retrievedPlayer = repository.findById(1L);
        assertThat(retrievedPlayer).isPresent();
        assertThat(retrievedPlayer.get().getUsername()).isEqualTo("John");
    }

    @Test
    @DisplayName("Поиск по ID: Должен возвращать пустой результат для неизвестного ID")
    public void findById_ShouldReturnEmptyForUnknownId() {
        Optional<Player> retrievedPlayer = repository.findById(99L);
        assertThat(retrievedPlayer).isNotPresent();
    }

    @Test
    @DisplayName("Поиск по имени: Должен возвращать правильного игрока")
    public void findByName_ShouldRetrieveCorrectPlayer() {
        Player player1 = new Player();
        player1.setUsername("John");
        repository.save(player1);

        Player player2 = new Player();
        player2.setUsername("Jake");
        repository.save(player2);

        Optional<Player> retrievedPlayer = repository.findByName("Jake");
        assertThat(retrievedPlayer).isPresent();
        assertThat(retrievedPlayer.get().getUsername()).isEqualTo("Jake");
    }

    @Test
    @DisplayName("Поиск по имени: Должен возвращать пустой результат для неизвестного имени")
    public void findByName_ShouldReturnEmptyForUnknownName() {
        Optional<Player> retrievedPlayer = repository.findByName("Unknown");
        assertThat(retrievedPlayer).isNotPresent();
    }


}
