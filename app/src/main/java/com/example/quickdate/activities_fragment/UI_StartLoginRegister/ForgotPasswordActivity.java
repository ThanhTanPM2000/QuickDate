package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.utility.regexString;
import com.google.firebase.auth.FirebaseAuth;
import com.thekhaeng.pushdownanim.PushDownAnim;


public class ForgotPasswordActivity extends AppCompatActivity {

    EditText et_email;
    ImageButton btn_submit;
    ImageView iv_backAct;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initialization();
        doFunctionInAct();
    }

    private void initialization() {
        et_email = findViewById(R.id.et_email_forgotPasswordAct);
        btn_submit = findViewById(R.id.btn_submit_forgotPassword);
        iv_backAct = findViewById(R.id.iv_backAct_forgotPasswordAct);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void doFunctionInAct() {
        forgotPasswordFunction();
        callBackAct();
    }

    private void forgotPasswordFunction() {
        PushDownAnim.setPushDownAnimTo(btn_submit).setOnClickListener(v -> {
            String mail = et_email.getText().toString();
            if (checkDataInput(mail)) {
                firebaseAuth.sendPasswordResetEmail(mail)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(ForgotPasswordActivity.this, "Check your email, to get new Password", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(ForgotPasswordActivity.this, "Seem like email wasn't register to QuickDate", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private boolean checkDataInput(String email) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ForgotPasswordActivity.this, "You must fill your email :P", Toast.LENGTH_SHORT).show();
        } else if (new regexString().regexFunc(getString(R.string.regexEmail), email)) {
            Toast.makeText(ForgotPasswordActivity.this, "You kidding me, it is not Email", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private void callBackAct() {
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}