package com.example.quickdate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quickdate.Models.*;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

public class Profile_User extends AppCompatActivity {
    private TextView user_name;
    private ImageView avatar_user;
    private Button btn_Logout;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        user_name = findViewById(R.id.username);
        avatar_user = findViewById(R.id.avatar);
        btn_Logout = findViewById(R.id.btn_logout);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // <-- get user from firebase auth
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // <-- get data from firebase store
        db.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    DocumentSnapshot value = task.getResult(); // <-- get document snapshot
                    Users user = value.toObject(Users.class); // <-- add value from document to class name: Users
                    user_name.setText(user.getUsername());
                    if(user.getImgAvt().equals("default")){
                        avatar_user.setImageResource(R.mipmap.ic_launcher);
                    }else{
                        Glide.with(Profile_User.this).load(user.getImgAvt()).into(avatar_user); // <-- load imageUrl to a picture
                    }
                }
            }
        });

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getBaseContext(),MainActivity.class));
                finish();
            }
        });
    }
}