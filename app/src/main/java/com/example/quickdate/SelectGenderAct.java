package com.example.quickdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.quickdate.action.deleteUser;

import com.google.firebase.auth.FirebaseAuth;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class SelectGenderAct extends AppCompatActivity {

    ConstraintLayout ctl_male, ctl_female;
    ImageView iv_backAct, iv_isCheckedFemale, iv_isCheckedMale;
    FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void doFunctionInAct(){
        chooseGender();
        callBackAct();
    }

    private void chooseGender(){
        PushDownAnim.setPushDownAnimTo(ctl_female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_isCheckedFemale.setVisibility(View.VISIBLE);
                iv_isCheckedMale.setVisibility(View.GONE);
            }
        });

        PushDownAnim.setPushDownAnimTo(ctl_male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}