package org.example.repository;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Properties;

/**
 * Класс для установления соединения с базой данных.
 * Он читает параметры подключения из файла конфигурации и создает соединение с базой данных.
 */
@Component
public class DatabaseConnection {

    /**
     * Получает соединение с базой данных.
     * Загружает настройки подключения из файла конфигурации и инициирует соединение.
     *
     * @return Объект соединения с базой данных.
     * @throws ClassNotFoundException Если драйвер базы данных не найден.
     * @throws SQLException Если произошла ошибка при установлении соединения с базой данных.
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Properties properties = new Properties();
        try {
            properties.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("application.yml"));
        } catch (Exception e) {
            throw new RuntimeException("Не удалось подключиться к базе данных");
        }

        String URL = properties.getProperty("url");
        String USER = properties.getProperty("user");
        String PASSWORD = properties.getProperty("password");
        Class.forName("org.postgresql.Driver");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
