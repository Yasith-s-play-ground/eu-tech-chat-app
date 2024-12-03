package com.eutech.chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/eutech/chatclient/LoginView.fxml"))));
            primaryStage.show();
            primaryStage.setTitle("EUTech Chat App : Login");
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}