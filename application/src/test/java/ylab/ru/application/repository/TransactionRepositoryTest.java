//package org.example.repository;
//
//import liquibase.Liquibase;
//import liquibase.database.Database;
//import liquibase.database.DatabaseFactory;
//import liquibase.database.jvm.JdbcConnection;
//import liquibase.exception.DatabaseException;
//import liquibase.resource.ClassLoaderResourceAccessor;
//import org.example.model.Transaction;
//import org.junit.jupiter.api.*;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//import java.math.BigDecimal;
//import java.sql.Connection;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//public class TransactionRepositoryTest {
//    private TransactionRepository repository;
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
//        this.repository = new TransactionRepository();
//    }
//
//    @Test
//    @DisplayName("Добавление транзакции: Должен добавлять транзакцию в репозиторий")
//    public void addTransaction_ShouldAddTransactionToRepository() {
//        UUID transactionId = UUID.randomUUID();
//        Transaction transaction = new Transaction(1L, Transaction.TransactionType.DEBIT,
//                BigDecimal.TEN, transactionId);
//        repository.addTransaction(transaction);
//
//        assertThat(repository.existsById(transactionId)).isTrue();
//    }
//
//    @Test
//    @DisplayName("Существование по ID: Должен возвращать true, если существует")
//    public void existsById_ShouldReturnTrueIfExists() {
//        UUID transactionId = UUID.randomUUID();
//        Transaction transaction = new Transaction(1L, Transaction.TransactionType.CREDIT,
//                BigDecimal.TEN, transactionId);
//        repository.addTransaction(transaction);
//
//        assertThat(repository.existsById(transactionId)).isTrue();
//    }
//
//    @Test
//    @DisplayName("Существование по ID: Должен возвращать false, если не существует")
//    public void existsById_ShouldReturnFalseIfNotExists() {
//        UUID nonExistentTransactionId = UUID.randomUUID();
//        assertThat(repository.existsById(nonExistentTransactionId)).isFalse();
//    }
//}
