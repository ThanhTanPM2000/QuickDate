package com.example.quickdate.model;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String idUser;
    private String email;
    private int status;
    private LookingFor lookingFor;
    private Info info;
    private ArrayList<Interest> interests;

    public User(){
    }

    public User(String idUser, String email, int status, LookingFor lookingFor, Info info, ArrayList<Interest> interests){
        this.idUser = idUser;
        this.email = email;
        this.status = status;
        this.lookingFor = lookingFor;
        this.info = info;
        this.interests = interests;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LookingFor getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(LookingFor lookingFor) {
        this.lookingFor = lookingFor;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public ArrayList<Interest> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<Interest> interests) {
        this.interests = interests;
    }
}


