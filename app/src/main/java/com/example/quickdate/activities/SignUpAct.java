package com.example.quickdate.activities;

import com.example.quickdate.R;
import com.example.quickdate.utility.regexString;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickdate.model.Info;
import com.example.quickdate.model.Interest;
import com.example.quickdate.model.LookingFor;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class SignUpAct extends AppCompatActivity {

    EditText et_passWord, et_email;
    Spinner sp_provincial;
    Button btn_submit;
    ImageView iv_backAct_signUpAct;
    CheckBox cb_policy;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    TextView tv_hyperLink;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialization();
        doFunctionInAct();

        tv_hyperLink = (TextView) findViewById(R.id.tv_hyperLink);
        tv_hyperLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initialization(){
        et_passWord = (EditText) findViewById(R.id.et_register_password_signUpAct);
        et_email = (EditText) findViewById(R.id.et_register_mail_signUpAct);
        sp_provincial = (Spinner) findViewById(R.id.sp_provincial_signUpAct);
        btn_submit = (Button) findViewById(R.id.btn_submit_signUpAct);
        iv_backAct_signUpAct = (ImageView) findViewById(R.id.iv_backAct_signUpAct);
        cb_policy = (CheckBox) findViewById(R.id.cb_policy_signUpAct);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        setValueForSpinner();
    }

    private void doFunctionInAct(){
        signUpFunction();
        callBackAct();
        termLinkToRead();
    }

    private void signUpFunction(){
        PushDownAnim.setPushDownAnimTo(btn_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str_password = et_passWord.getText().toString();
                        String str_email = et_email.getText().toString();
                        progressBar.setVisibility(View.VISIBLE);
                        if(isCheckDataInput(str_password, str_email)){
                            firebaseAuth.createUserWithEmailAndPassword(str_email, str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isComplete()){
                                        progressBar.setVisibility(View.GONE);
                                        firebaseUser = firebaseAuth.getCurrentUser();
                                        assert firebaseUser != null : "cant find user";
                                        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+ firebaseUser.getUid());

                                        User user = new User( firebaseUser.getUid(), str_email, 0, new LookingFor(), new Info( sp_provincial.getSelectedItem().toString()), new ArrayList<Interest>());

                                        databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(getApplicationContext(), "Register Successfully, please check your email and verification", Toast.LENGTH_LONG).show();
                                                                startActivity(new Intent(getApplicationContext(), LoginAct.class));
                                                                finish();
                                                            }else{
                                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Sign up failed !!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isCheckDataInput(String passWordCheck, String emailCheck){
        if(TextUtils.isEmpty(passWordCheck) && TextUtils.isEmpty(emailCheck)){
            Toast.makeText(getBaseContext(), "All fields should not empty", Toast.LENGTH_SHORT).show();
        }
        else if(new regexString().regexFunc(getString(R.string.regexEmail), emailCheck)){
            Toast.makeText(getApplicationContext(), "Field Email invalid", Toast.LENGTH_SHORT).show();
        }
        else if(passWordCheck.length() <8){
            Toast.makeText(getApplicationContext(), "Field Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
        }
        else if(!cb_policy.isChecked()){
            Toast.makeText(getApplicationContext(), "Please Agree to terms and Conditions", Toast.LENGTH_SHORT).show();
        }
        else
            return true;
        return false;
    }

    private void callBackAct(){
        PushDownAnim.setPushDownAnimTo(iv_backAct_signUpAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void setValueForSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.layout_spinner); // Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // Specify the layout to use when the list of choices appears
        sp_provincial.setAdapter(adapter); // Apply the adapter to the spinner
    }

    private void termLinkToRead(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}