//package ylab.ru.application.repository;
//
//
//import liquibase.integration.spring.SpringLiquibase;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import ru.ylab.common.model.ActionType;
//import ru.ylab.common.model.Audit;
//import ru.ylab.common.repository.AuditRepository;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import javax.sql.DataSource;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@JdbcTest
//@Import(AuditRepository.class)
//@Testcontainers
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class AuditRepositoryTest {
//
//    @Container
//    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("test-db")
//            .withUsername("test")
//            .withPassword("test");
//
//    @Autowired
//    private AuditRepository repository;
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
//    @Test
//    @DisplayName("Добавление записи: Должен добавить запись аудита")
//    public void addRecord_ShouldAddAuditRecord() {
//        Audit audit = new Audit(null, ActionType.LOGIN, LocalDateTime.now());
//        repository.addRecord(audit);
//        List<Audit> records = repository.getAllRecords();
//        assertThat(records).contains(audit);
//    }
//}
