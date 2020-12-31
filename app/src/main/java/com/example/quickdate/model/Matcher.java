package com.example.quickdate.model;

public class Matcher {
    private String uid;
    private String nickName;
    private String email;
    private String image;

    public Matcher(String uid, String nickName, String email, String image) {
        this.uid = uid;
        this.nickName = nickName;
        this.email = email;
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
