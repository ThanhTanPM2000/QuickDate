package com.example.quickdate.model;

public class Notification {

    private String senderId;
    private String type;
    private String receiverId;
    private String notification;
    private String timeStamp;

    public Notification(){

    }

    public Notification(String senderId, String type, String receiverId, String notification, String timeStamp) {
        this.senderId = senderId;
        this.type = type;
        this.receiverId = receiverId;
        this.notification = notification;
        this.timeStamp = timeStamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
