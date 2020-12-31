package com.example.quickdate.listener;

import com.example.quickdate.model.User;

import java.util.ArrayList;

public interface UserListener {
     void getUser(User user);
     void getUsers(ArrayList<User> users);
}
