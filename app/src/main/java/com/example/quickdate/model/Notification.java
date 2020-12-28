package com.example.quickdate.model;

public class Notification {
    private String sUid;
    private String sImage;
    private String hisUid;
    private String hisImage;
    private String message;
    private String timeStamp;

    public Notification(String sUid, String sImage, String hisUid, String hisImage, String message, String timeStamp) {
        this.sUid = sUid;
        this.sImage = sImage;
        this.hisUid = hisUid;
        this.hisImage = hisImage;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getsUid() {
        return sUid;
    }

    public String getsImage() {
        return sImage;
    }

    public String getHisUid() {
        return hisUid;
    }

    public String getHisImage() {
        return hisImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
