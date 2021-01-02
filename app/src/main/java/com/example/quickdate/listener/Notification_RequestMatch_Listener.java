package com.example.quickdate.listener;

import android.app.ProgressDialog;

import com.example.quickdate.model.Notification;

public interface Notification_RequestMatch_Listener {

    void unAcceptClick(Notification notification, int position, ProgressDialog pd);
    void acceptClick(Notification notification, int position, ProgressDialog pd);

}
