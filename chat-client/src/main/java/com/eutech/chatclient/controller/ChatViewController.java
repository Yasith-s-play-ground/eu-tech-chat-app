package com.eutech.chatclient.controller;

import com.eutech.chatclient.SocketManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatViewController {

    private String host = "localhost";
    private int port = 5050;
    private Socket socket;
    private List<String> onlineUsers = new ArrayList<>();
    @FXML
    private TextField messageInput;

    @FXML
    private VBox messagePane;

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private TextArea txtArea;

    @FXML
    private VBox userListPane;
    private String currentChatUser;
    @FXML
    private ListView<String> userListView;
    private SocketManager socketManager;
    private String username;

    @FXML
    void loadBtnOnAction(ActionEvent event) {
//        fetchOnlineUsers();
    }

    @FXML
    void sendMessage(ActionEvent event) {
        String message = messageInput.getText().trim();

        if (!message.isEmpty() && currentChatUser != null) {
            List<String> selectedUsers = userListView.getSelectionModel().getSelectedItems();
            // Send message to the selected user

            socketManager.saveMessageToDatabase(username, message, selectedUsers);
//                socketManager.sendMessage("SEND " + currentChatUser + " " + message);
            addMessageToChat("You: " + message);
            messageInput.clear();

        }
    }

    public void initialize() {

        userListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        // Initialize socket connection

        try {
            socketManager = new SocketManager(host, port);
            // Simulate loading user list from a service or database
//            Platform.runLater(this::fetchOnlineUsers);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set a listener for when a user is selected
        userListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentChatUser = newValue;
                try {
                    showMessagesForUser(currentChatUser);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // Fetch the list of online users
    private void fetchOnlineUsers() {
        // Send request to server to get the list of online users (this should be part of your server logic)
        try {
            // Example: Get users from the server (replace with your server call)
            List<String> users = socketManager.getOnlineUsers();
            onlineUsers.addAll(users);

            // Populate the ListView with the user list
            userListView.getItems().clear();
            userListView.getItems().addAll(onlineUsers);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Display messages between the current user and the selected user
    private void showMessagesForUser(String user) throws IOException {
        // This should be implemented to fetch previous messages (can be done via server or local storage)
        messagePane.getChildren().clear(); // Clear existing messages

        // Example of adding static messages, replace with dynamic message fetching
        messagePane.getChildren().add(new Text("Chat with " + user));
        String usersMessages = socketManager.getUsersMessages(user);
        String[] split = usersMessages.split("\n");
        for (String s : split) {
            Text text = new Text(s);
            messagePane.getChildren().add(text);
        }
    }

    // Add message to the chat view
    private void addMessageToChat(String message) {
        messagePane.getChildren().add(new Text(message));
        messageScrollPane.setVvalue(1.0);  // Scroll to the bottom
    }

    public void initData(String username) {
        this.username = username;
        System.out.println("inside init data of chat view");
    }
}
