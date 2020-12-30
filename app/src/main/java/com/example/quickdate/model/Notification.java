package com.example.quickdate.model;

public class Notification {
    private String type;
    private String Uid;
    private String myUid;
    private String hisImage;
    private String hisName;
    private String notification;
    private String timeStamp;

    public Notification(String type, String uid, String myUid, String hisImage, String hisName, String notification, String timeStamp) {
        this.type = type;
        Uid = uid;
        this.myUid = myUid;
        this.hisImage = hisImage;
        this.hisName = hisName;
        this.notification = notification;
        this.timeStamp = timeStamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getMyUid() {
        return myUid;
    }

    public void setMyUid(String myUid) {
        this.myUid = myUid;
    }

    public String getHisImage() {
        return hisImage;
    }

    public void setHisImage(String hisImage) {
        this.hisImage = hisImage;
    }

    public String getHisName() {
        return hisName;
    }

    public void setHisName(String hisName) {
        this.hisName = hisName;
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
