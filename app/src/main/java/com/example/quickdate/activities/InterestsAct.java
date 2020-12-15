package com.example.quickdate.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.adapter.InterestsAdapter;
import com.example.quickdate.listener.InterestsListener;
import com.example.quickdate.model.Interest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.HashMap;

public class InterestsAct extends AppCompatActivity implements InterestsListener {
    private InterestsAdapter interestsAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Interest> interests;
    ImageView iv_backAct, iv_submit;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        initialization();
        doFunctionInAct();
    }

    private void initialization(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_interestsAct);
        iv_backAct = (ImageView) findViewById(R.id.iv_backAct_interestsAct);
        iv_submit = (ImageView) findViewById(R.id.iv_submit_interestsAct);

        interests = new ArrayList<Interest>();
        interests.add(new Interest("Art & Design", false, R.drawable.image_artdesign));
        interests.add(new Interest("TV & Music", false, R.drawable.image_movie));
        interests.add(new Interest("Tech", false, R.drawable.image_tech));
        interests.add(new Interest("Food", false, R.drawable.image_food));
        interests.add(new Interest("Animals", false, R.drawable.image_animals));
        interests.add(new Interest("Fitness & Health", false, R.drawable.image_fitnessandhealth));
        interests.add(new Interest("Cars", false, R.drawable.image_cars));
        interests.add(new Interest("Sports", false, R.drawable.image_fooball));
        interests.add(new Interest("Books", false, R.drawable.image_book));

        interestsAdapter = new InterestsAdapter(interests, this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_interestsAct);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    private void doFunctionInAct(){
        loadDataToRecyclerView();

        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackAct();
            }
        });

        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSubmitAct();
            }
        });
    }

    private void loadDataToRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(interestsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void callBackAct(){
        startActivity(new Intent(getApplicationContext(), DoneActivity.class));
        finish();
    }

    private void callSubmitAct(){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + firebaseUser.getUid());
        databaseReference.child("interests").setValue(interests).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), SwipeAct.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onInterestsClicked(int position, Boolean status) {
        interests.get(position).setStatus(status);
    }
}