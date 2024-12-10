package com.eutech.server;

import com.eutech.model.Message;
import com.eutech.service.ChatHistoryService;
import com.eutech.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public String getUsername() {
        return username;
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                // Register or Authenticate User
                if (username == null) {

                    // Read the action type (login)
                    String action = in.readLine();

                    if ("login".equals(action)) {
                        String username = in.readLine();
                        String password = in.readLine();

                        // Check if credentials are correct
                        if (UserService.authenticateUser(username, password)) {
                            out.println("Login successful");
                            this.username = username;
//                            ServerAppInitializer.addClient(this);
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
                            this.username = username;
//                            ServerAppInitializer.addClient(this);
//                        out.println("Welcome to the chat, " + username + "!");
//                        ServerAppInitializer.broadcast(username + " has joined the chat.", this);
                        } else {
                            out.println("Registration failed");
                        }
                    }

                }
                String action = in.readLine();
                if (action == null) {
                    System.out.println("no action");
                    return;
                }
               // ServerAppInitializer.actionMethod(action, out, in);


                // Handle messages
//            String message;
//            while ((message = in.readLine()) != null) {
//                if (message.equalsIgnoreCase("exit")) {
//                    break;
//                }
////                ServerAppInitializer.broadcast(username + ": " + message, this);
//                ChatHistoryService.saveMessage(username, message);
//            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
//                ServerAppInitializer.removeClient(username);
//                ServerAppInitializer.broadcast(username + " has left the chat.", this);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    private void registerUser() throws IOException {
//        System.out.println("Enter username:");
//        String newUsername = in.readLine();
//        System.out.println("Enter password:");
//        String newPassword = in.readLine();
//        System.out.println("Enter email:");
//        String newEmail = in.readLine();
//        System.out.println("Enter phone:");
//        String newPhone = in.readLine();
//
//        if (UserService.registerUser(newUsername, newPassword, newEmail, newPhone)) {
//            out.println("Registration successful. You can now log in.");
//        } else {
//            out.println("Registration failed. Try again.");
//        }
//    }

//    private void authenticateUser() throws IOException {
//        System.out.println("Enter username:");
//        String enteredUsername = in.readLine();
//        System.out.println("Enter password:");
//        String enteredPassword = in.readLine();
//
//        if (UserService.authenticateUser(enteredUsername, enteredPassword)) {
//            username = enteredUsername;
//            System.out.println("Login successful.");
//        } else {
//            System.out.println("Invalid credentials. Try again.");
//        }
//    }

    public void sendMessage(String message) {
        System.out.println(message);
    }

    // Private message to specific user
//    private void sendPrivateMessage(String targetUsername, String message) {
//        boolean userFound = false;
//        for (ClientHandler clientHandler : ServerAppInitializer.CLIENT_LIST) {
//            if (clientHandler.username.equals(targetUsername)) {
//                clientHandler.sendMessage("Private message from " + username + ": " + message);
//                userFound = true;
//                break;
//            }
//        }
//        if (!userFound) {
//            sendMessage("User " + targetUsername + " not found.");
//        }
//    }
}

