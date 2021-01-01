package com.example.quickdate.model;

import com.google.firebase.database.PropertyName;

public class Chat {
    private String message;
    private String receiver;
    private String sender;
    private String timestamp;
    private boolean isSeen;

    public Chat() {

    }

    public Chat(String message, String receiver, String sender, String timestamp, boolean isSeen) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.timestamp = timestamp;
        this.isSeen = isSeen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @PropertyName("isSeen")
    public boolean isSeen() {
        return isSeen;
    }

    @PropertyName("isSeen")
    public void setSeen(boolean seen) {
        this.isSeen = seen;
    }
}
