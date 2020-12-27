package com.example.quickdate.listener;

import com.example.quickdate.model.User;
import com.example.quickdate.model.Users;

public interface UserListener {
     void getUser(User user);
     void getUsers(Users users);
}
