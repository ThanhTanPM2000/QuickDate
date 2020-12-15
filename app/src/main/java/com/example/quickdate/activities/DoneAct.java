package com.example.quickdate.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.quickdate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.Objects;

public class DoneAct extends AppCompatActivity {

    ImageView iv_submit;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        initialization();
        doFunctionInAct();
    }

    private void initialization(){
        iv_submit = (ImageView) findViewById(R.id.iv_submit_doneAct);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        parentView = findViewById(R.id.content);
    }

    private void doFunctionInAct(){
        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSubmitAct();
            }
        });
    }

    private void callSubmitAct(){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Uses/" + firebaseUser.getUid());
        databaseReference.child("status").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), SwipeAct.class));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }else{
                    Snackbar.make(parentView, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
        });

    }
}