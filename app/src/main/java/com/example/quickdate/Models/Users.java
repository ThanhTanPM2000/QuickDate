package com.example.quickdate.Models;

public class Users {
    private String username;
    private String email;
    private String phone;
    private String imgAvt;

    public Users() {
    }

    public Users(String username, String email, String phone, String imgAvt) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.imgAvt = imgAvt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgAvt() {
        return imgAvt;
    }

    public void setImgAvt(String imgAvt) {
        this.imgAvt = imgAvt;
    }
}
