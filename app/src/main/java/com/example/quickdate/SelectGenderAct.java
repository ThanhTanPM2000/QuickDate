package com.example.quickdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.action.deleteUser;

import com.example.quickdate.model.Info;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

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
        doFunctionInAct();
    }

    private void initialization(){
        ctl_female = (ConstraintLayout) findViewById(R.id.ctl_chooseFemale_selectGenderAct);
        ctl_male = (ConstraintLayout) findViewById(R.id.ctl_chooseMale_selectGenderAct);
        iv_backAct = (ImageView) findViewById(R.id.iv_backAct_selectGenderAct);
        iv_isCheckedFemale = (ImageView) findViewById(R.id.iv_isCheckedFemale_selectGenderAct);
        iv_isCheckedMale = (ImageView) findViewById(R.id.iv_isCheckedMale_selectGenderAct);
        iv_submit = (ImageView) findViewById(R.id.iv_submit_selectGender);
        firebaseAuth = FirebaseAuth.getInstance();

        info = new Info();
    }

    private void doFunctionInAct(){
        chooseGender();
        callBackAct();
        submitDataFunction();
    }

    private void chooseGender(){
        PushDownAnim.setPushDownAnimTo(ctl_female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setMale(false);
                iv_isCheckedFemale.setVisibility(View.VISIBLE);
                iv_isCheckedMale.setVisibility(View.GONE);
            }
        });

        PushDownAnim.setPushDownAnimTo(ctl_male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setMale(true);
                iv_isCheckedFemale.setVisibility(View.GONE);
                iv_isCheckedMale.setVisibility(View.VISIBLE);
            }
        });
    }

    private void callBackAct(){
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new deleteUser(firebaseAuth.getCurrentUser());
                startActivity(new Intent(getApplicationContext(), SignUpAct.class));
                finish();
            }
        });
    }

    private void submitDataFunction(){
        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + firebaseAuth.getCurrentUser().getUid());
                db.child("info").setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), BioPhotosAct.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

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