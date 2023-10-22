package org.example.repository;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author valeriali on {17.10.2023}
 * @project walletService
 */
public class DatabaseConnection {
    private static String url;
    private static String user;
    private static String password;

    static {

        Properties properties = new Properties();
        String env = System.getenv("environment");
        if (env == null) {
            env = "local";
        }
        String propertiesFile = "db-" + env + ".properties";
        System.out.println(propertiesFile + "!!!!!!!!!!!!");

        try {
            properties.load(DatabaseConnection.class.getClassLoader().getResourceAsStream(propertiesFile));
            url = properties.getProperty("url");
            user = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить настройки базы данных", e);
        }

    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        return DriverManager.getConnection(url, user, password);
    }

}
