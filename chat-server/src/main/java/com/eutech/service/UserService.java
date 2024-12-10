package com.eutech.service;

import com.eutech.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public static boolean registerUser(String username, String password, String email, String phone) {
        String query = "INSERT INTO users (username, password, email,phone) VALUES (?, ?, ?,?)";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            int i = stmt.executeUpdate();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to fetch all users from the database
    public static List<String> getAllUsers(String username) {
        List<String> users = new ArrayList<>();
        String query = "SELECT username FROM users"; // SQL query to fetch all usernames

        try {
            Connection connection = DatabaseConnection.getInstance();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                if (username.equals(rs.getString("username"))) {
                    continue;
                }
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}

