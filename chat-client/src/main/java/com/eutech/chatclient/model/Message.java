package com.eutech.chatclient.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    private int messageId;
    private String sender;
    private String receiver;
    private String message;
    private String sentAt;
}
