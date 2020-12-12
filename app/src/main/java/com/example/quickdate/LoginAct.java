package com.example.quickdate;

import com.example.quickdate.action.regexString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class LoginAct extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_submit;
    TextView tv_forgotPass, tv_signUp;
    CheckBox cb_rememberPass;
    FirebaseAuth firebaseAuth;
    ImageView iv_backAct;
    ProgressBar progressBar;

    private static final String remember = "vidslogin";
    private static final String emailRemember = "email";
    private static final String passWordRemember = "password";
    private static final String checkedRemember = "bool_checkbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();
        doFunctionInAct();
    }

    private void initialization(){
        et_email = (EditText) findViewById(R.id.et_mail_Login);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_submit = (Button) findViewById(R.id.btn_submit_login);
        tv_forgotPass = (TextView) findViewById(R.id.tv_forgotPass_loginAct);
        tv_signUp = (TextView) findViewById(R.id.tv_Signup_loginAct);
        cb_rememberPass = (CheckBox) findViewById(R.id.cb_rememberpass_loginAct);
        iv_backAct = (ImageView) findViewById(R.id.iv_backAct_loginAct);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences loginPreferences = getSharedPreferences(remember,
                Context.MODE_PRIVATE);
        et_email.setText(loginPreferences.getString(emailRemember, ""));
        et_password.setText(loginPreferences.getString(passWordRemember, ""));
        cb_rememberPass.setChecked(loginPreferences.getBoolean(checkedRemember, false));
    }

    private void doFunctionInAct(){
        loginFunction();
        callBackAct();
        callActForgotPassword();
        callActSignUp();
    }

    private void loginFunction(){
        PushDownAnim.setPushDownAnimTo(btn_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str_email = et_email.getText().toString().trim();
                        String str_password = et_password.getText().toString().trim();
                        progressBar.setVisibility(View.VISIBLE);
                        if(isCheckDataInput(str_email, str_password)){
                            isRememberPasswordFunction(str_email, str_password);
                            firebaseAuth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if(user.isEmailVerified()){
                                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + user.getUid());
                                            db.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    User u = snapshot.getValue(User.class);
                                                    if(u.getStatus() == 0){
                                                        startActivity(new Intent(getApplicationContext(), SelectGenderAct.class));
                                                    }else{
                                                        startActivity(new Intent(getApplicationContext(), SwipeAct.class));
                                                    }
                                                    finish();
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    System.out.println("The read failed: " + error.getCode());
                                                }
                                            });
                                        }else{
                                            Toast.makeText(getBaseContext(), "Please verification your email for login", Toast.LENGTH_LONG).show();

                                        }
                                    } else {
                                        Toast.makeText(getBaseContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private boolean isCheckDataInput(String emailCheck, String passWordCheck){
        if (TextUtils.isEmpty(emailCheck) && TextUtils.isEmpty(passWordCheck)) {
            Toast.makeText(getApplicationContext(), "All fields shouldn't empty", Toast.LENGTH_SHORT).show();
        } else if (new regexString().regexFunc(getString(R.string.regexEmail), emailCheck)) {
            Toast.makeText(getApplicationContext(), "Field Email invalid", Toast.LENGTH_SHORT).show();
        } else if (passWordCheck.length() < 8) {
            Toast.makeText(getApplicationContext(), "Email wasn't found or Password incorrect", Toast.LENGTH_SHORT).show();
        } else
            return true;
        return false;
    }

    private void isRememberPasswordFunction(String email, String passWord){
        if (cb_rememberPass.isChecked()) {
            SharedPreferences loginPreferences = getSharedPreferences(remember, Context.MODE_PRIVATE);
            loginPreferences.edit().putString(emailRemember, email).putString(passWordRemember, passWord).putBoolean(checkedRemember, true).apply();
        } else {
            SharedPreferences loginPreferences = getSharedPreferences(remember, Context.MODE_PRIVATE);
            loginPreferences.edit().clear().apply(); // <-- clear remember account
        }
    }

    private void callActForgotPassword(){
        PushDownAnim.setPushDownAnimTo(tv_forgotPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordAct.class));
                finish();
            }
        });
    }

    private void callActSignUp(){
        PushDownAnim.setPushDownAnimTo(tv_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpAct.class));
                finish();
            }
        });
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