package com.eutech.chatclient.controller;

import com.eutech.chatclient.SocketManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginViewController {

    private String host = "localhost";
    private int port = 5050;
    public Button btnLogin;


    @FXML
    private Button btnRegister;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;


    @FXML
    void btnRegisterOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/eutech/chatclient/RegisterView.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));

//        MainViewController controller = fxmlLoader.getController();
//        controller.initData(remoteSocket, username);

        stage.setTitle("EU Tech Chat App");
        stage.show();
        stage.centerOnScreen();

        ((Stage) btnLogin.getScene().getWindow()).close();
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isBlank()) {
            txtUsername.requestFocus();
            txtUsername.selectAll();
            return;
        }
        if (password.isBlank()) {
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        }

        try {
//            System.out.println("initialise socket");
            String response = sendLoginRequest(username, password);
            new Alert(Alert.AlertType.INFORMATION, response).show();
            ((Stage) btnLogin.getScene().getWindow()).close();

            Stage mainStage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/eutech/chatclient/ChatUI.fxml"));
            mainStage.setScene(new Scene(fxmlLoader.load()));

            ChatViewController controller = fxmlLoader.getController();
            controller.initData(username);

            mainStage.setTitle("EU Tech Chat App");
            mainStage.show();
            mainStage.centerOnScreen();

            mainStage.setOnCloseRequest((event) -> {
                try {
                    SocketManager.getInstance().closeConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (UnknownHostException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid IP Address/Host").show(); // if host / ip address is invalid
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid port number").show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to connect with the server").show();
        }


    }

    private String sendLoginRequest(String username, String password) throws IOException {
//        try (Socket socket = new Socket(host, port);
//             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
//
//            // Send login action type, username, and password
//            out.println("login");
//            out.println(username);
//            out.println(password);
//
//            // Receive response from the server
//            return in.readLine(); // Response from the server (e.g., "Login successful" or "Invalid username/password")
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Connection error.";
//        }
        return SocketManager.getInstance().sendLoginRequest(username, password);
    }

}
