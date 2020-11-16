package com.example.quickdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin, password;
    private CheckBox rememberpass;
    private TextView btnLogin, btnforgotpass, btnSignUp;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private static final String SPF_NAME = "vidslogin"; //  <--- Add this
    private static final String EMAIL = "email";  //  <--- To save username
    private static final String PASSWORD = "password";  //  <--- To save password
    private static final String ENABLE_CHECKBOX = "bool_checkbox"; // <---to check checkbox is checked or not

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSignUp = findViewById(R.id.btn_signup);
        emailLogin = findViewById(R.id.et_emailLogin);
        password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        rememberpass = findViewById(R.id.cb_rememberpass);

        SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME,
                Context.MODE_PRIVATE);
        emailLogin.setText(loginPreferences.getString(EMAIL, ""));
        password.setText(loginPreferences.getString(PASSWORD, ""));
        rememberpass.setChecked(loginPreferences.getBoolean(ENABLE_CHECKBOX, false));

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = emailLogin.getText().toString().trim();
                String str_password = password.getText().toString().trim();


                if(TextUtils.isEmpty(str_email) && TextUtils.isEmpty(str_password)){
                    Toast.makeText(LoginActivity.this, "All fields should not empty", Toast.LENGTH_SHORT).show();
                }else if(Regex(getString(R.string.regexEmail), str_email)){
                    Toast.makeText(LoginActivity.this, "Field Email invalid", Toast.LENGTH_SHORT).show();
                }else if(str_password.length() <=8){
                    Toast.makeText(LoginActivity.this, "Field Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                }else
                    loginFunc(str_email, str_password);


                if (rememberpass.isChecked()) {
                    SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
                    loginPreferences.edit().putString(EMAIL, str_email).putString(PASSWORD, str_password).putBoolean(ENABLE_CHECKBOX,true).commit(); // <-- add email, pass to remember acc
                } else {
                    SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
                    loginPreferences.edit().clear().commit(); // <-- clear remember account
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SignupActivity.class));
                finish();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

    }

    public void loginFunc(String str_email, String str_password){

        firebaseAuth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getBaseContext(), Profile_User.class));
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Authenticator failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // or your code
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public Boolean Regex(String str, String str2){
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(str2);
        return !matcher.find();
    }
}