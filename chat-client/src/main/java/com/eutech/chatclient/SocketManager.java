package com.eutech.chatclient;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class SocketManager {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public SocketManager(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String message) {
        out.println(message);  // Send message to server
    }

    public void saveMessageToDatabase(String username, String message, List<String> receivers) {
        out.println("save_message");
        out.println(username);
        out.println(message);
        StringBuilder receiversString = new StringBuilder();
        for (String receiver : receivers) {
            receiversString.append(receiver);
        }
        out.println(receiversString.toString());
    }

    public String getUsersMessages(String username) throws IOException {
        out.println("get_users_messages");
        out.println(username);
        return receiveMessage();
    }

    public String receiveMessage() throws IOException {
        return in.readLine();  // Receive message from server
    }

    public List<String> getOnlineUsers() throws IOException {
        sendMessage("GET_ALL_USERS");
        // Assuming server returns a comma-separated list of users
        String users = receiveMessage();
        return List.of(users.split(","));
    }

    public void closeConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}

