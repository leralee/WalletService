//package ylab.ru.application.repository;
//
//import liquibase.integration.spring.SpringLiquibase;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.ylab.common.model.Player;
//import ru.ylab.common.repository.PlayerRepository;
//import javax.sql.DataSource;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@JdbcTest
//@Import(PlayerRepository.class)
//@Testcontainers
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class PlayerRepositoryTest {
//
//    @Container
//    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("test-db")
//            .withUsername("test")
//            .withPassword("test");
//
//    @Autowired
//    private PlayerRepository repository;
//
//    static {
//        postgresqlContainer.start();
//    }
//
//    @Configuration
//    static class TestConfig {
//
//        @Bean
//        public DataSource dataSource() {
//            return DataSourceBuilder.create()
//                    .url(postgresqlContainer.getJdbcUrl())
//                    .username(postgresqlContainer.getUsername())
//                    .password(postgresqlContainer.getPassword())
//                    .build();
//        }
//
//        @Bean
//        public SpringLiquibase liquibase(DataSource dataSource) {
//            SpringLiquibase liquibase = new SpringLiquibase();
//            liquibase.setDataSource(dataSource);
//            liquibase.setChangeLog("classpath:/liquibase/db/changelog/master.xml");
//            return liquibase;
//        }
//    }
//
//    @Test
//    @DisplayName("Сохранение: Должен сохранять игрока")
//    public void save_ShouldSavePlayer() {
//        Player player = new Player();
//        player.setUsername("John");
//        player.setPassword("pswd");
//        player.setRole(Player.Role.USER);
//        repository.save(player);
//
//        Optional<Player> retrievedPlayer = repository.findByName("John");
//        assertThat(retrievedPlayer).isPresent();
//        assertThat(retrievedPlayer.get().getUsername()).isEqualTo("John");
//    }
//
//    @Test
//    @DisplayName("Поиск по ID: Должен возвращать пустой результат для неизвестного ID")
//    public void findById_ShouldReturnEmptyForUnknownId() {
//        Optional<Player> retrievedPlayer = repository.findById(99L);
//        assertThat(retrievedPlayer).isNotPresent();
//    }
//
//    @Test
//    @DisplayName("Поиск по имени: Должен возвращать правильного игрока")
//    public void findByName_ShouldRetrieveCorrectPlayer() {
//        Player player1 = new Player();
//        player1.setUsername("John2");
//        player1.setPassword("pswd");
//        player1.setRole(Player.Role.USER);
//        repository.save(player1);
//
//        Player player2 = new Player();
//        player2.setUsername("Jake");
//        player2.setPassword("pswd");
//        player2.setRole(Player.Role.USER);
//        repository.save(player2);
//
//        Optional<Player> retrievedPlayer = repository.findByName("Jake");
//        assertThat(retrievedPlayer).isPresent();
//        assertThat(retrievedPlayer.get().getUsername()).isEqualTo("Jake");
//    }
//
//    @Test
//    @DisplayName("Поиск по имени: Должен возвращать пустой результат для неизвестного имени")
//    public void findByName_ShouldReturnEmptyForUnknownName() {
//        Optional<Player> retrievedPlayer = repository.findByName("Unknown");
//        assertThat(retrievedPlayer).isNotPresent();
//    }
//
//}
