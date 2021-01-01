package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_QuickDate.Activity_Home;
import com.example.quickdate.model.Notification;
import com.example.quickdate.model.User;
import com.example.quickdate.utility.regexString;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Collection;

public class Activity_Login extends AppCompatActivity {
    private EditText et_email, et_password;
    private ImageButton btn_submit;
    private TextView tv_forgotPass, tv_signUp;
    private CheckBox cb_rememberPass;
    private FirebaseAuth firebaseAuth;
    private ImageView iv_backAct;
    private ProgressDialog progressDialog;

    private User user;
    private ArrayList<User> userArrayList;

    private static final String remember = "vidslogin";
    private static final String emailRemember = "email";
    private static final String passWordRemember = "password";
    private static final String checkedRemember = "bool_checkbox";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();
        doFunctionInAct();
    }

    private void initialization() {
        et_email = findViewById(R.id.et_mail_Login);
        et_password = findViewById(R.id.et_password);
        btn_submit = findViewById(R.id.btn_submit_login);
        tv_forgotPass = findViewById(R.id.tv_forgotPass_loginAct);
        tv_signUp = findViewById(R.id.tv_Signup_loginAct);
        cb_rememberPass = findViewById(R.id.cb_rememberpass_loginAct);
        iv_backAct = findViewById(R.id.iv_backAct_loginAct);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Activity_Login.this);
        progressDialog.setMessage("Loading...");

        SharedPreferences loginPreferences = getSharedPreferences(remember,
                Context.MODE_PRIVATE);
        et_email.setText(loginPreferences.getString(emailRemember, ""));
        et_password.setText(loginPreferences.getString(passWordRemember, ""));
        cb_rememberPass.setChecked(loginPreferences.getBoolean(checkedRemember, false));
    }

    private void doFunctionInAct() {
        loginFunction();
        callBackAct();
        callActForgotPassword();
        callActSignUp();
    }

    private void loginFunction() {
        PushDownAnim.setPushDownAnimTo(btn_submit)
                .setOnClickListener(view -> {
                    String str_email = et_email.getText().toString().trim();
                    String str_password = et_password.getText().toString().trim();
                    progressDialog.show();
                    if (isCheckDataInput(str_email, str_password)) {
                        isRememberPasswordFunction(str_email, str_password);
                        firebaseAuth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                //user.isEmailVerified()
                                if (true) {
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/UnRegisters/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            progressDialog.dismiss();
                                            User user = snapshot.getValue(User.class);
                                            if (user == null) {
                                                findCurrentUser();
                                            } else {
                                                Intent intent = new Intent(getApplicationContext(), Activity_SelectGender.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.putExtra("User", user);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please verification your email for login", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private boolean isCheckDataInput(String emailCheck, String passWordCheck) {
        if (TextUtils.isEmpty(emailCheck) && TextUtils.isEmpty(passWordCheck)) {
            Toast.makeText(Activity_Login.this, "All fields shouldn't empty", Toast.LENGTH_SHORT).show();
        } else if (new regexString().regexFunc(getString(R.string.regexEmail), emailCheck)) {
            Toast.makeText(Activity_Login.this, "Field Email invalid", Toast.LENGTH_SHORT).show();
        } else if (passWordCheck.length() < 8) {
            Toast.makeText(Activity_Login.this, "Email wasn't found or Password incorrect", Toast.LENGTH_SHORT).show();
        } else
            return true;
        return false;
    }

    private void isRememberPasswordFunction(String email, String passWord) {
        if (cb_rememberPass.isChecked()) {
            SharedPreferences loginPreferences = getSharedPreferences(remember, Context.MODE_PRIVATE);
            loginPreferences.edit().putString(emailRemember, email).putString(passWordRemember, passWord).putBoolean(checkedRemember, true).apply();
        } else {
            SharedPreferences loginPreferences = getSharedPreferences(remember, Context.MODE_PRIVATE);
            loginPreferences.edit().clear().apply(); // <-- clear remember account
        }
    }

    private void callActForgotPassword() {
        PushDownAnim.setPushDownAnimTo(tv_forgotPass).setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Login.this, Activity_ForgotPassword.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void callActSignUp() {
        PushDownAnim.setPushDownAnimTo(tv_signUp).setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Login.this, Activity_SignUp.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void callBackAct() {
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Login.this, Activity_Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        });
    }

    private void findCurrentUser() {
        String[] genders = new String[]{"Male", "Female"};
        for (int i = 0; i < 2; i++) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(genders[i]).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        user = snapshot.getValue(User.class);
                        user.setStatusOnline("Online");
                        databaseReference.child("statusOnline").setValue("Online");
                        getAllOppositeUsers();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void getAllOppositeUsers() {
        userArrayList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Users")
                .child(user.getInfo().getGender().equals("Male") ? "Female" : "Male")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            User tempUser = ds.getValue(User.class);
                            if (tempUser.getInfo().getAge() >= user.getLookingFor().getMin_age() &&
                                    tempUser.getInfo().getAge() <= user.getLookingFor().getMax_age() &&
                                    tempUser.getLookingFor().getLooking().equals(user.getLookingFor().getLooking()) &&
                                    tempUser.getInfo().getWeight() <= user.getLookingFor().getMax_weight() &&
                                    tempUser.getInfo().getWeight() >= user.getLookingFor().getMin_weight() &&
                                    tempUser.getInfo().getHeight() <= user.getLookingFor().getMax_height() &&
                                    tempUser.getInfo().getHeight() >= user.getLookingFor().getMin_height())
                            {
                                FirebaseDatabase.getInstance().getReference("Matchers").child(user.getIdUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            for(DataSnapshot ds : snapshot.getChildren()){
                                                if(!tempUser.getIdUser().equals(ds.getValue(String.class))){
                                                    userArrayList.add(tempUser);
                                                }
                                            }
                                        }
                                        else {
                                            userArrayList.add(tempUser);
                                        }
                                        Intent intent = new Intent(getApplicationContext(), Activity_Home.class);
                                        intent.putExtra("User", user);
                                        intent.putExtra("OppositeUsers", userArrayList);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}