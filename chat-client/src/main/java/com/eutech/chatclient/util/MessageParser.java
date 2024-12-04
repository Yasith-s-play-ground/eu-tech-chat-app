package com.eutech.chatclient.util;

import com.eutech.chatclient.model.Message;

import java.util.regex.*;
import java.util.HashMap;

public class MessageParser {
    public static Message parseMessage(String input) {
        // Regular expression to extract key-value pairs
        Pattern pattern = Pattern.compile("(\\w+)=(.*?)(?:,\\s|\\))");
        Matcher matcher = pattern.matcher(input);

        // Map to store the key-value pairs
        HashMap<String, String> map = new HashMap<>();

        while (matcher.find()) {
            String key = matcher.group(1); // Key
            String value = matcher.group(2); // Value
            map.put(key, value);
        }

        // Convert the map into a Message object
        int messageId = Integer.parseInt(map.get("messageId"));
        String sender = map.get("sender");
        String receiver = map.get("receiver");
        String message = map.get("message");
        String sentAt = map.get("sentAt");

        return new Message(messageId, sender, receiver, message, sentAt);
    }

}
