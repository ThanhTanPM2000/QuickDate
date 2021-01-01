package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_QuickDate.Activity_Home;
import com.example.quickdate.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class Activity_RegisterDone extends AppCompatActivity {

    private ImageView iv_submit;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        init();
        doFunctionInAct();
    }

    private void init() {
        // Init view
        iv_submit = findViewById(R.id.iv_submit_doneAct);
        progressBar = findViewById(R.id.progress_bar_done);

        // Init firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        user = (User) getIntent().getSerializableExtra("User");
    }

    private void doFunctionInAct() {
        progressBar.setVisibility(View.VISIBLE);

        user.setStatusOnline("" + System.currentTimeMillis());
        databaseReference = firebaseDatabase.getReference("Users/" + user.getInfo().getGender() + "/" + firebaseUser.getUid());
        databaseReference.setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                iv_submit.setVisibility(View.VISIBLE);

                databaseReference = firebaseDatabase.getReference("Users/UnRegisters/" + firebaseUser.getUid());
                databaseReference.removeValue();
            } else {
                Toast.makeText(Activity_RegisterDone.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(v -> callSubmitAct());
    }

    private void callSubmitAct() {
        Intent intent = new Intent(Activity_RegisterDone.this, Activity_Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}