package com.example.quickdate.action;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class deleteUser {
    private FirebaseUser firebaseUser;

    public deleteUser(FirebaseUser firebaseUser){
        this.firebaseUser = firebaseUser;
        firebaseUser.delete();
    }
}
