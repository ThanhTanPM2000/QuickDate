package com.example.quickdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpAct extends AppCompatActivity {

    EditText et_name, et_passWord, et_email;
    Spinner sp_provincial;
    Button btn_submit;
    ImageView iv_backAct_signUpAct;
    CheckBox cb_policy;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialization();
        doFunctionInAct();
    }

    private void initialization(){
        et_name = (EditText) findViewById(R.id.et_register_name_signUpAct);
        et_passWord = (EditText) findViewById(R.id.et_register_password_signUpAct);
        et_email = (EditText) findViewById(R.id.et_register_mail_signUpAct);
        sp_provincial = (Spinner) findViewById(R.id.sp_provincial_signUpAct);
        btn_submit = (Button) findViewById(R.id.btn_submit_signUpAct);
        iv_backAct_signUpAct = (ImageView) findViewById(R.id.iv_backAct_signUpAct);
        cb_policy = (CheckBox) findViewById(R.id.cb_policy_signUpAct);
        firebaseAuth = FirebaseAuth.getInstance();

        setValueForSpinner();
    }

    private void doFunctionInAct(){
        signUpFunction();
        callBackAct();
    }

    private void signUpFunction(){
        PushDownAnim.setPushDownAnimTo(btn_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str_username = et_name.getText().toString();
                        String str_password = et_passWord.getText().toString();
                        String str_email = et_email.getText().toString();

                        if(isCheckDataInput(str_username, str_password, str_email)){
                            firebaseAuth.createUserWithEmailAndPassword(str_email, str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isComplete()){
                                        firebaseUser = firebaseAuth.getCurrentUser();
                                        assert firebaseUser != null : "cant find user";
                                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                                        Map<String, Object> data = new HashMap<>();
                                        data.put("username", str_username);
                                        data.put("email", str_email);
                                        data.put("imgAvt", "default");
                                        data.put("provincial", sp_provincial.getSelectedItem().toString());  // get value item from spinner
                                        data.put("gender", "male");
                                        data.put("type_gender", "male");
                                        data.put("min_age", 18);
                                        data.put("max_age", 40);
                                        data.put("min_height", 150);
                                        data.put("max_height", 200);
                                        data.put("min_weight", 40);
                                        data.put("max_weight", 80);

                                        databaseReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getApplicationContext(), "Register Successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), SelectGenderAct.class));
                                                    finish();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(getApplicationContext(), "this email was register", Toast.LENGTH_SHORT).show();
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

    private boolean isCheckDataInput(String nameCheck, String passWordCheck, String emailCheck){
        if(TextUtils.isEmpty(nameCheck) && TextUtils.isEmpty(passWordCheck) && TextUtils.isEmpty(emailCheck)){
            Toast.makeText(getBaseContext(), "All fields should not empty", Toast.LENGTH_SHORT).show();
        }
        else if(regexFunc(getString(R.string.regexEmail), emailCheck)){
            Toast.makeText(getApplicationContext(), "Field Email invalid", Toast.LENGTH_SHORT).show();
        }
        else if(nameCheck.length() <8){
            Toast.makeText(getApplicationContext(), "Field Username must at least 8 characters", Toast.LENGTH_SHORT).show();
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

    private Boolean regexFunc(String str, String str2) {
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(str2);
        return !matcher.find();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}