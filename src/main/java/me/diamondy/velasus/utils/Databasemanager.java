package me.diamondy.velasus.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Databasemanager {
    private static final String JDBC_URL = "jdbc:h2:./velasusdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS staff_members (" +
                                    "username VARCHAR(255) PRIMARY KEY)";
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
}