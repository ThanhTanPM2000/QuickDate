package com.example.quickdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    Spinner spinner_provincial;
    Button btn_finish;
    EditText et_username, et_password, et_email, et_phone;
    CheckBox cb_agree;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        spinner_provincial = findViewById(R.id.spinner_provincial);
        btn_finish = findViewById(R.id.btnFinished);
        et_username = findViewById(R.id.et_username2);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password2);
        et_phone = findViewById(R.id.et_phone);
        firebaseAuth = FirebaseAuth.getInstance();
        cb_agree = (CheckBox) findViewById(R.id.cb_agree);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner); // Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // Specify the layout to use when the list of choices appears
        spinner_provincial.setAdapter(adapter); // Apply the adapter to the spinner

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_username = et_username.getText().toString();
                String str_password = et_password.getText().toString();
                String str_email = et_email.getText().toString();
                String str_phone = et_phone.getText().toString();


                if(TextUtils.isEmpty(str_username) && TextUtils.isEmpty(str_password) && TextUtils.isEmpty(str_email) && TextUtils.isEmpty(str_phone)){
                    Toast.makeText(getBaseContext(), "All fields should not empty", Toast.LENGTH_SHORT).show();
                }
                else if(Regex(getString(R.string.regexEmail), str_email)){
                    Toast.makeText(SignupActivity.this, "Field Email invalid", Toast.LENGTH_SHORT).show();
                }
                else if(Regex(getString(R.string.regexPhoneNumber), str_phone)){
                    Toast.makeText(SignupActivity.this, "Field Phone invalid", Toast.LENGTH_SHORT).show();
                }
                else if(str_username.length() <=8){
                    Toast.makeText(SignupActivity.this, "Field Username must at least 8 characters", Toast.LENGTH_SHORT).show();
                }
                else if(str_password.length() <=8){
                    Toast.makeText(SignupActivity.this, "Field Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                }
                else if(!cb_agree.isChecked()){
                    Toast.makeText(SignupActivity.this, "Please Agree to terms and Conditions", Toast.LENGTH_SHORT).show();
                }
                else
                    Register(str_username, str_password, str_email, str_phone);
            }
        });
    }

    public void Register(String str_username, String str_password, String str_email, String str_phone){
        firebaseAuth.createUserWithEmailAndPassword(str_email, str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance(); // get data from firebase store
                    assert user != null; // make sure user not null
                    String userId = user.getUid();
                    Map<String, Object> data = new HashMap<>();
                    data.put("username", str_username);
                    data.put("email", str_email);
                    data.put("phone", str_phone);
                    data.put("imgAvt", "default");
                    data.put("provincial", spinner_provincial.getSelectedItem().toString());  // get value item from spinner

                    db.collection("users").document(userId).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Success", "DocumentSnapshot successfully written!");
                            startActivity(new Intent(getBaseContext(), Profile_User.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("failure", "Error writing document", e);
                            Toast.makeText(SignupActivity.this, "System error", Toast.LENGTH_SHORT).show();
                        }
                    });
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

    // using Regex Expression to check gmail and phone
    public Boolean Regex(String regexStr, String str_editText){
         Pattern pattern = Pattern.compile(regexStr);
         Matcher matcher = pattern.matcher(str_editText);
        return !matcher.find();
    }
}