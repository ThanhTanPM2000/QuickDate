package com.example.quickdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView tv_quick, tv_date;
    Button btnFacebook, btnLogin, btnSignUp;
    CallbackManager fbCallBackManager;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        doFunctionInAct();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void initialization() {
        tv_quick = (TextView) findViewById(R.id.tv_quick);
        tv_date = (TextView) findViewById(R.id.tv_date);
        btnFacebook = (Button) findViewById(R.id.btn_facebookLogin);
        btnLogin = (Button) findViewById(R.id.btn_login_splashAct);
        btnSignUp = (Button) findViewById(R.id.btn_signUp_splashAct);
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
        facebookLogin();
        callActLogin();
        callActSignUp();
    }

    private void facebookLogin() {
        fbCallBackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());

        PushDownAnim.setPushDownAnimTo(btnFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(fbCallBackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("fb login", "Cancle");
                        Toast.makeText(MainActivity.this, "Cancle.",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("fb login status", "FB error: " + error.getMessage());
                        Toast.makeText(MainActivity.this, "Error.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken fbToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(fbToken.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

                            if(databaseReference != null){
                                startActivity(new Intent(getApplicationContext(), LoginAct.class));
                                finish();
                            }
                            else{
                                HashMap<String, Object> data = new HashMap<>();
                                data.put("username", user.getDisplayName());
                                data.put("email", user.getEmail());
                                data.put("phone", "");
                                data.put("imgAvt", user.getPhotoUrl().toString());
                                data.put("provincial", "HCMC");  // get value item from spinner
                                data.put("gender", "male");
                                data.put("age", 18);
                                data.put("description","");
                                data.put("height", 170);
                                data.put("weight", 50);
                                data.put("type_gender", "male");
                                data.put("min_age", 18);
                                data.put("max_age", 40);
                                data.put("min_height", 150);
                                data.put("max_height", 200);
                                data.put("min_weight", 40);
                                data.put("max_weight", 80);
                                data.put("interest", "");

                                databaseReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(getApplicationContext(), LoginAct.class));
                                            finish();
                                        }
                                    }
                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void callActLogin() {
        PushDownAnim.setPushDownAnimTo(btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginAct.class));
                finish();
            }
        });
    }

    private void callActSignUp() {
        PushDownAnim.setPushDownAnimTo(btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpAct.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        fbCallBackManager.onActivityResult(requestCode, resultCode, data);
    }
}