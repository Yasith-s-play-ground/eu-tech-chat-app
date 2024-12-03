package com.eutech.chatclient.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

public class MainViewController {
    public TextArea txtDisplay;
    public TextField txtMessage;
    public Button btnSend;
    private Socket remoteSocket;
    private String nickName;

    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        txtDisplay.appendText("You : " + txtMessage.getText().strip() + "\n");
        String message = nickName + ": " + txtMessage.getText().strip() + "\n";
        remoteSocket.getOutputStream().write(message.getBytes());
    }

    public void initData(Socket remoteSocket, String nickName) {
        this.remoteSocket = remoteSocket;
        this.nickName = nickName;
        appendText("You entered into the chat room \n");

        new Thread(() -> {
            try {
                InputStream is = remoteSocket.getInputStream();
                byte[] byteBuffer = new byte[1024];
                while (true) {
                    int read = is.read(byteBuffer);
                    if (read == -1) {
                        appendText("Connection lost");
                        Platform.runLater(() -> {
                            btnSend.setDisable(true);
                            txtMessage.setDisable(true);
                        });
                        break;
                    }
                    String message = new String(byteBuffer, 0, read);
                    Arrays.fill(byteBuffer, (byte) 0); // fill array with 0 again
                    appendText(message);
                }
            } catch (IOException e) {
                if (remoteSocket.isConnected()) throw new RuntimeException(e); // when socket is closed when window closed
            }
        }).start();
    }

    private void appendText(String message) {
        Platform.runLater(() -> txtDisplay.appendText(message));
    }
}
