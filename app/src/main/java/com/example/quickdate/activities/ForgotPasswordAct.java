package com.example.quickdate.activities;


import com.example.quickdate.R;
import com.example.quickdate.action.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class ForgotPasswordAct extends AppCompatActivity {

    EditText et_email;
    ConstraintLayout ctl_submit;
    ImageView iv_backAct;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initialization();
        doFunctionInAct();
    }

    private void initialization(){
        et_email = (EditText) findViewById(R.id.et_email_forgotPasswordAct);
        ctl_submit = (ConstraintLayout) findViewById(R.id.ctl_submit_forgotPasswordAct);
        iv_backAct = (ImageView) findViewById(R.id.iv_backAct_forgotPasswordAct);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void doFunctionInAct(){
        forgotPasswordFunction();
        callBackAct();
    }

    private void forgotPasswordFunction(){
        PushDownAnim.setPushDownAnimTo(ctl_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = et_email.getText().toString();
                if(checkDataInput(mail)){
                    firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getBaseContext(), "Check your email, to get new Password", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Seem like email wasn't register to QuickDate", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean checkDataInput(String email){
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getBaseContext(), "You must fill your email :P", Toast.LENGTH_SHORT).show();
        }else if(new regexString().regexFunc(getString(R.string.regexEmail), email)){
            Toast.makeText(getBaseContext(), "You kidding me, it is not Email", Toast.LENGTH_SHORT).show();
        }else {
            return true;
        }
        return false;
    }

    private void callBackAct(){
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}