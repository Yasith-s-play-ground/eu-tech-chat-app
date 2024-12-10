package com.eutech.server;

import com.eutech.model.Message;
import com.eutech.service.ChatHistoryService;
import com.eutech.service.UserService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerAppInitializer {

    private static final int PORT = 5050;
    static final List<Socket> CLIENT_LIST = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("Chat server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                BufferedReader in;
                PrintWriter out;
                Socket localSocket = serverSocket.accept();
                System.out.println("New client connected.");
                in = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
                out = new PrintWriter(localSocket.getOutputStream(), true);
                new Thread(() -> {
                    try {

                        while (true) {

                            String action = in.readLine();

                            if (action == null) {
                                System.out.println("No action");
                                CLIENT_LIST.remove(localSocket);
                                break;
                            }
                            if ("login".equals(action)) {
                                String username = in.readLine();
                                String password = in.readLine();

                                // Check if credentials are correct
                                if (UserService.authenticateUser(username, password)) {
                                    out.println("Login successful");

                                    CLIENT_LIST.add(localSocket);

//                        out.println("Welcome to the chat, " + username + "!");
//                        ServerAppInitializer.broadcast(username + " has joined the chat.", this);
                                } else {
                                    out.println("Invalid username or password");
                                }
                            } else if ("register".equals(action)) {
                                String username = in.readLine();
                                String password = in.readLine();
                                String email = in.readLine();
                                String phone = in.readLine();

                                // Check if credentials are correct
                                if (UserService.registerUser(username, password, email, phone)) {
                                    out.println("Registration successful");
//                                    CLIENT_LIST.add(localSocket);
//                        out.println("Welcome to the chat, " + username + "!");
//                        ServerAppInitializer.broadcast(username + " has joined the chat.", this);
                                } else {
                                    out.println("Registration failed");
                                }
                            } else {
                                actionMethod(localSocket, action, out, in);
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void actionMethod(Socket localSocket, String action, PrintWriter out, BufferedReader in) throws IOException {
        if (action.equals("GET_ALL_USERS")) {
            String user = in.readLine();
//            System.out.println("get all users");
            List<String> allUsers = UserService.getAllUsers(user);// Fetch and send all users
            StringBuilder users = new StringBuilder();
            for (String username : allUsers) {
                users.append(username).append(",");
            }
            out.println(users.toString()); //send list as a string

        } else if (action.equals("save_message")) {
            String sender = in.readLine();
            String message = in.readLine();
            String receivers = in.readLine();
            List<String> receiverList = List.of(receivers.split(","));
            ChatHistoryService.saveMessage(sender, message, receiverList); // Save message

//            broadcastRefresh(localSocket);

        } else if (action.equals("get_users_messages")) {
            String loggedUser = in.readLine();
            String selectedUser = in.readLine();
            List<Message> messagesOfUser = ChatHistoryService.getMessagesOfUser(loggedUser, selectedUser);// get messages
            StringBuilder messages = new StringBuilder();
            for (Message message : messagesOfUser) {
                messages.append(message).append(",-");
            }
            out.println(messages.toString());
        }
    }

    public static void broadcastRefresh(Socket client) {
        for (Socket socket : CLIENT_LIST) {
            if (client == socket) continue;
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Refresh Messages"); // Send the message to each client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


//    public static void addClient( ClientHandler clientHandler) {
//        CLIENT_LIST.add(clientHandler);
//    }
//
//    public static void removeClient(String username) {
//        for (ClientHandler client : CLIENT_LIST) {
//            if (client.getUsername().equals(username)) {
//                CLIENT_LIST.remove(client);
//            }
//        }
//
//    }
}

