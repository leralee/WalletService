package org.example;

import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseTestContainer {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.1")
            .withDatabaseName("wallet_service_db")
            .withUsername("valeriali")
            .withPassword("postgres");

    static {
        postgres.start();
    }

    public static String getJdbcUrl() {
        return postgres.getJdbcUrl();
    }

    public static String getUsername() {
        return postgres.getUsername();
    }

    public static String getPassword() {
        return postgres.getPassword();
    }
}
