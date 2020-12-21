package com.example.quickdate.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.utility.deleteUser;

import com.example.quickdate.model.Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.Objects;

public class SelectGenderAct extends AppCompatActivity {

    private ConstraintLayout ctl_male, ctl_female;
    private ImageView iv_backAct, iv_isCheckedFemale, iv_isCheckedMale, iv_submit;
    private FirebaseAuth firebaseAuth;
    private Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gender);

        initialization();
    }

    private void initialization() {
        ctl_female = (ConstraintLayout) findViewById(R.id.ctl_chooseFemale_selectGenderAct);
        ctl_male = (ConstraintLayout) findViewById(R.id.ctl_chooseMale_selectGenderAct);
        iv_backAct = (ImageView) findViewById(R.id.iv_backAct_selectGenderAct);
        iv_isCheckedFemale = (ImageView) findViewById(R.id.iv_isCheckedFemale_selectGenderAct);
        iv_isCheckedMale = (ImageView) findViewById(R.id.iv_isCheckedMale_selectGenderAct);
        iv_submit = (ImageView) findViewById(R.id.iv_submit_selectGender);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("Users/" + firebaseAuth.getCurrentUser().getUid() + "/info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                info = snapshot.getValue(Info.class);
                doFunctionInAct();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void doFunctionInAct() {
        PushDownAnim.setPushDownAnimTo(ctl_female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseGender(false);
            }
        });

        PushDownAnim.setPushDownAnimTo(ctl_male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseGender(true);
            }
        });
        //callBackAct();

        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDataFunction();
            }
        });
    }

    private void chooseGender(Boolean isMale) {
        if (isMale) {
            info.setMale(true);
            iv_isCheckedFemale.setVisibility(View.GONE);
            iv_isCheckedMale.setVisibility(View.VISIBLE);
        } else {
            info.setMale(false);
            iv_isCheckedFemale.setVisibility(View.VISIBLE);
            iv_isCheckedMale.setVisibility(View.GONE);
        }
    }

    private void callBackAct() {
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new deleteUser(Objects.requireNonNull(firebaseAuth.getCurrentUser()));
                startActivity(new Intent(getApplicationContext(), SignUpAct.class));
                finish();
            }
        });
    }

    private void submitDataFunction() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + firebaseAuth.getCurrentUser().getUid());
        db.child("info").setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SelectGenderAct.this, BioPhotosAct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}