package org.example.repository;

import java.sql.*;

/**
 * @author valeriali on {17.10.2023}
 * @project walletService
 */
public class DatabaseConnection {
    private static String url = "jdbc:postgresql://localhost:5432/wallet_service_db";
    private static String user = "valeriali";
    private static String password = "postgres";

    public static void setConnectionParameters(String newUrl, String newUser, String newPassword) {
        url = newUrl;
        user = newUser;
        password = newPassword;
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        return DriverManager.getConnection(url, user, password);

    }

    public static void migrationSetUp() {
            try (Statement statement = getConnection().createStatement()) {
                String checkTableExistsSql = "SELECT tablename FROM pg_tables " +
                        "WHERE schemaname = 'public' AND tablename IN ('databasechangelog', 'databasechangeloglock');";
                ResultSet resultSet = statement.executeQuery(checkTableExistsSql);

                if (resultSet.next()) {
                    // Таблицы существуют в схеме "public", выполнить ALTER TABLE
                    String alterTableSql = "ALTER TABLE public.databasechangelog SET SCHEMA migration;" +
                            "ALTER TABLE public.databasechangeloglock SET SCHEMA migration;";

                    // Выполнение SQL-запросов
                    statement.executeUpdate(alterTableSql);

                    System.out.println("Таблицы 'databasechangelog' и 'databasechangeloglock' были перенесены в схему 'migration'.");
                } else {
                    System.out.println("Таблицы 'databasechangelog' и 'databasechangeloglock' не существуют в схеме 'public', миграция не требуется.");
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


}
