package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.utility.regexString;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.thekhaeng.pushdownanim.PushDownAnim;


public class ForgotPasswordFragment extends Fragment {

    EditText et_email;
    ImageButton btn_submit;
    ImageView iv_backAct;
    FirebaseAuth firebaseAuth;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization(view);
        doFunctionInAct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    private void initialization(View view){
        et_email = (EditText) view.findViewById(R.id.et_email_forgotPasswordAct);
        btn_submit = (ImageButton) view.findViewById(R.id.btn_submit_forgotPassword);
        iv_backAct = (ImageView) view.findViewById(R.id.iv_backAct_forgotPasswordAct);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void doFunctionInAct(){
        forgotPasswordFunction();
        callBackAct();
    }

    private void forgotPasswordFunction(){
        PushDownAnim.setPushDownAnimTo(btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = et_email.getText().toString();
                if(checkDataInput(mail)){
                    firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Check your email, to get new Password", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Seem like email wasn't register to QuickDate", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean checkDataInput(String email){
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(), "You must fill your email :P", Toast.LENGTH_SHORT).show();
        }else if(new regexString().regexFunc(getString(R.string.regexEmail), email)){
            Toast.makeText(getActivity(), "You kidding me, it is not Email", Toast.LENGTH_SHORT).show();
        }else {
            return true;
        }
        return false;
    }

    private void callBackAct(){
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ForgotPasswordFragment.this)
                        .navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
            }
        });
    }
}