package com.eutech.server;

import com.eutech.model.Message;
import com.eutech.service.ChatHistoryService;
import com.eutech.service.UserService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerAppInitializer {

    private static final int PORT = 5050;
    static final List<Socket> CLIENT_LIST = new CopyOnWriteArrayList<>();
    private static BufferedReader in;
    private static PrintWriter out;
    public static void main(String[] args) {
        System.out.println("Chat server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {

                Socket localSocket = serverSocket.accept();
                System.out.println("New client connected.");
                in = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
                out = new PrintWriter(localSocket.getOutputStream(), true);
                new Thread(() -> {
                    try {
//                        InputStream is = localSocket.getInputStream();

                        while (true) {
//                            int read = is.read();
//                            if (read == -1) {
//                                CLIENT_LIST.remove(localSocket); // remove this socket after it exited
//                                break;
//                            }
                            String action = in.readLine();

                            if (action==null){
                                System.out.println("No action");
                                CLIENT_LIST.remove(localSocket);
                                return;
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
                            }else {
                                actionMethod(action, out, in);
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
//                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void actionMethod(String action, PrintWriter out, BufferedReader in) throws IOException {
        if (action.equals("GET_ALL_USERS")) {
//            System.out.println("get all users");
            List<String> allUsers = UserService.getAllUsers();// Fetch and send all users
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

        } else if (action.equals("get_users_messages")) {
            String user = in.readLine();
            List<Message> messagesOfUser = ChatHistoryService.getMessagesOfUser(user);// get messages
            StringBuilder messages = new StringBuilder();
            for (Message message : messagesOfUser) {
                messages.append(message).append("\n");
            }
            out.println(messages.toString());
        }
    }

//    public static void broadcast(String message, ClientHandler sender) {
//        for (ClientHandler client : CLIENT_LIST) {
//            if (!client.getUsername().equals(sender.getUsername())) {
//                client.sendMessage(message);
//            }
//        }
//    }

//    public static void sendPrivateMessage(String sender, String recipient, String message) {
//        ClientHandler recipientHandler=null;
//        for (ClientHandler client : CLIENT_LIST) {
//            if (client.getUsername().equals(recipient)) {
//                recipientHandler = client;
//                break;
//            }
//        }
//        if (recipientHandler != null) {
//            recipientHandler.sendMessage("[Private] " + sender + ": " + message);
//        } else {
//            // Notify the sender if the recipient is not found
//            for (ClientHandler client : CLIENT_LIST) {
//                if (client.getUsername().equals(sender)) {
//                    client.sendMessage("User " + recipient + " is not online.");
//                    break;
//                }
//            }
//        }
//    }


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

