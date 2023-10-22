//package org.example.repository;
//
//import liquibase.Liquibase;
//import liquibase.database.Database;
//import liquibase.database.DatabaseFactory;
//import liquibase.database.jvm.JdbcConnection;
//import liquibase.exception.DatabaseException;
//import liquibase.resource.ClassLoaderResourceAccessor;
//import org.example.model.Audit;
//import org.junit.jupiter.api.*;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class AuditRepositoryTest {
//    private AuditRepository repository;
//
//    private static PostgreSQLContainer<?> database;
//
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
//        try (Connection connection = DatabaseConnection.getConnection()) {
//
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
//        this.repository = new AuditRepository();
//    }
//
//    @Test
//    @DisplayName("Добавление записи: Должен добавить запись аудита")
//    public void addRecord_ShouldAddAuditRecord() {
//        Audit audit = new Audit(null, Audit.ActionType.LOGIN, LocalDateTime.now());
//        repository.addRecord(audit);
//
//        List<Audit> records = repository.getAllRecords();
//        System.out.println(records.get(0).getPlayerId());
//        assertThat(records).contains(audit);
//    }
//}
