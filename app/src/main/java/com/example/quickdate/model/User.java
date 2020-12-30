package com.example.quickdate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements Serializable {
    private ArrayList<Notification> notifications;
    private String idUser;
    private String email;
    private int status;
    private LookingFor lookingFor;
    private Info info;
    private ArrayList<Interest> interests;

    public User(){
    }

    public User(String idUser, String email, int status, LookingFor lookingFor, Info info){
        this.notifications = new ArrayList<Notification>();
        this.idUser = idUser;
        this.email = email;
        this.status = status;
        this.lookingFor = lookingFor;
        this.info = info;
        this.interests = new ArrayList<Interest>();
        this.interests.add(new Interest("Art & Design", false, "https://firebasestorage.googleapis.com/v0/b/quickdate-c927e.appspot.com/o/imageSystem%2Fimage_artDesign.jpg?alt=media&token=a53a7b34-f71a-4adb-85cd-2ee29e93bd52"));
        this.interests.add(new Interest("TV & Music", false, "https://firebasestorage.googleapis.com/v0/b/quickdate-c927e.appspot.com/o/imageSystem%2Fimage_movie.jpg?alt=media&token=05690019-15d5-4f58-b5be-c213bcdf7c3e"));
        this.interests.add(new Interest("Tech", false, "https://firebasestorage.googleapis.com/v0/b/quickdate-c927e.appspot.com/o/imageSystem%2Fimage_tech.jpg?alt=media&token=503e0e7e-b97a-4d85-aca1-6a100fdb756e"));
        this.interests.add(new Interest("Food", false,"https://firebasestorage.googleapis.com/v0/b/quickdate-c927e.appspot.com/o/imageSystem%2Fimage_food.jpg?alt=media&token=f02b3ae8-01e6-48ce-904d-188afe1b8d7b"));
        this.interests.add(new Interest("Animals", false, "https://firebasestorage.googleapis.com/v0/b/quickdate-c927e.appspot.com/o/imageSystem%2Fimage_animals.jpg?alt=media&token=a056f0a2-7a97-412a-bf0f-12d62c910441"));
        this.interests.add(new Interest("Fitness & Health", false, "https://firebasestorage.googleapis.com/v0/b/quickdate-c927e.appspot.com/o/imageSystem%2Fimage_fitness%E2%80%A9AndHealth.jpg?alt=media&token=4c6ae426-c7fd-46a2-80d8-153faa947e61"));
        this.interests.add(new Interest("Cars", false, "https://firebasestorage.googleapis.com/v0/b/quickdate-c927e.appspot.com/o/imageSystem%2Fimage_cars.jpg?alt=media&token=6370fadb-8132-467e-a933-350d7e94a9a2"));
        this.interests.add(new Interest("Sports", false, "https://firebasestorage.googleapis.com/v0/b/quickdate-c927e.appspot.com/o/imageSystem%2Fimage_fooball.jpg?alt=media&token=eae0fc7f-a071-4be9-97d3-d3ed1d934996"));
        this.interests.add(new Interest("Books", false, "https://firebasestorage.googleapis.com/v0/b/quickdate-c927e.appspot.com/o/imageSystem%2Fimage_book.jpg?alt=media&token=7faaf98e-fad9-4085-9f77-32e7ba8c4a9e"));
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
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


