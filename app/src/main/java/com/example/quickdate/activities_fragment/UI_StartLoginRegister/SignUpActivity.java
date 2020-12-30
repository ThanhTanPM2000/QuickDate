package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.model.Info;
import com.example.quickdate.model.LookingFor;
import com.example.quickdate.model.User;
import com.example.quickdate.utility.regexString;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class SignUpActivity extends AppCompatActivity {

    private EditText et_passWord, et_email;
    private Spinner sp_provincial;
    private ImageButton btn_submit;
    private ImageView iv_backAct_signUpAct;
    private CheckBox cb_policy;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialization();
        doFunctionInAct();


    }

    private void initialization(){
        et_passWord = findViewById(R.id.et_register_password_signUpAct);
        et_email = findViewById(R.id.et_register_mail_signUpAct);
        sp_provincial = findViewById(R.id.sp_provincial_signUpAct);
        btn_submit = findViewById(R.id.btn_submit_signUpAct);
        iv_backAct_signUpAct = findViewById(R.id.iv_backAct_signUpAct);
        cb_policy = findViewById(R.id.cb_policy_signUpAct);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        TextView tv_hyperLink = findViewById(R.id.tv_hyperLink);
        tv_hyperLink.setMovementMethod(LinkMovementMethod.getInstance());


        setValueForSpinner();
    }

    private void doFunctionInAct(){
        signUpFunction();
        callBackAct();
    }

    private void signUpFunction(){
        PushDownAnim.setPushDownAnimTo(btn_submit)
                .setOnClickListener(view -> {
                    String str_password = et_passWord.getText().toString();
                    String str_email = et_email.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    if(isCheckDataInput(str_password, str_email)){
                        firebaseAuth.createUserWithEmailAndPassword(str_email, str_password).addOnCompleteListener(task -> {
                            if(task.isComplete()){
                                progressBar.setVisibility(View.GONE);
                                firebaseUser = firebaseAuth.getCurrentUser();
                                assert firebaseUser != null : "cant find user";
                                databaseReference = FirebaseDatabase.getInstance().getReference("Users/UnRegisters/"+ firebaseUser.getUid());

                                User user = new User( firebaseUser.getUid(), str_email, 0, new LookingFor(), new Info( sp_provincial.getSelectedItem().toString()));

                                databaseReference.setValue(user).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task11 -> {
                                            if(task11.isSuccessful()){
                                                Toast.makeText(SignUpActivity.this, "Register Successfully, please check your email and verification", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(SignUpActivity.this, task11.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }else{
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "Sign up failed !!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isCheckDataInput(String passWordCheck, String emailCheck){
        if(TextUtils.isEmpty(passWordCheck) && TextUtils.isEmpty(emailCheck)){
            Toast.makeText(SignUpActivity.this, "All fields should not empty", Toast.LENGTH_SHORT).show();
        }
        else if(new regexString().regexFunc(getString(R.string.regexEmail), emailCheck)){
            Toast.makeText(SignUpActivity.this, "Field Email invalid", Toast.LENGTH_SHORT).show();
        }
        else if(passWordCheck.length() <8){
            Toast.makeText(SignUpActivity.this, "Field Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
        }
        else if(!cb_policy.isChecked()){
            Toast.makeText(SignUpActivity.this, "Please Agree to terms and Conditions", Toast.LENGTH_SHORT).show();
        }
        else
            return true;
        return false;
    }

    private void callBackAct(){
        PushDownAnim.setPushDownAnimTo(iv_backAct_signUpAct).setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setValueForSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignUpActivity.this, R.array.planets_array, R.layout.layout_spinner); // Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // Specify the layout to use when the list of choices appears
        sp_provincial.setAdapter(adapter); // Apply the adapter to the spinner
    }
}