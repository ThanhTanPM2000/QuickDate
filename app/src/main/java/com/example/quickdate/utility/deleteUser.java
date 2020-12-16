package com.example.quickdate.utility;

import com.google.firebase.auth.FirebaseUser;

public class deleteUser {
    private FirebaseUser firebaseUser;

    public deleteUser(FirebaseUser firebaseUser){
        this.firebaseUser = firebaseUser;
        firebaseUser.delete();
    }
}
