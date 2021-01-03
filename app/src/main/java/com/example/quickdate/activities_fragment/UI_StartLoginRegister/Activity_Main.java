package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_QuickDate.Activity_Home;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Executor;

public class Activity_Main extends AppCompatActivity {

    TextView tv_quick, tv_date;
    Button btnFacebook;
    ImageButton btnLogin, btnSignUp;
    CallbackManager fbCallBackManager;
    FirebaseAuth auth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        doFunctionInAct();
    }

    private void initialization() {
        tv_quick = findViewById(R.id.tv_quick);
        tv_date = findViewById(R.id.tv_date);
        btnLogin = findViewById(R.id.btn_login_splashAct);
        btnSignUp = findViewById(R.id.btn_signUp_splashAct);
        auth = FirebaseAuth.getInstance();

        changeColorComponents();
    }

    private void changeColorComponents() {
        tv_quick.setTextColor(Color.parseColor("#FF8960"));
        tv_date.setTextColor(Color.parseColor("#FF8960"));

        Shader textShader = new LinearGradient(0, 0, 0, 170,
                new int[]{
                        Color.parseColor("#FF8960"),
                        Color.parseColor("#FF62A5"),
                }, null, Shader.TileMode.CLAMP);
        tv_quick.getPaint().setShader(textShader);
        tv_date.getPaint().setShader(textShader);
    }

    private void doFunctionInAct() {
        callActLogin();
        callActSignUp();
    }

    private void callActLogin() {
        PushDownAnim.setPushDownAnimTo(btnLogin).setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Main.this, Activity_Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void callActSignUp() {
        PushDownAnim.setPushDownAnimTo(btnSignUp).setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Main.this, Activity_SignUp.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        fbCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(Activity_Main.this, Activity_Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}