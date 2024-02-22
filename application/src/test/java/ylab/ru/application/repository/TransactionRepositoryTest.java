package ylab.ru.application.repository;


import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.common.model.Transaction;
import ru.ylab.common.repository.TransactionRepository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JdbcTest
@Import(TransactionRepository.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {
    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TransactionRepository repository;

    static {
        postgresqlContainer.start();
    }

    @Configuration
    static class TestConfig {

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url(postgresqlContainer.getJdbcUrl())
                    .username(postgresqlContainer.getUsername())
                    .password(postgresqlContainer.getPassword())
                    .build();
        }

        @Bean
        public SpringLiquibase liquibase(DataSource dataSource) {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDataSource(dataSource);
            liquibase.setChangeLog("classpath:/liquibase/db/changelog/master.xml");
            return liquibase;
        }
    }

    @Test
    @DisplayName("Добавление транзакции: Должен добавлять транзакцию в репозиторий")
    public void addTransaction_ShouldAddTransactionToRepository() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction(1L, Transaction.TransactionType.DEBIT,
                BigDecimal.TEN, transactionId);
        repository.addTransaction(transaction);

        assertThat(repository.existsById(transactionId)).isTrue();
    }

    @Test
    @DisplayName("Существование по ID: Должен возвращать true, если существует")
    public void existsById_ShouldReturnTrueIfExists() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction(1L, Transaction.TransactionType.CREDIT,
                BigDecimal.TEN, transactionId);
        repository.addTransaction(transaction);

        assertThat(repository.existsById(transactionId)).isTrue();
    }

    @Test
    @DisplayName("Существование по ID: Должен возвращать false, если не существует")
    public void existsById_ShouldReturnFalseIfNotExists() {
        UUID nonExistentTransactionId = UUID.randomUUID();
        assertThat(repository.existsById(nonExistentTransactionId)).isFalse();
    }
}
