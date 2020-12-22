package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_QuickDate.SwipeAct;
import com.example.quickdate.model.User;
import com.example.quickdate.utility.regexString;
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

public class LoginFragment extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization(view);
        doFunctionInAct();
    }

    private void initialization(View view){
        et_email = (EditText) view.findViewById(R.id.et_mail_Login);
        et_password = (EditText) view.findViewById(R.id.et_password);
        btn_submit = (Button) view.findViewById(R.id.btn_submit_login);
        tv_forgotPass = (TextView) view.findViewById(R.id.tv_forgotPass_loginAct);
        tv_signUp = (TextView) view.findViewById(R.id.tv_Signup_loginAct);
        cb_rememberPass = (CheckBox) view.findViewById(R.id.cb_rememberpass_loginAct);
        iv_backAct = (ImageView) view.findViewById(R.id.iv_backAct_loginAct);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences loginPreferences = this.getActivity().getSharedPreferences(remember,
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
                                        assert user != null;
                                        if(user.isEmailVerified()){
                                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + user.getUid());
                                            db.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    User u = snapshot.getValue(User.class);
                                                    assert u != null : "Cant find user";
                                                    if(u.getStatus() == 0){
                                                        if (NavHostFragment.findNavController(LoginFragment.this).getCurrentDestination().getId() == R.id.loginFragment) {
                                                            NavHostFragment.findNavController(LoginFragment.this)
                                                                    .navigate(R.id.action_loginFragment_to_selectGenderFragment);
                                                        }
                                                    }else{
                                                        Intent intent = new Intent(getActivity(), SwipeAct.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    System.out.println("The read failed: " + error.getCode());
                                                }
                                            });
                                        }else{
                                            Toast.makeText(getActivity(), "Please verification your email for login", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private boolean isCheckDataInput(String emailCheck, String passWordCheck){
        if (TextUtils.isEmpty(emailCheck) && TextUtils.isEmpty(passWordCheck)) {
            Toast.makeText(getActivity(), "All fields shouldn't empty", Toast.LENGTH_SHORT).show();
        } else if (new regexString().regexFunc(getString(R.string.regexEmail), emailCheck)) {
            Toast.makeText(getActivity(), "Field Email invalid", Toast.LENGTH_SHORT).show();
        } else if (passWordCheck.length() < 8) {
            Toast.makeText(getActivity(), "Email wasn't found or Password incorrect", Toast.LENGTH_SHORT).show();
        } else
            return true;
        return false;
    }

    private void isRememberPasswordFunction(String email, String passWord){
        if (cb_rememberPass.isChecked()) {
            SharedPreferences loginPreferences = this.getActivity().getSharedPreferences(remember, Context.MODE_PRIVATE);
            loginPreferences.edit().putString(emailRemember, email).putString(passWordRemember, passWord).putBoolean(checkedRemember, true).apply();
        } else {
            SharedPreferences loginPreferences = this.getActivity().getSharedPreferences(remember, Context.MODE_PRIVATE);
            loginPreferences.edit().clear().apply(); // <-- clear remember account
        }
    }

    private void callActForgotPassword(){
        PushDownAnim.setPushDownAnimTo(tv_forgotPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });
    }

    private void callActSignUp(){
        PushDownAnim.setPushDownAnimTo(tv_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_signUpFragment);
            }
        });
    }

    private void callBackAct(){
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_splashFragment);
            }
        });
    }
}