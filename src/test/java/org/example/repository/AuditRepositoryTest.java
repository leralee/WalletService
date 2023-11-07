//package org.example.repository;
//
//import org.example.common.Audit;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.fail;
//
//@ExtendWith(SpringExtension.class)
//@Testcontainers
//public class AuditRepositoryTest {
//
//    @Container
//    public PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("test-db")
//            .withUsername("test")
//            .withPassword("test");
//
//    @Autowired
//    private AuditRepository repository;
//
//    @BeforeEach
//    public void setUp() {
//        String jdbcUrl = postgresqlContainer.getJdbcUrl();
//        String username = postgresqlContainer.getUsername();
//        String password = postgresqlContainer.getPassword();
//
//        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
//            repository = new AuditRepository((DatabaseConnection) conn);
//        } catch (Exception e) {
//            fail("Не удалось установить соединение с БД в контейнере", e);
//        }
//    }
//
//    @Test
//    @DisplayName("Добавление записи: Должен добавить запись аудита")
//    public void addRecord_ShouldAddAuditRecord() {
//        Audit audit = new Audit(null, Audit.ActionType.LOGIN, LocalDateTime.now());
//        repository.addRecord(audit);
//        List<Audit> records = repository.getAllRecords();
//        assertThat(records).contains(audit);
//    }
//}
