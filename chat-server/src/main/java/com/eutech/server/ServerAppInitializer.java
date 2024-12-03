package com.eutech.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerAppInitializer {

    private static final int PORT = 5050;
    static final List<ClientHandler> CLIENT_LIST = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("Chat server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected.");
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
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


    public static void addClient( ClientHandler clientHandler) {
        CLIENT_LIST.add(clientHandler);
    }

    public static void removeClient(String username) {
        for (ClientHandler client : CLIENT_LIST) {
            if (client.getUsername().equals(username)) {
                CLIENT_LIST.remove(client);
            }
        }

    }
}

