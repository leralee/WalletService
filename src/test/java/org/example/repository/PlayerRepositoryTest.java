//package org.example.repository;
//
//import liquibase.Liquibase;
//import liquibase.database.Database;
//import liquibase.database.DatabaseFactory;
//import liquibase.database.jvm.JdbcConnection;
//import liquibase.exception.DatabaseException;
//import liquibase.resource.ClassLoaderResourceAccessor;
//import org.example.model.Player;
//import org.junit.jupiter.api.*;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.sql.Connection;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Testcontainers
//public class PlayerRepositoryTest {
//    private static PostgreSQLContainer<?> database;
//    private PlayerRepository repository;
//    @BeforeAll
//    public static void initContainer() {
//        database = new PostgreSQLContainer<>("postgres:9.6.12")
//                .withDatabaseName("wallet_service_db")
//                .withUsername("valeriali")
//                .withPassword("postgres");
//        database.start();
//        DatabaseConnection.setConnectionParameters(database.getJdbcUrl(),
//                database.getUsername(), database.getPassword());
//    }
//
//    @AfterAll
//    public static void closeContainer() {
//        if (database != null) {
//            database.stop();
//        }
//    }
//
//    @BeforeEach
//    public void setUp() {
//        Liquibase liquibase = null;
//        try {
//            Connection connection = DatabaseConnection.getConnection();
//            Database databaseObject = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
//            liquibase = new Liquibase("db/changelog/master.xml",
//                    new ClassLoaderResourceAccessor(), databaseObject);
//            liquibase.update("");
//        } catch (Exception e) {
//            throw new RuntimeException("Ошибка при выполнении миграции Liquibase", e);
//        } finally {
//            if (liquibase != null && liquibase.getDatabase() != null) {
//                try {
//                    liquibase.getDatabase().close();
//                } catch (DatabaseException ex) {
//                    //
//                }
//            }
//        }
//        this.repository = new PlayerRepository();
//    }
//
//    @Test
//    @DisplayName("Сохранение: Должен сохранять игрока")
//    public void save_ShouldSavePlayer() {
//        Player player = new Player();
//        player.setUsername("John");
//        player.setPassword("pswd");
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
//        repository.save(player1);
//
//        Player player2 = new Player();
//        player2.setUsername("Jake");
//        player2.setPassword("pswd");
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
