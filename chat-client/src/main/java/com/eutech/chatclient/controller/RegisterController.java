package com.eutech.chatclient.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RegisterController {
    private String host = "localhost";
    private int port = 5050;
    @FXML
    private TextField emailField;

    @FXML
    private Text errorMessage;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameField;

    @FXML
    void loginOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/eutech/chatclient/LoginView.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));

        stage.setTitle("EU Tech Chat App");
        stage.show();
        stage.centerOnScreen();

        ((Stage) registerButton.getScene().getWindow()).close();
    }

    @FXML
    void registerButtonOnAction(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String response = sendRegisterRequest(username, password, email, phone);
        new Alert(Alert.AlertType.INFORMATION, response).show();
    }

    private String sendRegisterRequest(String username, String password, String email, String phone) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send login action type, username, and password,email,phone
            out.println("register");
            out.println(username);
            out.println(password);
            out.println(email);
            out.println(phone);

            // Receive response from the server
            return in.readLine(); // Response from the server (e.g., "Login successful" or "Invalid username/password")
        } catch (IOException e) {
            e.printStackTrace();
            return "Connection error.";
        }
    }

}
