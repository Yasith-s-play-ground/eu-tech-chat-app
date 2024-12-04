package com.eutech.service;

import com.eutech.db.DatabaseConnection;
import com.eutech.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatHistoryService {

    public static void saveMessage(String sender, String message, List<String> receivers) {
        String query = "INSERT INTO chat_history (sender, message) VALUES (?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, sender);
            stmt.setString(2, message);
            int i = stmt.executeUpdate();
            System.out.println("i is " + i);
            if (i > 0) {
                // Retrieve the generated ID
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        System.out.println("generated id is " + generatedId);
                        //add receivers
                        query = "INSERT INTO chat_receivers (chat_id, receiver) VALUES (?,?)";
                        PreparedStatement stm = conn.prepareStatement(query);
                        for (String receiver : receivers) {
                            System.out.println("receiver : " + receiver);
                            stm.setInt(1, generatedId);
                            stm.setString(2, receiver);
                            stm.addBatch();
                        }
                        stm.executeBatch();
                    }
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Message> getMessagesOfUser(String loggedUser,String selectedUser) {
        List<Message> messages = new ArrayList<>();
//        String query = "SELECT ch.id AS message_id, ch.sender, ch.message, ch.sent_at, NULL AS receiver FROM chat_history ch WHERE ch.sender = ? UNION ALL SELECT cr.chat_id AS message_id, ch.sender, ch.message, ch.sent_at, cr.receiver FROM chat_receivers cr JOIN chat_history ch ON cr.chat_id = ch.id WHERE cr.receiver = ? ORDER BY sent_at";
        String query = "SELECT ch.id AS chat_id, ch.sender, cr.receiver, ch.message, ch.sent_at FROM chat_history ch JOIN chat_receivers cr ON ch.id = cr.chat_id WHERE (ch.sender = ? AND cr.receiver = ?) OR (ch.sender = ? AND cr.receiver = ?) ORDER BY ch.sent_at;\n";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, loggedUser);
            stmt.setString(2, selectedUser);
            stmt.setString(3, selectedUser);
            stmt.setString(4, loggedUser);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getTimestamp(5));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static List<String> getReceiversOfMessage(int chatId) {
        String query = "SELECT cr.receiver FROM chat_receivers cr WHERE cr.chat_id = ?";
        List<String> receivers = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, chatId); // Set the chat_id parameter
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                receivers.add(rs.getString("receiver")); // Fetch the receiver
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return receivers;
    }
}
