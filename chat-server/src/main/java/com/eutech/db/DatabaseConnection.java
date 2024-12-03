package com.eutech.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:12500/eutech_chat";
    private static final String USER = "postgres";
    private static final String PASSWORD = "yasith12345";

    private static Connection connection;

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getInstance() {
        if (connection == null) {
            synchronized (DatabaseConnection.class) {
                if (connection == null) {
                    try {
                        connection = DriverManager.getConnection(URL, USER, PASSWORD);
                        System.out.println("Database connection established.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Failed to connect to the database");
                    }
                }
            }
        }
        return connection;
    }
}
