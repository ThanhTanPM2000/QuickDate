package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.widget.ImageView;

import com.example.quickdate.R;
import com.example.quickdate.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class SelectGenderActivity extends AppCompatActivity {

    private ConstraintLayout ctl_male, ctl_female;
    private ImageView iv_isCheckedFemale, iv_isCheckedMale, iv_submit;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gender);
        initialization();
        doFunctionInAct();
    }

    private void initialization() {
        ctl_female = findViewById(R.id.ctl_chooseFemale_selectGenderAct);
        ctl_male = findViewById(R.id.ctl_chooseMale_selectGenderAct);
        iv_isCheckedFemale = findViewById(R.id.iv_isCheckedFemale_selectGenderAct);
        iv_isCheckedMale = findViewById(R.id.iv_isCheckedMale_selectGenderAct);
        iv_submit = findViewById(R.id.iv_submit_selectGender);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        user = (User) getIntent().getSerializableExtra("User");
    }

    private void doFunctionInAct() {
        PushDownAnim.setPushDownAnimTo(ctl_female).setOnClickListener(v -> chooseGender(false));

        PushDownAnim.setPushDownAnimTo(ctl_male).setOnClickListener(v -> chooseGender(true));

        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(v -> submitDataFunction());
    }

    private void chooseGender(Boolean isMale) {
        if (isMale) {
            user.getInfo().setGender("Male");
            iv_isCheckedFemale.setVisibility(View.GONE);
            iv_isCheckedMale.setVisibility(View.VISIBLE);
        } else {
            user.getInfo().setGender("Female");
            iv_isCheckedFemale.setVisibility(View.VISIBLE);
            iv_isCheckedMale.setVisibility(View.GONE);
        }
    }

    private void submitDataFunction() {
        Intent intent = new Intent(SelectGenderActivity.this, BioPhotosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("User", user);
        startActivity(intent);
        finish();
    }
}